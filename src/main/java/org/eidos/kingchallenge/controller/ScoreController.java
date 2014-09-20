package org.eidos.kingchallenge.controller;

import java.util.Map;

import org.eidos.kingchallenge.domain.dto.KingResponseDTO;



public interface ScoreController {


	/**
	 *  Method to store a high score
	 * @param sessionKey
	 * @param level
	 * @param score
	 */
	public KingResponseDTO putHighScore(String sessionKey, Long level, int score);
	/**
	 * Returns the high score list on a map
	 * separated by , and with a top of 15 elements.

	 * @param level
	 * @return
	 */

	KingResponseDTO getHighScoreByLevel(Long level);
}
