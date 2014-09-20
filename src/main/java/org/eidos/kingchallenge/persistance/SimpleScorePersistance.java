package org.eidos.kingchallenge.persistance;

import java.util.Map.Entry;
import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.eidos.kingchallenge.KingConfigStaticProperties;
import org.eidos.kingchallenge.domain.KingSizeLimitedScore;
import org.eidos.kingchallenge.domain.comparator.KingScoreOrderByScore;
import org.eidos.kingchallenge.domain.comparator.KingScoreChainedComparator;
import org.eidos.kingchallenge.domain.comparator.KingScoreUserIdComparator;
import org.eidos.kingchallenge.domain.model.KingScore;
import org.eidos.kingchallenge.domain.model.KingUser;
import org.eidos.kingchallenge.utils.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ThreadSafe
/**
 * Simple Persistance Layer
 * It stores the Scoring in a  @NavigableMap , inside a @ConcurrentSkipListSet will store
 * the Score, set by a ChainedComparator
 * @author eidos71
 *
 */
final public class SimpleScorePersistance implements ScorePersistance {
	private static final NavigableSet<KingScore> EMPTY_SET;

	static {

		EMPTY_SET=  new ConcurrentSkipListSet<KingScore>(
				new KingScoreChainedComparator(
						new KingScoreOrderByScore() ,		
						new KingScoreUserIdComparator()

						));
	};
	static final Logger LOG = LoggerFactory
			.getLogger(SimpleScorePersistance.class);
	private static final int MAXSIZE = 10000000;
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
							new KingScoreOrderByScore(),
							new KingScoreUserIdComparator()

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
			
			LOG.trace(" Level:[{}", anEntry.getKey() );
			sf.append("Level["+anEntry.getKey() );
			for (KingScore setElem: anEntry.getValue() ) {
				LOG.trace("{}", setElem);
				sf.append("["+setElem.toString()+"],");
			}
			sf.append("]" );
			LOG.trace("]");
			
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
			LOG.warn("error clean collectoin {}",err);
		}
		return result;
	}
	
}
