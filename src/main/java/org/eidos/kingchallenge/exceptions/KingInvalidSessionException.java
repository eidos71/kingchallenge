package org.eidos.kingchallenge.exceptions;

public class KingInvalidSessionException  extends AbstractKingChallengeException  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 608073252748998885L;
	
    public KingInvalidSessionException()                      { super(); }
    public KingInvalidSessionException(String message)        { super(message); }
    public KingInvalidSessionException(Exception cause)       { super(cause); }
    public KingInvalidSessionException(String m, Exception c) { super(m, c); }
}

