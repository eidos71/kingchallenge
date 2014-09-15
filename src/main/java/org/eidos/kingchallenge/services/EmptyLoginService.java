package org.eidos.kingchallenge.services;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An LoginService that does nothing!
 * @author eidos71
 *
 */
public class EmptyLoginService implements LoginService {

	@Override
	public String loginToken(AtomicLong token) {
		// TODO Auto-generated method stub
		return "fakeSessionId";
	}

	@Override
	public void sessionCheck(Date currentDate) {
		// TODO Auto-generated method stub

	}

}
