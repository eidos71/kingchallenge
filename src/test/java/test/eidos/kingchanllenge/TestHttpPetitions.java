package test.eidos.kingchanllenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.eidos.kingchallenge.httpserver.KingdomServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestHttpPetitions  extends AbstractKingTest {
	private static final int _NUMPETITIONS = 1000;
	static final Logger LOG = Logger.getLogger(TestHttpPetitions.class.getName());
	private final ExecutorService executor = Executors.newCachedThreadPool();

	protected static final String _HIGHSCORE = "http://localhost:8000/11/highscorelist";
	protected static final String _LOGIN= "http://localhost:8000/13/login";
	protected static final String _POST= "http://localhost:8000/1/score?sessionkey=BabWcCKxRmnzPqyvZVKMfxWPuICmnBzEXIx";
	private List<Accessor>listAccesor= new ArrayList<Accessor>();
	private List<Future<?> > listFuture= new ArrayList<Future<?> >();
	 @Before
	 public void setup() {
			Accessor a;
			
			for (int i=0; i<_NUMPETITIONS; i++) {
				a= new Accessor();
				listAccesor.add(a);
			}
	 }
//	 @Ignore("This test is not meant to be run on a suite of test, only run it to test the server component") 
	 @Test
	 /**
	  * This test only checks handlers are answering
	  * @throws Exception
	  */
	public void testServer() throws Exception {
		

		 KingdomServer.main( new String[0]);
		 Thread.sleep(3000);
		for (Accessor a: listAccesor) {
			//Execute accesors
		
			listFuture.add(	executor.submit(a));
		}
		for (Future<?> future: listFuture) {
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {

			}
		}
	}
	@After
	public void after() throws InterruptedException {
		
	}
	private void doUrlConnection(String url) throws IOException {
		 URL kingdomclient = new URL(url);
	        URLConnection conn = kingdomclient.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	        		conn.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) 
	        	LOG.fine(inputLine);
	      
	        in.close();
	}
	private final class Accessor implements Runnable{


		@Override
		public void run() {
			try {
				doUrlConnection(_LOGIN);
				doUrlConnection(_HIGHSCORE);
				doUrlConnection(_POST);				
				
			}catch(Exception e) {
			
			}
			
			
		}
		
	}
}
