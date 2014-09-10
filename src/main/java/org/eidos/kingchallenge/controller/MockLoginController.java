package org.eidos.kingchallenge.controller;

import org.eidos.kingchallenge.services.LoginService;

/**
 * MockloginController, singleton stateles?
 * @author eidos71
 *
 */
public class MockLoginController  implements LoginController{
	
private final LoginService  loginService;

@Override
public String loginService(Integer token) throws RuntimeException {
	return this.loginService.loginToken(token);

}
/**
 * We block the Login Controller public class
 */
private  MockLoginController ()  {this.loginService=null;};
private MockLoginController(Builder builder) {
    this.loginService = builder.loginService;

 }

/** 
 * inner class Builder for LoginController
 * @author eidos71
 *
 */
public static class Builder{
		private LoginService loginService;
		/**
		 * 
		 * @param service
		 * @return
		 */
		public Builder service(LoginService service) {
			this.loginService=service;
			return this;
		}
	    public MockLoginController build() {
	    	return new MockLoginController(this);
	    }
}

}
