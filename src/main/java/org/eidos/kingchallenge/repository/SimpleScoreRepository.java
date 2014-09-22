package org.eidos.kingchallenge.repository;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.concurrent.ThreadSafe;

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

@ThreadSafe
public final class SimpleScoreRepository implements ScoreRepository{
	final AtomicBoolean isCacheValid = new AtomicBoolean(false);
	//Persistanc Map to avoid computing the expensive operation
	private   Map<Long,Set<KingScore>> cachedScore ;
		//It is not final because we need to mock it.
	private ScorePersistance scorePersistance;
	/**
	 * Public constructor, with specific Basic initialization from the Persistancebag
	 *  and the cache element.
	 */
	public SimpleScoreRepository() {
		this.scorePersistance=KingdomConfManager.getInstance().getPersistanceBag().getScorePersistance();
		this.cachedScore= new ConcurrentHashMap<Long,Set<KingScore>>(); 
	}
	@Override
	public Boolean insertScore(KingScoreDTO kngDto) {
		if (kngDto==null)
			throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
		
		boolean inserted = scorePersistance.put(kngDto.getLevel().intValue(), new KingScore.Builder(kngDto.getPoints(), kngDto.getKingUserId()).build());
		if (inserted){
			isCacheValid.set(false);
		}
		return inserted;
	}
	@Override
	public  Set<KingScore>   getTopScoresForLevel (Long levelValue){
		if (!isCacheValid.get() ){
			cachedScore.put(levelValue, 
					 Collections.unmodifiableSet(getInternalTopScoresForLevel( levelValue) ) );
			isCacheValid.set(true);
		
		}
		return cachedScore.get(levelValue);
		
	}
	/**
	 * Internal Private method that calculeates the top 15 elements for that level
	 * @param levelValue MapLevel
	 * @return a
	 */
	private Set<KingScore> getInternalTopScoresForLevel(Long levelValue) {
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




