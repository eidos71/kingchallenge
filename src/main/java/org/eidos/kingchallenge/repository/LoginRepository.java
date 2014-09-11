package org.eidos.kingchallenge.repository;



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
	
}
