package org.eidos.kingchallenge.httpserver.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Map.Entry;

import org.eidos.kingchallenge.KingConfigConstants;
import org.eidos.kingchallenge.controller.LoginController;
import org.eidos.kingchallenge.httpserver.enums.KingControllerEnum;
import org.eidos.kingchallenge.persistance.KingdomConfManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public final class SimplePageHandler implements HttpHandler {
	//TODO: Move to a static class

	static final Logger LOG = LoggerFactory.getLogger(SimplePageHandler.class);
	private final LoginController loginController;
	public SimplePageHandler() {
		this.loginController=KingdomConfManager.getInstance().getLoginController();
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String response = null;
		 OutputStream os =null;
		 int httpStatusCode=HttpURLConnection.HTTP_OK;
		Headers responseHeaders = httpExchange.getResponseHeaders();
	
		responseHeaders.set("Content-Type", "text/plain");
		HttpExchangeInfo httpExchangeInfo = new HttpExchangeInfo();
		LOG.debug("{}", httpExchangeInfo.getPath( httpExchange));
		httpExchangeInfo.parseGetParameters(httpExchange);
		httpExchangeInfo.parsePostParameters(httpExchange);
		//Specific to this handler class.
		parseUrlEncodedParameters(httpExchange);
		//
	    @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>)httpExchange.getAttribute(KingConfigConstants.KING_REQUEST_PARAM);
		if (LOG.isDebugEnabled()) {
			for (Entry<String, Object> value : params.entrySet()) 
				LOG.debug("String-{}  Value-{}", value.getKey(),
						value.getValue());
		}
		switch (httpExchangeInfo.defineController(httpExchange) ) {
		case HIGHSCORELIST:
			LOG.debug("HIGHSCORELIST");
			break;
		case LOGIN:
			
			LOG.debug("LOGIN");
			@SuppressWarnings("unchecked")
			Map<String, Object> requestParamsMap = (Map<String, Object>) httpExchange
					.getAttribute(KingConfigConstants.KING_REQUEST_PARAM);
			if (loginController==null) LOG.warn("loginController is null");
			String result=(String)requestParamsMap.get("userid");
			Long userid=Long.getLong(result );
			
			loginController.loginService(userid);
			break;
		case SCORE:
			LOG.debug("SCORE");
			break;
		case UNKNOWN:
			LOG.debug("UNKNOWN");
			httpStatusCode=HttpURLConnection.HTTP_BAD_REQUEST;
			break;
		default:
			httpStatusCode=HttpURLConnection.HTTP_BAD_REQUEST;
			LOG.debug("UNKNOWN");
			break;
		
		}
		httpExchange.sendResponseHeaders(httpStatusCode, (response==null)?0:response.length() );

		   os = httpExchange.getResponseBody();
		  try {
			  os.write(response.toString().getBytes());
		  }finally {
			  os.close();
		  }
	  	}

	/**
	 * Specific parsing of the incoming request, based on the Handler
	 * Due the way the URL pettions are set, is needed to reorder the different
	 * parameters and define what are they needed for.
	 * @param exchange
	 * @throws UnsupportedEncodingException
	 */
	private void parseUrlEncodedParameters(HttpExchange exchange)
			throws UnsupportedEncodingException {

		@SuppressWarnings("unchecked")
		Map<String, Object> requestParamsMap = (Map<String, Object>) exchange
				.getAttribute(KingConfigConstants.KING_REQUEST_PARAM);

		String uri = exchange.getRequestURI().toString();
		String[] tokens = uri.split("[/?=]");

		if (tokens.length > 2) {
		
			if (KingControllerEnum.SCORE.controller().equals(tokens[2])   
					|| KingControllerEnum.HIGHSCORELIST.controller().equals(tokens[2])  ) {
				requestParamsMap.put("levelid", tokens[1]);
				requestParamsMap.put("request", tokens[2]);
			}else if (KingControllerEnum.LOGIN.controller().equals(tokens[2]) ) {
				requestParamsMap.put("userid", tokens[1]);
				requestParamsMap.put("request", tokens[2]);
			} else {
				requestParamsMap.put("request", "unsupported");
			}
		} else {
			requestParamsMap.put("request", "unsupported");
		}
	}
}
