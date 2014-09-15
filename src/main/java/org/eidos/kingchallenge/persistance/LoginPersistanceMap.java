package org.eidos.kingchallenge.persistance;

import java.util.Map;


@SuppressWarnings("hiding")
public interface LoginPersistanceMap <Long, String, KingUser>{

	Map<Long, KingUser> getMapByLogin();


	Map<String, KingUser> getMapBySession();

	void put(Long loginKey, String sessionKey, KingUser value);
	
	



	void removeByLogin(Long loginKey);


	void removeBySession(String sessionKey);
}
