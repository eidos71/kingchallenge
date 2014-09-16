package test.eidos.kingchanllenge.services;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Date;

import org.easymock.EasyMockSupport;
import org.eidos.kingchallenge.exceptions.KingInvalidSessionException;
import org.eidos.kingchallenge.model.KingUser;
import org.eidos.kingchallenge.persistance.KingdomConfManager;
import org.eidos.kingchallenge.persistance.LoginPersistanceMap;
import org.eidos.kingchallenge.service.LoginService;
import org.eidos.kingchallenge.service.SimpleLoginService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.eidos.kingchanllenge.persistance.TestLoginPersistance;

/**
 * 
 * @author ernestpetit
 *
 */
public class TestCheckLoginService extends EasyMockSupport {
	private static final String EXPIREDUSER = "EXPIREDUSER";
	private static final String OKUSER = "OKUSER";
	private static final Logger LOG = LoggerFactory
			.getLogger(TestCheckLoginService.class);
	long tenminutsLong = MILLISECONDS.convert(
			10, MINUTES);
	long twentyminutsLong = MILLISECONDS.convert(
			20, MINUTES);
	Date now= new Date();
	Date tenMinutsAgo=new Date();
	Date twntyMinutesAgo= new Date();
	private Date twentyMinutesAgo = new Date();
	LoginService  	loginService = new SimpleLoginService();
	@Before
	public void init(){
		 KingdomConfManager.getInstance();
			LoginPersistanceMap<Long, String, KingUser> bag = KingdomConfManager
					.getInstance().getPersistanceBag().getLoginPersistance();
			//20 minuts ago
			tenMinutsAgo.setTime(now.getTime()-tenminutsLong);
			twentyMinutesAgo.setTime(now.getTime()-twentyminutsLong);
			KingUser goodUser= new KingUser.Builder(1).setSessionKey(OKUSER).setTime(tenMinutsAgo).build();
			KingUser invalidUser= new KingUser.Builder(1).setSessionKey(EXPIREDUSER).setTime(twentyMinutesAgo).build();
			bag.put(goodUser.getKingUserId().get(), goodUser.getSessionKey(), goodUser);
			bag.put(invalidUser.getKingUserId().get(), invalidUser.getSessionKey(), invalidUser);
	}
	@Test(expected=KingInvalidSessionException.class)
	public void testRenewLastLoginFailsNoSession(){
		String sessionKeyConst= "FAKE";
		String sessionKey=loginService.renewLastLogin(sessionKeyConst);
	}
	@Test
	public void testRenewLastLogin(){
		String sessionKey=loginService.renewLastLogin(OKUSER);
		assertThat("", sessionKey, equalTo(OKUSER));
		LOG.debug("{}",loginService.sessionCheckBySessionKey(sessionKey).getDateLogin());
		//We find if the value exists.
		 assertThat("", loginService.sessionCheckBySessionKey(sessionKey).getDateLogin() ,
				 Matchers.greaterThan (tenMinutsAgo));
/*		 assertThat("",twntyMinutesAgo ,
				 Matchers.greaterThan ( loginService.sessionCheckBySessionKey(sessionKey).getDateLogin()));*/
	}
	@Test(expected=KingInvalidSessionException.class)
	public void testRenewBadUser(){
		String sessionKey=loginService.renewLastLogin(EXPIREDUSER);
	
	}
}
