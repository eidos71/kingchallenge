package org.eidos.kingchallenge.repository;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.eidos.kingchallenge.KingConfigStaticProperties;
import org.eidos.kingchallenge.KingdomConfManager;
import org.eidos.kingchallenge.domain.comparator.KingScoreChainedComparator;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseOrderByScore;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseUserIdComparator;
import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
import org.eidos.kingchallenge.domain.model.KingScore;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.persistance.ScorePersistance;


public final class SimpleScoreRepository implements ScoreRepository{

		//It is not final because we need to mock it.
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
		Set<KingScore> kingSetScore = new TreeSet<KingScore>(
				new KingScoreChainedComparator(
						new KingScoreReverseOrderByScore(),
						new KingScoreReverseUserIdComparator()));
		
		Set<KingScore> kingUserUnique = new TreeSet<KingScore>(
				new KingScoreReverseUserIdComparator());
		KingScore pollResult;
		for (Iterator<KingScore> resultIterator = resultSet.iterator(); resultIterator
				.hasNext();) {
			pollResult = (KingScore) resultIterator.next();
			if (!kingSetScore.contains(pollResult)
					&& !kingUserUnique.contains(pollResult)) {
		
				kingSetScore.add(pollResult);
				kingUserUnique.add(pollResult);
				if (kingSetScore.size() >= KingConfigStaticProperties.TOPLISTSCORE)
					break;
			}
		
		}	

		return kingSetScore;
		}
		/**
		 * This setter is ONLY to be used by TEST 
		 * In another context, a FluidBuilderPattern with private reflection or some alternative
		 * will be defined to avoid setting the Persistance from a Setter method
		 * @param mockPersistance Persistance Mock
		 */
		public void setMockPersistance(ScorePersistance mockPersistance) {
			this.scorePersistance=mockPersistance;
		}
	}




