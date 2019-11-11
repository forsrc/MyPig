package com.forsrc.sso.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
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

	@Autowired
	private ClientHttpRequestFactory clientHttpRequestFactory;


	@Bean("restTemplate")
	@Primary
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder.build();
		restTemplate.setRequestFactory(clientHttpRequestFactory);
		return restTemplate;
	}

	@Bean("loadBalancedRestTemplate")
	@LoadBalanced
	public RestTemplate loadBalancedRestTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder.build();
		restTemplate.setRequestFactory(clientHttpRequestFactory);
		return restTemplate;
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	@Bean("tccOAuth2RestTemplate")
	public OAuth2RestTemplate tccOAuth2RestTemplate() {

		ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
		resourceDetails.setUsername("tcc");
		resourceDetails.setPassword("tcc");
		// resourceDetails.setAccessTokenUri("http://MYPIG-SSO-SERVER/sso/oauth/token");
		resourceDetails.setAccessTokenUri(accessTokenUri);
		// resourceDetails.setId("tcc");
		resourceDetails.setClientId("forsrc");
		resourceDetails.setClientSecret("forsrc");
		resourceDetails.setGrantType("password");
		resourceDetails.setScope(Arrays.asList("read", "write"));

		OAuth2RestTemplate tccOAuth2RestTemplate = new MyOAuth2RestTemplate(resourceDetails,
				new DefaultOAuth2ClientContext());
		// tccOAuth2RestTemplate.setMessageConverters(Arrays.asList(new
		// MappingJackson2HttpMessageConverter()));
		// tccOAuth2RestTemplate.setRetryBadAccessTokens(true);

//		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//		requestFactory.setOutputStreaming(false);
//		tccOAuth2RestTemplate.setRequestFactory(requestFactory);

		tccOAuth2RestTemplate.setRequestFactory(clientHttpRequestFactory);		
		List<AccessTokenProvider> providers = Arrays.<AccessTokenProvider> asList(
				new AuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(),
				new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider());
		providers.forEach(i -> {((OAuth2AccessTokenSupport)i).setRequestFactory(clientHttpRequestFactory);});
		AccessTokenProvider accessTokenProvider = new AccessTokenProviderChain(providers);
		tccOAuth2RestTemplate.setAccessTokenProvider(accessTokenProvider);
		return tccOAuth2RestTemplate;
	}

	@Bean
	public ClientHttpRequestFactory clientHttpRequestFactory()
			throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

		SSLConnectionSocketFactory sslConnectionSocketFactory = null;
		final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectionRequestTimeout(5000);
		factory.setReadTimeout(5000);
		factory.setReadTimeout(5000);
		final SSLContextBuilder builder = new SSLContextBuilder();
		try {
			builder.loadTrustMaterial(null, (X509Certificate[] x509Certificate, String s) -> true);
		} catch (NoSuchAlgorithmException e) {
			throw e;
		} catch (KeyStoreException e) {
			throw e;
		}
		try {
			sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(),
					new String[] { "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);
		} catch (NoSuchAlgorithmException e) {
			throw e;
		} catch (KeyManagementException e) {
			throw e;
		}
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", new PlainConnectionSocketFactory())
				.register("https", sslConnectionSocketFactory)
				.build();
		PoolingHttpClientConnectionManager phccm = new PoolingHttpClientConnectionManager(registry);
		phccm.setMaxTotal(500);
		final CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory)
				.setConnectionManager(phccm).setConnectionManagerShared(true).build();
		factory.setHttpClient(httpClient);
		return factory;
	}

}
