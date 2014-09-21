package org.eidos.kingchallenge;

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

import org.eidos.kingchallenge.KingConfigStaticProperties;
import org.eidos.kingchallenge.KingdomHandlerConf;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.persistance.PersistanceBag;
import org.eidos.kingchallenge.utils.FileReaderUtils;
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

	final private PersistanceBag persistanceBag;


	/**
	 * Usinc confinement to assure thread-bound scope private singleton
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private KingdomConfManager()  {
		persistanceBag = new PersistanceBag.Builder()
			.setLoginImp(KingConfigStaticProperties.LOGINPERSISTANCE)
			.setScorePersistance(KingConfigStaticProperties.SCOREPERSISTANCE)
			.build();
		handlerConfSet= new HashSet<KingdomHandlerConf> ();

		//Lets init...
	
		init();
	}
	/**
	 * Init load from the Handler
	 * Comes for a properties files
	 */
	private void init() {
		
		 InputStream inputStream = 
				 this.getClass().getClassLoader().getResourceAsStream(HANDLER_PROPERTIES);
		
		Map<String, String> handlerMap = FileReaderUtils.returnEnumList(inputStream);
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
	 * Retusn bag of persistance
	 * @return
	 */
	public PersistanceBag getPersistanceBag() {
		if (persistanceBag==null) throw 
		new IllegalStateException("No persistanceBag has been instanced");
		return persistanceBag;
	}
}
