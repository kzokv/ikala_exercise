package org.ikala.utils;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class WebDriverEventHandler extends AbstractWebDriverEventListener {

    private String webElementText;

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        System.out.printf("Trying to find the element with locator %s ...\n", by);
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        WaitUtils.waitUntilVisibilityOf(driver, element);
        webElementText = (null == element.getText() || "".equals(element.getText())) ? element.getAttribute("value") : element.getText();
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
       System.out.printf("... Success. Click '%s'\n", webElementText);
    }


    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        WaitUtils.waitUntilVisibilityOf(driver, element);
    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        if (element.toString().contains("password")) {
                keysToSend[0] = "****";
        }
        System.out.printf("... Success. Type '%s'\n", keysToSend);
    }
}
