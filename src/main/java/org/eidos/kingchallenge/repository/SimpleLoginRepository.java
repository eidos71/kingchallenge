package org.eidos.kingchallenge.repository;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.eidos.kingchallenge.model.KingUser;
import org.eidos.kingchallenge.persistance.LoginPersistanceMap;

public class SimpleLoginRepository  implements LoginRepository{

	
	@Override
	public Map<Long, KingUser> getAllKingdomByLogin() {
		return null;
	}
	@Override
	public Map<String, KingUser> getAllKingdomBySession(){
		return null;
	}
	@Override
	public void addKingUser(KingUser user) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeKingUserByLogin(AtomicLong loginId) {
		
	}
	@Override
	public void removeKingUserBySession(String sessionId) {
		
	}
	@Override
	public void removeKingUser(KingUser user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateKingUser(KingUser user) {
		// TODO Auto-generated method stub
		
	}

}
