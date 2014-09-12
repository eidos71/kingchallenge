package org.eidos.kingchallenge.exceptions;

import java.io.IOException;

/**
 *
 * Signals that an  exception of some sort has occurred. This
 * class is the general class of exceptions produced by failed or
 * interrupted operations.
 *
 * This is a RuntimeException, catch in all Exception

 * @author eidos71
 *
 */
public class KingRunTimeException extends AbstractKingChallengeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 608073252748998885L;
	
    public KingRunTimeException()                      { super(); }
    public KingRunTimeException(String message)        { super(message); }
    public KingRunTimeException(Exception cause)       { super(cause); }
    public KingRunTimeException(String m, Exception c) { super(m, c); }
}