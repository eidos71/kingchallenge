package org.eidos.kingchallenge.exceptions.enums;
/**
 * List of possible logical errors incoming from the server
 * to be provided to the client.
 * @author eidos71
 *
 */
public enum LogicKingError {

	PROCESSING_ERROR(1), 
	INVALID_TOKEN_SIZE(2), 
	INVALID_TOKEN(3), 
	INVALID_SESSION(4), 
	INVALIDHANDLER(5),
	INVALIDTOKENCONTROLLER(6),
	INVALIDCONTROLLERHANDLER(7),
	INVALID_LEVEL(7),
	INVALID_SCORE(8),
	USER_NOT_FOUND(9),
	USER_EXISTS(10),
	NONE(255), 
	UNKNOWN(256);

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
	 * @return the appropriate LogicKingError
	 */
	public static LogicKingError byCode(int code) {
		for (LogicKingError err : LogicKingError.values()) {
			if (err.code == code)
				return err;
		}

		return UNKNOWN;
	}
}
