package test.eidos.kingchanllenge.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
	KingdomConfManager kingdomManager;
	LoginService loginService;
	private static final Logger LOG = LoggerFactory
			.getLogger(TestLoginPersistance.class);
	private static final int BAG_SIZE = 5000;
	private static final int MUTATOR=100;
	private final ExecutorService executor = Executors.newCachedThreadPool();
	@Before
	public void init() {
		kingdomManager = KingdomConfManager.getInstance();
		LoginPersistanceMap<Long, String, KingUser> bag = KingdomConfManager
				.getInstance().getPersistanceBag().getLoginPersistance();
		loginService = new SimpleLoginService();
		KingUser user = null;
		for (int i = 1; i < BAG_SIZE; i++) {
			user = new KingUser.Builder(i).build();
			LOG.trace("inserting-> {}", bag);
			bag.put(user.getKingUserId().get(), user.getSessionKey(), user);
		}
	}

	@Test
	public void sessionCheck() throws InterruptedException {
		Thread.sleep(5000);
		if (loginService == null)
			LOG.debug("login service is null");
		loginService.sessionCheck();
	}

	@Test
	public void testSessionExecutor() throws InterruptedException,
			ExecutionException {
		Set<Mutator> mutatorList=new LinkedHashSet<Mutator>();
		 List<Future<?> > listFuture= new ArrayList<Future<?> >();
		for (int i=0;i < MUTATOR; i++) 
			mutatorList.add(new Mutator());
	
		ScheduledExecutorService scheduledExecutorService = Executors
				.newScheduledThreadPool(1);

		scheduledExecutorService.scheduleAtFixedRate(
				new SessionWorkerManager(), 0, 5, TimeUnit.SECONDS);
		Thread.sleep(2000);
		for ( Mutator mutator :mutatorList) {
			//listFuture.add(	executor.submit(mutator));
	
			executor.execute(mutator);
		}

/*		for (Future<?> future: listFuture) {
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				LOG.error("exception {}",e);
			}
		}*/
		
		// assertEquals(true, scheduledFuture.get() );

	}
	@After
	public void after() throws InterruptedException {
		Thread.sleep(15000);
	}
	/**
	 * 
	 * @author eidos71
	 *
	 */
	private final class Mutator implements Runnable {
		private final Random random = new Random();

		@Override
		public void run() {
			try {
				Thread.sleep(500+(random.nextInt(BAG_SIZE) ) );
				long randomInt = random.nextInt(BAG_SIZE);
				KingUser kingUser= new KingUser.Builder(randomInt)
				.build();
				
				 KingUser result = loginService.sessionCheckByLoginId(kingUser.getKingUserId());
					LOG.debug("result {}" , result.getSessionKey() );
				 
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		}

/*		@Override
		public Object call() throws Exception {
			long randomInt = random.nextInt(BAG_SIZE);
			return loginService.loginToken(new KingUser.Builder(randomInt)
					.build());
		}*/

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
