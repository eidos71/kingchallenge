package org.eidos.kingchallenge.repository;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.eidos.kingchallenge.KingConfigConstants;
import org.eidos.kingchallenge.domain.comparator.KingScoreChainedComparator;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseOrderByScore;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseUserIdComparator;
import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
import org.eidos.kingchallenge.domain.model.KingScore;
import org.eidos.kingchallenge.persistance.ScorePersistance;
import org.eidos.kingchallenge.persistance.SimpleScorePersistance;
import org.eidos.kingchallenge.service.SimpleLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleScoreRepository implements ScoreRepository{

	static final Logger LOG = LoggerFactory.getLogger(SimpleScoreRepository.class);
	private ScorePersistance scorePersistance;
	public SimpleScoreRepository() {
		this.scorePersistance=new SimpleScorePersistance();
	}
	@Override
	public Boolean insertScore(KingScoreDTO kingScore) {
		
		return null;
	}

	@Override
	public Set<KingScore> getTopScoresForLevel(Long levelValue) {
		SortedSet<KingScore> resultSet = scorePersistance.getScoresByLevel(levelValue.intValue());
		ConcurrentSkipListSet<KingScore> resultAnotherKingScores= new ConcurrentSkipListSet<KingScore>(
				new KingScoreChainedComparator( new KingScoreReverseOrderByScore(),new KingScoreReverseUserIdComparator()  ));
		resultAnotherKingScores.addAll(resultSet);
		Set<KingScore> kingSetScore= new TreeSet<KingScore>(new KingScoreReverseUserIdComparator());
		
		KingScore pollResult;
		while (kingSetScore.size()<KingConfigConstants.TOPLISTSCORE && resultAnotherKingScores.size()>0){
			 pollResult = resultAnotherKingScores.pollFirst();
			
			if (!kingSetScore.contains(pollResult) ){
				LOG.trace("inserting--> {}",pollResult);
				kingSetScore.add(	pollResult );
			}		
		
		}
		LOG.debug("Set to return {}", kingSetScore);
		return kingSetScore;
		}
	}



