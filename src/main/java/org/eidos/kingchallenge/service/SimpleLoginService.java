package org.eidos.kingchallenge.service;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import org.eidos.kingchallenge.KingConfigConstants;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.model.KingUser;
import org.eidos.kingchallenge.repository.LoginRepository;
import org.eidos.kingchallenge.repository.SimpleLoginRepository;
import org.eidos.kingchallenge.utils.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SimpleLoginService implements LoginService {
	static final Logger LOG = LoggerFactory.getLogger(SimpleLoginService.class);

	private final LoginRepository loginRepository = new SimpleLoginRepository();

	@Override
	public String loginToken(AtomicLong token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sessionCheckByLogin(KingUser user) {
		if (user == null)
			throw new LogicKingChallengeException(
					LogicKingError.INVALID_SESSION);
		sessionCheckBySession(user.getSessionKey(), user.getDateLogin());

	}

	@Override
	public void sessionCheck() {

		// we return a list for valid sessions
		Map<String, KingUser> loginUser = loginRepository
				.getAllKingdomBySession();

		for (Entry<String, KingUser> entry : loginUser.entrySet()) {
			sessionCheckBySession(entry.getKey(), entry.getValue()
					.getDateLogin());
		}

	}

	/**
	 * 
	 * @param sessionId
	 * @param lastLoginDate
	 */
	private void sessionCheckBySession(String sessionId, Date lastLoginDate) {
		long SESSION_EXPIRATION = MILLISECONDS.convert(
				KingConfigConstants._SESSION_EXPIRATION, MINUTES);
		if (Validator.validateSessionExpired(lastLoginDate, SESSION_EXPIRATION)) {
			LOG.debug("expiration is expired for {} ", sessionId);
			// we have to remove this user
			loginRepository.removeKingUserBySession(sessionId);
		}
	}

}
