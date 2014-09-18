package org.eidos.kingchallenge.repository;

import java.util.Collections;
import java.util.Map;
import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
/**
 * EmptyScore class repository
 * @author eidos71
 *
 */
public class EmptyScoreRepository implements ScoreRepository {


	@Override
	public Boolean insertScore(String sessionKey, Long gameLevel, int score) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<Long, KingScoreDTO> getTopScoresForLevel(Long levelValue) {
		// TODO Auto-generated method stub
		 Map<Long, KingScoreDTO>map= Collections.emptyMap();
		 return map;
	}

}
