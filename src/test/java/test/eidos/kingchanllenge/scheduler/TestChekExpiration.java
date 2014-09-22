package test.eidos.kingchanllenge.scheduler;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.easymock.EasyMockRunner;
import org.eidos.kingchallenge.KingdomConfManager;
import org.eidos.kingchallenge.controller.SessionWorkerManager;
import org.eidos.kingchallenge.domain.model.KingUser;
import org.eidos.kingchallenge.persistance.LoginPersistanceMap;
import org.eidos.kingchallenge.service.LoginService;
import org.eidos.kingchallenge.service.SimpleLoginService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.eidos.kingchanllenge.AbstractKingTest;

@RunWith(EasyMockRunner.class)
public class TestChekExpiration extends AbstractKingTest {
	
	LoginService  	loginService = new SimpleLoginService();

	private static final int BAG_SIZE = 1000;
	private static final int SEED_SESSIONEXPIRATION=15;
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
			
				user = new KingUser.Builder(i).setTime(finalDate).build();
				
				bag.put(user.getKingUserId().get(), user.getSessionKey(), user);
			}
			init=true;
		}

	}

	@Test
	public void sessionCheck() throws InterruptedException {
		
		if (loginService != null  && loginService.sessionCheck()  !=null)	{
			loginService.sessionCheck();
		}
		
	}

	@Test
	public void testSessionExecutor() throws InterruptedException,
			ExecutionException {
		Set<Mutator> mutatorList = new LinkedHashSet<Mutator>();
	
		for (int i = 0; i < MUTATOR; i++)
			mutatorList.add(new Mutator());

		ScheduledExecutorService scheduledExecutorService = Executors
				.newScheduledThreadPool(1);

		scheduledExecutorService.scheduleAtFixedRate(
				new SessionWorkerManager(loginService), 0, 5, TimeUnit.SECONDS);
		//Wait two seconds before executing elements
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
				//Randomly execut threads
				Thread.sleep(500 + (mutatorRandom.nextInt(BAG_SIZE)*5));
				long randomInt = mutatorRandom.nextInt(BAG_SIZE);
				KingUser result = loginService
						.sessionCheckByLoginId(new AtomicLong(randomInt));
				new KingUser.Builder(randomInt)
						.setSessionKey(result.getSessionKey()).build();
			


			} catch (InterruptedException e) {

			}

		}

		/*
		 * @Override public Object call() throws Exception { long randomInt =
		 * random.nextInt(BAG_SIZE); return loginService.loginToken(new
		 * KingUser.Builder(randomInt) .build()); }
		 */

	}


}
