package org.eidos.kingchallenge.model;

import java.io.Serializable;
import java.lang.reflect.Type;

import javax.annotation.concurrent.Immutable;

import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;

import com.sun.net.httpserver.HttpHandler;

@Immutable
public final class KingdomHandlerConf   implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 834203839898341579L;
	private final Class<?>   handlerClass;
	private final String contextPath;

	/**
	 * Avoid directly calling the private (reflection is always an issue)
	 */
	private KingdomHandlerConf() {
		throw new UnsupportedOperationException();
	}
	private KingdomHandlerConf(Builder build) {
		this.handlerClass=build.handlerClass;
		this.contextPath=build.contextPath;
	}
	public String getContext() {
		// TODO Auto-generated method stub
		return contextPath;
	}

	public Class gethandlerType() {
		// TODO Auto-generated method stub
		return handlerClass;
	}
	public static class Builder {
		private final Class<?>   handlerClass;
		private  final String contextPath;
		public Builder(String phandlerType,String pContextPath) {
			
			if (phandlerType==null || "".equals(phandlerType))
				throw new LogicKingChallengeException(LogicKingError.INVALIDHANDLER);
			try {
				handlerClass = Class.forName(phandlerType);
				if  (pContextPath==null) pContextPath="";
				contextPath=pContextPath;
			} catch (ClassNotFoundException e) {
				throw new LogicKingChallengeException(LogicKingError.INVALIDHANDLER, e);
			}
		}
		/**
		 * public constructor
		 * 
		 * @return a KingUser
		 */
		public KingdomHandlerConf build() {
			return new KingdomHandlerConf(this);
		}
	}
}
