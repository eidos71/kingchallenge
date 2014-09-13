package test.eidos.kingchanllenge;

import static org.junit.Assert.*;

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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestHttpPetitions {
	static final Logger LOG = LoggerFactory.getLogger(TestHttpPetitions.class);
	private final ExecutorService executor = Executors.newCachedThreadPool();
	protected static final String _HERALD = "http://localhost:8000/herald";
	private List<Accessor>listAccesor= new ArrayList<Accessor>();
	private List<Future<?> > listFuture= new ArrayList<Future<?> >();
	 @Before
	 public void setup() {
			Accessor a;
			
			for (int i=0; i<10000; i++) {
				a= new Accessor();
				listAccesor.add(a);
			}
	 }
	@Test
	public void test() throws IOException {
		
		for (Accessor a: listAccesor) {
			//Execute accesors
		
			listFuture.add(	executor.submit(a));
		}
		for (Future<?> future: listFuture) {
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	        	LOG.debug(inputLine);
	      
	        in.close();
	}
	private final class Accessor implements Runnable{


		@Override
		public void run() {
			try {
				doUrlConnection(_HERALD);
			}catch(Exception e) {
				LOG.warn("{}",e);
			}
			
			
		}
		
	}
}
