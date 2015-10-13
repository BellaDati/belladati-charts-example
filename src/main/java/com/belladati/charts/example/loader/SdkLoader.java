package com.belladati.charts.example.loader;

import java.util.ArrayList;
import java.util.List;

import com.belladati.charts.example.ui.MainWindow;
import com.belladati.sdk.BellaDati;
import com.belladati.sdk.BellaDatiConnection;
import com.belladati.sdk.BellaDatiService;
import com.belladati.sdk.report.Report;
import com.belladati.sdk.report.ReportInfo;
import com.belladati.sdk.view.View;
import com.belladati.sdk.view.ViewLoader;
import com.belladati.sdk.view.ViewType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Loads JSON chart data from server via SDK.
 * @author Lubomir Elko
 */
public class SdkLoader extends AbstractLoader {

	private final BellaDatiConnection connection;
	private final BellaDatiService service;

	public SdkLoader(String consumerKey, String consumerSecret, String username, String password) throws Exception {
		connection = BellaDati.connect();
		service = connection.xAuth(consumerKey, consumerSecret, username, password);
	}

	public List<Report> getReports() {
		List<Report> reports = new ArrayList<Report>();

		List<ReportInfo> reportInfos = service.getReportInfo().load().toList();
		System.out.println("SDK returned " + reportInfos.size() + " reports.");
		for (ReportInfo reportInfo : reportInfos) {
			reports.add(reportInfo.loadDetails());
		}

		return reports;
	}

	public List<View> getViews(Report report) {
		List<View> views = new ArrayList<View>();

		for (View view : report.getViews()) {
			if (view.getType() == ViewType.CHART) {
				views.add(view);
			}
		}
		System.out.println("Report " + report.getName() + " contains " + views.size() + " chart views.");

		return views;
	}

	private String getJsonData(View view) {
		try {
			ViewLoader loader = view.createLoader();
			return postProcess((JsonNode) loader.loadContent());
		} catch (Exception e) {
			System.err.println("Error getting data: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private String postProcess(JsonNode node) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(node.get("content"));
	}

	public void renderChart(MainWindow window, View view) {
		// load chart JSON
		String jsonData = getJsonData(view);

		// load URL with index.html
		renderIndexHtml(window, view.getName());

		// render the chart by executing JavaScript
		window.loadJavaScript("Charts.createAndRender(\"chart\", " + jsonData + ");");
	}

}
