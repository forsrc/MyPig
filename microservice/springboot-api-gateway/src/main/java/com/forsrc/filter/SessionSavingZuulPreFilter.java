package com.forsrc.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collection;

@Component
public class SessionSavingZuulPreFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionSavingZuulPreFilter.class);

    @Autowired
    private FindByIndexNameSessionRepository repository;

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String accessToken = request.getParameter("access_token");
        if (accessToken == null) {
        }

        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            String username = principal.getName();
            LOGGER.info("--> SessionSavingZuulPreFilter Principal: {}", principal);

            Session session = getUserSession(username);
            if (session != null) {
                context.addZuulRequestHeader("Cookie", "SESSIONID=" + session.getId());
                LOGGER.info("--> SessionSavingZuulPreFilter session proxy: {}", session.getId());
            }
        }
        HttpSession session = request.getSession();
        context.addZuulRequestHeader("Cookie", "SESSIONID=" + session.getId());
        return null;
    }

    private Session getUserSession(String username) {

        Session userSession = null;
        Collection<? extends Session> usersSessions = repository.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME, username).values();

        if (CollectionUtils.isEmpty(usersSessions)) {
            return userSession;
        }
        if (usersSessions.size() > 1) {
            throw new IllegalStateException(
                    String.format("expected 1 session, but found %d", usersSessions.size()));
        }
        userSession = usersSessions.iterator().next();
        return userSession;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
