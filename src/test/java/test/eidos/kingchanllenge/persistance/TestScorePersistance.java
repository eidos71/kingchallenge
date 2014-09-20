package test.eidos.kingchanllenge.persistance;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NavigableSet;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.eidos.kingchallenge.domain.comparator.KingScoreOrderByScore;
import org.eidos.kingchallenge.domain.comparator.KingScoreChainedComparator;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseOrderByScore;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseUserIdComparator;
import org.eidos.kingchallenge.domain.comparator.KingScoreUserIdComparator;
import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
import org.eidos.kingchallenge.domain.model.KingScore;
import org.eidos.kingchallenge.persistance.ScorePersistance;
import org.eidos.kingchallenge.persistance.SimpleScorePersistance;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(EasyMockRunner.class)
public class TestScorePersistance extends EasyMock {
	static final Logger LOG = LoggerFactory.getLogger(TestScorePersistance.class);
	// 1 MILLION PUTS
	private static final int PUTS = 500000;
	// 20 DIFFERENT LEVELS
	private static final int LEVEL=20;
	//MAXSCORE
	private static final int SCORE=50000;
	private static final int DIFFERENTUSERS=1000;
	private static List<KingScoreDTO> listKingScore= new ArrayList<KingScoreDTO>();


	@BeforeClass public static void setupOnlyOnce() {
		 final Random random = new Random();
		Long level;
		
		Integer score;
		Long userId;

		for (int i=0; i<PUTS; i++){
			 level=new Long(random.nextInt(LEVEL )+1);
			 userId= new Long(100+random.nextInt(DIFFERENTUSERS ));
			 score=  random.nextInt(SCORE );
				 //LOG.debug("level-{}, points-{}, score- {}",level,points,score);
			listKingScore.add( new KingScoreDTO.Builder(level,  score, userId ).build()  );
		}
		LOG.debug("*************");
	}
	
	@Test
	public void testEmtpy(){
		listKingScore.size();
		Assert.assertTrue(true);
	}

	@Test
	public void testComparableList() {

		ConcurrentSkipListSet<KingScore> setKingScore = new ConcurrentSkipListSet<KingScore>(
				new KingScoreChainedComparator( new KingScoreOrderByScore(),new KingScoreUserIdComparator()  )	);
		setKingScore.add(	new KingScore.Builder( 3, 132L).build());
		setKingScore.add(	new KingScore.Builder( 1, 131L).build());
		setKingScore.add(	new KingScore.Builder( 1, 132L).build());
		setKingScore.add(	new KingScore.Builder( 3, 131L).build());
		setKingScore.add(	new KingScore.Builder( 6, 131L).build());
		setKingScore.add(	new KingScore.Builder( 6, 132L).build());
		setKingScore.add(	new KingScore.Builder( 6, 135L).build());
		setKingScore.add(	new KingScore.Builder( 7, 135L).build());
		setKingScore.add(	new KingScore.Builder( 8, 135L).build());
		setKingScore.add(	new KingScore.Builder( 9, 135L).build());
		setKingScore.add(	new KingScore.Builder( 10, 135L).build());
		setKingScore.add(	new KingScore.Builder( 13, 135L).build());
		setKingScore.add(	new KingScore.Builder( 11, 135L).build());
		setKingScore.add(	new KingScore.Builder( 9, 135L).build());
		
		KingScore result = setKingScore.ceiling(new KingScore.Builder( 1, 135L).build());
		if (result==null ) 
		setKingScore.add(new KingScore.Builder( 7, 135L).build());
		for ( KingScore elem : setKingScore ){
			LOG.debug(" {}",elem);
		}

		LOG.debug("element found  {}",  result);
	}

