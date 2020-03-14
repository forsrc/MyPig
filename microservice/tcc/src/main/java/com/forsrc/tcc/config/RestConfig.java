package com.forsrc.tcc.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.OAuth2AccessTokenSupport;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;

import com.forsrc.common.core.spring.MyOAuth2RestTemplate;

@Configuration
public class RestConfig {

    @Value("${security.oauth2.client.access-token-uri}")
    private String accessTokenUri;
    
	@Value("${security.oauth2.resource.userInfoUri}")
	private String userInfoEndpointUrl;
	@Value("${security.oauth2.resource.id}")
	private String clientId;

    @Autowired
	private ClientHttpRequestFactory clientHttpRequestFactory;


    @Bean("restTemplate")
    @Primary
    public RestTemplate restTemplate() {
    	RestTemplate restTemplate = new RestTemplate();
    	restTemplate.setRequestFactory(clientHttpRequestFactory);
        return restTemplate;
    }

    @Bean("loadBalancedRestTemplate")
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate() {
    	RestTemplate restTemplate = new RestTemplate();
    	restTemplate.setRequestFactory(clientHttpRequestFactory);
        return restTemplate;
    }

    @Bean("loadBalancedOAuth2RestTemplate")
    @Primary
    @LoadBalanced
    public OAuth2RestTemplate loadBalancedOAuth2RestTemplate(OAuth2ProtectedResourceDetails details) {
        OAuth2RestTemplate oAuth2RestTemplate = new MyOAuth2RestTemplate(details, new DefaultOAuth2ClientContext());
        //oAuth2RestTemplate.setRetryBadAccessTokens(true);
        oAuth2RestTemplate.setRequestFactory(clientHttpRequestFactory);
		List<AccessTokenProvider> providers = Arrays.<AccessTokenProvider> asList(
				new AuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(),
				new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider());
		providers.forEach(i -> {((OAuth2AccessTokenSupport)i).setRequestFactory(clientHttpRequestFactory);});
		AccessTokenProvider accessTokenProvider = new AccessTokenProviderChain(providers);
		oAuth2RestTemplate.setAccessTokenProvider(accessTokenProvider);
        return oAuth2RestTemplate;
    }

