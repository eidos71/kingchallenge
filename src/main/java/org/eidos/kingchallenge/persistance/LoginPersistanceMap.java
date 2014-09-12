package org.eidos.kingchallenge.persistance;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("hiding")
public interface LoginPersistanceMap <AtomicInteger, String, KingUser>{

	Map<AtomicInteger, KingUser> getMapByLogin();


	Map<String, KingUser> getMapBySession();

	void put(AtomicInteger loginKey, String sessionKey, KingUser value);
	
	



	void removeByLogin(AtomicInteger loginKey);


	void removeBySession(String sessionKey);
}
