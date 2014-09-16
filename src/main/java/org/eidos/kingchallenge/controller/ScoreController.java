package org.eidos.kingchallenge.controller;

import java.util.Map;

import org.eidos.kingchallenge.model.KingUser;

public interface ScoreController {
	/**
	 * Returns the high score list on a map
	 * separated by , and with a top of 15 elements.
	 * @return
	 */
	public String getHighScoresList();

	/**
	 *  Method to store a high score
	 * @param sessionKey
	 * @param level
	 * @param score
	 */
	public String putHighScore(String sessionKey, Long level, int score);
}
