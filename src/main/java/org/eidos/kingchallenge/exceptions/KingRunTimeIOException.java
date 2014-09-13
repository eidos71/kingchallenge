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
public class KingRunTimeIOException extends AbstractKingChallengeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 608073252748998885L;
	
    public KingRunTimeIOException()                      { super(); }
    public KingRunTimeIOException(String message)        { super(message); }
    public KingRunTimeIOException(IOException cause)       { super(cause); }
    public KingRunTimeIOException(String m, IOException c) { super(m, c); }
}