package test.eidos.kingchanllenge.services;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.Assert;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.eidos.kingchallenge.model.KingUser;
import org.eidos.kingchallenge.persistance.KingdomConfManager;
import org.eidos.kingchallenge.persistance.LoginPersistanceMap;
import org.eidos.kingchallenge.service.LoginService;
import org.eidos.kingchallenge.service.SimpleLoginService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.eidos.kingchanllenge.persistance.TestLoginPersistance;

@RunWith(EasyMockRunner.class)
public class TestChekExpiration extends EasyMockSupport {
	
	LoginService  	loginService = new SimpleLoginService();
	private static final Logger LOG = LoggerFactory
			.getLogger(TestLoginPersistance.class);
	private static final int BAG_SIZE = 1000;
	private static final int SESSIONEXPIRATION=20;
	private static final int SEED_SESSIONEXPIRATION=30;
	private static final int MUTATOR = 10;
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final Random random = new Random();
	private static boolean init=false;
	@Before
	public void init() {
		 KingdomConfManager.getInstance();
		if (!init){

			LoginPersistanceMap<Long, String, KingUser> bag = KingdomConfManager
					.getInstance().getPersistanceBag().getLoginPersistance();
		
			KingUser user = null;
			Date now= new Date();
			Date finalDate=null;
			//long duration = now.getTime() - lastDate.getTime();
			for (int i = 1; i < BAG_SIZE; i++) {
				 finalDate= new Date();
				long randomValue = MILLISECONDS.convert(
						random.nextInt(SEED_SESSIONEXPIRATION), MINUTES);
				finalDate.setTime(now.getTime()-randomValue);
				LOG.trace("now{}", finalDate);
				user = new KingUser.Builder(i).setTime(finalDate).build();
				LOG.debug("inserting-> {}", user);
				bag.put(user.getKingUserId().get(), user.getSessionKey(), user);
			}
			init=true;
		}

	}

	@Test
	public void sessionCheck() throws InterruptedException {
		
		if (loginService == null)
			LOG.debug("login service is null");
		loginService.sessionCheck();
	}

	@Test
	public void testSessionExecutor() throws InterruptedException,
			ExecutionException {
		Set<Mutator> mutatorList = new LinkedHashSet<Mutator>();
		List<Future<?>> listFuture = new ArrayList<Future<?>>();
		for (int i = 0; i < MUTATOR; i++)
			mutatorList.add(new Mutator());

		ScheduledExecutorService scheduledExecutorService = Executors
				.newScheduledThreadPool(1);

		scheduledExecutorService.scheduleAtFixedRate(
				new SessionWorkerManager(), 0, 5, TimeUnit.SECONDS);
		Thread.sleep(2000);
		for (Mutator mutator : mutatorList) {
			// listFuture.add( executor.submit(mutator));

			executor.execute(mutator);
		}

		/*
		 * for (Future<?> future: listFuture) { try { future.get(); } catch
		 * (InterruptedException | ExecutionException e) {
		 * LOG.error("exception {}",e); } }
		 */

		// assertEquals(true, scheduledFuture.get() );

	}

	@After
	public void after() throws InterruptedException {
		Thread.sleep(15000);
	}

	/**
	 * Implements a simulated user input
	 * 
	 * @author eidos71
	 *
	 */
	private final class Mutator implements Runnable {
		private final Random mutatorRandom = new Random();

		@Override
		public void run() {
			try {
				Thread.sleep(500 + (mutatorRandom.nextInt(BAG_SIZE)*5));
				long randomInt = mutatorRandom.nextInt(BAG_SIZE);
				KingUser result = loginService
						.sessionCheckByLoginId(new AtomicLong(randomInt));
				// We define a new KingUser using  keeping the SessionKey
				KingUser kingUser = new KingUser.Builder(randomInt)
						.setSessionKey(result.getSessionKey()).build();
			
				LOG.debug("result {}", result.getSessionKey());

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		/*
		 * @Override public Object call() throws Exception { long randomInt =
		 * random.nextInt(BAG_SIZE); return loginService.loginToken(new
		 * KingUser.Builder(randomInt) .build()); }
		 */

	}

	/**
	 * 
	 * @author eidos71
	 *
	 */
	private final class SessionWorkerManager implements Runnable {

		@Override
		public void run() {
			try {
				Boolean result = loginService.sessionCheck();
				LOG.warn("loginService {}", result);
			} catch (Exception e) {
				LOG.warn("{}", e);
			}

		}

	}
}
