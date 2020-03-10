package org.ikala.pageobject;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.ikala.utils.WaitUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service
@Scope("prototype")
public class LoginPage extends BasePage {

    @Autowired
    MainPage mainPage;

    @FindBy(how = How.XPATH, using = "//input[@type=\"email\"]")
    private WebElement inputFieldEmail;

    @FindBy(how = How.XPATH, using = "//span[@class=\"RveJvd snByac\"]")
    private WebElement btnNext;

    @FindBy(how = How.XPATH, using = "//input[@type=\"password\"]")
    private WebElement inputFieldPassword;

    @FindBy(how = How.ID, using = "totpPin")
    private WebElement inputFieldTotp;

    @FindBy(how = How.XPATH, using = "//div[@class=\"uVccjd iK47pf N2RpBe\"]")
    private WebElement checkBoxDontAskAgain;

    @FindBy(how = How.XPATH, using = "//div[@class=\"o6cuMc\"]")
    private WebElement divTotpMessage;

    private GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public LoginPage() {
        super();
    }

    public LoginPage openUrl(String url) {
        getWebDriver().get(url);
        return this;
    }

    public LoginPage inputEmail(String str) {
        inputFieldEmail.sendKeys(str);
        return this;
    }

    public LoginPage inputPassword(String str) {
        WaitUtils.waitUntilVisibilityOf(getWebDriver(), inputFieldPassword);
        inputFieldPassword.sendKeys(str);
        return this;
    }

    public LoginPage clickNextButton(BasePage pageToGo) {
        clickWebElementToPage(pageToGo, btnNext);
        return this;
    }

    public LoginPage untickCheckBoxDontAskAgain() {
        WaitUtils.waitUntilVisibilityOf(getWebDriver(), checkBoxDontAskAgain);
        if (checkBoxDontAskAgain.getAttribute("aria-checked").equals("true")) {
            //ElementClickInterceptedException is thrown by webelement click function.
            //clickWebElementToPage(this, checkBoxDontAskAgain);

            //Use JS to click the element instead.
            ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].click();", checkBoxDontAskAgain);
        }
        return this;
    }

    public BasePage inputTotpAndClickNextButton(String secretKey) throws InterruptedException {
        //5 attempts in case the generated tpop code expires at the 20-late-ish second
        for (int attempt=0; attempt < 5; attempt++) {
            try {
                //Get google authenticator code
                int code = gAuth.getTotpPassword(secretKey);
                //clear out the text
                inputFieldTotp.clear();
                //input code in totpPin field
                inputFieldTotp.sendKeys(String.format("%06d", code));
                //click next button
                clickNextButton(mainPage);
                //wait for the presence of weblement of welcome message in MainPage
                WaitUtils.waitUntilVisibilityOf(getWebDriver(), mainPage.getpWebElementWelcomeMessage());
                break;
            } catch (TimeoutException e) {
                if (attempt > 5) {
                    throw new InterruptedException(String.format("Fail to enter totp keys! %s attempts have been executed", (attempt+1)));
                }
            }
        }
        return mainPage;
    }

    public BasePage inputTotpAndClickNextButton(String secretKey, int numAddedToTotpCode) {
        //Get google authenticator code
        int code = gAuth.getTotpPassword(secretKey);
        //clear out the text
        inputFieldTotp.clear();
        //input code in totpPin field
        System.out.printf("Original generated totp code: %06d\n", code);
        System.out.printf("Entered totp code: %06d\n", code+numAddedToTotpCode);
        inputFieldTotp.sendKeys(String.format("%06d", code+numAddedToTotpCode));
        //click next button
        clickNextButton(this);
        WaitUtils.waitUntilVisibilityOf(getWebDriver(), divTotpMessage);
        return this;
    }

    //getter methods for web-elements

    public WebElement getDivTotpMessage() {
        return divTotpMessage;
    }
}
