package test.eidos.kingchanllenge.ptp;

import static org.easymock.EasyMock.*;

import org.easymock.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.eidos.kingchallenge.controller.MockLoginController;
import org.eidos.kingchallenge.services.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(EasyMockRunner.class)
public class TestInit extends EasyMockSupport {
	@Mock
	private LoginService loginService;
	@TestSubject
	private MockLoginController loginController = new MockLoginController.Builder()
			.service(loginService).build();

	/**
	 * -Test an Invalid character
	 */
	@Test(expected = RuntimeException.class)
	public void testInvalidNegativeLogin() {
		//
		expect(loginService.loginToken(-1234)).andThrow(new RuntimeException());
		replay(loginService);
		loginController.loginService(-1234);
	}
}
