package com.forsrc.gwt.client.application;

import com.forsrc.gwt.client.application.demo.DemoModule;
import com.forsrc.gwt.client.application.error.ErrorModule;
import com.forsrc.gwt.client.application.home.HomeModule;
import com.forsrc.gwt.client.application.login.LoginModule;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {


        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
                ApplicationPresenter.MyProxy.class);

        install(new ErrorModule());
        install(new HomeModule());
        install(new LoginModule());
        install(new DemoModule());
    }
}
