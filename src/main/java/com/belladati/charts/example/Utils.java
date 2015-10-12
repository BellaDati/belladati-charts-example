package com.belladati.charts.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {

	public static String getContentOfStaticFile(String filePath) {
		InputStream inputStream = getStaticInputStream(filePath);
		if (inputStream == null) {
			System.err.println("File does not exist: " + filePath);
			return null;
		}

		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 65728);
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static InputStream getStaticInputStream(String filePath) {
		return Utils.class.getResourceAsStream(filePath);
	}

}
