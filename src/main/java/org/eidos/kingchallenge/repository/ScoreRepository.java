package org.eidos.kingchallenge.repository;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.eidos.kingchallenge.domain.dto.KingScoreDTO;

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
	Boolean insertScore(String sessionKey, Long gameLevel, int score) ;
	/**
	 * 
	 * @param levelValue
	 * @return 
	 */
	Map<Long, KingScoreDTO> getTopScoresForLevel(Long levelValue);
}
