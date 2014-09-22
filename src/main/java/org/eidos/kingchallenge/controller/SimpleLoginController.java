package org.eidos.kingchallenge.controller;

import java.util.concurrent.atomic.AtomicLong;
import org.eidos.kingchallenge.KingConfigStaticProperties;
import org.eidos.kingchallenge.domain.dto.KingResponseDTO;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.httpserver.utils.MediaContentTypeEnum;
import org.eidos.kingchallenge.service.LoginService;
import org.eidos.kingchallenge.utils.Validator;

import javax.annotation.concurrent.Immutable;
/**
 * SimpleLoginController, singleton stateles?
 * @author eidos71
 *
 */
@Immutable
public final class SimpleLoginController  implements LoginController{
	
private final LoginService  loginService;

@Override
public KingResponseDTO loginService(Long token)   {
	if (token==null ||  ! Validator.isValidUnsignedInt(token) ) {
		throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
	}

	  KingResponseDTO httpReponseDTO =
			  new KingResponseDTO.Builder()
	  			.putContentBody(this.loginService.loginToken(new AtomicLong(token) ) )
	  			.putContentType(MediaContentTypeEnum.TEXT_PLAIN)
	  			.build();
	 ;
	 return  httpReponseDTO;
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
		public Builder() {
		
		}

	    public SimpleLoginController build() {
	    	if (loginService==null)
	    		this.loginService= KingConfigStaticProperties.LOGINSERVICE;
	    	return new SimpleLoginController(this);
	    }
}

}
