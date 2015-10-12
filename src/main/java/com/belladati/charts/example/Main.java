package com.belladati.charts.example;

public class Main {

	private static final String HTML_INDEX = "/com/belladati/charts/example/index.html";
	private static final String JSON_CHART = "/com/belladati/charts/example/chart.json";

	public static void main(String[] args) throws Exception {
		// create Swing UI
		MainWindow window = new MainWindow();
		window.setVisible(true);

		// load URL with index.html 
		window.loadUrl(getFilePath(HTML_INDEX));

		// render the chart by executing JavaScript 
		window.loadJavaScript("Charts.createAndRender(\"chart\", " + getFileContent(JSON_CHART) + ");");
	}

	private static String getFilePath(String filePath) {
		return "file://" + Main.class.getResource(filePath).getPath();
	}

	private static String getFileContent(String filePath) {
		return Utils.getContentOfStaticFile(filePath);
	}

}
