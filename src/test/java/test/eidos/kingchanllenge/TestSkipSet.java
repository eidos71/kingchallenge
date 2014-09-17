package test.eidos.kingchanllenge;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
import org.eidos.kingchallenge.domain.model.KingScore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSkipSet {
	static final Logger LOG = LoggerFactory.getLogger(TestSkipSet.class);


	@Test
	public void testComparableList() {
		;
		ConcurrentSkipListSet<KingScore> setKingScore = new ConcurrentSkipListSet<KingScore>(
				new KingScoreChainedComparator(new KingScoreUserIdComparator(),  new KingScoreByScore() )	);
		setKingScore.add(	new KingScore.Builder( 3, 132L).build());

		setKingScore.add(	new KingScore.Builder( 1, 131L).build());
		setKingScore.add(	new KingScore.Builder( 1, 132L).build());
		
		setKingScore.add(	new KingScore.Builder( 3, 131L).build());

		setKingScore.add(	new KingScore.Builder( 6, 131L).build());
		setKingScore.add(	new KingScore.Builder( 6, 135L).build());
		
		
		for ( KingScore elem : setKingScore ){
			LOG.debug(" {}",elem);
		}


	}
	
	 class KingScoreUserIdComparator implements Comparator<KingScore> {
		 
	    @Override
	    public int compare(KingScore 	o1 , KingScore o2) {
	        return  o1.getKingUserId().compareTo( o2.getKingUserId() );
	    }
	}
	 class KingScoreByScore implements Comparator<KingScore> {
		 
		    @Override
		    public int compare(KingScore 	o1 , KingScore o2) {
		        return  o1.getPoints().compareTo( o2.getPoints() );
		    }
		}
	 class KingScoreChainedComparator implements Comparator<KingScore> {

		    private List<Comparator<KingScore>> listComparators;
			@SafeVarargs
		    public KingScoreChainedComparator(Comparator<KingScore>... comparators) {
		        this.listComparators = Arrays.asList(comparators);
		    }
		@Override
		public int compare(KingScore o1, KingScore o2) {
			 for (Comparator<KingScore> comparator : listComparators) {
		            int result = comparator.compare(o1, o2);
		            if (result != 0) {
		                return result;
		            }
		        }
		        return 0;
		}
		 
		
	}
}
