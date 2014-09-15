package org.eidos.kingchallenge.controller;

import javax.annotation.concurrent.Immutable;

import org.eidos.kingchallenge.model.KingScore;
import org.eidos.kingchallenge.service.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Immutable
public final class SimpleScoreController implements ScoreController {
	private final ScoreService  scoreService;
	static final Logger LOG = LoggerFactory.getLogger(SimpleScoreController.class);
	@Override
	public String getHighScoresList() {
		//conver this to a response
		scoreService.getHighScoreList();
		return "";
	}
	@Override
	public String putHighScore(String sessionKey, int level, int score) {
		scoreService.insertScore(sessionKey,  new KingScore.Builder(score, level).build());
		return "";
	}
	
	private SimpleScoreController(Builder builder) {
	    this.scoreService = builder.scoreService;
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
		public Builder(ScoreService ascoreService) {
			this.scoreService=ascoreService;
		}

	    public SimpleScoreController build() {
	    	if (scoreService==null)
	    		throw  new IllegalStateException("No Score has been instanced"); 
	    	return new SimpleScoreController(this);
	    }
}
}
