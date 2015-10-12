package com.belladati.charts.example.render;

import com.belladati.charts.example.Utils;

public class HtmlReplacer {

	private String HTML_INDEX = "/com/belladati/charts/example/index.html";
	private String JSON_CHART = "/com/belladati/charts/example/chart.json";

	public String get() {
		final String path = getPathToResources();
		System.out.println("Path to resources: " + path);

		String html = getContentOfStaticFile(HTML_INDEX);

		html = html.replace("chartJsonReplacement", getChartJson());
		html = html.replaceAll("pathReplacement/", path);

		return html;
	}

	private String getPathToResources() {
		return "file://" + HtmlReplacer.class.getResource(HTML_INDEX).getPath().replace("index.html", "");
	}

	private String getChartJson() {
		return getContentOfStaticFile(JSON_CHART);
	}

	private String getContentOfStaticFile(String filePath) {
		// TODO open file and get text content
		return Utils.getContentOfStaticFile(filePath);
	}

}
