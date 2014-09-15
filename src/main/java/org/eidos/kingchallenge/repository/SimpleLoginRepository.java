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
public final class SimpleLoginRepository implements LoginRepository {
	static final Logger LOG = LoggerFactory
			.getLogger(SimpleLoginRepository.class);
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
	public void addKingUser(KingUser user) {
		synchronized (loginPersistance) {
			boolean exists = getAllKingdomByLogin().containsKey(
					user.getKingUserId().get());
			if (exists)
				throw new LogicKingChallengeException(
						LogicKingError.USER_EXISTS);
			this.loginPersistance.put(user.getKingUserId().get(),
					user.getSessionKey(), user);
		}

	}

	@Override
	public void removeKingUserByLogin(AtomicLong loginKey) {
		if (loginKey == null || loginKey.get() == 0)
			throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
		boolean missing = !getAllKingdomByLogin().containsKey(loginKey.get());
		if (missing)
			throw new LogicKingChallengeException(LogicKingError.USER_NOT_FOUND);
		this.loginPersistance.removeByLogin(loginKey.get());
	}

	@Override
	public void removeKingUserBySession(String sessionId) {
		if (sessionId == null || "".equals(sessionId))
			throw new LogicKingChallengeException(
					LogicKingError.INVALID_SESSION);
		LOG.debug("We want to remove user by sessionId" + sessionId);
		boolean missing = !getAllKingdomBySession().containsKey(sessionId);
		if (missing)
			throw new LogicKingChallengeException(LogicKingError.USER_NOT_FOUND);
		this.loginPersistance.removeBySession(sessionId);
	}

	@Override
	public void removeKingUser(KingUser user) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateKingUser(KingUser user) {
		// if the user comes empty, we return the update action
		if (user == null)
			return;
		synchronized (loginPersistance) {
			boolean found = !getAllKingdomByLogin().containsKey(
					user.getKingUserId().get());
			if (found) {
				this.loginPersistance.removeByLogin(user.getKingUserId().get());
			}
			this.loginPersistance.put(user.getKingUserId().get(),
					user.getSessionKey(), user);
		}

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
			throw new LogicKingChallengeException(
					LogicKingError.INVALID_SESSION);

		return getAllKingdomBySession().get(sessionId);
	}

}
