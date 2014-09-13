package org.eidos.kingchallenge.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.concurrent.GuardedBy;

import org.eidos.kingchallenge.exceptions.KingRunTimeIOException;

public class FilReaderUtils {
	private static Object lock = new Object();

	/**
	 * File Reader reciving an inputStream
	 * 
	 * @param inputStream
	 * @return
	 */
	public static Map<String, String> returnEnumList(InputStream inputStream) {
	
		Properties prop = new Properties();
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		try {
			synchronized (lock) {
				prop.load(inputStream);
				Set<String> result = prop.stringPropertyNames();
				for (String key : result) {
					hashTable.put(key, prop.getProperty(key));
				}
			}
			return Collections.unmodifiableMap(hashTable);
		} catch (IOException e) {
			throw new KingRunTimeIOException(e);
		}

	}
}
