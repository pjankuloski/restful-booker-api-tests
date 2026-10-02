package com.qa.restfulbooker.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Minimal console logger. Allure (via allure-testng + aspectjweaver, wired in pom.xml)
 * captures step/status data independently for the HTML report; this listener just
 * makes the terminal output easy to skim while running locally.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("STARTED  -> " + testName(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("PASSED   -> " + testName(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("FAILED   -> " + testName(result));
        System.out.println("  Reason: " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("SKIPPED  -> " + testName(result));
    }

    private String testName(ITestResult result) {
        return result.getTestClass().getName() + "#" + result.getMethod().getMethodName();
    }
}
