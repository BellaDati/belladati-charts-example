package com.belladati.charts.example.render;

import com.belladati.charts.example.Utils;

public class HtmlReplacer {

	private String HTML_INDEX = "/com/belladati/charts/example/index.html";

	public String getHtml() {
		final String path = getPathToResources();
		System.out.println("Path to resources: " + path);

		String html = getContentOfStaticFile(HTML_INDEX);

		html = html.replaceAll("pathReplacement/", path);

		return html;
	}

	private String getPathToResources() {
		return "file://" + HtmlReplacer.class.getResource(HTML_INDEX).getPath().replace("index.html", "");
	}

	private String getContentOfStaticFile(String filePath) {
		return Utils.getContentOfStaticFile(filePath);
	}

}
