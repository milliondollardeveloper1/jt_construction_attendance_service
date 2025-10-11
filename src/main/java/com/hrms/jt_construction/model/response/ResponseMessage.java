package com.hrms.jt_construction.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ResponseMessage {
    public ResponseMessage(){}
    public ResponseMessage(String message){ this.message = message; }
    public ResponseMessage(String message, int code){ this.message = message; this.createdID = code; }
    private String message;
    private long createdID;
}
