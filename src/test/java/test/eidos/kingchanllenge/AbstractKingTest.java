package test.eidos.kingchanllenge;



import java.io.IOException;
import java.util.logging.LogManager;

import org.easymock.EasyMock;

import test.eidos.kingchanllenge.services.TestCheckLoginService;

public class AbstractKingTest extends EasyMock{ 
	private static final LogManager LOGMANAGER = LogManager.getLogManager();
	static{
		try {
			LOGMANAGER.readConfiguration(AbstractKingTest.class.getClassLoader().getResourceAsStream(	"kinglog.properties") );
		} catch (IOException exception) {
			System.out.println("exception" + exception);
		}
	}
	public  static LogManager getManager(){
		return LOGMANAGER;
	}
}