    @Bean("oauth2RestTemplate")
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ProtectedResourceDetails details) {
        OAuth2RestTemplate oAuth2RestTemplate = new MyOAuth2RestTemplate(details, new DefaultOAuth2ClientContext());
        //oAuth2RestTemplate.setRetryBadAccessTokens(true);
        oAuth2RestTemplate.setRequestFactory(clientHttpRequestFactory);		
		List<AccessTokenProvider> providers = Arrays.<AccessTokenProvider> asList(
				new AuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(),
				new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider());
		providers.forEach(i -> {((OAuth2AccessTokenSupport)i).setRequestFactory(clientHttpRequestFactory);});
		AccessTokenProvider accessTokenProvider = new AccessTokenProviderChain(providers);
		oAuth2RestTemplate.setAccessTokenProvider(accessTokenProvider);
        return oAuth2RestTemplate;
    }

    @Bean("tccLoadBalancedOAuth2RestTemplate")
    @LoadBalanced
    public OAuth2RestTemplate tccLoadBalancedOAuth2RestTemplate() {

        OAuth2RestTemplate tccOAuth2RestTemplate = new MyOAuth2RestTemplate(tccResourceDetails(), new DefaultOAuth2ClientContext());
        //tccOAuth2RestTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));
        //tccOAuth2RestTemplate.setErrorHandler(new OAuth2ResponseErrorHandler(tccOAuth2RestTemplate));
        //tccOAuth2RestTemplate.setRetryBadAccessTokens(true);
//        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        requestFactory.setOutputStreaming(false);
//        tccOAuth2RestTemplate.setRequestFactory(requestFactory);


		tccOAuth2RestTemplate.setRequestFactory(clientHttpRequestFactory);		
		List<AccessTokenProvider> providers = Arrays.<AccessTokenProvider> asList(
				new AuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(),
				new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider());
		providers.forEach(i -> {((OAuth2AccessTokenSupport)i).setRequestFactory(clientHttpRequestFactory);});
		AccessTokenProvider accessTokenProvider = new AccessTokenProviderChain(providers);
		tccOAuth2RestTemplate.setAccessTokenProvider(accessTokenProvider);
        return tccOAuth2RestTemplate;
    }

    @Bean("tccOAuth2RestTemplate")
    public OAuth2RestTemplate tccOAuth2RestTemplate() {

        OAuth2RestTemplate tccOAuth2RestTemplate = new MyOAuth2RestTemplate(tccResourceDetails(), new DefaultOAuth2ClientContext());
        //tccOAuth2RestTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));
        //tccOAuth2RestTemplate.setErrorHandler(new OAuth2ResponseErrorHandler(tccOAuth2RestTemplate));
        //tccOAuth2RestTemplate.setRetryBadAccessTokens(true);

//        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        requestFactory.setOutputStreaming(false);
//        tccOAuth2RestTemplate.setRequestFactory(requestFactory);
        
        tccOAuth2RestTemplate.setRequestFactory(clientHttpRequestFactory);		
		List<AccessTokenProvider> providers = Arrays.<AccessTokenProvider> asList(
				new AuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(),
				new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider());
		providers.forEach(i -> {((OAuth2AccessTokenSupport)i).setRequestFactory(clientHttpRequestFactory);});
		AccessTokenProvider accessTokenProvider = new AccessTokenProviderChain(providers);
		tccOAuth2RestTemplate.setAccessTokenProvider(accessTokenProvider);
        return tccOAuth2RestTemplate;
    }

    private ResourceOwnerPasswordResourceDetails tccResourceDetails() {
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername("tcc");
        resourceDetails.setPassword("tcc");
        //resourceDetails.setAccessTokenUri("http://SPRINGBOOT-SSO-SERVER/sso/oauth/token");
        resourceDetails.setAccessTokenUri(accessTokenUri);
        //resourceDetails.setId("tcc");
        resourceDetails.setClientId("forsrc");
        resourceDetails.setClientSecret("forsrc");
        resourceDetails.setGrantType("password");
        resourceDetails.setScope(Arrays.asList("read", "write"));
        return resourceDetails;
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    /*
    public static class OAuth2ResponseErrorHandler implements ResponseErrorHandler {

        private OAuth2RestTemplate oAuth2RestTemplate;

        public OAuth2ResponseErrorHandler(OAuth2RestTemplate oAuth2RestTemplate) {
            this.oAuth2RestTemplate = oAuth2RestTemplate;
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            int statusCode = response.getStatusCode().value();
            return statusCode == HttpStatus.UNAUTHORIZED.value();
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            oAuth2RestTemplate.getOAuth2ClientContext().setAccessToken(null);
            oAuth2RestTemplate.getOAuth2ClientContext().getAccessToken().getValue();
        }

    }
     */

	@Bean
	public ClientHttpRequestFactory clientHttpRequestFactory()
			throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

		SSLConnectionSocketFactory sslConnectionSocketFactory = null;
		final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectionRequestTimeout(5000);
		factory.setReadTimeout(5000);
		factory.setConnectTimeout(5000);
		SSLContext sslContext = null;
		final SSLContextBuilder builder = new SSLContextBuilder();
		try {
			sslContext = builder.loadTrustMaterial(null, (X509Certificate[] x509Certificate, String s) -> true).build();
		} catch (NoSuchAlgorithmException e) {
			throw e;
		} catch (KeyStoreException e) {
			throw e;
		}
		sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
					new String[] { "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);
	
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", new PlainConnectionSocketFactory())
				.register("https", sslConnectionSocketFactory)
				.build();
		PoolingHttpClientConnectionManager phccm = new PoolingHttpClientConnectionManager(registry);
		phccm.setMaxTotal(500);
		final CloseableHttpClient httpClient = HttpClients
				.custom()
				.setSSLSocketFactory(sslConnectionSocketFactory)
				.setConnectionManager(phccm)
				.setConnectionManagerShared(true)
				.build();
		factory.setHttpClient(httpClient);
		return factory;
	}

	@Bean
	public UserInfoTokenServices userInfoTokenServices() {
		
		UserInfoTokenServices userInfoTokenServices = new MyUserInfoTokenServices(userInfoEndpointUrl, clientId);
		userInfoTokenServices.setRestTemplate(tccOAuth2RestTemplate());
		return userInfoTokenServices;
	}
	
	
    public static class MyUserInfoTokenServices extends UserInfoTokenServices {

	    public MyUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
		    super(userInfoEndpointUrl, clientId);
        }  
    }
}
