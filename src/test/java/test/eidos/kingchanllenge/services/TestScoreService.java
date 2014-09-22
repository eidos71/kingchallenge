package test.eidos.kingchanllenge.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Set;
import java.util.TreeSet;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.eidos.kingchallenge.domain.comparator.KingScoreChainedComparator;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseOrderByScore;
import org.eidos.kingchallenge.domain.comparator.KingScoreReverseUserIdComparator;
import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
import org.eidos.kingchallenge.domain.model.KingScore;
import org.eidos.kingchallenge.domain.model.KingUser;
import org.eidos.kingchallenge.exceptions.KingInvalidSessionException;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.repository.LoginRepository;
import org.eidos.kingchallenge.repository.ScoreRepository;
import org.eidos.kingchallenge.service.SimpleScoreService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.eidos.kingchanllenge.AbstractKingTest;
import test.eidos.kingchanllenge.utils.TestUtils;

@RunWith(EasyMockRunner.class)
public class TestScoreService extends AbstractKingTest{

	@Mock
	private LoginRepository loginRepository;
	@Mock
	private ScoreRepository scoreRepository;
	@TestSubject
	private SimpleScoreService scoreService= new SimpleScoreService();
	@Before
	public void init(){
		
	}
	@Test
	public void testScoreService() {
		
		KingScoreDTO score= new KingScoreDTO.Builder(1L, 15350,3150L).build();
		//expect(loginService.loginToken(new AtomicLong(-1234L))).andThrow(new RuntimeException());
		expect(loginRepository.findBySessionId("MOCK") ).andReturn(
				new KingUser.Builder(3150).build() );
		
		expect(scoreRepository.insertScore( score) ).andReturn(true);
		replay(loginRepository);
		replay(scoreRepository);
		scoreService.setLoginRepository(loginRepository);
		scoreService.setScoreRepository(scoreRepository);
		
		assertThat("", scoreService.insertScore("MOCK", score), equalTo(true));
	}
	@Test(expected=KingInvalidSessionException.class)
	public void testMissingScore() {
		expect(loginRepository.findBySessionId("MOCK") ).andReturn(null
				);
		replay(loginRepository);
		KingScoreDTO score= new KingScoreDTO.Builder(1L, 15350,3150L).build();
		assertThat("", scoreService.insertScore("MOCK", score), equalTo(true));
	}
	
	@Test(expected=KingInvalidSessionException.class)
	public void testInvalidSessionKey() {
		expect(loginRepository.findBySessionId("MOCK") ).andReturn(null
				);
		replay(loginRepository);
		KingScoreDTO score= new KingScoreDTO.Builder(1L, 15350,3150L).build();
		assertThat("", scoreService.insertScore(null, score), equalTo(true));
	}
	@Test
	public void testGetHighScoreAllOk(){

		String expectedResult="4=4000, 5=34, 3=34, 2=34, 1=34";
		
		Set<KingScore> setScore= new TreeSet<KingScore>(
				new KingScoreChainedComparator( new KingScoreReverseOrderByScore(),new KingScoreReverseUserIdComparator()  )	);
		//User 1
		setScore.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 1L)) 	 );
		//User 2
		setScore.add( TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 2L)) );	
		//User 3
		setScore.add(  TestUtils.transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 3L))  );	
		//User 4
		setScore.add( TestUtils. transformDTO(TestUtils.craeteKingScoreDTO(1L, 4000, 4L))  );	
		//User5
		setScore.add( TestUtils. transformDTO(TestUtils.craeteKingScoreDTO(1L, 34, 5L))  );	
		String sessionKey= "MOCK";
	
		//expect(loginService.loginToken(new AtomicLong(-1234L))).andThrow(new RuntimeException());
	

		expect(loginRepository.findBySessionId(sessionKey) ).andReturn(
				new KingUser.Builder(3150).build() );
		expect(scoreRepository.getTopScoresForLevel(1L) ).andReturn(setScore);	
		
		replay(loginRepository);
		replay(scoreRepository);		
		assertThat("", scoreService.getHighScoreList(sessionKey, 1L), equalTo(expectedResult));
		
	}
	@Test(expected=KingInvalidSessionException.class)
	public void testGetHighScoreSessionInvalid() {
		expect(loginRepository.findBySessionId("MOCK") )
			.andThrow(new KingInvalidSessionException());
		replay(loginRepository);		
		scoreService.getHighScoreList("MOCK",1L);
	}
	@Test
	public void testGetHighEmptyHighScoreForLevel() {
		Set<KingScore> setScore= new TreeSet<KingScore>();
		String sessionKey= "MOCK";
		//expect(loginService.loginToken(new AtomicLong(-1234L))).andThrow(new RuntimeException());
		expect(loginRepository.findBySessionId("MOCK") ).andReturn(
				new KingUser.Builder(3150).build() );
		expect(scoreRepository.getTopScoresForLevel(1L) ).andReturn(setScore);	
		replay(loginRepository);
		replay(scoreRepository);
		assertThat("", scoreService.getHighScoreList(sessionKey, 1L), equalTo(""));
	}
	@Test(expected=LogicKingChallengeException.class)
	public void testGetHighScoreWithNoLevelSet() {
		Set<KingScore> setScore= new TreeSet<KingScore>();
		String sessionKey= "MOCK";
		//expect(loginService.loginToken(new AtomicLong(-1234L))).andThrow(new RuntimeException());
		expect(loginRepository.findBySessionId("MOCK") ).andReturn(
				new KingUser.Builder(3150).build() );
		expect(scoreRepository.getTopScoresForLevel(1L) ).andReturn(setScore);	
		replay(loginRepository);
		replay(scoreRepository);
		assertThat("", scoreService.getHighScoreList(sessionKey, null), equalTo(""));
		
	}
	@Test(expected=KingInvalidSessionException.class)
	public void testgetHighScorewithSessionCaducated() {
		assertThat("", scoreService.getHighScoreList(null, 31L), equalTo(""));
		
	}

}
