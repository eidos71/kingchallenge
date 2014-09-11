package org.eidos.kingchallenge.model;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.concurrent.Immutable;

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
	private final AtomicInteger kingUserId;
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

	public AtomicInteger getKingUserId() {
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
		return "KingUser [dateLogin=" + dateLogin + ", kingUserId="
				+ kingUserId + ", sessionKey=" + sessionKey + "]";
	}



	/**
	 * inner class Builder for LoginController
	 * 
	 * @author eidos71
	 *
	 */
	public static class Builder {
		private AtomicInteger kingUserId;
		private String sessionKey;
		private Date loginDate;

		/**
		 * 
		 * @param longId
		 * @param sessionId
		 */
		Builder(int intKingUserId, Date date) {
			this.sessionKey = generateSessionKey(intKingUserId);
			this.kingUserId = new AtomicInteger(intKingUserId);
			this.loginDate = new Date();

		}

		/**
		 * Private method that generates a kinguserId
		 * 
		 * @param intKingUserId
		 * @return
		 */
		private final String generateSessionKey(int intKingUserId) {
			// TODO Auto-generated method stub
			return "EXCEHOMO";
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
