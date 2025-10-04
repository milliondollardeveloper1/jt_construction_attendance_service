package com.hrms.jt_construction.controller;

import com.hrms.jt_construction.jpa.RefreshToken;
import com.hrms.jt_construction.jpa.Users;
import com.hrms.jt_construction.model.request.LoginRequest;
import com.hrms.jt_construction.model.request.TokenRefreshRequest;
import com.hrms.jt_construction.model.response.ResponseMessage;
import com.hrms.jt_construction.model.response.LoginResponse;
import com.hrms.jt_construction.service.JwtTokenProvider;
import com.hrms.jt_construction.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        String jwt;
        RefreshToken refreshToken;
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate Access Token
            jwt = tokenProvider.generateToken(authentication);

            // Generate Refresh Token and save to DB
            refreshToken = refreshTokenService.createRefreshToken(
                    ((Users) authentication.getPrincipal()).getId()
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage("Invalid username or password."), HttpStatus.UNAUTHORIZED);
        } catch (AuthenticationException e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage("Authentication failed."), HttpStatus.UNAUTHORIZED);
        }

        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // Return both tokens to the client
        return ResponseEntity.ok(new LoginResponse(jwt, refreshToken.getToken()));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration) // 1. Check if token is expired
                .map(RefreshToken::getUser)                 // 2. Get the associated user
                .map(user -> {
                    // 3. Generate a new Access Token for the valid user
                    String newAccessToken = tokenProvider.generateToken(new UsernamePasswordAuthenticationToken(
                            user.getUsername(), user.getPassword(), user.getAuthorities()));

                    // 4. Return the new Access Token (keep the old Refresh Token for next refresh)
                    return ResponseEntity.ok(new LoginResponse(newAccessToken, requestRefreshToken));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token not found!"));
    }
}