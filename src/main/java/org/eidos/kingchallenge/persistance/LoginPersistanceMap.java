package org.eidos.kingchallenge.persistance;

import java.util.Map;


@SuppressWarnings("hiding")
public interface LoginPersistanceMap <Long, String, KingUser> extends KingPersistance{

	Map<Long, KingUser> getMapByLogin();


	Map<String, KingUser> getMapBySession();

	void put(Long loginKey, String sessionKey, KingUser value);
	
	



	boolean removeByLogin(Long loginKey);


	boolean removeBySession(String sessionKey);

	/**
	 * Cleans the Repository
	 */
	void clean();
}
