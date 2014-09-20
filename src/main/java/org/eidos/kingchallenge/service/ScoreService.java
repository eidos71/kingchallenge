package org.eidos.kingchallenge.service;

import java.util.Map;

import org.eidos.kingchallenge.domain.dto.KingScoreDTO;

/**
 * Score Service
 * @author eidos71
 *
 */
public interface ScoreService extends KingService{

	/**
	 * @param level 
	 * 
	 */
	public String getHighScoreList(Long level) ;

	public Boolean insertScore(String sessionKey, KingScoreDTO score);

	String getHighScoreList(String sessionKey, Long levelValue);
;
}
