package test.eidos.kingchanllenge.persistance;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import org.eidos.kingchallenge.domain.comparator.KingScoreByScore;
import org.eidos.kingchallenge.domain.comparator.KingScoreChainedComparator;
import org.eidos.kingchallenge.domain.comparator.KingScoreUserIdComparator;
import org.eidos.kingchallenge.domain.model.KingScore;
import org.eidos.kingchallenge.persistance.ScorePersistance;
import org.eidos.kingchallenge.persistance.SimpleScorePersistance;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSkipSet {
	static final Logger LOG = LoggerFactory.getLogger(TestSkipSet.class);
	@Test
	public void testAEmtpy(){
		
	}
	@Test
	public void testBagComparable(){
		ScorePersistance sp= new SimpleScorePersistance();
		sp.put(1, new KingScore.Builder( 3, 132L).build() );
		sp.put(1, new KingScore.Builder( 2, 132L).build() );
		sp.put(1, new KingScore.Builder( 5, 131L).build() );
		sp.put(5, new KingScore.Builder( 3, 132L).build() );
		
		sp.toString();
	}


	@Test
	public void testComparableList() {

		ConcurrentSkipListSet<KingScore> setKingScore = new ConcurrentSkipListSet<KingScore>(
				new KingScoreChainedComparator( new KingScoreByScore(),new KingScoreUserIdComparator()  )	);
		setKingScore.add(	new KingScore.Builder( 3, 132L).build());
		setKingScore.add(	new KingScore.Builder( 1, 131L).build());
		setKingScore.add(	new KingScore.Builder( 1, 132L).build());
		setKingScore.add(	new KingScore.Builder( 3, 131L).build());
		setKingScore.add(	new KingScore.Builder( 6, 131L).build());
		setKingScore.add(	new KingScore.Builder( 6, 132L).build());
		setKingScore.add(	new KingScore.Builder( 6, 135L).build());
		setKingScore.add(	new KingScore.Builder( 6, 135L).build());
		setKingScore.add(	new KingScore.Builder( 6, 135L).build());
		setKingScore.add(	new KingScore.Builder( 6, 135L).build());
		setKingScore.add(	new KingScore.Builder( 6, 135L).build());
		setKingScore.add(	new KingScore.Builder( 6, 135L).build());
		setKingScore.add(	new KingScore.Builder( 6, 135L).build());
		setKingScore.add(	new KingScore.Builder( 6, 135L).build());
		
		KingScore result = setKingScore.ceiling(new KingScore.Builder( 1, 135L).build());
		if (result==null ) 
		setKingScore.add(new KingScore.Builder( 7, 135L).build());
		for ( KingScore elem : setKingScore ){
			LOG.debug(" {}",elem);
		}

		LOG.debug("element found  {}",  result);
	}



}
