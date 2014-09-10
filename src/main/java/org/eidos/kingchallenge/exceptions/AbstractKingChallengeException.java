package org.eidos.kingchallenge.exceptions;

/**
 * Base class for all KingchallengeExceptions
 * @author eidos71
 *
 */
public abstract class AbstractKingChallengeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8679074147296963119L;

	public AbstractKingChallengeException() {
		super();
	}

	public AbstractKingChallengeException(String message) {
		super(message);
	}

	public AbstractKingChallengeException(Throwable cause) {
		super(cause);
	}

	public AbstractKingChallengeException(String m, Throwable c) {
		super(m, c);
	}
}
