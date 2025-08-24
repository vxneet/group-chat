package com.gpt.group_chat.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}