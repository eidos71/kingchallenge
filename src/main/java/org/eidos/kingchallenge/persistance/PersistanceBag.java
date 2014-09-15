package org.eidos.kingchallenge.persistance;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

import org.eidos.kingchallenge.controller.SimpleLoginController;

/**
 *  Holds the Bag of different persistances Elements that
 *  feed with data the app.
 *  By design is a builder and not a singleton, to allow different instances of
 *  Persistance Bags and delegates on KingdomComManager
 *  how many instances in the app will exist (on this case, only 1)
 * @author eidos71
 *
 */
@Immutable
public final class PersistanceBag implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7041243246192738570L;
	
	private final LoginPersistanceMap loginPersistance;
	private final ScorePersistance scorePersistance;
	
	/**
	 * Bag where we store all persistance related
	 * structures
	 */
	private PersistanceBag() {
		throw new UnsupportedOperationException();
	}
	private PersistanceBag(Builder builder) {
		this.loginPersistance=builder.loginPersistance;
		this.scorePersistance=builder.scorePersistance;
	}
	public static class Builder {
		LoginPersistanceMap loginPersistance;
		ScorePersistance scorePersistance;
		public Builder() {
			
		}
		public Builder setLoginImp(LoginPersistanceMap loginPersistance) {
				this.loginPersistance=loginPersistance;
				return this;
		}
		public Builder setScorePersistance(ScorePersistance scorePersistance) {
				this.scorePersistance=scorePersistance;
				return this;
		}
	    public PersistanceBag build() {
	    	if (loginPersistance==null)
	    		throw  new IllegalStateException("No LoginService has been instanced"); 
	    	if ( scorePersistance==null)
	    		throw  new IllegalStateException("No LoginService has been instanced"); 
	    	return new PersistanceBag(this);
	    }
	}
}
