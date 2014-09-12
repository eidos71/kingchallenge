package org.eidos.kingchallenge.persistance;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("hiding")
public interface LoginPersistanceMap <Integer, String, KingUser>{

	Map<Integer, KingUser> getMapByLogin();


	Map<String, KingUser> getMapBySession();

	void put(Integer loginKey, String sessionKey, KingUser value);
	
	



	void removeByLogin(Integer loginKey);


	void removeBySession(String sessionKey);
}
