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

    private static ThreadLocal<WebDriverWait> webDriverWaitThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<Long> timeoutInSeconds = new ThreadLocal<>();
    private static ThreadLocal<Long> sleepInMillis = new ThreadLocal<>();
    private final static long DEFAULT_TIMEOUT_IN_SECONDS = 15;
    private final static long DEFAULT_SLEEP_IN_MILLIS = 1000;

    public static WebElement waitUntilVisibilityOf(WebDriver webDriver, WebElement webElement) {
        setWebDriverWait(webDriver);
        return getWebDriverWait().until(ExpectedConditions.visibilityOf(webElement));
    }

    public static void waitUntilPageLoadComplete(WebDriver webDriver){
        untilWaitCondition(webDriver, PAGE_LOAD_COMPLETE_FUNCTION);
    }

    private static void untilWaitCondition(WebDriver webDriver, Function<WebDriver, Boolean> waitCondition) throws NotFoundException {
        setWebDriverWait(webDriver);
        getWebDriverWait().until(waitCondition);
    }

    public static WebDriverWait getWebDriverWait() {
        return webDriverWaitThreadLocal.get();
    }

    private static void setWebDriverWait(WebDriver webDriver) {
        if (timeoutInSeconds.get() == null || sleepInMillis.get() == null) {
            resetSettingsForTimeoutAndSleep();
        }
        webDriverWaitThreadLocal.set(new WebDriverWait(webDriver, timeoutInSeconds.get(), sleepInMillis.get()));
    }

    public static void resetSettingsForTimeoutAndSleep() {
        timeoutInSeconds.set(DEFAULT_TIMEOUT_IN_SECONDS);
        sleepInMillis.set(DEFAULT_SLEEP_IN_MILLIS);
    }

}
