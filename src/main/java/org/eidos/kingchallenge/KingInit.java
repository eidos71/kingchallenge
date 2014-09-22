package org.eidos.kingchallenge;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.concurrent.GuardedBy;

import org.eidos.kingchallenge.persistance.KingPersistance;
import org.eidos.kingchallenge.repository.KingdomRepo;
import org.eidos.kingchallenge.service.KingService;


public class KingInit {
	private static final String CONFIGURATION_PROPERTIES = "configuration.properties";
	static final Logger LOG = Logger
			.getLogger(KingInit.class.getName());
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
			input = this.getClass().getClassLoader().
					getResourceAsStream(CONFIGURATION_PROPERTIES);
			prop.load(input);
			} catch (IOException e) {
				LOG.severe(String.format("Property config not found %1$s", e) );
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

		String classToInstance= prop.getProperty(key);
			if (classToInstance==null  || "".equals(classToInstance) ) {
			return null ;}
			Class<?> clazz;
			try {
				clazz = Class.forName(classToInstance);
				return (KingService) clazz.newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				LOG.fine(String.format("error, %1$s", e) );
				return null;
			}
			
	}
	public KingdomRepo getRepo(String key) {

		String classToInstance= prop.getProperty(key);

		if (classToInstance==null  || "".equals(classToInstance) ) {
			return null ;}
			Class<?> clazz;
			try {
				clazz = Class.forName(classToInstance);
				return (KingdomRepo) clazz.newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				LOG.fine(String.format("error, %1$s", e) );
				return null;
			}

	}
	public KingPersistance getPersistStorage(String key) {

		String classToInstance= prop.getProperty(key);

		if (classToInstance==null  || "".equals(classToInstance) ) {
			return null ;}
			Class<?> clazz;
			try {
				clazz = Class.forName(classToInstance);
				return (KingPersistance) clazz.newInstance();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				if (LOG.isLoggable(Level.FINE)) LOG.fine(String.format("error, %1$s", e) );
				return null;
			}

	}
}
