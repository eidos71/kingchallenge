package org.eidos.kingchallenge.persistance;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.model.KingUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 
 * @author eidos71
 *
 */
@ThreadSafe
public final class SimpleLoginPersistanceMap implements LoginPersistanceMap<AtomicInteger, String, KingUser> {
	static final Logger LOG = LoggerFactory.getLogger(SimpleLoginPersistanceMap.class);
	@GuardedBy("this")
	private final Map<AtomicInteger, KingUser> mapByLogin = 
				new ConcurrentHashMap<AtomicInteger, KingUser>();
	@GuardedBy("this")
	private final Map<String, KingUser> mapBySession =
				new ConcurrentHashMap<String, KingUser>();
	@Override
	public Map<AtomicInteger, KingUser> getMapByLogin() {
		return Collections.unmodifiableMap(mapByLogin);
	}

	@Override
	public Map<String, KingUser> getMapBySession() {
		return Collections.unmodifiableMap(mapBySession);
	}

	@Override
	public void  put(AtomicInteger loginKey, String sessionKey, KingUser value) {
		synchronized(this) {
			LOG.debug("loginKey {}, sessionKey {}, value {} ",loginKey,sessionKey,value );
			mapByLogin.put(loginKey, value);
			mapBySession.put(sessionKey, value);
		
		}
	}
	@Override
	public void removeBySession (String sessionKey) {
		// If empty remove
		if (  sessionKey==null || "".equals(sessionKey) ) 
				throw new LogicKingChallengeException(LogicKingError.INVALID_SESSION);
		synchronized(this) {
			KingUser removed = mapBySession.remove(sessionKey);
			mapByLogin.remove(removed);
		}
	}
	@Override
	public void removeByLogin(AtomicInteger loginKey) {
		if (loginKey==null || loginKey.equals(0))
			throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
		synchronized(this) {
			KingUser removed = mapByLogin.remove(loginKey);
			if (removed==null) {
				LOG.warn("invalid Token- loginkey {}", loginKey);
				throw   new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
			}
				
			mapBySession.remove(removed);
		}
	}

}
