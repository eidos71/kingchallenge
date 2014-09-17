package org.eidos.kingchallenge.service;

import java.util.concurrent.atomic.AtomicLong;

import org.eidos.kingchallenge.model.KingUser;

/**
 * An LoginService that does nothing!
 * @author eidos71
 *
 */
public class EmptyLoginService implements LoginService {

	@Override
	public String loginToken(AtomicLong token) {
		// TODO Auto-generated method stub
		return "fakeSessionId";
	}


	@Override
	public Boolean sessionCheck() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public KingUser sessionCheckBySessionKey(String sessionKey) {
		// TODO Auto-generated method stub
		return null;
		
	}


	@Override
	public KingUser sessionCheckByLoginId(AtomicLong loginId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public KingUser renewLastLogin(String sessionKey) {
		// TODO Auto-generated method stub
		return null;
	}


}
