package org.eidos.kingchallenge.httpserver.handlers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.eidos.kingchallenge.KingConfigConstants;
import org.eidos.kingchallenge.exceptions.KingRunTimeIOException;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.httpserver.enums.KingControllerEnum;
import org.eidos.kingchallenge.utils.HttpExchangeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;

public class HttpExchangeInfo {
	private static final String UTF_8 = "utf-8";

	static final Logger LOG = LoggerFactory.getLogger(HttpExchangeInfo.class);



	public HttpExchangeInfo() {
	
	}

	public String getRemoteHostName(HttpExchange httpExchange) {
		return httpExchange.getRemoteAddress().getHostName();
	}

	public String getQuery(HttpExchange httpExchange) {
		return httpExchange.getRequestURI().getQuery();
	}

	public String getPath(HttpExchange httpExchange) {
		return httpExchange.getRequestURI().getPath();
	}

	/**
	 * Parses parameters coming from a GET
	 * 
	 * @param exchange
	 * @throws UnsupportedEncodingException
	 */
	protected void parseGetParameters(HttpExchange exchange)
			throws UnsupportedEncodingException {

		Map<String, Object> parameters = new HashMap<String, Object>();
		URI requestedUri = exchange.getRequestURI();
		String query = requestedUri.getRawQuery();
		HttpExchangeUtils.parseQuery(query, parameters);

		exchange.setAttribute(KingConfigConstants.KING_REQUEST_PARAM, parameters);
	}
	/**
	 * Parses parameters coming from a POST
	 * 
	 * @param exchange
	 * @throws UnsupportedEncodingException
	 */
	protected void parsePostParameters(HttpExchange exchange) throws IOException {

		if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
			@SuppressWarnings("unchecked")
			Map<String, Object> parameters = (Map<String, Object>) exchange
					.getAttribute(KingConfigConstants.KING_REQUEST_PARAM);
			InputStreamReader isr = new InputStreamReader(
					exchange.getRequestBody(), UTF_8);
			BufferedReader br = new BufferedReader(isr);
			String query = br.readLine();
	
			HttpExchangeUtils.parseQuery(query, parameters);

		}

	}
	/**
	 * Usage , to define specify which controll this rquestHander will invoke
	 * @param httpExchange Exchange
	 * @return @KingConfigConstants controller
	 */
	protected KingControllerEnum defineController(HttpExchange httpExchange) {
		@SuppressWarnings("unchecked")
		Map<String, Object> requestParamsMap = (Map<String, Object>) httpExchange
				.getAttribute(KingConfigConstants.KING_REQUEST_PARAM);

		String uri = httpExchange.getRequestURI().toString();
		String[] tokens = uri.split("[/?=]");
		if (tokens.length<2) 
			return KingControllerEnum.UNKNOWN;
		
		if (KingControllerEnum.SCORE.controller().equals(tokens[2]))
			return KingControllerEnum.SCORE;
		else if (KingControllerEnum.HIGHSCORELIST.controller().equals(tokens[2]))
			return KingControllerEnum.HIGHSCORELIST;
		else if (KingControllerEnum.LOGIN.controller().equals(tokens[2]))
			return KingControllerEnum.LOGIN;
		return KingControllerEnum.UNKNOWN;
	}

}
