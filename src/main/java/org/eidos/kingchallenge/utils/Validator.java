package org.eidos.kingchallenge.utils;

import org.eidos.kingchallenge.utils.UtilsEnum.Mode;

public class Validator {
	private static final Mode DEFAULT_MODE = Mode.ALPHA;
	/**
	 * Validates if it is a valid Unsigned Int
	 * 
	 * @param long to evaluate.
	 * @return if it is false or not.
	 */
	public static boolean isValidUnsignedInt(long val) {
		// We exclude 0 as a posible valid value.
		if ((val < 1) || (val > 4294967295L))
			return false;
		else
			return true;
	}
	/**
	 * Validates a String , @Mode is set to default Mode Alpha
	 * @param val string to validate
	 * @return True if matches, False if not
	 */
	public static boolean isValidString (String val) {
		
		return isValidString(val,DEFAULT_MODE);
	}
	/**
	 * Validates if is a Valid String
	 * @param val string to validate
	 * @param mode @Mode to evaluate, Alpha only, Aphanumeric or Numeric
	 * @return True if matches, False if not
	 */
	public static boolean isValidString(String val, Mode mode) {
		if (val==null || "".equals(val)) return false;
		if (mode==null) mode=DEFAULT_MODE;
		String pattern="";
		switch(mode) {
		case ALPHA:
			 pattern="^[\\pL]+$";
			break;
		case ALPHANUMERIC:
			 pattern= "^[\\pL\\pN]+$";
			break;
		case NUMERIC:
			 pattern= "^[\\pN]+$";
			break;
		default:
			//Default case sets to Alpha
			 pattern="^[\\pL]+$";
			break;
		
		}
	    if(val.matches(pattern)){
            return true;
        }
        return false;   
		
	}
}
