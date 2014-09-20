package org.eidos.kingchallenge.repository;



import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.eidos.kingchallenge.domain.model.KingUser;



/**
 *  Repository to store the storage values.
 * @author eidos71
 *
 */
public interface LoginRepository {
	/**
	 * Adds a king User into the repository
	 * @param user adds a king User
	 * @return Session string
	 */
	String addKingUser(KingUser user);
	/**
	 * Removes a kingUser in the repository
	 * @param user user to delete
	 * 	 @return Session string
	 */
	void removeKingUser (KingUser user);
	/**
	 *  Updates a King user,
	 *  For thread-safe Issues updateKingUser
	 *  will remove the @KingUser existent and create a new @KingUser
	 * @param user User to update
	 */
	KingUser updateKingUser(KingUser user);
	/**
	 * Gets all users of Kingod, key is by its int LoginId
	 * @return Key is the loginId
	 */
	Map<Long, KingUser> getAllKingdomByLogin();
	/**
	 * Gets all users by Session, 
	 * @return Key is string of session, its User
	 */
	Map<String, KingUser> getAllKingdomBySession();


	/**
	 * 
	 * @param loginId
	 */
	boolean removeKingUserByLogin(AtomicLong loginId);
	/**
	 * 
	 * @param sessionId
	 * @return 
	 */
	boolean removeKingUserBySession(String sessionId);
	
	/**
	 * 
	 * @param loginId
	 * @return 
	 */
	KingUser findByLoginId(AtomicLong loginId);
	/**
	 * 
	 * @param sessionId
	 */
	KingUser findBySessionId(String sessionId);
	
	
	
}
