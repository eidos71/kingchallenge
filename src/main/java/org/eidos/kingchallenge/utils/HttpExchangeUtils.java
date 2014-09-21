package org.eidos.kingchallenge.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

public class HttpExchangeUtils {
	/**
	 * 
	 * @param query
	 * @param parameters
	 * @throws UnsupportedEncodingException
	 */
	public static void parseQuery(String query, Map<String, Object> parameters)
			throws UnsupportedEncodingException {

		if (query != null) {
			String pairs[] = query.split("[&]");

			for (String pair : pairs) {
				String param[] = pair.split("[=]");

				String key = null;
				String value = null;
				if (param.length > 0) {
					// Retrieve the key
					key = URLDecoder.decode(param[0],
							System.getProperty("file.encoding"));
				}

				if (param.length > 1) {
				
					value = URLDecoder.decode(param[1],
							System.getProperty("file.encoding"));
				}
				if (key!=null)
					parameters.put(key.toLowerCase(), value);
			}
		}
	}
}
