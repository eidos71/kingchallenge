package test.eidos.kingchanllenge.persistance;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.easymock.EasyMockRunner;
import org.eidos.kingchallenge.model.KingUser;
import org.eidos.kingchallenge.persistance.LoginPersistanceMap;
import org.eidos.kingchallenge.persistance.SimpleLoginPersistanceMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RunWith(EasyMockRunner.class)
public class TestUserPersistance {
	static final Logger LOG = LoggerFactory
			.getLogger(TestUserPersistance.class);
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private static final LoginPersistanceMap<AtomicInteger, String, KingUser> bag = new SimpleLoginPersistanceMap();
	private final static int BAG_SIZE = 10;

	@Before
	public void setup() {
		KingUser user = null;
		for (int i = 0; i < BAG_SIZE; i++) {
			user = new KingUser.Builder(i).build();
			LOG.debug("insert->", bag);
			bag.put(user.getKingUserId(), user.getSessionKey(), user);
		}
	}
	@After
	public void after() throws InterruptedException {
		Thread.sleep(50000);
	}
	@Test
	public void testDoAsync() {
		Accessor a1 = new Accessor(bag);
		Accessor a2 = new Accessor(bag);
		Mutator m = new Mutator(bag);

		executor.execute(a1);
		executor.execute(m);
		executor.execute(a2);
	}

	/**
	 * Private inner class for Accessors
	 * 
	 * @author eidos71
	 *
	 */
	private final class Accessor implements Runnable {
		private final LoginPersistanceMap<AtomicInteger, String, KingUser> theBag;

		public Accessor(
				LoginPersistanceMap<AtomicInteger, String, KingUser> abag) {
			this.theBag = abag;
		}

		@Override
		public void run() {
			for (Entry<AtomicInteger, KingUser> entry : this.theBag
					.getMapByLogin().entrySet()) {
				LOG.debug("Key- {}, Value {}", entry.getKey(), entry.getValue());
			}
		}
	}

	private final class Mutator implements Runnable {

		private final LoginPersistanceMap<AtomicInteger, String, KingUser> theBag;
		private final Random random = new Random();

		public Mutator(LoginPersistanceMap<AtomicInteger, String, KingUser> abag) {
			this.theBag = abag;
		}

		@Override
		public void run() {
			KingUser user = null;
			for (int i = 0; i < BAG_SIZE; i++) {
				int randomInt = random.nextInt(BAG_SIZE);
				LOG.debug("element GOING to be deleted-> {}", randomInt);
				this.theBag.removeByLogin(new AtomicInteger(randomInt));
				user = new KingUser.Builder(randomInt).build();
				bag.put(user.getKingUserId(), user.getSessionKey(), user);
				LOG.debug("element modified-> {}", randomInt);
			}
		}
	}

}
