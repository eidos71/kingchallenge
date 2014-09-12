package org.eidos.kingchallenge.utils;

public class Validator {
	/**
	 * Validates if it is
	 * @param long to evaluate.
	 * @return if it is false or not.
	 */
	public static boolean isValidUnsignedInt(long val) {
		//We exclude 0 as a posible valid value.
		   if ( (val <1)  || (val > 4294967295L))
		       return false;
		   else
		       return true; }
}
