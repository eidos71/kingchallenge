package org.eidos.kingchallenge.persistance;

import org.eidos.kingchallenge.domain.model.KingScore;

public interface ScorePersistance {

	void put(Integer login, KingScore kingscore);
	
	
	

}
