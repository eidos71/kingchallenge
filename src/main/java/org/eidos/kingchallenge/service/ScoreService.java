package org.eidos.kingchallenge.service;

import java.util.Map;

import org.eidos.kingchallenge.model.KingScore;

/**
 * Score Service
 * @author eidos71
 *
 */
public interface ScoreService {

	/**
	 * @param level 
	 * 
	 */
	public Map<Long ,KingScore > getHighScoreList(Long level) ;

	public Boolean insertScore(String sessionKey, KingScore score);
;
}
