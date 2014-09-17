package org.eidos.kingchallenge.service;

import java.util.Map;

import org.eidos.kingchallenge.domain.dto.KingScoreDTO;

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
	public Map<Long ,KingScoreDTO > getHighScoreList(Long level) ;

	public Boolean insertScore(String sessionKey, KingScoreDTO score);
;
}
