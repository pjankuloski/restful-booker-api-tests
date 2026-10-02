package com.qa.restfulbooker.tests;

import com.qa.restfulbooker.base.BaseTest;
import com.qa.restfulbooker.clients.AuthClient;
import com.qa.restfulbooker.models.AuthRequest;
import com.qa.restfulbooker.models.AuthResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthTests extends BaseTest {

    private final AuthClient authClient = new AuthClient();

    @Test(description = "POST /auth with valid credentials returns a non-empty token")
    public void testCreateTokenWithValidCredentials() {
        AuthRequest request = new AuthRequest("admin", "password123");
        Response response = authClient.createToken(request);

        Assert.assertEquals(response.getStatusCode(), 200);

        AuthResponse authResponse = response.as(AuthResponse.class);
        Assert.assertNotNull(authResponse.getToken(), "Token should not be null");
        Assert.assertFalse(authResponse.getToken().isEmpty(), "Token should not be empty");
    }

    @Test(description = "POST /auth with invalid credentials returns no token, with a reason")
    public void testCreateTokenWithInvalidCredentials() {
        AuthRequest request = new AuthRequest("invalidUser", "invalidPass");
        Response response = authClient.createToken(request);

        // Note: restful-booker still replies 200 OK for bad creds - it signals
        // failure via payload ("reason") rather than an HTTP error status.
        Assert.assertEquals(response.getStatusCode(), 200);

        AuthResponse authResponse = response.as(AuthResponse.class);
        Assert.assertNull(authResponse.getToken(), "Token should be null for invalid credentials");
        Assert.assertEquals(authResponse.getReason(), "Bad credentials");
    }
}
