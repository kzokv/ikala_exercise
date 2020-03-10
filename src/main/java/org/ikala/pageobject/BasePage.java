package org.ikala.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class BasePage {
    protected ThreadLocal<WebDriver> webDriverThreadLocal = new ThreadLocal<>();

    public BasePage() {}

    public BasePage setWebDriverThreadLocal(WebDriver webDriver) {
        initElements(webDriver);
        return this;
    }

    protected WebDriver getWebDriver() {
        return this.webDriverThreadLocal.get();
    }

    public void initElements(WebDriver webDriver) {
        this.webDriverThreadLocal.set(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    protected BasePage clickWebElementToPage(BasePage pageToGo, WebElement webElement) {
        webElement.click();
        pageToGo.setWebDriverThreadLocal(getWebDriver());
        return pageToGo;
    }
}
