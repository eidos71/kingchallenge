package org.eidos.kingchallenge.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.concurrent.Immutable;

import org.eidos.kingchallenge.exceptions.KingRunTimeException;
import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.utils.RandomUuidFactory;
import org.eidos.kingchallenge.utils.Validator;

/**
 * 
 * @author eidos71
 *
 */
@Immutable
public final class KingUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8792697318509222577L;
	private final Date dateLogin;
	private final AtomicLong kingUserId;
	private final String sessionKey;


	/**
	 * Avoid directly calling the private (reflection is always an issue)
	 */
	private KingUser() {
		throw new UnsupportedOperationException();
	}

	public Date getDateLogin() {
		return dateLogin;
	}

	public AtomicLong getKingUserId() {
		return kingUserId;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	private KingUser(Builder builder) {

		this.kingUserId = builder.kingUserId;
		this.dateLogin = builder.loginDate;
		this.sessionKey = builder.sessionKey;
	

	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sessionKey == null) ? 0 : sessionKey.hashCode());
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
		KingUser other = (KingUser) obj;
		if (sessionKey == null) {
			if (other.sessionKey != null)
				return false;
		} else if (!sessionKey.equals(other.sessionKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "KingUser [dateLogin=" + dateLogin+ ",EpochInms="+dateLogin.getTime()  +", kingUserId="
				+ kingUserId + ", sessionKey=" + sessionKey + "]";
	}



	/**
	 * inner class Builder for LoginController
	 * 
	 * @author eidos71
	 *
	 */
	public static class Builder {
		private AtomicLong kingUserId;
		private String sessionKey;
		private Date loginDate;
		
		/**
		 * 
		 * @param longId
		 * @param sessionId
		 */
		public Builder(long longKingUserId) {
			if (!Validator.isValidUnsignedInt(longKingUserId) )
				throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
			this.sessionKey = generateSessionKey();
			this.kingUserId = new AtomicLong(longKingUserId);
			this.loginDate = new Date();

		}
		/**
		 * Sets a login time with a specific date
		 * @param date date to be specific 
		 * @return Builder
		 */
		public Builder  setTime(Date date) {
			if ((date==null)  )
				throw 	 new IllegalArgumentException( "Invalid Date ");
			this.loginDate=date;
			return this;
		}
		/**
		 * 
		 * @param aSessionkey
		 * @return
		 */
		public Builder setSessionKey(String aSessionkey) {
			if ((aSessionkey==null) || "".equals(aSessionkey) )
				throw 	 new IllegalArgumentException( "Invalid Session Key");
			this.sessionKey=aSessionkey;
			return this;
		}
		
		
		/**
		 * Private method that generates a kinguserId
		 * 
		 * @param intKingUserId
		 * @return
		 */
		private final String generateSessionKey() {
			try {
				return  RandomUuidFactory.getInstance().createSessionID();
			}catch (Exception err) {
				throw new KingRunTimeException( err);
			}
			
		}
		
		/**
		 * public constructor
		 * 
		 * @return a KingUser
		 */
		public KingUser build() {
			return new KingUser(this);
		}
	}

}
