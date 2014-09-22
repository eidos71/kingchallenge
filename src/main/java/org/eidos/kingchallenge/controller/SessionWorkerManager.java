package org.eidos.kingchallenge.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eidos.kingchallenge.service.LoginService;

public class SessionWorkerManager implements Runnable {
	final LoginService  loginService ;
	static final Logger LOG = Logger
			.getLogger(SessionWorkerManager.class.getName());
	
	public SessionWorkerManager(LoginService service) {
		loginService= service;
		if (service==null)
			throw  new IllegalArgumentException("Login Service is not defined");
	
	}
	@Override
	public void run() {
		try {
			Boolean result = loginService.sessionCheck();
			if (LOG.isLoggable(Level.FINE))  LOG.fine(String.format("False: No session had expired, True: Sessions were removed -->%1$s", result) );
		} catch (Exception e) {
			if (LOG.isLoggable(Level.INFO)) LOG.log(Level.INFO," exception",e);
		} catch (Throwable e) {
			LOG.log(Level.SEVERE," exception",e);
		}

	}
}
