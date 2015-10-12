package com.belladati.charts.example;

import com.belladati.charts.example.loader.ChartLoader;
import com.belladati.charts.example.render.HtmlReplacer;
import com.belladati.charts.example.ui.MainWindow;

public class Main {

	public static void main(String[] args) throws Exception {
		MainWindow window = new MainWindow();
		window.setVisible(true);

		window.loadHtmlText(new HtmlReplacer().getHtml());

		window.loadJavaScript("Charts.createAndRender(\"chart\", " + new ChartLoader().getJson() + ");");
	}
}
