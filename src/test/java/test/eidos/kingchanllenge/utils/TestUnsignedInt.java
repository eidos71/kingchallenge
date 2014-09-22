package test.eidos.kingchanllenge.utils;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Random;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.eidos.kingchallenge.utils.Validator;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.eidos.kingchanllenge.AbstractKingTest;





@RunWith(EasyMockRunner.class)
/**
 *  Fast test to validate randomNumbers unsigned Int
 * @author ernestpetit
 *
 */
public class TestUnsignedInt extends AbstractKingTest {

	private static final int _NUMGEN = 100;


	static final Logger LOG = Logger.getLogger(TestUnsignedInt.class.getName());

	@Test
	public void testValidatesCreation() {
		int size = _NUMGEN;
		long[] num = new long[size + 1];
		Random random = new Random();
		for (int i = 1; i < size + 1; i++) {
			long result = (long) random.nextInt() + (long) (1L << 31);

			num[i] = (Validator.isValidUnsignedInt(result)) ? result : 0;
			Long uuid = new Long(num[i]);
			LOG.info(String.format("result:: %1$s}", uuid) );

		}

	}
}
