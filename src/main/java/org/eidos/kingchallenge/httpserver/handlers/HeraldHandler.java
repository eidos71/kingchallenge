package org.eidos.kingchallenge.httpserver.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HeraldHandler implements HttpHandler {
	static final Logger LOG = LoggerFactory.getLogger(HeraldHandler.class);

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		LOG.info("herald summoned");

		Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", "text/plain");
		exchange.sendResponseHeaders(200, 0);
		OutputStream responseBody = exchange.getResponseBody();
		Headers requestHeaders = exchange.getRequestHeaders();
		Set<String> keySet = requestHeaders.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			List values = requestHeaders.get(key);
			String s = key + " = " + values.toString() + "\n";
			responseBody.write(s.getBytes());
		}
		responseBody.close();

	}

}
