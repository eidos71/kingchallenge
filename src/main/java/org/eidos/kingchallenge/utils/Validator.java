package org.eidos.kingchallenge.utils;

import java.util.Date;

import org.eidos.kingchallenge.utils.UtilsEnum.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Validator {
	private static final Logger LOG = LoggerFactory.getLogger(Validator.class);
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
	 *  Validates if it is a positive Int
	 * @param val to evaluate
	 * @return
	 */
	public static boolean isValidPositiveInt(int val) {
		if (val <0) {
			return false;
		}else {
			return true;
		}
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
	/**
	 * Validates is a valid Date
	 * Checks it is not null and it is not on the future of now
	 * @param currentDate
	 */
	public static boolean isValidDate(Date currentDate) {
		if (currentDate==null || currentDate.after(new Date()))
			return false;
		else
			return true;
	}
	/**
	 * Validates a Date has expired
	 * @param currentDate
	 * @param SESSION_EXPIRATION 
	 * @return
	 */
	public static boolean validateSessionExpired(Date lastDate, long SESSION_EXPIRATION ) {
		Date now= new Date();
		long duration = now.getTime() - lastDate.getTime();
		if (duration >= SESSION_EXPIRATION) {
			LOG.debug("Session expiration greater for date {} and now {} ", lastDate, now );
			return true;
		}
		return false;
	}
}
