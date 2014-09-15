package org.eidos.kingchallenge.persistance;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.model.KingUser;
import org.eidos.kingchallenge.utils.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 
 * @author eidos71
 *
 */
@ThreadSafe
public final class SimpleLoginPersistanceMap implements LoginPersistanceMap<Long, String, KingUser> {
	static final Logger LOG = LoggerFactory.getLogger(SimpleLoginPersistanceMap.class);
	private final Object lockLogin= new Object();
	private final Object lockSession= new Object();
	
	@GuardedBy("lockLogin")
	private final Map<Long, KingUser> mapByLogin = 
				new ConcurrentHashMap<Long, KingUser>();
	@GuardedBy("lockSession")
	private final Map<String, KingUser> mapBySession =
				new ConcurrentHashMap<String, KingUser>();
	
	@Override
	public Map<Long, KingUser> getMapByLogin() {
		return Collections.unmodifiableMap(mapByLogin);
	}

	@Override
	public Map<String, KingUser> getMapBySession() {
		return Collections.unmodifiableMap(mapBySession);
	}

	@Override
	public void  put(Long loginKey, String sessionKey, KingUser value) {
		synchronized(lockLogin) {
			if (!Validator.isValidUnsignedInt(loginKey) )
				throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
			LOG.trace("loginKey {}, sessionKey {}, value {} ",loginKey,sessionKey,value );
			mapByLogin.put(loginKey, value);
			mapBySession.put(sessionKey, value);
		
		}
	}
	@Override
	public void clean() {
		synchronized(lockSession) {
			mapBySession.clear();
			mapByLogin.clear();
		}
	}
	@Override
	public void removeBySession (String sessionKey) {
		// If empty remove
		if (  sessionKey==null || "".equals(sessionKey) ) 
				throw new LogicKingChallengeException(LogicKingError.INVALID_SESSION);
		synchronized(lockSession) {
			KingUser removedSession = mapBySession.remove(sessionKey);
			if (removedSession==null) {
				LOG.debug("- sessionKey {} was not found", sessionKey);
			}
				
			mapByLogin.remove(removedSession.getKingUserId().get() );
		
			
		}
	}
	@Override
	public void removeByLogin(Long loginKey) {
		if (!Validator.isValidUnsignedInt(loginKey) )
			throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
		synchronized(lockLogin) {
			KingUser removed = mapByLogin.remove(loginKey);
			if (removed==null) {
				LOG.debug("- loginkey {} was not found", loginKey);
				return;
			}
			removed= mapBySession.remove(removed.getSessionKey() );
			if (removed==null) {
				LOG.debug("- loginkey {} was not found", loginKey);
			}
		}
	}

}
