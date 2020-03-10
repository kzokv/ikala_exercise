package org.ikala;

import org.ikala.helper.ApplicationContextProvider;
import org.ikala.helper.ContextDataProtoTypeHolder;
import org.ikala.helper.IkalaBeans;
import org.ikala.manager.MavenArgsManager;
import org.ikala.pageobject.LoginPage;
import org.ikala.pageobject.MainPage;
import org.ikala.utils.WebDriverEventHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;



public class Exercise {
    private ThreadLocal<WebDriver> webDriverThreadLocal = new ThreadLocal<>();
    private MavenArgsManager mavenArgsManager;

    private ThreadLocal<LoginPage> loginPageThreadLocal = new ThreadLocal<>();

    private final String GOOGLE_LOGIN_URL = "https://accounts.google.com/signin/v2/identifier?flowName=GlifWebSignIn&flowEntry=ServiceLogin&hl=en";

    @BeforeMethod
    public void setup(ITestContext iTestContext) {

        //Init spring beans
        new AnnotationConfigApplicationContext(IkalaBeans.class);

        ContextDataProtoTypeHolder contextDataProtoTypeHolder = ApplicationContextProvider.getApplicationContext().getBean(ContextDataProtoTypeHolder.class);
        //Set this object for future usage

        //maven argument manager
        mavenArgsManager = contextDataProtoTypeHolder.getMavenArgsManager();

        //Webdriver
        File driver = new File(this.getClass().getClassLoader().getResource("webdriver/chromedriver.exe").getFile());
        System.setProperty("webdriver.chrome.driver", driver.getAbsolutePath());

        WebDriver localWebDriver = new ChromeDriver();

        //Register local webDriver to EventFiringWebDriver
        webDriverThreadLocal.set(new EventFiringWebDriver(localWebDriver));
        ((EventFiringWebDriver) webDriverThreadLocal.get()).register(new WebDriverEventHandler());

        loginPageThreadLocal.set((LoginPage) contextDataProtoTypeHolder.getPageObjectManager().getLoginPage().setWebDriverThreadLocal(webDriverThreadLocal.get()));
    }

    @Test
    public void testLoginGoogleValidTotp() throws InterruptedException {

        MainPage mainPage = (MainPage) loginPageThreadLocal.get().openUrl(GOOGLE_LOGIN_URL) //go to google account login page
                .inputEmail(mavenArgsManager.getEmail())  //enter email
                .clickNextButton(loginPageThreadLocal.get())  //click next button
                .inputPassword(mavenArgsManager.getPassword()) // input password
                .clickNextButton(loginPageThreadLocal.get()) //click next button
                .untickCheckBoxDontAskAgain()  //untick do-not-ask-again checkbox
                .inputTotpAndClickNextButton(mavenArgsManager.getSecretGoogleAuthKey()); //enter Totp key

        //assert that WelcomeMessage element is present
        Assert.assertTrue(mainPage.getpWebElementWelcomeMessage().isDisplayed());

    }

    @Test
    public void testLoginGoogleInvalidTotp() {

        loginPageThreadLocal.get().openUrl(GOOGLE_LOGIN_URL) //go to google account login page
                .inputEmail(mavenArgsManager.getEmail())  //enter email
                .clickNextButton(loginPageThreadLocal.get())  //click next button
                .inputPassword(mavenArgsManager.getPassword()) // input password
                .clickNextButton(loginPageThreadLocal.get()) //click next button
                .untickCheckBoxDontAskAgain()  //untick do-not-ask-again checkbox
                .inputTotpAndClickNextButton(mavenArgsManager.getSecretGoogleAuthKey(),55); //enter Totp key

        //assert that message Wrong code is present for both weblement and its text.
        Assert.assertTrue(loginPageThreadLocal.get().getDivTotpMessage().isDisplayed());
        Assert.assertEquals(loginPageThreadLocal.get().getDivTotpMessage().getText(),"Wrong code. Try again.");

    }

    @AfterMethod
    public void tearDown() {
        webDriverThreadLocal.get().quit();
    }
}
