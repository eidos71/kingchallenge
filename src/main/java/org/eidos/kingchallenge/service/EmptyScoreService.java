package org.eidos.kingchallenge.service;

import java.util.Map;

import org.eidos.kingchallenge.domain.dto.KingScoreDTO;

public class EmptyScoreService implements ScoreService{



	@Override
	public Boolean insertScore(String sessionKey, KingScoreDTO score) {
		return false;
		
	}

	@Override
	public Map<Long, KingScoreDTO> getHighScoreList(Long level) {
		// TODO Auto-generated method stub
		return null;
	}

}
