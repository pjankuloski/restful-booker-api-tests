package com.qa.restfulbooker.tests;

import com.qa.restfulbooker.base.BaseTest;
import com.qa.restfulbooker.clients.PingClient;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PingTests extends BaseTest {

    private final PingClient pingClient = new PingClient();

    @Test(description = "GET /ping should return HTTP 201 (API health check)")
    public void testHealthCheck() {
        Response response = pingClient.healthCheck();
        Assert.assertEquals(response.getStatusCode(), 201, "Ping endpoint should return HTTP 201");
    }
}
