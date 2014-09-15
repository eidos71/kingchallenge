package test.eidos.kingchanllenge;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Test;


public class SchedulerTimeWatch {

	@Test
	public void testFuture() throws InterruptedException, ExecutionException {
		ScheduledExecutorService scheduledExecutorService =
		        Executors.newScheduledThreadPool(1);

		ScheduledFuture scheduledFuture =
		    scheduledExecutorService.schedule(new Callable() {
		        public Object call() throws Exception {
		            System.out.println("Executed!");
		            return "Called!";
		        }
		    },
		    5,
		    TimeUnit.SECONDS);

	System.out.println("result = " + scheduledFuture.get());

	scheduledExecutorService.shutdown();

	}
}
