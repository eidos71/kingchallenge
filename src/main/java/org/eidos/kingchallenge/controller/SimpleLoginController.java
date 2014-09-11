package org.eidos.kingchallenge.controller;

import org.eidos.kingchallenge.services.LoginService;
import javax.annotation.concurrent.Immutable;
/**
 * MockloginController, singleton stateles?
 * @author eidos71
 *
 */
@Immutable
public final class SimpleLoginController  implements LoginController{
	
private final LoginService  loginService;

@Override
public String loginService(Integer token) throws RuntimeException {
	return this.loginService.loginToken(token);

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
 * inner class Builder for LoginController
 * @author eidos71
 *
 */
public static class Builder{
		private  LoginService loginService;
		/**
		 * 
		 * @param service
		 * @return
		 */
		public Builder service(LoginService service) {
			loginService=service;
			return this;
		}
	    public SimpleLoginController build() {
	    	return new SimpleLoginController(this);
	    }
}

}
