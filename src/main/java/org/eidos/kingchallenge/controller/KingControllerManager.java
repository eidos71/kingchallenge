package org.eidos.kingchallenge.controller;


import javax.annotation.concurrent.GuardedBy;

import org.eidos.kingchallenge.KingConfigStaticProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KingControllerManager {
	static final Logger LOG = LoggerFactory
			.getLogger(KingControllerManager.class);
	final private LoginController loginController;
	final private ScoreController scoreController;
	@GuardedBy("lock")
	private static volatile KingControllerManager instance = null;
	private final static Object lock = new Object();
	
	/**
	 * Returns a loginController
	 * @return
	 */
	public LoginController getLoginController() {
		if (loginController==null) throw 
			new IllegalStateException("No LoginController has been instanced");
		return loginController;
	}
	public static KingControllerManager getInstance() {

		if (instance == null) {
			synchronized (lock) {
				if (instance == null)
					instance = new KingControllerManager();
			}
		}
		return instance;
	}
	/**
	 * Returns a loginController
	 * @return
	 */
	public ScoreController getScoreController() {

		if (scoreController==null) throw 
			new IllegalStateException("No LoginController has been instanced");
		return scoreController;
	}
	private KingControllerManager() {
		loginController=KingConfigStaticProperties.LOGINCONTROLLER;
		scoreController=KingConfigStaticProperties.SCORECONTROLLER;
	}
}
