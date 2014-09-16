package test.eidos.kingchanllenge.services;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.eidos.kingchallenge.repository.LoginRepository;
import org.eidos.kingchallenge.repository.ScoreRepository;
import org.eidos.kingchallenge.service.LoginService;
import org.eidos.kingchallenge.service.ScoreService;
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
	@Test(expected = RuntimeException.class)
	public void testScoreService() {
		scoreService.setLoginRepository(loginRepository);
		scoreService.setScoreRepository(scoreRepository);
		
	}
}
