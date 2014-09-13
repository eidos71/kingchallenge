package org.eidos.kingchallenge.model;

import java.io.Serializable;
import javax.annotation.concurrent.Immutable;

import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;

/**
 * KingdomHandler that defines the specific
 * handler and context related.
 * @author eidos71
 *
 */
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
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contextPath == null) ? 0 : contextPath.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KingdomHandlerConf other = (KingdomHandlerConf) obj;
		if (contextPath == null) {
			if (other.contextPath != null)
				return false;
		} else if (!contextPath.equals(other.contextPath))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "KingdomHandlerConf [handlerClass=" + handlerClass
				+ ", contextPath=" + contextPath + "]";
	}


	public static class Builder {
		private final Class<?>   handlerClass;
		private   String contextPath;
		/**
		 * constructor 
		 * @param phandlerType  Handler Class full qualifying name
		 * @param pContextPath ContextPath 
		 */
		public Builder(String pHandlerClass) {
			
			if (pHandlerClass==null || "".equals(pHandlerClass))
				throw new LogicKingChallengeException(LogicKingError.INVALIDHANDLER);
			try {
				handlerClass = Class.forName(pHandlerClass);
				contextPath="/";
			} catch (ClassNotFoundException e) {
				throw new LogicKingChallengeException(LogicKingError.INVALIDHANDLER, e);
			}
		}
		public Builder contextPath(String context) {
			if  (context==null) context="/";
			contextPath=context;
			this.contextPath=context;
			return this;
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
