package org.eidos.kingchallenge.service;

import java.util.Collections;
import java.util.Map;

import org.eidos.kingchallenge.domain.dto.KingScoreDTO;

public class EmptyScoreService implements ScoreService{



	@Override
	public Boolean insertScore(String sessionKey, KingScoreDTO score) {
		return false;
		
	}

	@Override
	public String getHighScoreList(Long level) {
		Map<Long, KingScoreDTO>map= Collections.emptyMap();
		return "";
	}

	@Override
	public String getHighScoreList(String sessionKey,
			Long levelValue) {
	
		return "";
	}

}
