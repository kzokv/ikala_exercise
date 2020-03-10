package org.ikala.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.function.Function;

public class WaitUtils {
    private static final Function<WebDriver, Boolean> PAGE_LOAD_COMPLETE_FUNCTION = webDriver -> {
        Boolean isPageLoaded = ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete");
        if (!isPageLoaded) System.out.println(("Document is loading"));
        else System.out.println("Document is loaded");
        return isPageLoaded;
    };

    private static WebDriverWait webDriverWait;
    private static long timeoutInSeconds;
    private static long sleepInMillis;
    private final static long DEFAULT_TIMEOUT_IN_SECONDS = 10;
    private final static long DEFAULT_SLEEP_IN_MILLIS = 200;

    public static WebElement waitUntilVisibilityOf(WebDriver webDriver, WebElement webElement) {
        setWebDriverWait(webDriver);
        return webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
    }

    public static void waitUntilPageLoadComplete(WebDriver webDriver){
        untilWaitCondition(webDriver, PAGE_LOAD_COMPLETE_FUNCTION);
    }

    private static void untilWaitCondition(WebDriver webDriver, Function<WebDriver, Boolean> waitCondition) throws NotFoundException {
        setWebDriverWait(webDriver);
        webDriverWait.until(waitCondition);
    }

    private static void setWebDriverWait(WebDriver webDriver) {
        if (timeoutInSeconds == 0 || sleepInMillis == 0) {
            resetSettingsForTimeoutAndSleep();
        }
        webDriverWait = new WebDriverWait(webDriver, timeoutInSeconds, sleepInMillis);
    }

    public static void resetSettingsForTimeoutAndSleep() {
        timeoutInSeconds = DEFAULT_TIMEOUT_IN_SECONDS;
        sleepInMillis = DEFAULT_SLEEP_IN_MILLIS;
    }

}
