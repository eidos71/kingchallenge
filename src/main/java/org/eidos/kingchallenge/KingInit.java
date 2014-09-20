package org.eidos.kingchallenge;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.concurrent.GuardedBy;

import org.eidos.kingchallenge.persistance.KingPersistance;
import org.eidos.kingchallenge.persistance.KingdomConfManager;
import org.eidos.kingchallenge.repository.KingdomRepo;
import org.eidos.kingchallenge.service.KingService;
import org.eidos.kingchallenge.service.LoginService;
import org.eidos.kingchallenge.service.SimpleLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KingInit {
	private static final String CONFIGURATION_PROPERTIES = "configuration.properties";
	static final Logger LOG = LoggerFactory.getLogger(KingConfigStaticProperties.class);
	private  final Properties prop;
	@GuardedBy("lock")
	private static volatile KingInit instance = null;
	private final static Object lock = new Object();
	private KingInit() {
		prop = new Properties();
		loadPropertyConf() ;
	}
	static KingInit getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null)
					instance = new KingInit();
			}
		}
		return instance;	
		
	}
	private  void loadPropertyConf()  {
		InputStream input = null;
		try {
			input = getClass().getClassLoader().
					getResourceAsStream(CONFIGURATION_PROPERTIES);
			prop.load(input);
			} catch (IOException e) {
				LOG.error("Property config not found {}", e);
			}	
	}
	public  String getProperty(String key) {
		return prop.getProperty(key);
		}
	/**
	 * This method allows to pick services
	 * All the services need to have a default empty constructor
	 * 
	 * @param key
	 * @return
	 */
	public KingService  getService(String key) {
		LOG.debug("Key, {}", key);
		String classToInstance= prop.getProperty(key);
		LOG.debug("classToInstance, {}", classToInstance);
		if (classToInstance==null  || "".equals(classToInstance) ) {
			return null ;}
			Class<?> clazz;
			try {
				clazz = Class.forName(classToInstance);
				return (KingService) clazz.newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				LOG.debug("error, {}", e);
				return null;
			}
			
	}
	public KingdomRepo getRepo(String key) {
		LOG.debug("Key, {}", key);
		String classToInstance= prop.getProperty(key);
		LOG.debug("classToInstance, {}", classToInstance);
		if (classToInstance==null  || "".equals(classToInstance) ) {
			return null ;}
			Class<?> clazz;
			try {
				clazz = Class.forName(classToInstance);
				return (KingdomRepo) clazz.newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				LOG.debug("error, {}", e);
				return null;
			}

	}
	public KingPersistance getPersistStorage(String key) {
		LOG.debug("Key, {}", key);
		String classToInstance= prop.getProperty(key);
		LOG.debug("classToInstance, {}", classToInstance);
		if (classToInstance==null  || "".equals(classToInstance) ) {
			return null ;}
			Class<?> clazz;
			try {
				clazz = Class.forName(classToInstance);
				return (KingPersistance) clazz.newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				LOG.debug("error, {}", e);
				return null;
			}

	}
}
