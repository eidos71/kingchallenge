package test.eidos.kingchanllenge.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.TreeSet;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.eidos.kingchallenge.domain.comparator.KingScoreChainedComparator;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseOrderByScore;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseUserIdComparator;
import org.eidos.kingchallenge.domain.model.KingScore;
import org.eidos.kingchallenge.utils.CollectionsChallengeUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(EasyMockRunner.class)
public class TestCollectionUtils extends EasyMock {
	private static final Logger LOG = LoggerFactory
			.getLogger(TestCollectionUtils.class);
	private static TreeSet<KingScore> setScore=  new TreeSet<KingScore>(
			new KingScoreChainedComparator( new KingScoreReverseOrderByScore(),new KingScoreReverseUserIdComparator()  )	);
	
	@BeforeClass public static void setupOnlyOnce() {
				//User 1
				setScore.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 1L)) 	 );
				//User 2
				setScore.add( TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 2L)) );	
				//User 3
				setScore.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 3L))  );	
				//User 4
				setScore.add(   TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 4L))  );	
				//User5
				setScore.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 5L))  );	
	}
	@Test
	public void validateOkCollecion(){
		String expected= "5=34, 4=34, 3=34, 2=34, 1=34";
			CollectionsChallengeUtils.returnCsvFromCollection(setScore);
		assertThat("", CollectionsChallengeUtils.returnCsvFromCollection(setScore), equalTo(expected));
		
	}
	@Test
	public void validateEmptyCollection(){
		String expected= "";
		TreeSet<KingScore> emptySet=  new TreeSet<KingScore>(
				new KingScoreChainedComparator( new KingScoreReverseOrderByScore(),new KingScoreReverseUserIdComparator()  )	);
		assertThat("", CollectionsChallengeUtils.returnCsvFromCollection(emptySet), equalTo(expected));
		
	}
	@Test
	public void validateNullCollection(){
		String expected= "";
		TreeSet<KingScore> nullSet=null;
		assertThat("", CollectionsChallengeUtils.returnCsvFromCollection(nullSet), equalTo(expected));

	}
	@Test
	public void validateCollectionWithNullElements(){
		String expected="1=34, null, 3=34, null, 5=34";
		ArrayList<KingScore> hasNullsArrayList = new ArrayList<KingScore>();
		hasNullsArrayList.add(TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 1L)) ) ;
		hasNullsArrayList.add(null);
		hasNullsArrayList.add(TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 3L)) ) ;	
		hasNullsArrayList.add(null);
		hasNullsArrayList.add(TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 5L)) ) ;	

		assertThat("", CollectionsChallengeUtils.returnCsvFromCollection(hasNullsArrayList), equalTo(expected));
	
		
	}
}
