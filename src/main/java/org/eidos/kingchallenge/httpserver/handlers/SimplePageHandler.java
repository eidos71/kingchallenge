package org.eidos.kingchallenge.httpserver.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.eidos.kingchallenge.httpserver.utils.HttpExchangeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public final class SimplePageHandler implements  HttpHandler {
	static final Logger LOG = LoggerFactory.getLogger(SimplePageHandler.class);
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", "text/plain");
		HttpExchangeInfo httpExchangeInfo= new HttpExchangeInfo(exchange);
		LOG.debug("{}",httpExchangeInfo.getPath() );
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		
	}
	private  SimplePageHandler() {
		
	}
}
