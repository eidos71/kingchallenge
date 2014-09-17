package org.eidos.kingchallenge.persistance;

import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.annotation.concurrent.ThreadSafe;

import org.eidos.kingchallenge.domain.comparator.KingScoreByScore;
import org.eidos.kingchallenge.domain.comparator.KingScoreChainedComparator;
import org.eidos.kingchallenge.domain.comparator.KingScoreUserIdComparator;
import org.eidos.kingchallenge.domain.model.KingScore;
import org.eidos.kingchallenge.utils.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ThreadSafe
final public class SimpleScorePersistance implements ScorePersistance {
	static final Logger LOG = LoggerFactory
			.getLogger(SimpleScorePersistance.class);

	private final NavigableMap<Integer, NavigableSet<KingScore>> navmapScore;

	public SimpleScorePersistance() {
		this.navmapScore = new ConcurrentSkipListMap<Integer, NavigableSet<KingScore>>();
	}

	@Override
	public void put(Integer level, KingScore kingScore) {
		if (!Validator.isValidUnsignedInt(level))
			return;
		if (kingScore == null)
			return;
		// If Level is not Set
		if (navmapScore.get(level) == null) {
			// We create a new SkipListSet
			NavigableSet<KingScore> setKingScore = new ConcurrentSkipListSet<KingScore>(
					new KingScoreChainedComparator(
							new KingScoreUserIdComparator(),
							new KingScoreByScore()));
			setKingScore.add(kingScore);
			navmapScore.put(level, setKingScore);
		} else {
			NavigableSet<KingScore> existLevelMap = navmapScore.get(level);
			existLevelMap.add(kingScore);
		}
	}

	@Override
	public String toString() {
	
		StringBuilder sf= new StringBuilder();
		
		for (Entry<Integer, NavigableSet<KingScore>> anEntry: navmapScore.entrySet()){
			
			LOG.debug(" {}", anEntry.getKey() );
			for (KingScore setElem: anEntry.getValue() ) {
				LOG.debug("{}", setElem);
			}
		}
		
		return "SimpleScorePersistance [navmapScore=" + navmapScore + "]";
	}
	
}
