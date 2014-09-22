package org.eidos.kingchallenge;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eidos.kingchallenge.controller.LoginController;
import org.eidos.kingchallenge.controller.ScoreController;
import org.eidos.kingchallenge.controller.SimpleLoginController;
import org.eidos.kingchallenge.controller.SimpleScoreController;
import org.eidos.kingchallenge.persistance.KingPersistance;
import org.eidos.kingchallenge.persistance.LoginPersistanceMap;
import org.eidos.kingchallenge.persistance.ScorePersistance;
import org.eidos.kingchallenge.repository.KingdomRepo;
import org.eidos.kingchallenge.repository.LoginRepository;
import org.eidos.kingchallenge.repository.ScoreRepository;
import org.eidos.kingchallenge.service.KingService;
import org.eidos.kingchallenge.service.LoginService;
import org.eidos.kingchallenge.service.ScoreService;


@SuppressWarnings("rawtypes")
public class KingConfigStaticProperties {

	// Used to store
	static final Logger LOG = Logger
			.getLogger(KingConfigStaticProperties.class.getName());
	
	public static final String KING_REQUEST_PARAM;
	public static final int BINDING_PORT;
	public static final int HTTP_POOL_CONNECTIONS;
	public static final int SESSION_EXPIRATION_MINUTES;
	public static final int TOPLISTSCORE;
	public static final int WORKMAN_SCHEDULE_INSECONDS;
	public static final int PERSISTANCE_SCORE_MAX_ELEMS_PER_LVL;
	public static final ScoreRepository SCOREREPO;
	public static final LoginRepository LOGINREPO;
	public static final LoginService LOGINSERVICE;
	public static final ScoreService SCORESERVICE;
	public static final LoginController LOGINCONTROLLER;
	public static final ScoreController SCORECONTROLLER; 

	public static final LoginPersistanceMap LOGINPERSISTANCE;
	public static final ScorePersistance SCOREPERSISTANCE;

	static {
		if (LOG.isLoggable(Level.FINE)) LOG.fine("static:init*");
		KING_REQUEST_PARAM = (getProperty("KING_REQUEST_PARAM") != null) ? getProperty("KING_REQUEST_PARAM")
				: "kingparams";
		// load a properties file
		HTTP_POOL_CONNECTIONS = (getProperty("HTTP_POOL_CONNECTIONS") != null) ? Integer
				.parseInt(getProperty("HTTP_POOL_CONNECTIONS")) : 15;
		SESSION_EXPIRATION_MINUTES = (getProperty("SESSION_EXPIRATION_MINUTES") != null) ? Integer
				.parseInt(getProperty("SESSION_EXPIRATION_MINUTES")) : 10;
		TOPLISTSCORE = (getProperty("TOPLISTSCORE") != null) ? Integer
				.parseInt(getProperty("TOPLISTSCORE")) : 15;
		BINDING_PORT = (getProperty("BINDING_PORT") != null) ? Integer
				.parseInt(getProperty("BINDING_PORT")) : 8000;
		WORKMAN_SCHEDULE_INSECONDS = (getProperty("WORKMAN_SCHEDULE_INSECONDS") != null) ? Integer
				.parseInt(getProperty("WORKMAN_SCHEDULE_INSECONDS")) : 30;
		PERSISTANCE_SCORE_MAX_ELEMS_PER_LVL = (getProperty("PERSISTANCE_SCORE_MAX_ELEMS_PER_LVL") != null) ? Integer
				.parseInt(getProperty("PERSISTANCE_SCORE_MAX_ELEMS_PER_LVL"))
				: 100000;
	
		LOGINPERSISTANCE=(LoginPersistanceMap)getPersistance("LOGINPERSISTANCE");
		SCOREPERSISTANCE=(ScorePersistance) getPersistance("SCOREPERSISTANCE");
		SCOREREPO = (ScoreRepository) getRepo("SCOREREPO");
		LOGINREPO = (LoginRepository) getRepo("LOGINREPO");				
		LOGINSERVICE = (LoginService) getService("LOGINSERVICE");
		SCORESERVICE = (ScoreService) getService("SCORESERVICE");
		LOGINCONTROLLER= new SimpleLoginController.Builder().build();
		SCORECONTROLLER= new SimpleScoreController.Builder().build();
		if (LOG.isLoggable(Level.FINE)){
			LOG.fine(String.format("*static:end %1$s, %2$s ", SCOREREPO, LOGINREPO) );
		}
	}

	private static KingService getService(String key) {
		return   KingInit.getInstance().getService(key);
	
	
	}

	private static KingPersistance getPersistance(String key) {
		return KingInit.getInstance().getPersistStorage(key);
		
	}

	private static KingdomRepo getRepo(String key) {
	
		return KingInit.getInstance().getRepo(key);
	}

	private static String getProperty(String key) {

		return KingInit.getInstance().getProperty(key);
	}
}
