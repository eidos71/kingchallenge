package org.eidos.kingchallenge.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.eidos.kingchallenge.KingConfigStaticProperties;
import org.eidos.kingchallenge.KingdomConfManager;
import org.eidos.kingchallenge.KingdomHandlerConf;
import org.eidos.kingchallenge.controller.KingControllerManager;
import org.eidos.kingchallenge.controller.SessionWorkerManager;
import org.eidos.kingchallenge.httpserver.handlers.GenericPageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic KingDom Server
 * 
 * @author eidos71
 *
 */
@SuppressWarnings("restriction")
public class KingdomServer {

	static final Logger LOG = LoggerFactory.getLogger(KingdomServer.class);
	private static final int HTTP_POOL_CONNECTIONS = KingConfigStaticProperties.HTTP_POOL_CONNECTIONS;
	private static final int HTTP_MAX_CONNECTIONS = HTTP_POOL_CONNECTIONS * 2;
	private static final int HTTP_QUEUE_MAX_ITEMS = HTTP_MAX_CONNECTIONS+HTTP_POOL_CONNECTIONS * 2;
	private static final int DELAY_FOR_TERMINATION = 0;
	final int serverPort = Integer.getInteger("serverPort",  KingConfigStaticProperties.BINDING_PORT);

	//static Socket clientSocket;
	
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
		}


	protected void initServer() throws IOException, InterruptedException {

		LOG.info("Sarting server on {} ", serverPort);
		server = HttpServer.create(new InetSocketAddress(serverPort),
				HTTP_POOL_CONNECTIONS);

		createContext();
		initControllers();
		startSessionManager();
		serverExecutor = new ThreadPoolExecutor(HTTP_POOL_CONNECTIONS,
				HTTP_MAX_CONNECTIONS, DELAY_FOR_TERMINATION,
				TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(
						HTTP_QUEUE_MAX_ITEMS), new ThreadPoolExecutor.AbortPolicy());
		server.setExecutor(serverExecutor);
		server.start();
	}
	/**
	 * Starts the SessionManager
	 * Its Scheduled to find for timeout sessiones and remove them periodcally
	 * 
	 */
	private void startSessionManager() {
		//Number of ScheudledThreadsPool to manage the session expiration
		//watchdog. By default we only need 1.
		ScheduledExecutorService scheduledExecutorService = Executors
				.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleAtFixedRate(
				new SessionWorkerManager(KingConfigStaticProperties.LOGINSERVICE ), 0, KingConfigStaticProperties.WORKMAN_SCHEDULE_INSECONDS, TimeUnit.SECONDS);
		
	}
	private void initControllers() {
		KingControllerManager
		.getInstance();
		
	}
	/**
	 * Stop the server.
	 * It has been removed due time constraints
	 * We left here what it was doing before.
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
	@SuppressWarnings("unchecked")
	protected void createContext() {
		for (KingdomHandlerConf confHandler : KingdomConfManager
				.getInstance().getHandlerConfList()) {
			createContext(confHandler.getContext(),
					confHandler.gethandlerType());
		}
	}


}
