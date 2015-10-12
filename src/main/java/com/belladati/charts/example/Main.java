package com.belladati.charts.example;

import com.belladati.charts.example.render.HtmlReplacer;
import com.belladati.charts.example.ui.MainWindow;

public class Main {

	public static void main(String[] args) throws Exception {
		MainWindow window = new MainWindow();
		window.setVisible(true);
		window.loadHtmlText(new HtmlReplacer().get());
	}

}
