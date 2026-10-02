package com.qa.restfulbooker.models;

/**
 * Maps POST /auth response.
 * On success: { "token": "abc123" }
 * On bad credentials: { "reason": "Bad credentials" }
 */
public class AuthResponse {

    private String token;
    private String reason;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
