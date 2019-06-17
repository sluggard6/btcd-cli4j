package com.neemre.btcdcli4j.core.http.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;

import com.neemre.btcdcli4j.core.NodeProperties;
import com.neemre.btcdcli4j.core.common.Constants;
import com.neemre.btcdcli4j.core.common.Errors;
import com.neemre.btcdcli4j.core.http.HttpConstants;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class OkHttpClientImpl implements SimpleHttpClient {

//	private static final Logger LOG = LoggerFactory.getLogger(SimpleHttpClientImpl.class);

	private OkHttpClient provider;
	private Properties nodeConfig;

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public OkHttpClientImpl(OkHttpClient provider, Properties nodeConfig) {
		super();
		this.provider = provider;
		this.nodeConfig = nodeConfig;
	}

	@Override
	public String execute(String reqMethod, String reqPayload) {
		try {
			Response response = provider.newCall(getNewRequest(reqMethod, reqPayload)).execute();
			return response.body().toString();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void close() {

	}

	private Request getNewRequest(String reqMethod, String reqPayload)
			throws URISyntaxException, UnsupportedEncodingException, MalformedURLException {
		RequestBody body = RequestBody.create(JSON, reqPayload);

		Request request = new Request.Builder()
				.addHeader(HttpConstants.HEADER_AUTH,
						HttpConstants.AUTH_SCHEME_BASIC + " " + getCredentials(HttpConstants.AUTH_SCHEME_BASIC))
				.url(new URL(String.format("%s://%s:%s/", nodeConfig.getProperty(NodeProperties.RPC_PROTOCOL.getKey()),
						nodeConfig.getProperty(NodeProperties.RPC_HOST.getKey()),
						nodeConfig.getProperty(NodeProperties.RPC_PORT.getKey()))))
				.post(body).build();
		return request;
	}

	private String getCredentials(String authScheme) {
		if (authScheme.equals(HttpConstants.AUTH_SCHEME_NONE)) {
			return Constants.STRING_EMPTY;
		} else if (authScheme.equals(HttpConstants.AUTH_SCHEME_BASIC)) {
			return Base64.encodeBase64String((nodeConfig.getProperty(NodeProperties.RPC_USER.getKey()) + ":"
					+ nodeConfig.getProperty(NodeProperties.RPC_PASSWORD.getKey())).getBytes());
		}
		throw new IllegalArgumentException(Errors.ARGS_HTTP_AUTHSCHEME_UNSUPPORTED.getDescription());
	}

}
