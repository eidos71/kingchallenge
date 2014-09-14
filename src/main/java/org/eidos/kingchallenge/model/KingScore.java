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
	private final int level;
	private KingScore() {
		throw new UnsupportedOperationException();
	}
	private  KingScore(Builder builder) {
		this.points=builder.points;
		this.level=builder.level;
		
	}
	
	// Setters
	public int getPoints() {
		return points;
	}
	public int getLevel() {
		return level;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + level;
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
		if (level != other.level)
			return false;
		if (points != other.points)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "KingScore [points=" + points + ", level=" + level + "]";
	}


	/**
	 * Builder for KingScore
	 * @author eidos71
	 *
	 */
	public static class Builder {
		private int points;
		private int level;
		/**
		 * 
		 * @param points 
		 * @param level
		 */
		public Builder (int pPoints, int pLevel) {
			if (!Validator.isValidPositiveInt(pLevel) )
				throw new LogicKingChallengeException(LogicKingError.INVALID_SCORE);
			this.points=pPoints;
			this.level=pLevel;
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
