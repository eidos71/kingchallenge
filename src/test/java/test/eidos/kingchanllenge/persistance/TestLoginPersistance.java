package test.eidos.kingchanllenge.persistance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.model.KingUser;
import org.eidos.kingchallenge.persistance.LoginPersistanceMap;
import org.eidos.kingchallenge.persistance.SimpleLoginPersistanceMap;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RunWith(EasyMockRunner.class)
public class TestLoginPersistance  extends EasyMockSupport  {
	static final Logger LOG = LoggerFactory
			.getLogger(TestLoginPersistance.class);
	private static final LoginPersistanceMap<Long, String, KingUser> bag = new SimpleLoginPersistanceMap();
	private KingUser aKingUser=null;
	
	/**
	 * Inserts an element in bag
	 */
	@Test
	public void testInsertBag() {
		aKingUser =new KingUser.Builder(1).build();
		bag.put(aKingUser.getKingUserId().get(), aKingUser.getSessionKey(), aKingUser);
		 assertThat("Total King users 1", bag.getMapByLogin().size() , equalTo(1));
//		 assertThat("Total King users exceeds 0", bag.getMapByLogin().size() , Matchers.greaterThan (0));
	}
	
	@Test (expected=LogicKingChallengeException.class)
	public void testInsertWrongUserId() {
		aKingUser =new KingUser.Builder(0).build();
	}
}
