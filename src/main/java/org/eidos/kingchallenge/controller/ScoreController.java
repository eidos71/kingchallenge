package org.eidos.kingchallenge.controller;

import java.util.Map;

import org.eidos.kingchallenge.model.KingUser;

public interface ScoreController {
	/**
	 * Returns the high score list on a map
	 * separated by , and with a top of 15 elements.
	 * @return
	 */
	public Map<String,String> getHighScoresList();

	/**
	 * 
	 * @param user
	 * @param score
	 */
	public void putHighScore(KingUser user, int score);
}
