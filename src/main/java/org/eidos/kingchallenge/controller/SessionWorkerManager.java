package org.eidos.kingchallenge.controller;

import org.eidos.kingchallenge.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionWorkerManager implements Runnable {
	final LoginService  loginService ;
	static final Logger LOG = LoggerFactory
			.getLogger(SessionWorkerManager.class);
	
	public SessionWorkerManager(LoginService service) {
		loginService= service;
		if (service==null)
			throw  new IllegalArgumentException("Login Service is not defined");
	
	}
	@Override
	public void run() {
		try {
			Boolean result = loginService.sessionCheck();
			LOG.debug("False: No session had expired, True: Sessions were removed --> {}", result);
		} catch (Exception e) {
			LOG.warn("{}", e);
		} catch (Throwable e) {
			LOG.error("{}", e);
		}

	}
}
