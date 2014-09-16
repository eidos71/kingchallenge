package org.eidos.kingchallenge.repository;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author eidos71
 *
 */
public interface ScoreRepository {
	
	Boolean insertScore(String sessionKey, Long gameLevel, int score) ;
}
