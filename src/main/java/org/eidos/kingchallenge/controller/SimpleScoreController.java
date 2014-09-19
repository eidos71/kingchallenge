package org.eidos.kingchallenge.controller;

import javax.annotation.concurrent.Immutable;

import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
import org.eidos.kingchallenge.domain.model.KingUser;
import org.eidos.kingchallenge.repository.SimpleLoginRepository;
import org.eidos.kingchallenge.service.EmptyLoginService;
import org.eidos.kingchallenge.service.EmptyScoreService;
import org.eidos.kingchallenge.service.LoginService;
import org.eidos.kingchallenge.service.ScoreService;
import org.eidos.kingchallenge.service.SimpleLoginService;
import org.eidos.kingchallenge.service.SimpleScoreService;
import org.eidos.kingchallenge.utils.CollectionsChallengeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Immutable
public final class SimpleScoreController implements ScoreController {
	private final ScoreService  scoreService;
	private final LoginService loginService;
	static final Logger LOG = LoggerFactory.getLogger(SimpleScoreController.class);

	@Override
	public String putHighScore(String sessionKey, Long level, int score) {
		//Validates session is valid and 
		String response="";
		KingUser user= loginService.renewLastLogin(sessionKey);
		scoreService.insertScore(sessionKey,  new KingScoreDTO.Builder(level,score,user.getKingUserId().get()).build());
		return response;
	}
	@Override
	public String getHighScoreByLevel(String sessionKey, Long level ) {
		String response="";
		 	//loginService.renewLastLogin(sessionKey);
		 	return  scoreService.getHighScoreList(sessionKey, level);
	//	return  CollectionsChallengeUtils.returnCsvFromCollection( );
	}
	private SimpleScoreController(Builder builder) {
	    this.scoreService = builder.scoreService;
	    this.loginService =builder.loginService;
	 }
	

	/**
	 * We block the Login Controller public class
	 */
	private  SimpleScoreController ()  {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 
	 * @author eidos71
	 *
	 */
	public static class Builder{
		private  ScoreService scoreService;
		private LoginService loginService;

		public Builder() {
		}
		/**
		 * Sets a scoreService
		 * @param scoreService
		 * @return
		 */
		public Builder setScoreService(ScoreService scoreService) {
	    	if (scoreService==null)
	    		throw  new IllegalStateException("No Score has been instanced"); 
	    	this.scoreService=scoreService;
	    	return this;
		}
		/**
		 * Creates a new instace of SimpleScorecontroller
		 * @return a new SimpleScoreController
		 */
	    public SimpleScoreController build() {
	    	if (this.scoreService==null){
	    		this.scoreService=new SimpleScoreService();
	    	
	    	}
	    	if (this.loginService == null) {
	    		this.loginService=new SimpleLoginService();
	    	
	    	}
	    	return new SimpleScoreController(this); 
	    }
}


}
