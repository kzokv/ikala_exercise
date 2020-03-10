package org.ikala.pageobject;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.springframework.stereotype.Service;

@Service
public class MainPage extends BasePage {

    @FindBy(how = How.XPATH, using = "//h1[@class=\"x7WrMb\"]")
    private WebElement pWebElementWelcomeMessage;

    public MainPage() {
        super();
    }

    public String getWelcomeMessage() {
        return pWebElementWelcomeMessage.getText().trim();
    }

    //getter methods for web-elements
    public WebElement getpWebElementWelcomeMessage() {
        return pWebElementWelcomeMessage;
    }
}
