package test.eidos.kingchanllenge.ptp;

import static org.easymock.EasyMock.*;

import java.util.concurrent.atomic.AtomicLong;

import org.easymock.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.eidos.kingchallenge.controller.SimpleLoginController;
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
	@Test(expected = RuntimeException.class)
	public void testInvalidNegativeLogin() {
		//
		expect(loginService.loginToken(new AtomicLong(-1234L))).andThrow(new RuntimeException());
		replay(loginService);
		loginController.loginService(1234L );
	}
}
