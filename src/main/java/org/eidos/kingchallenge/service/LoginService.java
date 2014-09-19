package org.eidos.kingchallenge.service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.eidos.kingchallenge.domain.model.KingUser;
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

	/**
	 * Checks if a session is valid and store in the persistance
	 * @param sessionKey session to be validated on the persistance
	 * @return the @KingUser found
	 */
	KingUser sessionCheckBySessionKey(String sessionKey);

	KingUser sessionCheckByLoginId(AtomicLong loginId);
	/**
	 * Checks a SessionKey is valid (still between the time of persistence of session)
	 * and refreshes the date of the session
	 * @param sessionKey SessionKey
	 * @return a String representing a sesskionKey
	 */
	KingUser renewLastLogin(String sessionKey);

	


}
