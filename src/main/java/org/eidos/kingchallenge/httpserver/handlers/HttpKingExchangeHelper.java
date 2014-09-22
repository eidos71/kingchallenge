package org.eidos.kingchallenge.httpserver.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eidos.kingchallenge.KingConfigStaticProperties;
import org.eidos.kingchallenge.exceptions.KingRunTimeIOException;
import org.eidos.kingchallenge.httpserver.enums.KingControllerEnum;
import org.eidos.kingchallenge.utils.HttpExchangeUtils;

import com.sun.net.httpserver.HttpExchange;
@SuppressWarnings("restriction")
public class HttpKingExchangeHelper {
	private static final String UTF_8 = "utf-8";

	static final Logger LOG = Logger.getLogger(HttpKingExchangeHelper.class.getName() );

	

	/**
	 * Parses parameters coming from a GET
	 * 
	 * @param exchange
	 * @throws UnsupportedEncodingException
	 */
	protected static void parseGetParameters(final HttpExchange exchange)
			throws UnsupportedEncodingException {

		Map<String, Object> parameters = new HashMap<String, Object>();
		URI requestedUri = exchange.getRequestURI();
		String query = requestedUri.getRawQuery();
		HttpExchangeUtils.parseQuery(query, parameters);

		exchange.setAttribute(KingConfigStaticProperties.KING_REQUEST_PARAM, parameters);
	}
	/**
	 * Parses parameters coming from a POST
	 * 
	 * @param exchange
	 * @throws UnsupportedEncodingException
	 */
	protected static void parsePostParameters(HttpExchange exchange) throws IOException {

		if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
			@SuppressWarnings("unchecked")
			Map<String, Object> parameters = (Map<String, Object>) exchange
					.getAttribute(KingConfigStaticProperties.KING_REQUEST_PARAM);
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
	protected static KingControllerEnum defineController(HttpExchange httpExchange) {
		
/*		Map<String, Object> requestParamsMap = (Map<String, Object>) httpExchange
				.getAttribute(KingConfigConstants.KING_REQUEST_PARAM);*/

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



	public static String getKey(Map<String,Object> requestParamMap, String mapkey ) {
		if (requestParamMap==null)  	throw new KingRunTimeIOException("System Error");
		try {
			return (String) requestParamMap.get( mapkey);

		}catch(NullPointerException er) {
			throw er;
		}catch (Exception er) {
			throw er;
		}
		
	
	}

	public static Long getLongValue(Map<String,Object> requestParamMap, String keyValue ) {
				if (requestParamMap==null)  	throw new KingRunTimeIOException("System Error");
		try {
			LOG.fine(String.format("keyValue, %1$s", keyValue) );
			return	Long.parseLong((String) requestParamMap
					.get(keyValue));
		}catch(NullPointerException | NumberFormatException er) {
			LOG	.warning(String.format("keyvalue, %1$s, %2$s", keyValue, er) );
			throw er;
		}
			
	}
}
