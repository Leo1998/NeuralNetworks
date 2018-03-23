package com.nn.utils;

import java.io.Closeable;
import java.io.IOException;

public class StreamUtils {
	
	private StreamUtils() {}
	
	public static void closeQuiet(Closeable c) {
		try {
			c.close();
		} catch(IOException e) {}
	}
	
}