	@Test()
	public void testC_BagComparable(){
	
		ScorePersistance sp= new SimpleScorePersistance();
		boolean result= sp.dumpPersistance();
		for (KingScoreDTO kingScoreDto: listKingScore){
			//LOG.debug("{}",kingScoreDto);
			sp.put(kingScoreDto.getLevel().intValue(), new KingScore.Builder( kingScoreDto.getPoints(),kingScoreDto.getLevel() ).build() );
		}

		
		
		//sp.toString();
	}
	@Test()
	public void testA_BagComparable(){

		ScorePersistance sp= new SimpleScorePersistance();
		boolean result= sp.dumpPersistance();
		for (KingScoreDTO kingScoreDto: listKingScore){
			//LOG.debug("{}",kingScoreDto);
			sp.put(kingScoreDto.getLevel().intValue(), new KingScore.Builder( kingScoreDto.getPoints(),kingScoreDto.getLevel() ).build() );
		}

		//sp.toString();
	}
	@Test
	public void testControlledBag() throws Exception{

		ScorePersistance sp= new SimpleScorePersistance();
		boolean result= sp.dumpPersistance();
		if (!result)
			throw new Exception("pum");
		//User 1
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 30, 1L)));
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 32, 1L)));
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 31, 1L)));
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 34, 1L)));		
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 33, 1L)));
		//User 2
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 30, 2L)));

		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 33, 2L)));
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 34, 2L)));	
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 31, 2L)));
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 32, 2L)));
		//User 3
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 33, 3L)));
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 34, 3L)));	
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 31, 3L)));
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 30, 3L)));

		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 32, 3L)));
	
		//User 4
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 30, 4L)));

		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 32, 4L)));
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 33, 4L)));
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 31, 4L)));
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 34, 4L)));		
		//User 5
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 30, 5L)));
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 33, 5L)));
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 32, 5L)));
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 31, 5L)));
		sp.put(1, transformDTO(craeteKingScoreDTO(1L, 34, 5L)));		
	
		sp.toString();
		 
		SortedSet<KingScore> resultSet = sp.getScoresByLevel(1);
		ConcurrentSkipListSet<KingScore> resultAnotherKingScores= new ConcurrentSkipListSet<KingScore>(
				new KingScoreChainedComparator( new KingScoreReverseOrderByScore(),new KingScoreReverseUserIdComparator()  ));
		resultAnotherKingScores.addAll(resultSet);
		Set<KingScore> kingSetScore= new TreeSet<KingScore>(new KingScoreReverseUserIdComparator());

		int _maxNumElemsn=5;
		KingScore pollResult;
		while (kingSetScore.size()<_maxNumElemsn && resultAnotherKingScores.size()>0){
			 pollResult = resultAnotherKingScores.pollFirst();
				LOG.debug("pollResult {}, {} ",pollResult);
			if (!kingSetScore.contains(pollResult) ){
				LOG.debug("inserting--> {}",pollResult);
				kingSetScore.add(	pollResult );
			}
	
		}
	
		LOG.debug("size-{}",resultSet.size() );
		resultSet.remove(transformDTO(craeteKingScoreDTO(1L, 34, 5L)) );
		resultSet.size();
		LOG.debug("size-{}",resultSet.size() );
		LOG.debug("resultAnotherKingScores-{}",resultAnotherKingScores.size() );
		LOG.debug("Set to return {}", kingSetScore);
		
	
	} 
	
	@Test
	public void testAutomaticBagPersisatnce() throws Exception{
		
		 final Random random = new Random();
		 ScorePersistance sp= new SimpleScorePersistance();
			boolean result= sp.dumpPersistance();
		Long level;
		
		Integer score;
		Long userId;

		for (int i=0; i<PUTS; i++){
			
			 level=new Long(random.nextInt(LEVEL )+1);
			 userId= new Long(100+random.nextInt(DIFFERENTUSERS ));
			 score=  random.nextInt(SCORE );

			sp.put(level.intValue(), transformDTO(craeteKingScoreDTO(level, score, userId)));
			}
		SortedSet<KingScore> resultSet = sp.getScoresByLevel(1);
		ConcurrentSkipListSet<KingScore> resultAnotherKingScores= new ConcurrentSkipListSet<KingScore>(
				new KingScoreChainedComparator( new KingScoreReverseOrderByScore(),new KingScoreReverseUserIdComparator()  ));
		resultAnotherKingScores.addAll(resultSet);
		Set<KingScore> kingSetScore= new TreeSet<KingScore>(new KingScoreReverseUserIdComparator());
		int _maxNumElemsn=5;
		KingScore pollResult;
		while (kingSetScore.size()<_maxNumElemsn && resultAnotherKingScores.size()>0){
			 pollResult = resultAnotherKingScores.pollFirst();
				LOG.debug("pollResult {}, {} ",pollResult);
			if (!kingSetScore.contains(pollResult) ){
				LOG.debug("inserting--> {}",pollResult);
				kingSetScore.add(	pollResult );
			}		
		
		}
		LOG.debug("Set to return {}", kingSetScore);
		}
	
	/**
	 * 
	 * @return
	 */
	private  KingScoreDTO  craeteKingScoreDTO(Long lvl, Integer score, Long userId){
		return new KingScoreDTO.Builder(lvl, score, userId).build();
		
	}
	private KingScore transformDTO(KingScoreDTO dto){
		return new KingScore.Builder(  dto.getPoints(), dto.getKingUserId() ).build() ;
	}

}
