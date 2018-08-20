package com.personal.segments.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

/**
 * 基于apache httpclient工具类，复用连接
 *
 */
public class HttpUtils {

	public static final int DEFAULT_MAX_CONNECTIONS = 100;
	public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 100;
	public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;		//ms
	public static final int DEFAULT_HTTP_CLIENT_SOCKET_TIMEOUT = 5000; // ms

	private static volatile HttpClient httpClient = null;
	private static RequestConfig defaultRequestConfig = null;

	private HttpUtils() {
	}

	private static HttpClient buildClient() {
		// 设置超时自动断开无效连接
		// 为所有请求设置默认配置
		defaultRequestConfig = RequestConfig.custom().setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
				.setConnectionRequestTimeout(DEFAULT_CONNECTION_TIMEOUT)
				.setSocketTimeout(DEFAULT_HTTP_CLIENT_SOCKET_TIMEOUT).build();

		SSLContext sslcontext = null;
		try {
			sslcontext = createIgnoreVerifySSL();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", new SSLConnectionSocketFactory(sslcontext)).build();

		// 创建足够的连接
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		connectionManager.setMaxTotal(DEFAULT_MAX_CONNECTIONS);
		connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);

		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(defaultRequestConfig)
				.setConnectionManager(connectionManager)
				// 支持jvm代理设置
				.useSystemProperties().setRedirectStrategy(new DefaultRedirectStrategy()).setUserAgent("-") // needed
																											// for
																											// Okta
				.build();

		return httpClient;
	}

	public static HttpClient getHttpClient() {
		if (httpClient == null) {
			synchronized (HttpUtils.class) {
				if (httpClient == null) {
					buildClient();
				}
			}
		}
		return httpClient;
	}

	public static String doPost(String url) {
		HttpPost post = new HttpPost(url);
		post.setConfig(defaultRequestConfig);
		post.setHeader("Content-type", "application/json;charset=UTF-8");
		
		String res = null;
		
		try {
			res = EntityUtils.toString(getHttpClient().execute(post).getEntity());
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}

		return res;
	}
	
	/**
	 * 绕过验证
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sc = SSLContext.getInstance("SSLv3");

		// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};

		sc.init(null, new TrustManager[] { trustManager }, null);
		return sc;
	}
}
