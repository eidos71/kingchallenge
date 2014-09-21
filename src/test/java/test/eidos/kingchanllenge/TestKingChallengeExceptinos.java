package test.eidos.kingchanllenge;

import java.io.IOException;

import org.eidos.kingchallenge.exceptions.KingInvalidSessionException;
import org.eidos.kingchallenge.exceptions.KingRunTimeException;
import org.eidos.kingchallenge.exceptions.KingRunTimeIOException;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.junit.Test;

public class TestKingChallengeExceptinos {
	
	@Test(expected=LogicKingChallengeException.class)
	public void test_INVALID_LEVEL() {
		throw new LogicKingChallengeException(LogicKingError.INVALID_LEVEL);
	}
	@Test(expected=LogicKingChallengeException.class)
	public void tesINVALID_SCOREt() {
		throw new LogicKingChallengeException(LogicKingError.INVALID_SCORE);
	}
	@Test(expected=LogicKingChallengeException.class)
	public void testINVALID_SESSION() {
		throw new LogicKingChallengeException(LogicKingError.INVALID_SESSION);
	}	
	@Test(expected=LogicKingChallengeException.class)
	public void INVALID_TOKEN() {
		throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
	}	
	@Test(expected=LogicKingChallengeException.class)
	public void INVALID_TOKEN_SIZE() {
		throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN_SIZE);
	}		
	@Test(expected=LogicKingChallengeException.class)
	public void INVALIDCONTROLLERHANDLER() {
		throw new LogicKingChallengeException(LogicKingError.INVALIDCONTROLLERHANDLER);
	}		
	@Test(expected=LogicKingChallengeException.class)
	public void INVALIDHANDLER() {
		throw new LogicKingChallengeException(LogicKingError.INVALIDHANDLER);
	}			
	@Test(expected=LogicKingChallengeException.class)
	public void INVALIDTOKENCONTROLLER() {
		throw new LogicKingChallengeException(LogicKingError.INVALIDTOKENCONTROLLER);
	}			
	@Test(expected=LogicKingChallengeException.class)
	public void NONE() {
		throw new LogicKingChallengeException(LogicKingError.NONE);
	}			 
	@Test(expected=LogicKingChallengeException.class)
	public void PROCESSING_ERROR() {
		throw	 new LogicKingChallengeException(LogicKingError.PROCESSING_ERROR);
	}			
	@Test(expected=LogicKingChallengeException.class)
	public void UNKNOWN() {
		throw	 new LogicKingChallengeException(LogicKingError.UNKNOWN);
	}				
	@Test(expected=LogicKingChallengeException.class)
	public void USER_EXISTS() {
		throw	 new LogicKingChallengeException(LogicKingError.USER_EXISTS);
	}			
	@Test(expected=LogicKingChallengeException.class)
	public void USER_NOT_FOUND() {
		throw new LogicKingChallengeException(LogicKingError.USER_NOT_FOUND);
	}
	@Test(expected=KingRunTimeIOException.class)
	public void TestKingRunTimeIOException() {
		throw new KingRunTimeIOException();
	}
	@Test(expected=KingRunTimeIOException.class)
	public void TestKingRunTimeIOExceptionMessage() {
		throw new KingRunTimeIOException("mock");
	}
	@Test(expected=KingRunTimeIOException.class)
	public void KingRunTimeIOException() {
		try {
			throw new IOException("mock");
		}catch (IOException e) {
			throw new KingRunTimeIOException(e);
		}
		
	}	
	@Test(expected=KingRunTimeIOException.class)
	public void KingRunTimeIOExceptionMC() {
		try {
			throw new IOException("mock");
		}catch (IOException e) {
			throw new KingRunTimeIOException("message",e);
		}
	}
	
	
	
	@Test(expected=KingRunTimeException.class)
	public void KingRunTimeException() {
		throw new KingRunTimeException();
	}
	@Test(expected=KingRunTimeException.class)
	public void KingRunTimeExceptionMessage() {
		throw new KingRunTimeException("mock");
	}
	@Test(expected=KingRunTimeException.class)
	public void KingRunTimeExceptionM() {
		try {
			throw new IOException("mock");
		}catch (IOException e) {
			throw new KingRunTimeException(e);
		}
		
	}	
	@Test(expected=KingInvalidSessionException.class)
	public void KingRunTimeExceptionCM() {
		try {
			throw new IOException("mock");
		}catch (IOException e) {
			throw new KingInvalidSessionException("message",e);
		}
	}
	@Test(expected=KingInvalidSessionException.class)
	public void KingInvalidSessionExceptionCM() {
		try {
			throw new IOException("mock");
		}catch (IOException e) {
			throw new KingInvalidSessionException("message",e);
		}
	}

}
