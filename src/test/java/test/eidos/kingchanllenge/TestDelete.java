package test.eidos.kingchanllenge;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;
import static java.util.concurrent.TimeUnit.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.model.KingdomHandlerConf;
import org.eidos.kingchallenge.persistance.KingdomConfManager;
import org.eidos.kingchallenge.utils.UtilsEnum.Mode;
import org.eidos.kingchallenge.utils.Validator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RunWith(EasyMockRunner.class)
public class TestDelete extends EasyMockSupport {
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

	@Test(expected =LogicKingChallengeException.class)
	public void testInvalidClass() {
		KingdomHandlerConf handler = new KingdomHandlerConf.Builder("wrong.eidos.kingchallenge.httpserver.handlers.HeraldHandler")
	.contextPath("/herald").build();
	}
	@Test
	public void testKingomDefaultConstructor() {
		String context="/";
		KingdomHandlerConf handler = new KingdomHandlerConf.Builder("org.eidos.kingchallenge.httpserver.handlers.HeraldHandler").build();
		LOG.debug(handler.toString() );
		 assertThat("", handler.getContext(), equalTo(context));
	}
	@Test
	public void testKindomHandler() {
		String context="/herald";
			KingdomHandlerConf handler = new KingdomHandlerConf.Builder("org.eidos.kingchallenge.httpserver.handlers.HeraldHandler")
		.contextPath(context).build();
			LOG.debug(handler.toString() );
			 assertThat("", handler.getContext(), equalTo(context));
	}
	@Test
	public void testKindomHandlerWithNullContextPath() {
		String context="/";
			KingdomHandlerConf handler = new KingdomHandlerConf.Builder("org.eidos.kingchallenge.httpserver.handlers.HeraldHandler")
		.contextPath(null).build();
			LOG.debug(handler.toString() );
			 assertThat("", handler.getContext(), equalTo(context));
	}
	@Test
	public void testInitHandler() {
		Set<KingdomHandlerConf> map = KingdomConfManager.getInstance().getHandlerConfList();
		 assertThat("", map.size(), equalTo(2));
	}
}
