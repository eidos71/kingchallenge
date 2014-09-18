package test.eidos.kingchanllenge.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
import org.eidos.kingchallenge.domain.model.KingUser;
import org.eidos.kingchallenge.exceptions.KingInvalidSessionException;
import org.eidos.kingchallenge.repository.LoginRepository;
import org.eidos.kingchallenge.repository.ScoreRepository;
import org.eidos.kingchallenge.service.SimpleScoreService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(EasyMockRunner.class)
public class TestScoreService extends EasyMock{
	private static final Logger LOG = LoggerFactory
			.getLogger(TestScoreService.class);
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
	public void testGetHighScore(){
		scoreService.getHighScoreList(1L);
	}
}
