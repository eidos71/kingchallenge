package org.eidos.kingchallenge.exceptions;

import org.eidos.kingchallenge.exceptions.enums.LogicKingError;



public class LogicKingChallengeException extends AbstractKingChallengeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2432695584526646728L;
    private final LogicKingError logicKingError;
    private final Throwable cause;
    public LogicKingChallengeException(LogicKingError error) {
        this.logicKingError = error;
        this.cause=null;
    }
    public LogicKingChallengeException(LogicKingError error, Throwable t) {
        this.logicKingError = error;
        this.cause=t;
    }
    @Override
    public String getMessage() {
        return "Exception with error code " + logicKingError.code();
    }

    public LogicKingError getLogicError() {
        return logicKingError;
    }
    
}
