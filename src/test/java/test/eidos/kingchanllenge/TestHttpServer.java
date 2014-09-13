package test.eidos.kingchanllenge;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class TestHttpServer {
	 void delay () {
		           Thread.yield();
		        try {
		              Thread.sleep (200);
		           } catch (InterruptedException e) {}
	 }   
	private void doSomething() {
		System.out.println("do Something" + Thread.currentThread().getName());
		delay();
	}
	public static void main(String[] args) throws Exception {
		//HttpURLConnection.
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 10);
		server.createContext("/test2", new MyHandler());
		server.createContext("/test3", new MyHandler());
		server.setExecutor(Executors.newCachedThreadPool()); // creates a
																// default
																// executor
		server.start();
	}

	static class MyHandler implements HttpHandler {
		public void handle(HttpExchange exchange) throws IOException {
			String requestMethod = exchange.getRequestMethod();


			
			if (requestMethod.equalsIgnoreCase("GET")) {
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
	}
}
