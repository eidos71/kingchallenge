package test.eidos.kingchanllenge.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.eidos.kingchallenge.domain.comparator.KingScoreChainedComparator;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseOrderByScore;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseUserIdComparator;
import org.eidos.kingchallenge.domain.model.KingScore;
import org.eidos.kingchallenge.persistance.EmptyScorePersistance;
import org.eidos.kingchallenge.persistance.ScorePersistance;
import org.eidos.kingchallenge.repository.SimpleScoreRepository;
import org.eidos.kingchallenge.utils.CollectionsChallengeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import test.eidos.kingchanllenge.AbstractKingTest;
import test.eidos.kingchanllenge.utils.TestUtils;
@RunWith(EasyMockRunner.class)
public class TestScoreRespository  extends AbstractKingTest {

	@Mock()
	private ScorePersistance scorePersistance;
	@TestSubject
	private SimpleScoreRepository 	scoreRepo= new SimpleScoreRepository();;
	@Before
	public void init(){
		
	}
	
	@Test
	/**
	 * Actually this is a functional issue, I don't know what is the proper thing to do if more than x different users has
	 * more than expected TOPLISTELEMS, right now it returns the first 15 with the order of SessionId as secondary
	 * order
	 */
	public void  testListofelementsWithAllToplistSimilarScore() {
	
		String expectedResult="16=34, 15=34, 14=34, 13=34, 12=34, 11=34, 10=34, 9=34, 8=34, 7=34, 6=34, 5=34, 4=34, 3=34, 2=34";
		SortedSet<KingScore> scoreSet= new ConcurrentSkipListSet<KingScore>(
				new KingScoreChainedComparator(
						new KingScoreReverseOrderByScore(),
						new KingScoreReverseUserIdComparator()
						));
		//User 1
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 1L)) 	 );
		//User 2
		scoreSet.add( TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 2L)) );	
		//User 3
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 3L))  );	
		//User14
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 14L))  );	
		//User15
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 15L))  );	
		//User 8
		scoreSet.add( TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 8L)) );	
		//User 9
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 9L))  );	
		//User 10
		scoreSet.add( TestUtils. transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 10L))  );	
		//User11
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 11L))  );	
		//User12
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 12L))  );			
		//User 4
		scoreSet.add( TestUtils. transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 4L))  );	
		//User5
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 5L))  );	
		//User6
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 6L))  );	
		//User 7
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 7L)) 	 );		
		//User13
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 13L))  );	
	//User16
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 16L))  );			
		expect(scorePersistance.getScoresByLevel(1) ).andReturn(scoreSet
			 );
		replay(scorePersistance);		
		scoreRepo.setMockPersistance(scorePersistance);		
		assertThat("", CollectionsChallengeUtils.returnCsvFromCollection(scoreRepo.getTopScoresForLevel(1L)), equalTo(expectedResult));
		
	}
	@Test
	public void testEmptySetMapForNoresults() {
		String expectedResult="";
		SortedSet<KingScore> scoreSet= new ConcurrentSkipListSet<KingScore>(
				new KingScoreChainedComparator(
						new KingScoreReverseOrderByScore(),
						new KingScoreReverseUserIdComparator()
						));
			expect(scorePersistance.getScoresByLevel(1) ).andReturn(scoreSet
				 );
			replay(scorePersistance);		
			scoreRepo.setMockPersistance(scorePersistance);		
			assertThat("", CollectionsChallengeUtils.returnCsvFromCollection(scoreRepo.getTopScoresForLevel(1L)), equalTo(expectedResult));
	}
	@Test
	public void testtestNullSetMapForNoresults() {
		String expectedResult="";

			expect(scorePersistance.getScoresByLevel(1) ).andReturn(null
				 );
			replay(scorePersistance);		
			scoreRepo.setMockPersistance(scorePersistance);		
			assertThat("", CollectionsChallengeUtils.returnCsvFromCollection(scoreRepo.getTopScoresForLevel(1L)), equalTo(expectedResult));
	}
	@Test
	public void testUserWithMultipleTopScoresButOnlyReturningHerHighest() {
		String expectedResult="10=1000, 14=800, 16=34, 15=34, 13=34, 12=34, 11=34, 9=34, 8=34, 7=34, 6=34, 5=34, 4=34, 3=34, 2=34";
		SortedSet<KingScore> scoreSet= new ConcurrentSkipListSet<KingScore>(
				new KingScoreChainedComparator(
						new KingScoreReverseOrderByScore(),
						new KingScoreReverseUserIdComparator()
						));
		//User 1
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 1L)) 	 );
		//User 2
		scoreSet.add( TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 2L)) );	
		//User 3
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 3L))  );	
		//User14
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 14L))  );	
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 700, 14L))  );	
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 800, 14L))  );	
		//User15
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 15L))  );	
		//User 8
		scoreSet.add( TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 8L)) );	
		//User 9
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 9L))  );	
		//User 10
		scoreSet.add( TestUtils. transformDTO(TestUtils.craeteKingScoreDTO(1L, 1000, 10L))  );	
		scoreSet.add( TestUtils. transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 10L))  );	
		//User11
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 11L))  );	
		//User12
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 12L))  );			
		//User 4
		scoreSet.add( TestUtils. transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 4L))  );	
		//User5
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 5L))  );	
		//User6
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 6L))  );	
		//User 7
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 7L)) 	 );		
		//User13
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 13L))  );	
		//User16
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 16L))  );			
		//User17
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 3, 16L))  );			
		//User18
		scoreSet.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 1, 16L))  );					
		expect(scorePersistance.getScoresByLevel(1) ).andReturn(scoreSet
			 );
		replay(scorePersistance);		
		scoreRepo.setMockPersistance(scorePersistance);		
		assertThat("", CollectionsChallengeUtils.returnCsvFromCollection(scoreRepo.getTopScoresForLevel(1L)), equalTo(expectedResult));
	}
	
	
}