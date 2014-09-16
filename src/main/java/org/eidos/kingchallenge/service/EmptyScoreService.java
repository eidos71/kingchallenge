package org.eidos.kingchallenge.service;

import java.util.Map;

import org.eidos.kingchallenge.model.KingScore;

public class EmptyScoreService implements ScoreService{

	@Override
	public Map<String, String> getHighScoreList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean insertScore(String sessionKey, KingScore score) {
		return false;
		
	}

}
