package com.forsrc.gwt.client.application.login;

import com.forsrc.gwt.client.application.ApplicationPresenter;
import com.forsrc.gwt.client.application.ApplicationView;
import com.forsrc.gwt.client.commons.model.AccessToken;
import com.forsrc.gwt.client.event.MyEvent;
import com.forsrc.gwt.client.event.MyEvent.MyEventData;
import com.forsrc.gwt.client.event.MyEvent.MyEventHandler;
import com.forsrc.gwt.client.place.NameTokens;
import com.forsrc.gwt.client.resources.i18n.Messages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.storage.client.Storage;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.gwt.crypto.bouncycastle.util.encoders.Base64;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import gwt.material.design.client.base.validator.MessageFormat;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialToast;

public class LoginPresenter extends Presenter<LoginPresenter.MyView, LoginPresenter.MyProxy>
        implements MyEventHandler, LoginUiHandlers {

    interface MyView extends View, HasUiHandlers<LoginUiHandlers> {
        public void onSearch(String text);

        public void onSideNavEvent(int w);
    }

    @ProxyStandard
    @NameToken(NameTokens.LOGIN)
    @NoGatekeeper
    interface MyProxy extends ProxyPlace<LoginPresenter> {
    }

    private final PlaceManager placeManager;
    private final AccessToken accessToken;

    @Inject
    LoginPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            PlaceManager placeManager,
            AccessToken accessToken
            ) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN);
        getView().setUiHandlers(this);
        this.placeManager = placeManager;
        this.accessToken = accessToken;
    }

    @Override
    protected void onBind() {
        super.onBind();
        addRegisteredHandler(MyEvent.TYPE, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
    }

    @Override
    public void onMyEventHandler(MyEvent event) {
        MyEventData data = event.getData();
        if (ApplicationView.TYPE_SEARECH.equals(data.getType())) {
            getView().onSearch((String) data.getData());
        } else if (ApplicationView.TYPE_SIDEBAR.equals(data.getType())) {
            getView().onSideNavEvent((int) data.getData());
        }
    }

    Messages messages = GWT.create(Messages.class);

    @Override
    public boolean checklogin(String username, String password) {
        MaterialToast.fireToast("Login username: " + username);
        return username != null && username.length() > 0 && password != null && password.length() > 0;
    }
}
