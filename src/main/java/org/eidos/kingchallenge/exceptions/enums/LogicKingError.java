package org.eidos.kingchallenge.exceptions.enums;
/**
 * List of possible logical errors incoming from the server
 * to be provided to the client.
 * @author eidos71
 *
 */
public enum LogicKingError {

	PROCESSING_ERROR(1), INVALID_TOKEN_SIZE(2), INVALID_TOKEN(3), INVALID_SESSION(
			4), NONE(255), UNKNOWN(256);

	private final byte code;

	LogicKingError(int code) {
		this.code = (byte) code;
	}

	public int code() {
		return code;
	}

	/**
	 * Returns the appropriate {@code LogicKingError} enum
	 * 
	 * @param code
	 *            status code
	 * @return the appropriate DeliveryError
	 */
	public static LogicKingError byCode(int code) {
		for (LogicKingError err : LogicKingError.values()) {
			if (err.code == code)
				return err;
		}

		return UNKNOWN;
	}
}
