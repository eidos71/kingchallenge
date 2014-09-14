package org.eidos.kingchallenge.persistance;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.eidos.kingchallenge.controller.LoginController;
import org.eidos.kingchallenge.controller.SimpleLoginController;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.model.KingdomHandlerConf;
import org.eidos.kingchallenge.services.EmptyLoginService;
import org.eidos.kingchallenge.utils.FilReaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager that helds all Handler managed by the Kingdom
 * 
 * @author eidos71
 *
 */
@ThreadSafe
public class KingdomConfManager {
	static final Logger LOG = LoggerFactory
			.getLogger(KingdomConfManager.class);

	private static final String HANDLER_PROPERTIES = "handler.properties";
	@GuardedBy("lock")
	private static volatile KingdomConfManager instance = null;
	private final static Object lock = new Object();
	final private Set<KingdomHandlerConf> handlerConfSet;
	
	final private LoginController loginController;



	/**
	 * Usinc confinement to assure thread-bound scope private singleton
	 * 
	 * @throws IOException
	 */
	private KingdomConfManager()  {
		handlerConfSet= new HashSet<KingdomHandlerConf> ();
		loginController=
				new SimpleLoginController.Builder(new EmptyLoginService()).build();
		//Lets init...
		init();
	}
	/**
	 * Init load from the Handler
	 * Comes for a properti files
	 */
	private void init() {
		
		 InputStream inputStream = 
	    		   getClass().getClassLoader().getResourceAsStream(HANDLER_PROPERTIES);
		
		Map<String, String> handlerMap = FilReaderUtils.returnEnumList(inputStream);
		 Iterator<Entry<String, String>> it = handlerMap.entrySet().iterator();
		 synchronized(lock) {
				while (it.hasNext()) {
					 try {
						 Map.Entry<String,String> mapPairs = (Map.Entry<String,String>)it.next();
						handlerConfSet.add(
								new KingdomHandlerConf.Builder(mapPairs.getValue())
								.contextPath(mapPairs.getKey())
								.build()
								);
					 }catch(LogicKingChallengeException ex) {
						 LOG.warn("exceptoion {}", ex);
					 }
				 }
		 }

	}
	public static KingdomConfManager getInstance() {

		if (instance == null) {
			synchronized (lock) {
				if (instance == null)
					instance = new KingdomConfManager();
			}
		}
		return instance;
	}

	public synchronized Set<KingdomHandlerConf> getHandlerConfList() {
		return Collections.unmodifiableSet(handlerConfSet);
	}
	/**
	 * Returns a loginController
	 * @return
	 */
	public LoginController getLoginController() {
		if (loginController==null) throw 
			new IllegalStateException("No LoginController has been instanced");
		return loginController;
	}

}
