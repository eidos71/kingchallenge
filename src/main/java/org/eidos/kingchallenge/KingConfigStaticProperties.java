package org.eidos.kingchallenge;

import org.eidos.kingchallenge.controller.LoginController;
import org.eidos.kingchallenge.controller.ScoreController;
import org.eidos.kingchallenge.controller.SimpleLoginController;
import org.eidos.kingchallenge.controller.SimpleScoreController;
import org.eidos.kingchallenge.repository.KingdomRepo;
import org.eidos.kingchallenge.repository.LoginRepository;
import org.eidos.kingchallenge.repository.ScoreRepository;
import org.eidos.kingchallenge.service.KingService;
import org.eidos.kingchallenge.service.LoginService;
import org.eidos.kingchallenge.service.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KingConfigStaticProperties {
	// Used to store
	static final Logger LOG = LoggerFactory
			.getLogger(KingConfigStaticProperties.class);
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

	static {
		LOG.debug("static:init*");
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
				SCOREREPO = (ScoreRepository) getRepo("SCOREREPO");
				LOGINREPO = (LoginRepository) getRepo("LOGINREPO");				
		LOGINSERVICE = (LoginService) getService("LOGINSERVICE");
		SCORESERVICE = (ScoreService) getService("SCORESERVICE");
		LOGINCONTROLLER= new SimpleLoginController.Builder().build();
		SCORECONTROLLER= new SimpleScoreController.Builder().build();
		LOG.debug("*static:end {}, {} ", SCOREREPO, LOGINREPO);
	}

	private static KingService getService(String key) {
		KingService result = KingInit.getInstance().getService(key);
		LOG.debug("{}", result.getClass());
		return result;
	}

	private static KingdomRepo getRepo(String key) {
		KingdomRepo result = KingInit.getInstance().getRepo(key);
		LOG.debug("{}", result.getClass());
		return result;
	}

	private static String getProperty(String key) {

		return KingInit.getInstance().getProperty(key);
	}
}
