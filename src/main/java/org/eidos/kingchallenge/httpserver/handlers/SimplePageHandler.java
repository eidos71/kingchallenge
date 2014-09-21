package org.eidos.kingchallenge.httpserver.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


	static final Logger LOG = LoggerFactory.getLogger(SimplePageHandler.class);
	private final LoginController loginController;
	private final ScoreController scoreController;
	private final Object locked= new Object();

	public SimplePageHandler() {
		this.loginController = KingControllerManager.getInstance()
				.getLoginController();
		this.scoreController = KingControllerManager.getInstance()
				.getScoreController();

	}

	@SuppressWarnings({  "resource", "unchecked" })
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
	
			if (LOG.isDebugEnabled()) {
				for (Entry<String, Object> value : params.entrySet())
					LOG.debug("String-{}  Value-{}", value.getKey(),
							value.getValue());
				LOG.debug("response {}", response);
			}
		

				streamWriterToResponse(HttpURLConnection.HTTP_OK, prepareResponse(params,httpExchange),httpExchange);
		
		} catch (KingInvalidSessionException ex) {
			LOG.info("{}", ex);
			streamWriterToResponse(HttpURLConnection.HTTP_FORBIDDEN,
					new KingResponseDTO.Builder().putContentBody(ex.getMessage()).build(),httpExchange);
		} catch (KingRunTimeIOException | LogicKingChallengeException ex) {
			LOG.info("KingRunTimeIOException or KingRunTimeIOException {}", ex);
			 streamWriterToResponse(HttpURLConnection.HTTP_INTERNAL_ERROR,
					new KingResponseDTO.Builder().putContentBody(ex.getMessage()).build() ,httpExchange);
		}catch (Exception ex) {
			LOG.info("Exception {}", ex);
			streamWriterToResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, new KingResponseDTO.Builder().putContentBody("").build(),httpExchange );
		}catch (Throwable ex) {
			LOG.info("{}", ex);
			 streamWriterToResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, null,httpExchange );
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
			LOG.debug("HIGHSCORELIST");
			if (scoreController == null)
				throw new LogicKingChallengeException(
						LogicKingError.PROCESSING_ERROR);

			response = scoreController.getHighScoreByLevel(Long
					.parseLong((String) requestParamMap.get("levelid")));
			break;
		case LOGIN:
			LOG.debug("LOGIN");
			if (loginController == null)
				throw new LogicKingChallengeException(
						LogicKingError.PROCESSING_ERROR);

			response = loginController.loginService(Long
					.parseLong((String) requestParamMap.get("userid")));
			break;
		case SCORE:
			LOG.debug("SCORE");
			if (scoreController == null) 
				throw new LogicKingChallengeException(
						LogicKingError.PROCESSING_ERROR);
			Integer score =null;
			for (Entry<String, Object> elem : requestParamMap.entrySet()) {
	
					//If the key is a number, it is the Post parameter
					try {
						if (Validator.isValidString(elem.getKey(), Mode.NUMERIC)) {
							score= new Integer(elem.getKey());
							LOG.debug("****** {}", elem.getKey()  );
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
			LOG.debug("UNKNOWN");
			throw new KingRunTimeIOException("HTTP_BAD_REQUEST");

		default:
			LOG.debug("UNKNOWN");
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
