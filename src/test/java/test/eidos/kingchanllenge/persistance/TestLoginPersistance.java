package test.eidos.kingchanllenge.persistance;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Map;
import java.util.Map.Entry;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.model.KingUser;
import org.eidos.kingchallenge.persistance.LoginPersistanceMap;
import org.eidos.kingchallenge.persistance.SimpleLoginPersistanceMap;
import org.hamcrest.Matchers;
import org.junit.Before;
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
	@Before
	public void setup() {
		//Make sure the map is clean at the start of each of this Test
		Map<Long, KingUser> result = bag.getMapByLogin();
		for (Entry<Long, KingUser> entry : result.entrySet() ){
			bag.removeByLogin(entry.getKey());
		}
	}
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
	public void testInsertLoginIDasZero() {
		aKingUser =new KingUser.Builder(0).build();
	}
	@Test (expected=LogicKingChallengeException.class)
	public void testInsertLoginIDNegative() {
		aKingUser =new KingUser.Builder(-132).build();
	}
	@Test  (expected=AssertionError.class)
	public void testInsertDuplicateIdStoresfistEntry() {
		aKingUser =new KingUser.Builder(1).build();
		bag.put(aKingUser.getKingUserId().get(), aKingUser.getSessionKey(), aKingUser);
		LOG.debug("{} ",aKingUser);
		KingUser secondUser = new KingUser.Builder(1).build();
		bag.put(aKingUser.getKingUserId().get(), aKingUser.getSessionKey(), secondUser);
		LOG.debug("{} ",secondUser);
		 assertThat("T", bag.getMapByLogin().get(1L).getSessionKey() , equalTo(aKingUser.getSessionKey()));

	}
	@Test
	public void testInsertDuplicateIDStoresLastEntry() {
		aKingUser =new KingUser.Builder(1).build();
		bag.put(aKingUser.getKingUserId().get(), aKingUser.getSessionKey(), aKingUser);
		LOG.debug("{} ",aKingUser);
		aKingUser =new KingUser.Builder(1).build();
		bag.put(aKingUser.getKingUserId().get(), aKingUser.getSessionKey(), aKingUser);
		LOG.debug("{} ",aKingUser);
		 assertThat("Total King users 1", bag.getMapByLogin().get(1L).getSessionKey() , equalTo(aKingUser.getSessionKey()));

	}
}
