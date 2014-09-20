package org.eidos.kingchallenge.repository;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.eidos.kingchallenge.KingConfigStaticProperties;
import org.eidos.kingchallenge.domain.comparator.KingScoreChainedComparator;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseOrderByScore;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseUserIdComparator;
import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
import org.eidos.kingchallenge.domain.model.KingScore;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.persistance.KingdomConfManager;
import org.eidos.kingchallenge.persistance.ScorePersistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleScoreRepository implements ScoreRepository{

	static final Logger LOG = LoggerFactory.getLogger(SimpleScoreRepository.class);
	private ScorePersistance scorePersistance;
	public SimpleScoreRepository() {
		this.scorePersistance=KingdomConfManager.getInstance().getPersistanceBag().getScorePersistance();
	}
	@Override
	public Boolean insertScore(KingScoreDTO kngDto) {
		if (kngDto==null)
			throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
		return scorePersistance.put(kngDto.getLevel().intValue(), new KingScore.Builder(kngDto.getPoints(), kngDto.getKingUserId()).build());
	}

	@Override
	public Set<KingScore> getTopScoresForLevel(Long levelValue) {
		SortedSet<KingScore> resultSet = scorePersistance.getScoresByLevel(levelValue.intValue());
		if (resultSet==null || resultSet.size()==0) {
			return Collections.emptySet();
		}
		ConcurrentSkipListSet<KingScore> resultAnotherKingScores= new ConcurrentSkipListSet<KingScore>(
				new KingScoreChainedComparator( new KingScoreReverseOrderByScore(),new KingScoreReverseUserIdComparator()  ));
		resultAnotherKingScores.addAll(resultSet);
		Set<KingScore> kingSetScore= new TreeSet<KingScore>(new KingScoreReverseUserIdComparator());
		
		KingScore pollResult;
		while (kingSetScore.size()<KingConfigStaticProperties.TOPLISTSCORE && resultAnotherKingScores.size()>0){
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




