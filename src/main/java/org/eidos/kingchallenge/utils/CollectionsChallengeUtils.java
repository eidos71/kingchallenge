package org.eidos.kingchallenge.utils;

import java.util.Collection;
import org.eidos.kingchallenge.domain.model.KingScore;




public class CollectionsChallengeUtils {
	private static final String ARRAY_START = "{";
	private static final String EMPTY_STRING = "";
	private static final String ARRAY_ELEMENT_SEPARATOR = ", ";

	private static final String ARRAY_END = "}";
	private static final String EMPTY_ARRAY = ARRAY_START + ARRAY_END;

	private static final String DEFAULT_EMTPYCOLLECTION = "";



	public static String returnCsvFromCollection(
			Collection<KingScore> highScoreCollection) {

		if (highScoreCollection == null || highScoreCollection.isEmpty())
			return DEFAULT_EMTPYCOLLECTION;
		String result= arrayToDelimitedString(highScoreCollection.toArray(),
				ARRAY_ELEMENT_SEPARATOR);

		return result;
	}

	public static String arrayToDelimitedString(Object[] arr, String delim) {

		if (isEmpty(arr)) {

			return "";
		}

		if (arr.length == 1) {

			return nullSafeToString(arr[0]);
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < arr.length; i++) {

			if (i > 0) {
				sb.append(delim);

			}
			sb.append(arr[i]);

		}

		return sb.toString();

	}
	
	public static boolean isEmpty(Object[] array) {

		return (array == null || array.length == 0);

	}
	/**
	 * Metodh that returns an EMPTY_STRING when a null is sent
	 * For timing reasons, this object only evaluates  not primitives Objects
	 * @param obj object to be evaluated
	 * @return EMPTY_STRING
	 */
	public static String nullSafeToString(Object obj) {
		String str = obj.toString();

		return (str != null ? str : EMPTY_STRING);

	}

}
