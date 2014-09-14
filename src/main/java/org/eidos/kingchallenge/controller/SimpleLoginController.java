package org.eidos.kingchallenge.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.services.LoginService;
import org.eidos.kingchallenge.utils.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.Immutable;
/**
 * SimpleLoginController, singleton stateles?
 * @author eidos71
 *
 */
@Immutable
public final class SimpleLoginController  implements LoginController{
	
private final LoginService  loginService;
static final Logger LOG = LoggerFactory.getLogger(SimpleLoginController.class);
@Override
public String loginService(Long token)   {
	if (token==null ||  ! Validator.isValidUnsignedInt(token) ) {
		throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
	}
	StringBuilder response= new StringBuilder();
	response.append(this.loginService.loginToken(new AtomicLong(token) ) );
	return response.toString();
}
/**
 * We block the Login Controller public class
 */
private  SimpleLoginController ()  {
	throw new UnsupportedOperationException();
};

private SimpleLoginController(Builder builder) {
    loginService = builder.loginService;
 }

/**
 * Inner Builder for LoginController
 * By constructor, we are setting all the needed
 * Services
 * @author eidos71
 *
 */
public static class Builder{
		private  LoginService loginService;
		public Builder(LoginService aLoginService) {
			this.loginService=aLoginService;
		}

	    public SimpleLoginController build() {
	    	if (loginService==null)
	    		throw  new IllegalStateException("No LoginService has been instanced"); 
	    	return new SimpleLoginController(this);
	    }
}

}
