package org.eidos.kingchallenge.persistance;


import java.util.SortedSet;

import org.eidos.kingchallenge.domain.model.KingScore;

public interface ScorePersistance {

	boolean put(Integer login, KingScore kingscore);

	 SortedSet<KingScore> getScoresByLevel(Integer level);
	/***
	 * Cleans the persistance storage from old versios.
	 */
	boolean dumpPersistance();
	
	
	

}
