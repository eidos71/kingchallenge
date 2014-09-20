package org.eidos.kingchallenge.httpserver.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
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
import org.eidos.kingchallenge.httpserver.utils.MediaContentTypeEnum;
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
	// Theard Local Exchange and Info storage
	private static ThreadLocal<HttpExchange> tlEx = new ThreadLocal<HttpExchange>();
	private static ThreadLocal<HttpKingExchangeHelper> tlExInfo = new ThreadLocal<HttpKingExchangeHelper>();

	static final Logger LOG = LoggerFactory.getLogger(SimplePageHandler.class);
	private final LoginController loginController;
	private final ScoreController scoreController;

	public SimplePageHandler() {
		this.loginController = KingControllerManager.getInstance()
				.getLoginController();
		this.scoreController = KingControllerManager.getInstance()
				.getScoreController();

	}

	@SuppressWarnings({  "resource" })
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {

		tlEx.set(httpExchange);
		tlExInfo.set(new HttpKingExchangeHelper());

		KingResponseDTO response = null;
		OutputStream os = null;
	
		try {

			if (tlExInfo.get() == null) LOG.info("null tlExinfo");
			if (tlEx.get() == null) LOG.info("null tlEx");

			LOG.debug("{}", tlExInfo.get().getPath(tlEx.get()));
			tlExInfo.get().parseGetParameters(tlEx.get());
			tlExInfo.get().parsePostParameters(tlEx.get());
			// Specific to this handler class.
			parseUrlEncodedParameters(tlEx.get());
			//
			@SuppressWarnings("unchecked")
			Map<String, Object> params = (Map<String, Object>) tlEx.get()
					.getAttribute(KingConfigStaticProperties.KING_REQUEST_PARAM);
			if (LOG.isDebugEnabled()) {
				for (Entry<String, Object> value : params.entrySet())
					LOG.debug("String-{}  Value-{}", value.getKey(),
							value.getValue());
			}
			LOG.debug("response {}", response);
			os= streamWriterToResponse(HttpURLConnection.HTTP_OK, prepareResponse(params));
		} catch (KingInvalidSessionException ex) {
			LOG.info("{}", ex);
			os= streamWriterToResponse(HttpURLConnection.HTTP_FORBIDDEN,
					new KingResponseDTO.Builder().putContentBody(ex.getMessage()).build() );
		} catch (KingRunTimeIOException | LogicKingChallengeException ex) {
			LOG.info("{}", ex);
			os= streamWriterToResponse(HttpURLConnection.HTTP_INTERNAL_ERROR,
					new KingResponseDTO.Builder().putContentBody(ex.getMessage()).build() );
		}catch (Exception ex) {
			LOG.info("{}", ex);
			os= streamWriterToResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, null );
		}catch (Throwable ex) {
			LOG.info("{}", ex);
			os= streamWriterToResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, null );
		} finally {
			os.close();
			tlExInfo.remove();
			tlEx.remove();
		}
	}
	/**
	 * 
	 * @param httpCodeHeader
	 * @param os
	 * @throws IOException 
	 */
	private OutputStream  streamWriterToResponse(int httpCodeHeader, KingResponseDTO response ) throws IOException {
		OutputStream os = null;

		Headers responseHeaders = tlEx.get().getResponseHeaders();
		responseHeaders.set("Content-Type", response.getContentType().code());
		tlEx.get().sendResponseHeaders(HttpURLConnection.HTTP_OK,
				(response == null) ? 0 : response.getContentBody().length());
		os = tlEx.get().getResponseBody();
		os.write(response.getContentBody().toString().getBytes());
		return os;
	}
	/**
	 * 
	 * @param params
	 * @param controller
	 * @param httpExchange
	 * @return
	 */
	protected KingResponseDTO prepareResponse(Map<String, Object> requestParamMap) {
		KingResponseDTO response;
		switch (tlExInfo.get().defineController(tlEx.get())) {
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
					LOG.debug("****** {}", elem.getKey()  );
					//If the key is a number, it is the Post parameter
					if (Validator.isValidString(elem.getKey(), Mode.NUMERIC)) {
						score= new Integer(elem.getKey());
						break;
					}
		
			}
			response = scoreController
					.putHighScore((String) requestParamMap.get("sessionkey"),
							Long.parseLong((String) requestParamMap
									.get("levelid")), score);
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
