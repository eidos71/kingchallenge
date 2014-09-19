package test.eidos.kingchanllenge.utils;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.eidos.kingchallenge.utils.Validator;
import org.eidos.kingchallenge.utils.UtilsEnum.Mode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RunWith(EasyMockRunner.class)
public class TestSessionValidators extends EasyMock{
	static final Logger LOG = LoggerFactory
			.getLogger(TestSessionValidators.class);
	@Test
	public  void TestAlphaValidNullString() {
		Assert.assertFalse("Expected to fail an Apha String ", Validator.isValidString(null, Mode.ALPHA) );

	}
	@Test
	public  void TestAlphaValidEmptyString() {
		Assert.assertFalse("Expected to fail  ", Validator.isValidString("", Mode.ALPHA) );
	}
	@Test
	public  void TestAlphaNullMode() {
		Assert.assertTrue("Expected to fail  ", Validator.isValidString("king", null) );
	}
	@Test
	public void validStringAlpha() {
		Assert.assertTrue(" ", Validator.isValidString("king", Mode.ALPHA) );
	}
	@Test
	public void validStringAlphaOneArgument() {
		Assert.assertTrue(" ", Validator.isValidString("king") );
	}
	@Test (expected=AssertionError.class)
	public void invalidAphaStringAlpha() {
		Assert.assertTrue(" ", Validator.isValidString("k3ng", Mode.ALPHA) );
	}
	@Test (expected=AssertionError.class)
	public void invalidNumberStringAlpha() {
		Assert.assertTrue(" ", Validator.isValidString("3333", Mode.ALPHA) );
	}
	@Test
	public void validStringAlphaNum() {
		Assert.assertTrue(" ", Validator.isValidString("k3ng",Mode.ALPHANUMERIC) );
	}
	@Test 
	public void validAphaStringAlphaNum() {
		Assert.assertTrue("", Validator.isValidString("k3ng", Mode.ALPHANUMERIC) );
	}
	@Test 
	public void validNumberStringAlphaNum() {
		Assert.assertTrue(" ", Validator.isValidString("3333", Mode.ALPHANUMERIC) );
	}
	@Test  (expected=AssertionError.class)
	public void invalidStringNum() {
		Assert.assertTrue("", Validator.isValidString("king",Mode.NUMERIC) );
	}
	@Test  (expected=AssertionError.class)
	public void invalidAphaNumNum() {
		Assert.assertTrue(" ", Validator.isValidString("k3ng", Mode.NUMERIC) );
	}
	@Test 
	public void validNumberStringNum() {
		Assert.assertTrue("", Validator.isValidString("3333", Mode.NUMERIC) );
	}

}
