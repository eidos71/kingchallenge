package org.eidos.kingchallenge.service;

import org.eidos.kingchallenge.KingConfigStaticProperties;
import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
import org.eidos.kingchallenge.domain.model.KingUser;
import org.eidos.kingchallenge.exceptions.KingInvalidSessionException;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.repository.LoginRepository;
import org.eidos.kingchallenge.repository.ScoreRepository;
import org.eidos.kingchallenge.utils.CollectionsChallengeUtils;
import org.eidos.kingchallenge.utils.Validator;

public final class SimpleScoreService implements ScoreService {

	private ScoreRepository scoreRepository= KingConfigStaticProperties.SCOREREPO;
	private LoginRepository loginRepository = KingConfigStaticProperties.LOGINREPO;


	@Override
	public Boolean insertScore(String sessionKey, KingScoreDTO score) {
		if (sessionKey==null  || "".equals(sessionKey) )
			throw new KingInvalidSessionException("Invalid sessionKey");
		KingUser kinguser= this.loginRepository.findBySessionId(sessionKey);
		if (kinguser==null )
			throw new KingInvalidSessionException("Invalid sessionKey: " + sessionKey);
		if (score==null)
			throw new LogicKingChallengeException(LogicKingError.PROCESSING_ERROR);
			
		return this.scoreRepository.insertScore(score); 
	}
	/**
	 * This setter hs not to be used outside a TEST environment.
	 * @param loginRepository
	 */
	public void setLoginRepository(LoginRepository loginRepository) {
		
		this.loginRepository=loginRepository;
	}
	/**
	 * This setter hs not to be used outside a TEST environment.
	 * @param scoreService
	 */
	public void setScoreRepository(ScoreRepository scoreRepository) {
		this.scoreRepository=scoreRepository;
		
	}
	@Override
	public String getHighScoreList(String sessionKey, Long levelValue) {
		if (sessionKey==null  || "".equals(sessionKey) )
			throw new KingInvalidSessionException("Invalid sessionKey");
		KingUser kinguser= this.loginRepository.findBySessionId(sessionKey);
		if (kinguser==null )
			throw new KingInvalidSessionException("Invalid sessionKey: " + sessionKey);
		if (levelValue==null)
			throw new LogicKingChallengeException(LogicKingError.INVALID_LEVEL);

		if ( ! Validator.isValidUnsignedInt(levelValue) )
			throw new LogicKingChallengeException(LogicKingError.INVALID_LEVEL);
		return 	CollectionsChallengeUtils.returnCsvFromCollection(scoreRepository.getTopScoresForLevel(levelValue));
		
	}
	@Override
	public String getHighScoreList(Long levelValue) {
		if (levelValue==null)
			throw new LogicKingChallengeException(LogicKingError.INVALID_LEVEL);
		if ( ! Validator.isValidUnsignedInt(levelValue) )
			throw new LogicKingChallengeException(LogicKingError.INVALID_LEVEL);
		return 	CollectionsChallengeUtils.returnCsvFromCollection(scoreRepository.getTopScoresForLevel(levelValue));
	}





}
