package test.eidos.kingchanllenge;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSkipSet {
	static final Logger LOG = LoggerFactory.getLogger(TestSkipSet.class);
	Comparator<KingScoreDTO> insertionOrderComparator = new Comparator<KingScoreDTO>() {
		private final ConcurrentHashMap<KingScoreDTO, Integer> order = new ConcurrentHashMap<KingScoreDTO, Integer>();
		@Override
		public int compare(KingScoreDTO o1, KingScoreDTO o2) {
			if (!order.contains(o2))
				order.put(o2, 0);
			if (order.containsKey(o1))
				return order.get(o1).compareTo(order.get(o2));
			order.put(o1, order.size());
			return 1;
		}
	};

	@Test
	public void testComparableList() {
		;
		ConcurrentSkipListSet<KingScoreDTO> setKingScore = new ConcurrentSkipListSet<KingScoreDTO>(
				new KingScoreChainedComparator(new KingScoreUserIdComparator(),  new KingScoreByScore() )	);
		setKingScore.add(	new KingScoreDTO.Builder(1L, 3, 132L).build());

		setKingScore.add(	new KingScoreDTO.Builder(1L, 1, 131L).build());
		setKingScore.add(	new KingScoreDTO.Builder(1L, 1, 132L).build());
		
		setKingScore.add(	new KingScoreDTO.Builder(1L, 3, 131L).build());

		setKingScore.add(	new KingScoreDTO.Builder(1L, 6, 131L).build());
		setKingScore.add(	new KingScoreDTO.Builder(1L, 6, 135L).build());
		
		
		for ( KingScoreDTO elem : setKingScore ){
			LOG.debug(" {}",elem);
		}


	}
	
	 class KingScoreUserIdComparator implements Comparator<KingScoreDTO> {
		 
	    @Override
	    public int compare(KingScoreDTO 	o1 , KingScoreDTO o2) {
	        return  o1.getKingUserId().compareTo( o2.getKingUserId() );
	    }
	}
	 class KingScoreByScore implements Comparator<KingScoreDTO> {
		 
		    @Override
		    public int compare(KingScoreDTO 	o1 , KingScoreDTO o2) {
		        return  o1.getPoints().compareTo( o2.getPoints() );
		    }
		}
	 class KingScoreChainedComparator implements Comparator<KingScoreDTO> {

		    private List<Comparator<KingScoreDTO>> listComparators;
			@SafeVarargs
		    public KingScoreChainedComparator(Comparator<KingScoreDTO>... comparators) {
		        this.listComparators = Arrays.asList(comparators);
		    }
		@Override
		public int compare(KingScoreDTO o1, KingScoreDTO o2) {
			 for (Comparator<KingScoreDTO> comparator : listComparators) {
		            int result = comparator.compare(o1, o2);
		            if (result != 0) {
		                return result;
		            }
		        }
		        return 0;
		}
		 
		
	}
}
