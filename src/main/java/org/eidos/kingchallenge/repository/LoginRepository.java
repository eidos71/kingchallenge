package org.eidos.kingchallenge.repository;



import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.eidos.kingchallenge.model.KingUser;

/**
 *  Repository to store the storage values.
 * @author eidos71
 *
 */
public interface LoginRepository {
	/**
	 * Adds a king User into the repository
	 * @param user adds a king User
	 */
	void addKingUser(KingUser user);
	/**
	 * Removes a kingUser in the repository
	 * @param user user to delete
	 */
	void removeKingUser (KingUser user);
	/**
	 *  Updates a King user,
	 *  For thread-safe Issues updateKingUser
	 *  will remove the @KingUser existent and create a new @KingUser
	 * @param user User to update
	 */
	void updateKingUser(KingUser user);
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
	void removeKingUserByLogin(AtomicLong loginId);
	/**
	 * 
	 * @param sessionId
	 */
	void removeKingUserBySession(String sessionId);

	
	
	
}
