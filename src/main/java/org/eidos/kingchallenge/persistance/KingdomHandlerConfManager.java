package org.eidos.kingchallenge.persistance;

import java.util.List;

import org.eidos.kingchallenge.model.KingdomHandlerConf;

/**
 * Manager that helds all Handler managed by the Kingdom
 * @author eidos71
 *
 */
public class KingdomHandlerConfManager {
	
	/**
	 * private singleton
	 */
	private KingdomHandlerConfManager() {
		
	}
	public static KingdomHandlerConfManager  getInstance() {
		// TODO Auto-generated method stub
		return new KingdomHandlerConfManager();
	}

	public List<KingdomHandlerConf>getHandlerConfList(){
		return null;
	}

}
