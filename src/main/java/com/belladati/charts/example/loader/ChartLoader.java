package com.belladati.charts.example.loader;

import com.belladati.charts.example.Utils;

public class ChartLoader {

	private String JSON_CHART = "/com/belladati/charts/example/chart.json";

	public String getJson() {
		return getContentOfStaticFile(JSON_CHART);
	}

	private String getContentOfStaticFile(String filePath) {
		return Utils.getContentOfStaticFile(filePath);
	}

}
