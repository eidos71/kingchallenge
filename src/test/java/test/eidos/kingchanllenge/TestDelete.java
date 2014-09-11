package test.eidos.kingchanllenge;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import static java.util.concurrent.TimeUnit.*;
import org.junit.Test;

public class TestDelete {
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
}
