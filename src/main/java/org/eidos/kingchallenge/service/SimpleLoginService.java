package org.eidos.kingchallenge.service;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import org.eidos.kingchallenge.KingConfigConstants;
import org.eidos.kingchallenge.domain.model.KingUser;
import org.eidos.kingchallenge.exceptions.KingInvalidSessionException;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.repository.LoginRepository;
import org.eidos.kingchallenge.repository.SimpleLoginRepository;
import org.eidos.kingchallenge.utils.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SimpleLoginService implements LoginService {
	static final Logger LOG = LoggerFactory.getLogger(SimpleLoginService.class);

	private  LoginRepository loginRepository = new SimpleLoginRepository();

	public SimpleLoginService(){
		
	}
	
	@Override
	public String loginToken(AtomicLong token) {
		throw new UnsupportedOperationException();
	}
	@Override
	public KingUser sessionCheckByLoginId(AtomicLong loginId) {
		
		KingUser usr = loginRepository.findByLoginId(loginId);
		if (usr==null)
			throw new KingInvalidSessionException();
		return sessionCheckBySessionKey(usr.getSessionKey() );
	}
	@Override
	public KingUser sessionCheckBySessionKey(String sessionKey) {
		if (sessionKey == null || "".equals(sessionKey) )
			throw new KingInvalidSessionException();
		KingUser kingUser=loginRepository.findBySessionId(sessionKey);	
		if (kingUser==null) throw	new KingInvalidSessionException("Not found sessionKey");
		//We check if it is still a valid Session
		LOG.debug("user to check: {}",kingUser);
		boolean isInvalid=!checkInvalidSessionByKey(sessionKey,kingUser.getDateLogin());
		if (isInvalid) 	throw new KingInvalidSessionException();
		return kingUser;
		
	}

	@Override
	public Boolean sessionCheck() {
		Boolean result=false;
		// we return a list for valid sessions
		Map<String, KingUser> mapLoginUser = loginRepository
				.getAllKingdomBySession();
		if (mapLoginUser.isEmpty()) result=false;
		for (Entry<String, KingUser> entry : mapLoginUser.entrySet()) {
			try {
				LOG.trace("entry-> {}",entry.getValue());
				checkInvalidSessionByKey(entry.getKey(), entry.getValue()
						.getDateLogin());
			}catch (NullPointerException  | LogicKingChallengeException err) {
				LOG.info("This entry {} has failed:  with the following error {} ",entry .getKey(), err);
			}
		
		}
		return result;
	}

	/**
	 * 
	 * @param sessionId
	 * @param lastLoginDate
	 */
	private boolean checkInvalidSessionByKey(String sessionId, Date lastLoginDate) {
		if (sessionId==null || "".equals(sessionId) || lastLoginDate==null) {
			throw new LogicKingChallengeException(
					LogicKingError.PROCESSING_ERROR);
		}
		boolean isValid=true;	
		long SESSION_EXPIRATION = MILLISECONDS.convert(
				KingConfigConstants._SESSION_EXPIRATION, MINUTES);
		if (Validator.validateSessionExpired(lastLoginDate, SESSION_EXPIRATION)) {
			LOG.debug("expiration is expired for {} with lastLoginDate {}", sessionId, lastLoginDate);
			// we have to remove this user
			loginRepository.removeKingUserBySession(sessionId);
			isValid=false;
		}
		return isValid;
	}

	@Override
	public KingUser renewLastLogin(String sessionKey ) {
		LOG.debug("loginToken  {} ", sessionKey);
		if (sessionKey==null || "".equals(sessionKey) )
			throw new KingInvalidSessionException();
	
		KingUser toUpdateUser= sessionCheckBySessionKey(sessionKey);
		LOG.debug("user to Update: {}",toUpdateUser);
		toUpdateUser= new KingUser.Builder(toUpdateUser.getKingUserId().get()).setSessionKey(toUpdateUser.getSessionKey()).build();
		return this.loginRepository.updateKingUser(toUpdateUser);

	}

	public LoginRepository getLoginRepository() {
		return loginRepository;
	}

}
