package test.eidos.kingchanllenge.persistance;

import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.eidos.kingchallenge.domain.model.KingUser;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.persistance.LoginPersistanceMap;
import org.eidos.kingchallenge.persistance.SimpleLoginPersistanceMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(EasyMockRunner.class)
/**
 * This Testclass  is mean to replicate the component of sessionmanagement timeout
 * that will  periodically remove all users invalitaed., with the component that tests if a userr has
 * a sessiontimeout.
 * 
 * @author ernestpetit
 *
 */
public class TestUserPersistance extends EasyMockSupport {
	static final Logger LOG = LoggerFactory
			.getLogger(TestUserPersistance.class);
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private static final LoginPersistanceMap<Long, String, KingUser> bag = new SimpleLoginPersistanceMap();
	private final static int BAG_SIZE = 40000; // 10

	@Before
	public void setup() {
		KingUser user = null;
		for (int i = 1; i < BAG_SIZE; i++) {
			user = new KingUser.Builder(i).build();
			LOG.trace("inserting-> {}", bag);
			bag.put(user.getKingUserId().get(), user.getSessionKey(), user);
		}
	}

	@After
	public void after() throws InterruptedException {
		Thread.sleep(30000);
	}
	 @Ignore("This test is not meant to be run on a suite of test, only run it to test a specific component on a very stresfful conditions") 
	@Test
	public void testAsyncExecStable() {
		// Threads reading from the persistance bag
		Accessor a1 = new Accessor(bag);
		Accessor a2 = new Accessor(bag);
		Accessor a3 = new Accessor(bag);
		// Threads modifying the persistance bag
		Mutator m1 = new Mutator(bag);
		Mutator m2 = new Mutator(bag);

		executor.execute(a1);
		executor.execute(m1);
		executor.execute(a2);
		executor.execute(m2);
		executor.execute(a3);
	}

	/**
	 * Private inner class for Accessors
	 * 
	 * @author eidos71
	 *
	 */
	private final class Accessor implements Runnable {
		private final LoginPersistanceMap<Long, String, KingUser> theBag;

		public Accessor(LoginPersistanceMap<Long, String, KingUser> abag) {
			this.theBag = abag;
		}

		@Override
		public void run() {
			for (Entry<Long, KingUser> entry : this.theBag.getMapByLogin()
					.entrySet()) {
				LOG.debug("Key- {}, Value {}", entry.getKey(), entry.getValue());
			}
		}
	}

	private final class Mutator implements Runnable {

		private final LoginPersistanceMap<Long, String, KingUser> theBag;
		private final Random random = new Random();

		public Mutator(LoginPersistanceMap<Long, String, KingUser> abag) {
			this.theBag = abag;
		}

		@Override
		public void run() {
			KingUser user = null;
			for (int i = 1; i < BAG_SIZE; i++) {
				try {
					long randomInt = random.nextInt(BAG_SIZE);
					LOG.debug("element GOING to be deleted-> {}", randomInt);
					this.theBag.removeByLogin(randomInt);
					user = new KingUser.Builder(randomInt).build();
					bag.put(user.getKingUserId().get(), user.getSessionKey(),
							user);
					LOG.debug("element modified-> {}", randomInt);
				} catch (LogicKingChallengeException exception) {
					// if the LogicKingChallengeException is of type
					// INVALID_TOKEN
					if (LogicKingError.INVALID_TOKEN.equals(exception
							.getLogicError())) {
						LOG.info("0 is not a valid sessionID, yet for this test this error is catched");
						// Just an invalid token was sent, we can properly
						// ignore it.
					} else {
						// Its an error so we just throw it.
						throw exception;
					}
				}

			}
		}
	}

}
