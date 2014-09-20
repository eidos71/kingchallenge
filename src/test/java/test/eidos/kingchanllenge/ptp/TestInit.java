package test.eidos.kingchanllenge.ptp;

import static org.easymock.EasyMock.*;

import java.util.concurrent.atomic.AtomicLong;

import org.easymock.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.eidos.kingchallenge.controller.SimpleLoginController;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.httpserver.enums.KingControllerEnum;
import org.eidos.kingchallenge.httpserver.utils.MediaContentTypeEnum;
import org.eidos.kingchallenge.service.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public class TestInit extends EasyMockSupport {
	@Mock
	private LoginService loginService;
	@TestSubject
	private SimpleLoginController loginController = new SimpleLoginController.Builder().build();

	/**
	 * -Test an Invalid character
	 */
	@Test(expected = LogicKingChallengeException.class)
	public void testInvalidNegativeLogin() {
	
		expect(loginService.loginToken(new AtomicLong(-1234L))).andThrow(new LogicKingChallengeException(LogicKingError.INVALID_TOKEN));
		replay(loginService);
		loginController.loginService(-1234L );
	}
	
	@Test
	public void expectedControllers() {
		assertThat("", KingControllerEnum.LOGIN.controller(), equalTo("login"	));
		assertThat("", KingControllerEnum.SCORE.controller(), equalTo("score"	));

		assertThat("", KingControllerEnum.HIGHSCORELIST.controller(), equalTo("highscorelist"	) );
		assertThat("", KingControllerEnum.UNKNOWN.controller(), equalTo(""	) );

		assertThat("", MediaContentTypeEnum.APPLICATION_XML.code(), equalTo("application/xmlcharset=UTF-8"	) );
		assertThat("", MediaContentTypeEnum.TEXT_PLAIN.code(), equalTo("application/json; charset=UTF-8"	) );

		
	}
}
