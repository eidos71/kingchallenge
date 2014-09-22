package org.eidos.kingchallenge.httpserver.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eidos.kingchallenge.KingConfigStaticProperties;
import org.eidos.kingchallenge.controller.KingControllerManager;
import org.eidos.kingchallenge.controller.LoginController;
import org.eidos.kingchallenge.controller.ScoreController;
import org.eidos.kingchallenge.domain.dto.KingResponseDTO;
import org.eidos.kingchallenge.exceptions.KingInvalidSessionException;
import org.eidos.kingchallenge.exceptions.KingRunTimeIOException;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.httpserver.enums.KingControllerEnum;
import org.eidos.kingchallenge.utils.UtilsEnum.Mode;
import org.eidos.kingchallenge.utils.Validator;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Simple page handler It implements the default page for the KingChallenge
 * 
 * @author eidos71
 *
 */
@SuppressWarnings("restriction")
public final class SimplePageHandler implements HttpHandler {


	static final Logger LOG = Logger.getLogger(SimplePageHandler.class.getName());
	private final LoginController loginController;
	private final ScoreController scoreController;
	private final Object locked= new Object();

	public SimplePageHandler() {
		this.loginController = KingControllerManager.getInstance()
				.getLoginController();
		this.scoreController = KingControllerManager.getInstance()
				.getScoreController();

	}

	@SuppressWarnings({  "unchecked" })
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {

		
		KingResponseDTO response = null;
	
		try {
			Map<String, Object> params ;
			synchronized(locked){
				HttpKingExchangeHelper.parseGetParameters(httpExchange);
				HttpKingExchangeHelper.parsePostParameters(httpExchange);
				// Specific to this handler class.
				parseUrlEncodedParameters(httpExchange);
			
				params = Collections.synchronizedMap((Map<String, Object>)httpExchange
						.getAttribute(KingConfigStaticProperties.KING_REQUEST_PARAM) );
			}
	
			if (LOG.isLoggable(Level.FINE)) {
				for (Entry<String, Object> value : params.entrySet()) {
					LOG.fine(String.format("String- %1$s  Value-%2$s",
							value.getKey(), value.getValue()));
				}
				LOG.fine(String.format("response %1$s ", response));
			}
			streamWriterToResponse(HttpURLConnection.HTTP_OK, prepareResponse(params,httpExchange),httpExchange);
			
		} catch (KingInvalidSessionException ex) {
			if (LOG.isLoggable(Level.INFO )  ) LOG.log(Level.INFO," exception",ex);
			streamWriterToResponse(HttpURLConnection.HTTP_FORBIDDEN,
					new KingResponseDTO.Builder().putContentBody(ex.getMessage()).build(),httpExchange);
		} catch (KingRunTimeIOException | LogicKingChallengeException ex) {
			if (LOG.isLoggable(Level.INFO )  )  LOG.log(Level.INFO," exception",ex);
			 streamWriterToResponse(HttpURLConnection.HTTP_INTERNAL_ERROR,
					new KingResponseDTO.Builder().putContentBody(ex.getMessage()).build() ,httpExchange);
		}catch (Exception ex) {
			if (LOG.isLoggable(Level.INFO )  ) LOG.log(Level.INFO," exception",ex);
			streamWriterToResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, new KingResponseDTO.Builder().putContentBody("").build(),httpExchange );
		}
		
	}
	/**
	 * 
	 * @param httpCodeHeader
	 * @param httpExchange 
	 * @param os
	 * @throws IOException 
	 */
	private OutputStream  streamWriterToResponse(int httpCodeHeader, KingResponseDTO responseDto, HttpExchange httpExchange ) throws IOException {
			if (responseDto==null) {
				responseDto=new KingResponseDTO.Builder().putContentBody("").build();
			}
			OutputStream os = null;
			try {
				Headers responseHeaders = httpExchange.getResponseHeaders();
				responseHeaders.set("Content-Type", responseDto.getContentType().code());
				httpExchange.sendResponseHeaders(httpCodeHeader,
						(responseDto == null) ? 0 : responseDto.getContentBody().length());
				os = httpExchange.getResponseBody();
				os.write(responseDto.getContentBody().toString().getBytes());
				return os;
			}finally {
				if (os!=null)
					os.close();
			}
	

	}
	/**
	 * 
	 * @param params
	 * @param controller
	 * @param httpExchange
	 * @return
	 */
	protected KingResponseDTO prepareResponse(Map<String, Object> requestParamMap, HttpExchange httpExchange) {
		KingResponseDTO response;
		
		switch (HttpKingExchangeHelper.defineController( httpExchange)) {
		case HIGHSCORELIST:
				if (scoreController == null)
				throw new LogicKingChallengeException(
						LogicKingError.PROCESSING_ERROR);

			response = scoreController.getHighScoreByLevel(Long
					.parseLong((String) requestParamMap.get("levelid")));
			break;
		case LOGIN:
				if (loginController == null)
				throw new LogicKingChallengeException(
						LogicKingError.PROCESSING_ERROR);

			response = loginController.loginService(Long
					.parseLong((String) requestParamMap.get("userid")));
			break;
		case SCORE:
				if (scoreController == null) 
				throw new LogicKingChallengeException(
						LogicKingError.PROCESSING_ERROR);
			Integer score =null;
			for (Entry<String, Object> elem : requestParamMap.entrySet()) {
	
					//If the key is a number, it is the Post parameter
					try {
						if (Validator.isValidString(elem.getKey(), Mode.NUMERIC)) {
							score= new Integer(elem.getKey());
							break;
						}
					}catch (NumberFormatException e) {
						throw new LogicKingChallengeException(
								LogicKingError.PROCESSING_ERROR, e);
					}catch(NullPointerException e) {
						
					}
	
		
			}
		
			response = scoreController
					.putHighScore(HttpKingExchangeHelper.getKey(requestParamMap, "sessionkey"),
						HttpKingExchangeHelper.getLongValue(requestParamMap,"levelid"), score);
			break;
		case UNKNOWN:
			throw new KingRunTimeIOException("HTTP_BAD_REQUEST");

		default:
				throw new KingRunTimeIOException("HTTP_BAD_REQUEST");

		}
		return response;

	}
	
	/**
	 * Specific parsing of the incoming request, based on the Handler Due the
	 * way the URL pettions are set, is needed to reorder the different
	 * parameters and define what are they needed for.
	 * 
	 * @param exchange
	 * @throws UnsupportedEncodingException
	 */

	private void parseUrlEncodedParameters(HttpExchange exchange)
			throws UnsupportedEncodingException {

		@SuppressWarnings("unchecked")
		Map<String, Object> requestParamsMap = (Map<String, Object>) exchange
				.getAttribute(KingConfigStaticProperties.KING_REQUEST_PARAM);

		String uri = exchange.getRequestURI().toString();
		String[] tokens = uri.split("[/?=]");

		if (tokens.length > 2) {

			if (KingControllerEnum.SCORE.controller().equals(tokens[2])
					|| KingControllerEnum.HIGHSCORELIST.controller().equals(
							tokens[2])) {
				requestParamsMap.put("levelid", tokens[1]);
				requestParamsMap.put("request", tokens[2]);
			} else if (KingControllerEnum.LOGIN.controller().equals(tokens[2])) {
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
