package org.eidos.kingchallenge.services;

import java.util.Date;


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
	public String loginToken(Integer token) ;
	/**
	 *  Checks if session is invalidated or not
	 * @param currentTime Current
	 * @return
	 */
	public void sessionCheck (Date currentDate ) ;
		
	 
	}


