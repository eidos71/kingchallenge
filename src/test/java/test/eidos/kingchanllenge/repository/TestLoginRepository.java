package test.eidos.kingchanllenge.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.concurrent.atomic.AtomicLong;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.eidos.kingchallenge.domain.model.KingUser;
import org.eidos.kingchallenge.exceptions.KingInvalidSessionException;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.persistance.KingdomConfManager;
import org.eidos.kingchallenge.persistance.LoginPersistanceMap;
import org.eidos.kingchallenge.repository.LoginRepository;
import org.eidos.kingchallenge.repository.SimpleLoginRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(EasyMockRunner.class)
public class TestLoginRepository extends EasyMock {
	private static final Logger LOG = LoggerFactory
			.getLogger(TestLoginRepository.class);
	private LoginRepository loginRepository = new SimpleLoginRepository();
	private  static boolean isInit=false;
	private static final int BAG_SIZE = 10000;


	@Before
	public void init() {
		 KingdomConfManager.getInstance();
		LoginPersistanceMap<Long, String, KingUser> bag = KingdomConfManager
				.getInstance().getPersistanceBag().getLoginPersistance();
		bag.clean();
		KingUser user = null;
		

			for (int i = 1; i < BAG_SIZE + 1; i++) {
				user = new KingUser.Builder(i).build();
				bag.put(user.getKingUserId().get(), user.getSessionKey(), user);
			}

	
	
	}

	@Test
	public void testNewInsertElement() {
		KingUser kingUser = new KingUser.Builder(BAG_SIZE + 5).build();
		loginRepository.addKingUser(kingUser);
		assertThat("Total King users ", loginRepository.getAllKingdomByLogin()
				.size(), equalTo(BAG_SIZE +1));
	}

	@Test
	public void testDelteByLogin() {

		KingUser kingUser = new KingUser.Builder(3).build();
		loginRepository.addKingUser(kingUser);
	
		assertThat(" ", loginRepository.removeKingUserByLogin(kingUser.getKingUserId()), equalTo(true));
	}

	@Test
	public void updateExistingElement() throws InterruptedException {
		KingUser kingUser = new KingUser.Builder(2).build();

		KingUser oldKingUser = loginRepository.findByLoginId(new AtomicLong(2));
		LOG.debug("oldKingUser {} ", oldKingUser);
		loginRepository.updateKingUser(kingUser);
		KingUser newKingUser = loginRepository.findByLoginId(new AtomicLong(2));
		LOG.debug("newKingUser {} ", newKingUser);
	}

	@Test
	public void deleteExistingElement() {

		KingUser kingUser = loginRepository.findByLoginId(new AtomicLong(2));
		loginRepository.removeKingUserBySession(kingUser.getSessionKey());
		assertThat("Total King users ", loginRepository.getAllKingdomBySession()
				.size(), equalTo(BAG_SIZE-1));

		//loginRepository.addKingUser(kingUser);
	}

	@Test(expected = LogicKingChallengeException.class)
	public void deleteMissingElement() {
		// A new king user, guarantees a different session ID.
		loginRepository.removeKingUserBySession("FAKESESSIONID");
	}

	@Test(expected =  KingInvalidSessionException.class)
	public void deleteEmptySessionKeyElement() {
		// A new king user, guarantees a different session ID.
		loginRepository.removeKingUserBySession("");
	}
	@Test(expected =  KingInvalidSessionException.class)
	public void deleteNullSessionKeyElement() {
		// A new king user, guarantees a different session ID.
		loginRepository.removeKingUserBySession(null);
	}
	@Test(expected = LogicKingChallengeException.class)
	public void findByNullLogind() {
		loginRepository.findByLoginId(null);
	}
	@Test(expected = LogicKingChallengeException.class)
	public void findBy0Logind() {
		loginRepository.findByLoginId(new AtomicLong(0) );
	}
	@Test(expected = KingInvalidSessionException.class)
	public void findByEmptyString() {
		loginRepository.findBySessionId("");
	}
	@Test(expected = KingInvalidSessionException.class)
	public void findByNullString() {
		loginRepository.findBySessionId(null);
	}
	 @Test 
	 public void findByLoginId() {
			KingUser kingUser = loginRepository.findByLoginId(new AtomicLong(2));
			assertThat("", kingUser.getKingUserId().get()
					, equalTo(2L));
	 }
	 @Test 
	 public void findBySessionId() {
			KingUser kingUser = loginRepository.findByLoginId(new AtomicLong(2));
			 kingUser = loginRepository.findBySessionId(kingUser.getSessionKey() );
			assertThat("", kingUser.getKingUserId().get()
					, equalTo(2L));
	 }
}
