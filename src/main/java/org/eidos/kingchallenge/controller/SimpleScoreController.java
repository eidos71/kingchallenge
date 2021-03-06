package org.eidos.kingchallenge.controller;

import javax.annotation.concurrent.Immutable;

import org.eidos.kingchallenge.KingConfigStaticProperties;
import org.eidos.kingchallenge.domain.dto.KingResponseDTO;
import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
import org.eidos.kingchallenge.domain.model.KingUser;
import org.eidos.kingchallenge.httpserver.utils.MediaContentTypeEnum;
import org.eidos.kingchallenge.service.LoginService;
import org.eidos.kingchallenge.service.ScoreService;


@Immutable
public final class SimpleScoreController implements ScoreController {
	private final ScoreService  scoreService;
	private final LoginService loginService;


	@Override
	public KingResponseDTO putHighScore(String sessionKey, Long level, int score) {
		//Validates session is valid and 
		final String  defensiveSessionKey=sessionKey;
		final Long defensiveLevel= level;
		final int defensiveScore=score;
		KingUser user= loginService.renewLastLogin(defensiveSessionKey);
		scoreService.insertScore(defensiveSessionKey,  new KingScoreDTO.Builder(defensiveLevel,defensiveScore,user.getKingUserId().get()).build());
		// Result is always an empty value
		  KingResponseDTO httpReponseDTO =
				  new KingResponseDTO.Builder()
		  			.putContentBody("")
		  			.putContentType(MediaContentTypeEnum.TEXT_PLAIN)
		  			.build();
		return httpReponseDTO;
	}
	@Override
	public KingResponseDTO getHighScoreByLevel(Long level ) {
		final Long  defensiveLevel=level;
		return  new KingResponseDTO.Builder()
		  			.putContentBody(scoreService.getHighScoreList( defensiveLevel))
		  			.putContentType(MediaContentTypeEnum.TEXT_PLAIN)
		  			.build();
			
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
	    		this.scoreService=KingConfigStaticProperties.SCORESERVICE;
	    	
	    	}
	    	if (this.loginService == null) {
	    		this.loginService=KingConfigStaticProperties.LOGINSERVICE;
	    	
	    	}
	    	return new SimpleScoreController(this); 
	    }
}


}
