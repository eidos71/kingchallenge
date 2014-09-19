package test.eidos.kingchanllenge.utils;

import static org.junit.Assert.*;

import java.util.Random;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.eidos.kingchallenge.utils.Validator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(EasyMockRunner.class)
/**
 *  Fast test to validate randomNumbers unsigned Int
 * @author ernestpetit
 *
 */
public class TestUnsignedInt extends EasyMockSupport {
	

	private static final int _NUMGEN = 100;
	static final Logger LOG = LoggerFactory.getLogger(TestUnsignedInt.class);

	@Test
	public void testValidatesCreation() {
		int size = _NUMGEN;
		long[] num = new long[size + 1];
		Random random = new Random();
		for (int i = 1; i < size + 1; i++) {
			long result = (long) random.nextInt() + (long) (1L << 31);

			num[i] = (Validator.isValidUnsignedInt(result)) ? result : 0;
			Long uuid = new Long(num[i]);
			LOG.debug("result:: {}", uuid);

		}

	}
}
