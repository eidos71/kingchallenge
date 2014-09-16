package org.eidos.kingchallenge.service;

import java.util.Map;

import org.eidos.kingchallenge.model.KingScore;
import org.eidos.kingchallenge.repository.EmptyScoreRepository;
import org.eidos.kingchallenge.repository.LoginRepository;
import org.eidos.kingchallenge.repository.ScoreRepository;
import org.eidos.kingchallenge.repository.SimpleLoginRepository;

public final class SimpleScoreService implements ScoreService {

	private ScoreRepository scoreRepository= new EmptyScoreRepository();
	private LoginRepository loginRepository = new SimpleLoginRepository();
	@Override
	public Map<String, String> getHighScoreList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertScore(String sessionKey, KingScore score) {
		// TODO Auto-generated method stub

	}

	public void setLoginRepository(LoginRepository loginRepository) {
		// TODO Auto-generated method stub
		this.loginRepository=loginRepository;
	}
	/**
	 * This is to be set 
	 * @param scoreService
	 */
	public void setScoreRepository(ScoreRepository scoreRepository) {
		this.scoreRepository=scoreRepository;
		
	}

}
