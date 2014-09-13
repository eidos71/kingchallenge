package org.eidos.kingchallenge.httpserver.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.eidos.kingchallenge.exceptions.KingRunTimeException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Generic Page Handler deals with a standard petition
 * 
 * @author eidos71
 *
 */
public class GenericPageHandler implements HttpHandler {
	private final HttpHandler delegatedHandler;

	public <T extends HttpHandler> GenericPageHandler(Class<T> clazz) {
		try {
			// Only uses public 0 arguments for now
			this.delegatedHandler = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new KingRunTimeException(e);
		}
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		/**
		 * No delegator has been set this is an error and is reported to the
		 * client
		 */
		if (delegatedHandler == null) {
			try {
				exchange.sendResponseHeaders(
						HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
			} finally {
				// close exchange;
				exchange.close();
			}
			return;
		}
		delegatedHandler.handle(exchange);
	}

}
