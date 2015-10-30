package com.belladati.charts.example.loader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.belladati.charts.example.ui.MainWindow;
import com.belladati.sdk.BellaDati;
import com.belladati.sdk.BellaDatiConnection;
import com.belladati.sdk.BellaDatiService;
import com.belladati.sdk.dataset.Attribute;
import com.belladati.sdk.dataset.AttributeType;
import com.belladati.sdk.dataset.AttributeValue;
import com.belladati.sdk.filter.Filter;
import com.belladati.sdk.filter.FilterOperation;
import com.belladati.sdk.filter.FilterValue;
import com.belladati.sdk.filter.Filter.MultiValueFilter;
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
	private static final String EMPTY_LABEL = "(empty)";

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

	public List<Attribute> getAttributes(Report report) {
		List<Attribute> list = new ArrayList<Attribute>();

		for (Attribute attribute : report.getAttributes()) {
			if (attribute.getType() == AttributeType.TEXT) {
				list.add(attribute);
			}
		}
		System.out.println("Report " + report.getName() + " contains " + list.size() + " text attributes.");
		return list;
	}

	public List<String> getAttributeValues(Attribute attribute) {
		List<String> list = new ArrayList<String>();

		for (AttributeValue value : attribute.getValues().load().get()) {
			if (value.getValue() == null || value.getValue().isEmpty()) {
				list.add(EMPTY_LABEL);
			} else {
				list.add(value.getValue());
			}
		}
		System.out.println("Attribute " + attribute.getCode() + " contains " + list.size() + " values.");

		return list;
	}

	private String getJsonData(Report report, View view, Attribute attribute, List<String> values) {
		try {
			ViewLoader loader = view.createLoader();
			if (attribute != null && values != null) {
				loader.addFilters(createFilters(report.getId(), attribute, values));
			} else {
				System.out.println("Loading JSON data (no filter applied)");
			}
			return postProcess((JsonNode) loader.loadContent());
		} catch (Exception e) {
			System.err.println("Error getting data: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private Collection<Filter<?>> createFilters(String reportId, Attribute attribute, List<String> values) {
		Collection<Filter<?>> filters = new ArrayList<Filter<?>>();

		MultiValueFilter filter = FilterOperation.IN.createFilter(service, reportId, attribute.getCode());
		for (String value : values) {
			if (value.equalsIgnoreCase(EMPTY_LABEL)) {
				filter.addValue(new FilterValue(""));
			} else {
				filter.addValue(new FilterValue(value));
			}
		}
		System.out.println("Loading JSON data for filter: " + filter);
		filters.add(filter);

		return filters;
	}

	private String postProcess(JsonNode node) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(node.get("content"));
	}

	public void renderChart(MainWindow window, Report report, View view) {
		// load chart JSON without filter
		String jsonData = getJsonData(report, view, null, null);

		// load URL with index.html
		renderIndexHtml(window, view.getName());

		// render the chart by executing JavaScript
		window.loadJavaScript("Charts.createAndRender(\"chart\", " + jsonData + ");");

		// initialize available filters
		window.showFiltersForView(this, report, view);
	}

	public void renderChart(MainWindow window, Report report, View view, Attribute attribute, List<String> values) {
		// load chart JSON with filter
		String jsonData = getJsonData(report, view, attribute, values);

		// load URL with index.html
		renderIndexHtml(window, view.getName());

		// render the chart by executing JavaScript
		window.loadJavaScript("Charts.createAndRender(\"chart\", " + jsonData + ");");
	}

}
