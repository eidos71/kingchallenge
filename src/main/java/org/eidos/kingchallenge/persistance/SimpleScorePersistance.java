package org.eidos.kingchallenge.persistance;

import java.util.Map.Entry;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.eidos.kingchallenge.KingConfigStaticProperties;
import org.eidos.kingchallenge.domain.KingSizeLimitedScore;
import org.eidos.kingchallenge.domain.comparator.KingScoreChainedComparator;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseOrderByScore;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseUserIdComparator;
import org.eidos.kingchallenge.domain.model.KingScore;
import org.eidos.kingchallenge.utils.Validator;


@ThreadSafe
/**
 * Simple Persistance Layer
 * It stores the Scoring in a  @NavigableMap , inside a @ConcurrentSkipListSet will store
 * the Score, set by a ChainedComparator
 * @author eidos71
 *
 */
final  public class SimpleScorePersistance implements ScorePersistance {
	private static final NavigableSet<KingScore> EMPTY_SET;
	static final Logger LOG = Logger.getLogger(SimpleScorePersistance.class.getName() );

	static {

		EMPTY_SET=  new ConcurrentSkipListSet<KingScore>(
				new KingScoreChainedComparator(
						new KingScoreReverseOrderByScore(),
						new KingScoreReverseUserIdComparator()

						));
	};


	@GuardedBy("navmapScore")
	private final NavigableMap<Integer, NavigableSet<KingScore>> navmapScore;


	public SimpleScorePersistance() {
		this.navmapScore = new ConcurrentSkipListMap<Integer, NavigableSet<KingScore>>();
	}

	@Override
	public boolean put(Integer level, KingScore kingScore) {
		if (!Validator.isValidUnsignedInt(level))
			return false;
		if (kingScore == null)
			return false;
		// If Level is not Set
		if (navmapScore.get(level) == null) { 
			// We create a new SkipListSet
			synchronized(navmapScore) {
			NavigableSet<KingScore> setKingScore = new KingSizeLimitedScore<KingScore>(level,
					KingConfigStaticProperties.PERSISTANCE_SCORE_MAX_ELEMS_PER_LVL, 
					new KingScoreChainedComparator(
							new KingScoreReverseOrderByScore(),
							new KingScoreReverseUserIdComparator()

					));
			setKingScore.add(kingScore);
			navmapScore.put(level, setKingScore);
			}
		} else {
			NavigableSet<KingScore> existLevelMap = navmapScore.get(level);

			existLevelMap.add(kingScore) ;
			
		}
		return true;
	}

	@Override
	public  SortedSet<KingScore> getScoresByLevel(Integer level) {
		if (level==null || navmapScore==null) {
			return EMPTY_SET ;
		}
		NavigableSet<KingScore> levelNavMap = navmapScore.get(level);
		if (levelNavMap==null) return EMPTY_SET;
		return Collections.synchronizedSortedSet(levelNavMap);
		
	}

	@Override
	/**
	 * 
	 */
	public String toString() {
	
		StringBuilder sf= new StringBuilder("navmapScore:");
		
		for (Entry<Integer, NavigableSet<KingScore>> anEntry: navmapScore.entrySet()){
			
		
			sf.append("Level["+anEntry.getKey() );
			for (KingScore setElem: anEntry.getValue() ) {
		
				sf.append("["+setElem.toString()+"],");
			}
			sf.append("]" );
	
			
		}
		
		return sf.toString();
	}

	
	@Override
	public boolean dumpPersistance() {
		boolean result=false;
		try{
			synchronized (navmapScore) {
				for (Entry<Integer, NavigableSet<KingScore>> level : navmapScore
						.entrySet()) {
					// We clear all elements
					level.getValue().clear();
				}
				this.navmapScore.clear();
				result=true;
			}
		}catch (Exception err){
			LOG.severe(String.format("error clean collectoin %1$s",err) );
		}
		return result;
	}
	
}
