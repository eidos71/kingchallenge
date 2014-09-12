package org.eidos.kingchallenge.services;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;


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
	public void sessionCheck (Date currentDate ) ;
		
	 
	}


