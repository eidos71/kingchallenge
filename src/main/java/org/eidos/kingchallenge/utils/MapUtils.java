package org.eidos.kingchallenge.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.eidos.kingchallenge.model.KingScore;

public class MapUtils {

	/**
	 * Use- converts map into a CSV
	 * @param highScoreList Map to transform
	 * @return CSV string,
	 */
	public static String returnCsvFromMap(Map<Long, KingScore> highScoreMap) {
		if (highScoreMap==null) return "";
		StringBuilder builder= new StringBuilder();
		for (Entry<Long, KingScore> entry:highScoreMap.entrySet() ) {
			
		}
		
		return "";
	}
	public static String returnCsvFromCollection(Collection<String> highScoreCollection) {
		return "";
	}

}
