package com.qa.restfulbooker.base;

import com.qa.restfulbooker.clients.AuthClient;
import com.qa.restfulbooker.config.ConfigManager;
import com.qa.restfulbooker.models.AuthRequest;
import com.qa.restfulbooker.models.AuthResponse;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;

/**
 * Every test class extends this. It runs once per suite:
 *  - points REST Assured at the base URL from config.properties
 *  - enables request/response logging ONLY when an assertion fails (keeps console clean otherwise)
 *  - fetches a single auth token, shared by any test class that needs PUT/PATCH/DELETE access
 */
public class BaseTest {

    protected static String authToken;

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        RestAssured.baseURI = ConfigManager.getInstance().getBaseUrl();
        RestAssured.config = RestAssuredConfig.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        AuthClient authClient = new AuthClient();
        AuthRequest authRequest = new AuthRequest(
                ConfigManager.getInstance().getUsername(),
                ConfigManager.getInstance().getPassword()
        );

        AuthResponse authResponse = authClient.createToken(authRequest).as(AuthResponse.class);
        Assert.assertNotNull(authResponse.getToken(), "Could not obtain auth token during suite setup");
        authToken = authResponse.getToken();
    }
}
