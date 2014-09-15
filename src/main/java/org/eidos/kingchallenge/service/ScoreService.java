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
	 * 
	 */
	public Map<String,String > getHighScoreList() ;

	public void insertScore(String sessionKey, KingScore score);
}
