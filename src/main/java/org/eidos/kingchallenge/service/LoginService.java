package org.eidos.kingchallenge.service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.eidos.kingchallenge.model.KingUser;


/**
 * Interface that deals with LoginServices
 * @author eidos71
 *
 */
public interface LoginService {
	
	/**
	 * 
	 * @param token
	 * @return
	 */
	public String loginToken(AtomicLong token) ;
	/**
	 *  Checks if session is invalidated or not
	 * @param currentTime Current
	 * @return
	 */
	
	public Boolean sessionCheck();
	/**
	 * Check one user by login
	 * @param user
	 */
	public void sessionCheckByLogin(KingUser user);
		
	 
	}


