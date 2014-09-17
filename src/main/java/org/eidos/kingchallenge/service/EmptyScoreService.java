package org.eidos.kingchallenge.service;

import java.util.Map;

import org.eidos.kingchallenge.model.KingScore;

public class EmptyScoreService implements ScoreService{



	@Override
	public Boolean insertScore(String sessionKey, KingScore score) {
		return false;
		
	}

	@Override
	public Map<Long, KingScore> getHighScoreList(Long level) {
		// TODO Auto-generated method stub
		return null;
	}

}
