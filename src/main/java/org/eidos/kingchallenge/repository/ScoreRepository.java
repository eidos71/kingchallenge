package org.eidos.kingchallenge.repository;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
import org.eidos.kingchallenge.domain.model.KingScore;

/**
 * 
 * @author eidos71
 *
 */
public interface ScoreRepository {
	
	/**
	 * 
	 * @param sessionKey
	 * @param gameLevel
	 * @param score
	 * @return
	 */
	Boolean insertScore(KingScoreDTO kingScore) ;
	/**
	 * 
	 * @param levelValue
	 * @return 
	 */
	Set<KingScore> getTopScoresForLevel(Long levelValue);
}
