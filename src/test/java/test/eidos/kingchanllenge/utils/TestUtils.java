package test.eidos.kingchanllenge.utils;

import org.eidos.kingchallenge.domain.dto.KingScoreDTO;
import org.eidos.kingchallenge.domain.model.KingScore;

public class TestUtils {

	public static KingScore transformDTO(KingScoreDTO dto) {
		return new KingScore.Builder(  dto.getPoints(), dto.getKingUserId() ).build() ;
	}

	public static KingScoreDTO craeteKingScoreDTO(long lvl, int score, long userId) {
		return new KingScoreDTO.Builder(lvl, score, userId).build();
	}

}
