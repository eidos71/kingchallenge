package org.eidos.kingchallenge.service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.eidos.kingchallenge.model.KingUser;
import org.eidos.kingchallenge.repository.LoginRepository;

/**
 * Interface that deals with LoginServices
 * 
 * @author eidos71
 *
 */
public interface LoginService {

	/**
	 * 
	 * @param token
	 * @return
	 */
	public String loginToken(AtomicLong token);

	/**
	 * Checks if session is invalidated or not
	 * 
	 * @param currentTime
	 *            Current
	 * @return
	 */

	public Boolean sessionCheck();


	KingUser sessionCheckBySessionKey(String sessionKey);

	KingUser sessionCheckByLoginId(AtomicLong loginId);
	/**
	 * 
	 * @param sessionKey
	 * @return
	 */
	String renewLastLogin(String sessionKey);

	


}
