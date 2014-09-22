package org.eidos.kingchallenge.persistance;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.eidos.kingchallenge.domain.model.KingUser;
import org.eidos.kingchallenge.exceptions.KingInvalidSessionException;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.utils.Validator;



/**
 * 
 * @author eidos71
 *
 */
@ThreadSafe
public final class SimpleLoginPersistanceMap implements LoginPersistanceMap<Long, String, KingUser> {
	static final Logger LOG = Logger.getLogger(SimpleLoginPersistanceMap.class.getName() );
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
	public boolean removeBySession (String sessionKey) {
		// If empty remove
		if (  sessionKey==null || "".equals(sessionKey) ) 
				throw new KingInvalidSessionException();
		synchronized(lockSession) {
			KingUser removedSession = mapBySession.remove(sessionKey);
			if (removedSession==null) {
				if (LOG.isLoggable(Level.FINE )  )  LOG.fine(String.format("sessionKey %1$s was not found", sessionKey) );
					return false;
			}else {
				return mapByLogin.remove(removedSession.getKingUserId().get() ) != null;
			}
			}
	}
	@Override
	public boolean removeByLogin(Long loginKey) {
		
		if (!Validator.isValidUnsignedInt(loginKey) )
			throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
		synchronized(lockLogin) {
			KingUser removed = mapByLogin.remove(loginKey);
			if (removed==null) {
					return false;
			}
			return (mapBySession.remove(removed.getSessionKey() ) != null );
		
		}
	}

}
