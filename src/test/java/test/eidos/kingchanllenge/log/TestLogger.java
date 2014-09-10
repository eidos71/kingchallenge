package test.eidos.kingchanllenge.log;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 
 * @author eidos71
 *
 */
public final class TestLogger {
	static final Logger LOG = LoggerFactory.getLogger(TestLogger.class);
	@Test
	public void testElement() {
		LOG.trace("trace!");
		LOG.debug("debug");
		LOG.info("info.");
		LOG.warn("warn.");
		LOG.error("error");
		assertEquals(true, true);
	}
}
