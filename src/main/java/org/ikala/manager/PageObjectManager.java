package org.ikala.manager;

import org.ikala.pageobject.LoginPage;
import org.ikala.pageobject.MainPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class PageObjectManager {

    @Autowired
    LoginPage loginPage;
    @Autowired
    MainPage mainPage;

    public LoginPage getLoginPage() {
        return loginPage;
    }

    public MainPage getMainPage() {
        return mainPage;
    }
}
