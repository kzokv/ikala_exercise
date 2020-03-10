package org.ikala;

import org.ikala.helper.ApplicationContextProvider;
import org.ikala.helper.ContextDataProtoTypeHolder;
import org.ikala.helper.ContextHelper;
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
    private final String XPATH_QUERY_NEXT = "//span[@class=\"RveJvd snByac\"]";

    private WebDriver webDriver;
    private MavenArgsManager mavenArgsManager;

    private LoginPage loginPage;
    private MainPage mainPage;

    @BeforeMethod
    public void setup(ITestContext iTestContext) {


        //Init spring beans
        new AnnotationConfigApplicationContext(IkalaBeans.class);

        ContextDataProtoTypeHolder contextDataProtoTypeHolder = ApplicationContextProvider.getApplicationContext().getBean(ContextDataProtoTypeHolder.class);
        //Set this object for future usage

        ContextHelper.setContextDataProtoTypeHolder(contextDataProtoTypeHolder);
        //maven argument manager
        mavenArgsManager = contextDataProtoTypeHolder.getMavenArgsManager();

        //Webdriver
        File driver = new File(this.getClass().getClassLoader().getResource("webdriver/chromedriver.exe").getFile());
        System.setProperty("webdriver.chrome.driver", driver.getAbsolutePath());

        WebDriver localWebDriver = new ChromeDriver();

        //Register local webDriver to EventFiringWebDriver
        webDriver = new EventFiringWebDriver(localWebDriver);
        ((EventFiringWebDriver) webDriver).register(new WebDriverEventHandler());

        loginPage = (LoginPage) contextDataProtoTypeHolder.getPageObjectManager().getLoginPage().setWebDriverThreadLocal(webDriver);
    }

    @Test
    public void testLoginGoogle() throws InterruptedException {

        mainPage = (MainPage) loginPage.openUrl("https://accounts.google.com/signin/v2/identifier?flowName=GlifWebSignIn&flowEntry=ServiceLogin&hl=en") //go to google account login page
                .inputEmail(mavenArgsManager.getEmail())  //enter email
                .clickNextButton(loginPage)  //click next button
                .inputPassword(mavenArgsManager.getPassword()) // input password
                .clickNextButton(loginPage) //click next button
                .untickCheckBoxDontAskAgain()  //untick do-not-ask-again checkbox
                .inputTotpAndClickNextButton(mavenArgsManager.getSecretGoogleAuthKey()); //enter Totp key

        //assertion
        Assert.assertEquals(mainPage.getWelcomeMessage(), "Welcome, Keith Chuang");

    }

    @AfterMethod
    public void tearDown() {
        webDriver.quit();
    }
}
