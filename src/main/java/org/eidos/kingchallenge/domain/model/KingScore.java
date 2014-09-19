package org.eidos.kingchallenge.domain.model;

import java.io.Serializable;

import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.utils.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KingScore implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5652221039613378809L;
	static final Logger LOG = LoggerFactory
			.getLogger(KingScore.class);

	private final Long kingUserId;
	private final int points;
	private KingScore() {
		throw new UnsupportedOperationException();
	}
	private  KingScore(Builder builder) {
			this.points=builder.points;
			this.kingUserId=builder.userId;
		
	}
	// GETTERS
	public Long getKingUserId() {
		return kingUserId;
	}

	public Integer getPoints() {
		return points;
	}



	/**
	 * Builder for KingScore
	 * @author eidos71
	 *
	 */
	public static class Builder {
		private int points;
		private Long userId;
		/**
		 * 
		 * @param points 

		 * @param UserID
		 */
		public Builder ( int pPoints, Long pUserId ) {
			if (!Validator.isValidPositiveInt(pPoints) )
				throw new LogicKingChallengeException(LogicKingError.INVALID_SCORE);

			if (!Validator.isValidUnsignedInt(pUserId))
				throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
			this.points=pPoints;
			this.userId=pUserId;
		}
		/**
		 * public constructor
		 * 
		 * @return a KingScore
		 */
		public KingScore build() {
			return new KingScore(this);
		}



		
	}



	@Override
	public String toString(){
		return  kingUserId + "=" + points;
				
	}
/*	@Override
	public String toString() {
		return "KingScore [kingUserId=" + kingUserId + ", points=" + points
				+ "]";
	}*/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kingUserId == null) ? 0 : kingUserId.hashCode());
		result = prime * result + points;
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
		KingScore other = (KingScore) obj;
		if (kingUserId == null) {
			if (other.kingUserId != null)
				return false;
		} else if (!kingUserId.equals(other.kingUserId))
			return false;
		if (points != other.points)
			return false;
		return true;
	}
	
	
	
}
	


