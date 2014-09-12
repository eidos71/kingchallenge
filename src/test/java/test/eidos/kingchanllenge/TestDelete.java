package test.eidos.kingchanllenge;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.*;

import org.eidos.kingchallenge.utils.Validator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDelete {
	static final Logger LOG = LoggerFactory.getLogger(TestDelete.class);
	Map<AtomicInteger,Date> lastLogin=Collections.synchronizedMap(new HashMap<AtomicInteger,Date>() );
	@Test
	public void testImutableLastLogin() {
		lastLogin.put(new AtomicInteger(341), new Date());
		Date previous=new Date();
		Date now= new Date();
		long MAX_DURATION = MILLISECONDS.convert(20, MINUTES);
		long duration = now.getTime() - previous.getTime();
		if (duration >= MAX_DURATION) {
		   
		}
	}
	@Test
	public void testNavigableMap() {
		NavigableMap<Integer ,String> navmap=new ConcurrentSkipListMap<Integer, String>();

	    navmap.put(1, "Sunday");  

	    navmap.put(2, "Monday");

	    navmap.put(3, "Tuesday");

	    navmap.put(4, "Wednesday");

	    navmap.put(5, "Thursday");

	    navmap.put(6, "Friday");

	    navmap.put(7, "Saturday");
	    LOG.debug("Data in the navigable map:{} " , navmap.descendingMap() );

	  //Retrieving first data

	      LOG.debug("First data:{} " , navmap.firstEntry() );

	      //Retrieving last data

	      LOG.debug("Last data:{} " , navmap.lastEntry());

	      //Retrieving the nearest less than or equal to the given key

	      LOG.debug("Nearest less than or equal to the given key:  {}  ", navmap.floorEntry(5) );

	      //Retrieving the greatest key strictly less than the given key

	      LOG.debug("Retrieving the greatest key strictly less than  the given key:  {}  " , navmap.lowerEntry(3));

	      //Retrieving a key-value associated with the least key strictly greater than the given key

	      LOG.debug("Retriving data from navigable map greater than the given key: {}   " , navmap.higherEntry(5) );

	      //Removing first

	      LOG.debug("Removing First:  {}", navmap.pollFirstEntry());

	      //Removing last

	      LOG.debug("Removing Last: {}" ,navmap.pollLastEntry() );

	      //Displaying all data

	      LOG.debug("Now data: {}" ,navmap.descendingMap());

	}
	@Test
	public void testInteger() {
		int size=100;
		long[]num = new long[size + 1];
		Random random = new Random();
		for (int i = 1; i < size + 1; i++) {
		long result = (long)random.nextInt()+(long)(1L<<31);
		
		num[i] =(Validator.isValidUnsignedInt(result) )?result:0 ;
		Long uuid= new Long(num[i]);
	    LOG.debug("result:: {}" ,uuid );
	
		}
		
	}
}
