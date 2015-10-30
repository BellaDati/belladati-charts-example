package com.belladati.charts.example.loader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.belladati.charts.example.ui.MainWindow;

/**
 * Loads JSON chart data from static file located in src/main/resources.
 * @author Lubomir Elko
 */
public class SampleLoader extends AbstractLoader {

	private static final String JSON_CHART = "/com/belladati/charts/example/chart.json";

	public SampleLoader() {
	}

	public void renderChart(MainWindow window) {
		// load URL with index.html
		renderIndexHtml(window, "Sample Chart");

		// render the chart by executing JavaScript 
		window.loadJavaScript("Charts.createAndRender(\"chart\", " + getFileContent(JSON_CHART) + ");");
	}

	public void renderChart(MainWindow window, String jsonData) {
		// load URL with index.html
		renderIndexHtml(window, "Custom JSON data");

		// render the chart by executing JavaScript 
		window.loadJavaScript("Charts.createAndRender(\"chart\", " + jsonData + ");");
	}

	private String getFileContent(String filePath) {
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

	private InputStream getStaticInputStream(String filePath) {
		return SampleLoader.class.getResourceAsStream(filePath);
	}

}
