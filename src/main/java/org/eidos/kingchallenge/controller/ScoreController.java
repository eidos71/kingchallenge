package org.eidos.kingchallenge.controller;

import java.util.Map;



public interface ScoreController {


	/**
	 *  Method to store a high score
	 * @param sessionKey
	 * @param level
	 * @param score
	 */
	public String putHighScore(String sessionKey, Long level, int score);
	/**
	 * Returns the high score list on a map
	 * separated by , and with a top of 15 elements.
	 * @param sessionKey
	 * @param level
	 * @return
	 */
	String getHighScoreByLevel(String sessionKey, Long level);
}
