package org.eidos.kingchallenge.repository;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.model.KingUser;
import org.eidos.kingchallenge.persistance.KingdomConfManager;
import org.eidos.kingchallenge.persistance.LoginPersistanceMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ThreadSafe
public final class SimpleLoginRepository  implements LoginRepository{
	static final Logger LOG = LoggerFactory
			.getLogger(SimpleLoginRepository.class);
	/**
	 * Persistance storage for logged users
	 */
	@GuardedBy("loginPersistance")
	private final LoginPersistanceMap<Long, String, KingUser> loginPersistance;
	private final Object update = new Object();

	public SimpleLoginRepository(){
		loginPersistance= KingdomConfManager.getInstance().getPersistanceBag().getLoginPersistance();
	}
	
	@Override
	public Map<Long, KingUser> getAllKingdomByLogin() {
		Map<Long, KingUser> result = loginPersistance.getMapByLogin();
		if (result==null) return Collections.emptyMap();
		return result;
	}
	@Override
	public Map<String, KingUser> getAllKingdomBySession(){
		Map<String, KingUser> result = loginPersistance.getMapBySession();
		if (result==null) return Collections.emptyMap();
		return result;
		
	}
	@Override
	public void addKingUser(KingUser user) {
		synchronized (loginPersistance) {
			boolean exists=  getAllKingdomByLogin().containsKey(user.getKingUserId() );
			if (exists)  throw new LogicKingChallengeException(LogicKingError.USER_EXISTS);
			this.loginPersistance.put(user.getKingUserId().get(), user.getSessionKey(), user);
		}

		
	}
	@Override
	public void removeKingUserByLogin(AtomicLong loginId) {
		boolean missing= ! getAllKingdomByLogin().containsKey(loginId );
		if (missing) throw new LogicKingChallengeException(LogicKingError.USER_NOT_FOUND);
	}
	@Override
	public void removeKingUserBySession(String sessionId) {
		LOG.debug("We want to remove user by sessionId" + sessionId );
		boolean missing= ! getAllKingdomBySession().containsKey(sessionId );
		if (missing) throw new LogicKingChallengeException(LogicKingError.USER_NOT_FOUND);
		this.loginPersistance.removeBySession(sessionId);
	}
	@Override
	public void removeKingUser(KingUser user) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void updateKingUser(KingUser user) {
		//if the user comes empty, we return the update action
		if (user==null) return ;
		synchronized (loginPersistance){
			boolean missing= ! getAllKingdomByLogin().containsKey(user.getKingUserId() );
			if (!missing) {
			
				this.removeKingUserByLogin(user.getKingUserId() );
				this.addKingUser(user);
		
			}
		}

		
	}

}
