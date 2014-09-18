package org.eidos.kingchallenge.persistance;

import java.util.Set;
import java.util.SortedSet;

import org.eidos.kingchallenge.domain.model.KingScore;

public class EmptyScorePersistance implements ScorePersistance {

	@Override
	public void put(Integer login, KingScore kingscore) {
	
		
	}

	@Override
	public SortedSet<KingScore> getScoresByLevel(Integer level) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean dumpPersistance() {
		// TODO Auto-generated method stub
		return false;
	}

}
