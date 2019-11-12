package com.forsrc.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

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
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.Host;
import org.springframework.cloud.netflix.zuul.filters.route.SimpleHostRoutingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class RestConfig {

	@Autowired
	private ClientHttpRequestFactory clientHttpRequestFactory;

	// @Bean
	public CloseableHttpClient httpClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				return true;
			}
		}).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
		return HttpClients.custom().setSSLSocketFactory(sslsf).build();

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
				.register("http", new PlainConnectionSocketFactory()).register("https", sslConnectionSocketFactory)
				.build();
		PoolingHttpClientConnectionManager phccm = new PoolingHttpClientConnectionManager(registry);
		phccm.setMaxTotal(500);
		final CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory)
				.setConnectionManager(phccm).setConnectionManagerShared(true).build();
		factory.setHttpClient(httpClient);
		return factory;
	}

	@Bean
	public SimpleHostRoutingFilter simpleHostRoutingFilter(ProxyRequestHelper helper, ZuulProperties properties,
			ApacheHttpClientConnectionManagerFactory connectionManagerFactory,
			ApacheHttpClientFactory httpClientFactory) {
		return new MySimpleHostRoutingFilter(helper, properties, connectionManagerFactory, httpClientFactory);
	}

	public static class MySimpleHostRoutingFilter extends SimpleHostRoutingFilter {

		private Host hostProperties;
		private ApacheHttpClientFactory httpClientFactory;
		private HttpClientConnectionManager connectionManager;
		private boolean sslHostnameValidationEnabled;

		public MySimpleHostRoutingFilter(ProxyRequestHelper helper, ZuulProperties properties,
				ApacheHttpClientConnectionManagerFactory connectionManagerFactory,
				ApacheHttpClientFactory httpClientFactory) {
			super(helper, properties, connectionManagerFactory, httpClientFactory);
			this.hostProperties = properties.getHost();
			this.httpClientFactory = httpClientFactory;
			this.sslHostnameValidationEnabled = properties.isSslHostnameValidationEnabled();
		}

		@Override
		protected CloseableHttpClient newClient() {

			final RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(this.hostProperties.getConnectionRequestTimeoutMillis())
					.setSocketTimeout(this.hostProperties.getSocketTimeoutMillis())
					.setConnectTimeout(this.hostProperties.getConnectTimeoutMillis())
					.setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();

			HttpClientBuilder httpClientBuilder = HttpClients.custom();
			if (!this.sslHostnameValidationEnabled) {
				httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
			}
			return httpClientBuilder.setConnectionManager(newConnectionManager()).disableContentCompression()
					.useSystemProperties().setDefaultRequestConfig(requestConfig)
					.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
					.setRedirectStrategy(new RedirectStrategy() {
						@Override
						public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)
								throws ProtocolException {
							return false;
						}

						@Override
						public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response,
								HttpContext context) throws ProtocolException {
							return null;
						}
					}).build();
		}

		private PoolingHttpClientConnectionManager newConnectionManager() {
			try {

				final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
				sslContext.init(null, new TrustManager[] { new X509TrustManager() {
					@Override
					public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
							throws CertificateException {
					}

					@Override
					public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
							throws CertificateException {
					}

					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
				} }, new SecureRandom());

				RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder
						.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE);
				if (this.sslHostnameValidationEnabled) {
					registryBuilder.register("https", new SSLConnectionSocketFactory(sslContext));
				} else {
					registryBuilder.register("https",
							new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE));
				}
				final Registry<ConnectionSocketFactory> registry = registryBuilder.build();

				PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry,
						null, null, null, hostProperties.getTimeToLive(), hostProperties.getTimeUnit());
				connectionManager.setMaxTotal(hostProperties.getMaxTotalConnections());
				connectionManager.setDefaultMaxPerRoute(hostProperties.getMaxPerRouteConnections());
				return connectionManager;

			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}
}
