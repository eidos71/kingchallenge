package org.eidos.kingchallenge.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.eidos.kingchallenge.httpserver.handlers.GenericPageHandler;
import org.eidos.kingchallenge.model.KingdomHandlerConf;
import org.eidos.kingchallenge.persistance.KingdomHandlerConfManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic KingDom Server
 * 
 * @author eidos71
 *
 */
public class KingdomServer {

	static final Logger LOG = LoggerFactory.getLogger(KingdomServer.class);
	private static final int HTTP_POOL_CONNECTIONS = 50;
	private static final int HTTP_MAX_CONNECTIONS = HTTP_POOL_CONNECTIONS * 2;
	private static final int DELAY_FOR_TERMINATION = 0;
	final int serverPort = Integer.getInteger("serverPort", 8000);
	final int shutdownPort = Integer.getInteger("shutdownPort", 8009);
	static Socket clientSocket;
	HttpServer server;
	private ExecutorService serverExecutor;
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new KingdomServer().start();
	}
	protected void start() throws Exception {
		initServer();
		new ServerSocket(shutdownPort).accept();
		requestShutdown();

	}

	protected void initServer() throws IOException {
		LOG.info("Sarting server on {} :  Shutdown port on:{} ", serverPort,
				shutdownPort);
		server = HttpServer.create(new InetSocketAddress(serverPort),
				HTTP_POOL_CONNECTIONS);

		createContext();
		serverExecutor = new ThreadPoolExecutor(HTTP_POOL_CONNECTIONS,
				HTTP_MAX_CONNECTIONS, DELAY_FOR_TERMINATION,
				TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(
						HTTP_POOL_CONNECTIONS));
		server.setExecutor(serverExecutor);
		server.start();
	}

	/**
	 * Stop the server.
	 * 
	 * @return
	 */
	public boolean stop() {
		if (server != null) {
			server.stop(0);
			serverExecutor.shutdown();
			return true;

		}
		return false;
	}

	/**
	 * Redoes a shutdown
	 */
	protected void requestShutdown() {
		try {
			if (stop()) {
				LOG.info("restarting server");
				initServer();
			}

		} catch (Exception e) {
			LOG.warn("starting server {}", e);
		}
	}

	protected <T extends HttpHandler> void createContext(String context,
			Class<T> clazz) {
		LOG.info("context: {},  handler: {}",context, clazz.getName());
		server.createContext(context, new GenericPageHandler(clazz));
	}

	/**
	 * Initalizates all the createContexts
	 */
	protected void createContext() {
		for (KingdomHandlerConf confHandler : KingdomHandlerConfManager
				.getInstance().getHandlerConfList()) {
			createContext(confHandler.getContext(),
					confHandler.gethandlerType());
		}
	}


}
