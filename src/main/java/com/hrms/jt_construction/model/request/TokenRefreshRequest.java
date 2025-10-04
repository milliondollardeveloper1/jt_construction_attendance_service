package com.hrms.jt_construction.model.request;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
