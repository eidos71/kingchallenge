package org.eidos.kingchallenge.repository;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
import org.eidos.kingchallenge.domain.model.KingScore;

/**
 * Repository that deals with the persistance storage
 * to manage the scores
 * @author eidos71
 *
 */
public interface ScoreRepository  extends KingdomRepo{
	
	/**
	 * Inserts a score
	 * @param kingScore a KingScoreDTO 
	 * @return True if inserted /false if not
	 */
	Boolean insertScore(KingScoreDTO kingScore) ;
	/**
	 *  Gets a list of top scores
	 * @param levelValue a Level Long
	 * @return 
	 */
	Set<KingScore> getTopScoresForLevel(Long levelValue);
	/**
	 * Gets a list of top scores ignoring the cache
	 * @param level Level to create
	 * @return a Set of the defined top scores for a particular level
	 */
	Set<KingScore> forceTopScoresForLevel(Long level);
}
