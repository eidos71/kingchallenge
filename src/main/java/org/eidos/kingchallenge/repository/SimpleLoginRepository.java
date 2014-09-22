package org.eidos.kingchallenge.repository;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.eidos.kingchallenge.KingdomConfManager;
import org.eidos.kingchallenge.domain.model.KingUser;
import org.eidos.kingchallenge.exceptions.KingInvalidSessionException;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.persistance.LoginPersistanceMap;

@ThreadSafe
public final class SimpleLoginRepository implements LoginRepository {

	/**
	 * Persistance storage for logged users
	 */
	@GuardedBy("loginPersistance")
	private final LoginPersistanceMap<Long, String, KingUser> loginPersistance;

	public SimpleLoginRepository() {
		loginPersistance = KingdomConfManager.getInstance().getPersistanceBag()
				.getLoginPersistance();
	}

	@Override
	public Map<Long, KingUser> getAllKingdomByLogin() {
		Map<Long, KingUser> result = loginPersistance.getMapByLogin();
		if (result == null)
			return Collections.emptyMap();
		return result;
	}

	@Override
	public Map<String, KingUser> getAllKingdomBySession() {
	
		Map<String, KingUser> result = loginPersistance.getMapBySession();
	
		if (result == null)
			return Collections.emptyMap();
		return result;

	}

	@Override
	public String addKingUser(KingUser user) {
		synchronized (loginPersistance) {
			boolean exists = getAllKingdomByLogin().containsKey(
					user.getKingUserId().get());
			if (exists) {
				this.loginPersistance.removeByLogin(user.getKingUserId().get());
			}
				//throw new LogicKingChallengeException(LogicKingError.USER_EXISTS);
			this.loginPersistance.put(user.getKingUserId().get(),
					user.getSessionKey(), user);
		}
		return user.getSessionKey();

	}

	@Override
	public boolean removeKingUserByLogin(AtomicLong loginKey) {
		if (loginKey == null || loginKey.get() == 0)
			throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
		boolean missing = !getAllKingdomByLogin().containsKey(loginKey.get());
		if (missing)
			throw new LogicKingChallengeException(LogicKingError.USER_NOT_FOUND);
		return this.loginPersistance.removeByLogin(loginKey.get());
	}

	@Override
	public boolean removeKingUserBySession(String sessionId) {
		if (sessionId == null || "".equals(sessionId))
			throw new  KingInvalidSessionException();

		boolean missing = !getAllKingdomBySession().containsKey(sessionId);
		if (missing)
			throw new LogicKingChallengeException(LogicKingError.USER_NOT_FOUND);
		return this.loginPersistance.removeBySession(sessionId);
	}

	@Override
	public void removeKingUser(KingUser user) {
		throw new UnsupportedOperationException();

	}

	@Override
	public KingUser updateKingUser(KingUser user) {
		// if the user comes empty, we return the update action
		String resultSession="";
		if (user == null)
			return null;
		synchronized (loginPersistance) {
			boolean found = !getAllKingdomByLogin().containsKey(
					user.getKingUserId().get());
			if (found) {
				this.loginPersistance.removeByLogin(user.getKingUserId().get());
			}
		 this.loginPersistance.put(user.getKingUserId().get(),
					user.getSessionKey(), user);

		}
		return user;

	}

	@Override
	public KingUser findByLoginId(AtomicLong loginKey) {
		if (loginKey == null || loginKey.get() == 0)
			throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
		return getAllKingdomByLogin().get(loginKey.get());
	}

	@Override
	public KingUser findBySessionId(String sessionId) {
		if (sessionId == null || "".equals(sessionId))
			throw new KingInvalidSessionException();

		return getAllKingdomBySession().get(sessionId);
	}

}
