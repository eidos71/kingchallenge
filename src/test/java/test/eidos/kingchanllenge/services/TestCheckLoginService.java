package test.eidos.kingchanllenge.services;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Date;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.eidos.kingchallenge.domain.model.KingUser;
import org.eidos.kingchallenge.exceptions.KingInvalidSessionException;
import org.eidos.kingchallenge.persistance.KingdomConfManager;
import org.eidos.kingchallenge.persistance.LoginPersistanceMap;
import org.eidos.kingchallenge.service.LoginService;
import org.eidos.kingchallenge.service.SimpleLoginService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ernestpetit
 *
 */
@RunWith(EasyMockRunner.class)
public class TestCheckLoginService extends EasyMock {
	private static final String EXPIREDUSER = "EXPIREDUSER";
	private static final String OKUSER = "OKUSER";
	private static final Logger LOG = LoggerFactory
			.getLogger(TestCheckLoginService.class);
	long fiveMinutesLong = MILLISECONDS.convert(
			5, MINUTES);
	long tenMinutesLong = MILLISECONDS.convert(
			10, MINUTES);
	Date now= new Date();
	Date fiveMinutesDate=new Date();

	private Date tenMinutesDate = new Date();
	LoginService  	loginService = new SimpleLoginService();
	@Before
	public void init(){
		 KingdomConfManager.getInstance();
			LoginPersistanceMap<Long, String, KingUser> bag = KingdomConfManager
					.getInstance().getPersistanceBag().getLoginPersistance();
			//20 minuts ago
			fiveMinutesDate.setTime(now.getTime()-fiveMinutesLong);
			tenMinutesDate.setTime(now.getTime()-tenMinutesLong);
			KingUser goodUser= new KingUser.Builder(1).setSessionKey(OKUSER).setTime(fiveMinutesDate).build();
			KingUser invalidUser= new KingUser.Builder(1).setSessionKey(EXPIREDUSER).setTime(tenMinutesDate).build();
			bag.put(goodUser.getKingUserId().get(), goodUser.getSessionKey(), goodUser);
			bag.put(invalidUser.getKingUserId().get(), invalidUser.getSessionKey(), invalidUser);
	}
	@Test(expected=KingInvalidSessionException.class)
	public void testRenewLastLoginFailsNoSession(){
		String sessionKeyConst= "FAKE";
		loginService.renewLastLogin(sessionKeyConst);
	}
	@Test
	public void testRenewLastLogin(){
		 KingUser sessionUser = loginService.renewLastLogin(OKUSER);
		 assertThat("", sessionUser.getSessionKey(), equalTo(OKUSER));
		 LOG.debug("{}", sessionUser);
//		//We find if the value exists.
		 assertThat("", loginService.sessionCheckBySessionKey(sessionUser.getSessionKey() ).getDateLogin() ,
				 Matchers.greaterThan (fiveMinutesDate));
		 assertThat("", loginService.sessionCheckBySessionKey(sessionUser.getSessionKey() ).getDateLogin() ,
				 Matchers.greaterThan (fiveMinutesDate ));
	}
	@Test(expected=KingInvalidSessionException.class)
	public void testRenewBadUser(){
		loginService.renewLastLogin(EXPIREDUSER);
	}
}
