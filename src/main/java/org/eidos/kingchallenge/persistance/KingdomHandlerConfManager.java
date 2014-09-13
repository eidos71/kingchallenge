package org.eidos.kingchallenge.persistance;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.eidos.kingchallenge.exceptions.KingRunTimeIOException;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.model.KingdomHandlerConf;
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
public class KingdomHandlerConfManager {
	static final Logger LOG = LoggerFactory
			.getLogger(SimpleLoginPersistanceMap.class);

	private static final String HANDLER_PROPERTIES = "handler.properties";
	@GuardedBy("lock")
	private static volatile KingdomHandlerConfManager instance = null;
	private static Object lock = new Object();
	final private Set<KingdomHandlerConf> handlerConfSet;

	/**
	 * Usinc confinement to assure thread-bound scope private singleton
	 * 
	 * @throws IOException
	 */
	private KingdomHandlerConfManager()  {
		handlerConfSet= new HashSet<KingdomHandlerConf> ();
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
								new KingdomHandlerConf.Builder(mapPairs.getKey())
								.contextPath(mapPairs.getValue())
								.build()
								);
					 }catch(LogicKingChallengeException ex) {
						 LOG.warn("exceptoion {}", ex);
					 }
				 }
		 }

	}
	public static KingdomHandlerConfManager getInstance() {

		if (instance == null) {
			synchronized (lock) {
				if (instance == null)
					instance = new KingdomHandlerConfManager();
			}
		}
		return instance;
	}

	public Set<KingdomHandlerConf> getHandlerConfList() {
		return Collections.unmodifiableSet(handlerConfSet);
	}

}
