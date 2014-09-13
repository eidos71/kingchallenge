package org.eidos.kingchallenge.httpserver.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eidos.kingchallenge.exceptions.KingRunTimeIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;

public class HttpExchangeInfo {
	static final Logger LOG = LoggerFactory.getLogger(HttpExchangeInfo.class);
	HttpExchange httpExchange;
	@SuppressWarnings("unchecked")
	Map<String, Object> parameterMap;
	String urlQuery;
	String requestBody;
	String[] args;

	public HttpExchangeInfo(HttpExchange httpExchange) {
		this.httpExchange = httpExchange;
	}

	public String getRemoteHostName() {
		return httpExchange.getRemoteAddress().getHostName();
	}

	public String getQuery() {
		return httpExchange.getRequestURI().getQuery();
	}

	public String getPath() {
		return httpExchange.getRequestURI().getPath();
	}

	public String[] getPathArgs() {
		if (args == null) {
			args = httpExchange.getRequestURI().getPath().substring(1)
					.split("/");
		}
		return args;
	}

	public int getPathLength() {
		return getPathArgs().length;
	}

	public String getRequestBody() {
		if (requestBody == null) {
			requestBody = convertStreamToString(httpExchange.getRequestBody());
		}
		return requestBody;
	}

	public boolean isParameter(String name) {
		Object value = getParameterMap().get(name);
		if (value == null)
			return false;
		return value.equals("on");
	}

	public Map<String, Object> getParameterMap() {
		if (parameterMap == null) {
			parseParameterMap();
		}
		return parameterMap;
	}

	private void parseParameterMap() {
		parameterMap = new HashMap<String, Object>();
		urlQuery = httpExchange.getRequestURI().getQuery();
		if (httpExchange.getRequestMethod().equals("POST")) {
			urlQuery = convertStreamToString(httpExchange.getRequestBody());
		}
		LOG.debug("parseParameterMap {}", httpExchange.getRequestMethod());
		if (urlQuery == null) {
			return;
		}
		int index = 0;
		while (index < urlQuery.length()) {
			int endIndex = urlQuery.indexOf("&", index);
			if (endIndex > 0) {
				put(urlQuery.substring(index, endIndex));
				index = endIndex + 1;
			} else if (index < urlQuery.length()) {
				put(urlQuery.substring(index));
				return;
			}
		}
	}

	private void put(String substring) {
		LOG.debug(" substring->{}", substring);

	}

	public Object getParameter(String key) {
		return parameterMap.get(key);
	}

	/**
	 * Method that converts a InputStrea
	 * 
	 * @param requestBody
	 *            InputStream from requestBody
	 * @return String
	 */
	private String convertStreamToString(InputStream requestBody) {
		BufferedInputStream bis = new BufferedInputStream(requestBody);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int result;
		try {
			result = bis.read();
			while (result != -1) {
				byte b = (byte) result;
				buf.write(b);
				result = bis.read();
			}
			return buf.toString();
		} catch (IOException e) {
			throw new KingRunTimeIOException(e);
		}

	}
}
