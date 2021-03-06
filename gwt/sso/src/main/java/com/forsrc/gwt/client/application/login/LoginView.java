package com.forsrc.gwt.client.application.login;

import javax.inject.Inject;

import com.google.gwt.dom.client.FormElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import gwt.material.design.client.ui.*;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;

public class LoginView extends ViewWithUiHandlers<LoginUiHandlers> implements LoginPresenter.MyView {
    interface Binder extends UiBinder<Widget, LoginView> {
    }


    @UiField
    MaterialContainer loginContainer;
    @UiField
    MaterialTextBox username;
    @UiField
    MaterialTextBox password;
    @UiField
    MaterialTextBox csrf;
    @UiField
    FormPanel loginForm;

    @Inject
    LoginView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        MaterialLoader.progress(true);
        loginForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                MaterialToast.fireToast("Login submit complete");
            }
        });
    }

    @Override
    protected void onAttach() {
        username.setLength(20);
        password.setLength(20);
    }

    @Override
    public void onSearch(String text) {
    }

    @Override
    public void onSideNavEvent(int w) {
        MaterialToast.fireToast("onSideNav");
        // loginContainer.setMarginLeft(w);
        Transition transition = Transition.fromStyleName(Transition.RUBBERBAND.getCssName());
        MaterialAnimation animation = new MaterialAnimation();
        animation.setTransition(transition);
        animation.setDelay(0);
        animation.setDuration(1000);
        animation.setInfinite(false);
        animation.animate(loginContainer);
    }

    @UiHandler("login")
    public void onLogin(ClickEvent clickEvent) {
        ((FormElement)loginForm.getElement().cast()).setTarget("");
        MaterialLoader.progress(true);
        boolean check = getUiHandlers().checklogin(username.getValue(), password.getValue());
        if (check) {
            String _csrf = Cookies.getCookie("X-XSRF-TOKEN");
            csrf.setText(_csrf);
            loginForm.submit();
            MaterialLoader.progress(false);
        }
    }
}
