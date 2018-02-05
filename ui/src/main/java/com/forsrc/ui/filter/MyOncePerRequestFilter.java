package com.forsrc.ui.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;


@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyOncePerRequestFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyOncePerRequestFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getParameter("access_token");
        if (token != null) {
            LOGGER.info("--> access_token: {}", token);
            token = URLDecoder.decode(token, "UTF-8");
            HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
            requestWrapper.addHeader("Authorization", "Bearer " + token);
        }
        filterChain.doFilter(request, response);
    }

    public static class HeaderMapRequestWrapper extends HttpServletRequestWrapper {

        public HeaderMapRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        private Map<String, String> headerMap = new HashMap<String, String>();

        public void addHeader(String name, String value) {
            headerMap.put(name, value);
        }

        @Override
        public String getHeader(String name) {
            String headerValue = super.getHeader(name);
            if (headerMap.containsKey(name)) {
                headerValue = headerMap.get(name);
            }
            return headerValue;
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> names = Collections.list(super.getHeaderNames());
            for (String name : headerMap.keySet()) {
                names.add(name);
            }
            return Collections.enumeration(names);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            List<String> values = Collections.list(super.getHeaders(name));
            if (headerMap.containsKey(name)) {
                values.add(headerMap.get(name));
            }
            return Collections.enumeration(values);
        }

    }
}