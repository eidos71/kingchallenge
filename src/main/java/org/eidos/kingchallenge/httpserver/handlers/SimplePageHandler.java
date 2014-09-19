package org.eidos.kingchallenge.httpserver.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Map.Entry;

import org.eidos.kingchallenge.KingConfigConstants;
import org.eidos.kingchallenge.controller.KingControllerManager;
import org.eidos.kingchallenge.controller.LoginController;
import org.eidos.kingchallenge.controller.ScoreController;
import org.eidos.kingchallenge.exceptions.KingRunTimeIOException;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.httpserver.enums.KingControllerEnum;
import org.eidos.kingchallenge.httpserver.utils.MediaContentTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Simple page handler
 * It implements the default page for the KingChallenge
 * @author eidos71
 *
 */
public final class SimplePageHandler implements HttpHandler {
	//Theard Local Exchange and Info storage
	 private static ThreadLocal <HttpExchange> tlEx = new ThreadLocal<HttpExchange>();
	 private static ThreadLocal <HttpExchangeInfo> tlExInfo= new ThreadLocal<HttpExchangeInfo>(); 


	static final Logger LOG = LoggerFactory.getLogger(SimplePageHandler.class);
	private final LoginController loginController;
	private final ScoreController scoreController;
	public SimplePageHandler() {
		this.loginController=KingControllerManager.getInstance().getLoginController();
		this.scoreController=KingControllerManager.getInstance().getScoreController();
		
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
	
		tlEx.set(httpExchange);
		tlExInfo.set( new HttpExchangeInfo() );
		
		String response = null;
		OutputStream os = null;
		int httpStatusCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
		try {
		
		if (tlExInfo.get()==null) LOG.info("null tlExinfo");
		if (tlEx.get()==null) LOG.info("null tlEx");
	
		LOG.debug("{}", tlExInfo.get().getPath(tlEx.get()));
		tlExInfo.get().parseGetParameters(tlEx.get());
		tlExInfo.get().parsePostParameters(tlEx.get());
		// Specific to this handler class.
		parseUrlEncodedParameters(tlEx.get());
		//
		@SuppressWarnings("unchecked")
		Map<String, Object> params = (Map<String, Object>) tlEx.get()
				.getAttribute(KingConfigConstants.KING_REQUEST_PARAM);
		if (LOG.isDebugEnabled()) {
			for (Entry<String, Object> value : params.entrySet()) 
				LOG.debug("String-{}  Value-{}", value.getKey(),
						value.getValue());
		}		
		Headers responseHeaders = tlEx.get().getResponseHeaders();
		responseHeaders.set("Content-Type", MediaContentTypeEnum.TEXT_PLAIN.code());


			// we send to the bussiness Logic and return the response
			response=prepareResponse(params);
			//Prepare response
			tlEx.get().sendResponseHeaders(HttpURLConnection.HTTP_OK, (response == null) ? 0
					: response.length());
			os = tlEx.get().getResponseBody();
			os.write(response.toString().getBytes());
		}catch (KingRunTimeIOException ex) {
			tlEx.get().sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, (response == null) ? 0
					: response.length());
			response=ex.getMessage();
			os = tlEx.get().getResponseBody();
			os.write(response.toString().getBytes());
		}catch (LogicKingChallengeException ex) {

			tlEx.get().sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, (response == null) ? 0
					: response.length());
			response=ex.getMessage();
			os = tlEx.get().getResponseBody();
			os.write(response.toString().getBytes());
		}catch(Exception ex) {
			LOG.error(" big boom {}", ex);
			tlEx.get().sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, (response == null) ? 0
					: response.length());

		} finally {
			os.close();
			tlExInfo.remove();
			tlEx.remove();
		}
	}
	/**
	 * 
	 * @param params
	 * @param controller
	 * @param httpExchange 
	 * @return
	 */
	protected String prepareResponse(Map<String, Object> requestParamMap   ) {
		String response ="";
	switch (tlExInfo.get().defineController(tlEx.get())  ) {
		case HIGHSCORELIST:
			LOG.debug("HIGHSCORELIST");
			if (scoreController==null) 	throw new  LogicKingChallengeException(LogicKingError.PROCESSING_ERROR);
			requestParamMap.get("levelid");
			
			break;
		case LOGIN:
			LOG.debug("LOGIN");
			if (loginController==null) 	throw new  LogicKingChallengeException(LogicKingError.PROCESSING_ERROR);
			response = loginController.loginService(Long.parseLong((String)requestParamMap.get("userid") ) );
			break;
		case SCORE:
			LOG.debug("SCORE");
			if (scoreController==null) 	throw new  LogicKingChallengeException(LogicKingError.PROCESSING_ERROR);
			break;
		case UNKNOWN:
			LOG.debug("UNKNOWN");
			throw new  KingRunTimeIOException("HTTP_BAD_REQUEST");
	
		default:
			LOG.debug("UNKNOWN");
			throw new  KingRunTimeIOException("HTTP_BAD_REQUEST");
			
		}
		return response;
		
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
