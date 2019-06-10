package com.forsrc.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.HTTPS_SCHEME;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.HTTP_SCHEME;

@Configuration
public class HttpsConfig {
    @Value("${server.port}")
    private Integer port;
    @Value("${server.http.port}")
    private Integer httpPort;

//    static {
//        HostnameVerifier hv = new HostnameVerifier() {
//            public boolean verify(String urlHostName, SSLSession session) {
//                System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
//                return true;
//            }
//        };
//        HttpsURLConnection.setDefaultHostnameVerifier(hv);
//    }

    @Bean
    @ConditionalOnProperty(name = "server.http.http2https", havingValue = "true", matchIfMissing = false)
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint constraint = new SecurityConstraint();
                constraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                constraint.addCollection(collection);
                context.addConstraint(constraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(httpConnector());
        return tomcat;
    }

    @Bean
    @ConditionalOnProperty(name = "server.http.http2https", havingValue = "true", matchIfMissing = false)
    public Connector httpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setSecure(false);
        connector.setPort(httpPort);
        connector.setRedirectPort(port);
        return connector;
    }

//    @Bean
//    public RegistryBuilder registryBuilder() throws NoSuchAlgorithmException, KeyManagementException {
//
//        TrustManager[] trustAllCerts = new TrustManager[1];
//        TrustManager trustManager = new Trust();
//        trustAllCerts[0] = trustManager;
//        SSLContext sslContext = SSLContext.getInstance("SSL");
//        sslContext.init(null, trustAllCerts, null);
//
//        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
//            public boolean verify(String urlHostName, SSLSession session) {
//                System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
//                return true;
//            }
//        };
//
//        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
//        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
//
//
//        return RegistryBuilder.<ConnectionSocketFactory>create()
//                .register(HTTP_SCHEME, PlainConnectionSocketFactory.INSTANCE)
//                .register(HTTPS_SCHEME, new SSLConnectionSocketFactory(sslContext, hostnameVerifier));
//    }
//
//    static class Trust implements TrustManager, X509TrustManager {
//
//        @Override
//        public X509Certificate[] getAcceptedIssuers() {
//            return null;
//        }
//
//
//        @Override
//        public void checkServerTrusted(X509Certificate[] certs, String authType) {
//
//        }
//
//        @Override
//        public void checkClientTrusted(X509Certificate[] certs, String authType) {
//        }
//
//
//        public boolean isServerTrusted(
//                java.security.cert.X509Certificate[] certs) {
//            return true;
//        }
//
//        public boolean isClientTrusted(
//                java.security.cert.X509Certificate[] certs) {
//            return true;
//        }
//    }

}
