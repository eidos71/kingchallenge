package org.eidos.kingchallenge.exceptions;

import org.eidos.kingchallenge.exceptions.enums.LogicKingError;



public class LogicKingChallengeException extends AbstractKingChallengeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2432695584526646728L;
    private final LogicKingError logicKingError;

    public LogicKingChallengeException(LogicKingError error) {
        this.logicKingError = error;
    }

    @Override
    public String getMessage() {
        return "Failed to deliver notification with error code " + logicKingError.code();
    }

    public LogicKingError getDeliveryError() {
        return logicKingError;
    }
    
}
