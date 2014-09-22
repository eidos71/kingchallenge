package test.eidos.kingchanllenge.log;

import static org.junit.Assert.assertEquals;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import test.eidos.kingchanllenge.AbstractKingTest;



/**
 * 
 * @author eidos71
 *
 */
public final class TestLogger extends AbstractKingTest {
	static final Logger LOG = Logger.getLogger(TestLogger.class.getName());
	@Test
	public void testElement() {
		LOG.severe("Info Log");
		LOG.warning("Info Log");
		LOG.info("Info Log");
		LOG.finest("Really not important");
		LOG.fine(String.format("message %1$s, %2$s ", "test", true) );
		LOG.fine(String.format("%1$s  ", LOG, true) );

		try{
			throw new NullPointerException("this is an exception");
		}catch (Exception err){
			LOG.log(Level.SEVERE," exception",err);
		}
		String keyValue="bum";
		try{
			new Long(keyValue);
		}catch(Exception er){
			LOG	.warning(String.format("keyvalue= %1$s, %2$s", keyValue, er) );
		}
		assertEquals(true, true);
	}
}
