package org.eidos.kingchallenge.repository;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
import org.eidos.kingchallenge.domain.model.KingScore;
/**
 * EmptyScore class repository
 * @author eidos71
 *
 */
public class EmptyScoreRepository implements ScoreRepository {

	@Override
	public Boolean insertScore(KingScoreDTO kingScore) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<KingScore> getTopScoresForLevel(Long levelValue) {
		// TODO Auto-generated method stub
		return null;
	}




}
