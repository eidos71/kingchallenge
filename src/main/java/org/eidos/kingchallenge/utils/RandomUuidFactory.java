package org.eidos.kingchallenge.utils;

import java.util.Random;

public class RandomUuidFactory {
	private static final RandomUuidFactory _SINGLETON = new RandomUuidFactory();

	public static enum Mode {
		ALPHA, ALPHANUMERIC, NUMERIC
	}

	/**
	 * 
	 * The default constructor is explicit so we can make it private and require
	 * use of getInstance() for instantiation.
	 * 
	 * @see #getInstance()
	 *
	 */
	private RandomUuidFactory() {

	}
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String createSessionID() throws Exception {
		return generateRandomString(35, Mode.ALPHA);
	}
	/**
	 *  Creates 
	 * @return
	 */
	public Long createFakeUid() {
		Random random = new Random();
		long result = (long)random.nextInt()+(long)(1L<<31);
		return (Validator.isValidUnsignedInt(result) )?result:0 ;
	}
	/**
	 * Help method to generate randome strings
	 * @param length max lenght of the string
	 * @param mode @Mode defining only Alpha, Alphanumeric or NumericW
	 * @return random String of requested Lenght
	 * @throws Exception
	 */
	public String generateRandomString(int length, Mode mode) throws Exception {

		StringBuffer buffer = new StringBuffer();
		String characters = "";

		switch (mode) {

		case ALPHA:
			characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
			break;

		case ALPHANUMERIC:
			characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
			break;

		case NUMERIC:
			characters = "1234567890";
			break;
		}

		int charactersLength = characters.length();

		for (int i = 0; i < length; i++) {
			double index = Math.random() * charactersLength;
			buffer.append(characters.charAt((int) index));
		}
		return buffer.toString();
	}

	/**
	 * 
	 * @return The singleton instance of this class.
	 *
	 */
	public static RandomUuidFactory getInstance() {
		return _SINGLETON;
	}
}