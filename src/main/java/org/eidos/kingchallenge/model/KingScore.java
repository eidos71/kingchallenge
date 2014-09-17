package org.eidos.kingchallenge.model;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;

import org.eidos.kingchallenge.exceptions.LogicKingChallengeException;
import org.eidos.kingchallenge.exceptions.enums.LogicKingError;
import org.eidos.kingchallenge.utils.Validator;
/**
 * Model 
 * @author eidos71
 *
 */
@Immutable
public final class KingScore  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5652221039613378809L;

	private final int points;
	private final Long level;
	private final Long kingUserId;
	private KingScore() {
		throw new UnsupportedOperationException();
	}
	private  KingScore(Builder builder) {
		this.points=builder.points;
		this.level=builder.level;
		this.kingUserId=builder.userId;;
		
	}
	// GETTERS
	public Long getKingUserId() {
		return kingUserId;
	}

	public int getPoints() {
		return points;
	}
	public Long getLevel() {
		return level;
	}
	
	@Override
	public String toString() {
		return "KingScore [points=" + points + ", level=" + level
				+ ", kingUserId=" + kingUserId + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kingUserId == null) ? 0 : kingUserId.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
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
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (points != other.points)
			return false;
		return true;
	}


	/**
	 * Builder for KingScore
	 * @author eidos71
	 *
	 */
	public static class Builder {
		private int points;
		private Long level;
		private Long userId;
		/**
		 * 
		 * @param points 
		 * @param level
		 */
		public Builder ( Long pLevel,int pPoints, Long pUserId ) {
			if (!Validator.isValidPositiveInt(pPoints) )
				throw new LogicKingChallengeException(LogicKingError.INVALID_SCORE);
			if (!Validator.isValidUnsignedInt(pLevel))
				throw new LogicKingChallengeException(LogicKingError.INVALID_LEVEL);
			if (!Validator.isValidUnsignedInt(pUserId))
				throw new LogicKingChallengeException(LogicKingError.INVALID_TOKEN);
			this.points=pPoints;
			this.level=pLevel;
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



	


}
