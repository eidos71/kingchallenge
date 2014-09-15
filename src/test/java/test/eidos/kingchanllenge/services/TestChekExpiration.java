package test.eidos.kingchanllenge.services;

import static org.junit.Assert.*;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.eidos.kingchallenge.model.KingUser;
import org.eidos.kingchallenge.persistance.KingdomConfManager;
import org.eidos.kingchallenge.persistance.LoginPersistanceMap;
import org.eidos.kingchallenge.service.LoginService;
import org.eidos.kingchallenge.service.SimpleLoginService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.eidos.kingchanllenge.persistance.TestLoginPersistance;
@RunWith(EasyMockRunner.class)
public class TestChekExpiration   extends EasyMockSupport  {
	private   KingdomConfManager kingdomManager;
	private LoginService loginService;
	private 
	static final Logger LOG = LoggerFactory
			.getLogger(TestLoginPersistance.class);
	private static final int BAG_SIZE = 5;
	@Before
	public void init() {
		kingdomManager=KingdomConfManager.getInstance();
		LoginPersistanceMap<Long, String, KingUser> bag = KingdomConfManager.getInstance().getPersistanceBag().getLoginPersistance();
		   loginService = new SimpleLoginService();
			KingUser user = null;
			for (int i = 1; i < BAG_SIZE; i++) {
				user = new KingUser.Builder(i).build();
				LOG.trace("inserting-> {}", bag);
				bag.put(user.getKingUserId().get(), user.getSessionKey(), user);
			}
	}
	@Test
	public void test() {
		if (loginService==null) LOG.debug("login service is null");
		loginService.sessionCheck();
	}

}
