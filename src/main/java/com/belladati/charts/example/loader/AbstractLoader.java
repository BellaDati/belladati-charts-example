package com.belladati.charts.example.loader;

import com.belladati.charts.example.ui.MainWindow;

/**
 * Abstract loader used to load HTML with chart.
 * @author Lubomir Elko
 */
public abstract class AbstractLoader {

	protected final String HTML_INDEX = "/com/belladati/charts/example/index.html";

	/**
	 * Loads URL with index.html and sets chart title.
	 * @param window
	 */
	protected final void renderIndexHtml(MainWindow window, String chartTitle) {
		window.loadUrl(getFilePath(HTML_INDEX));
		window.loadJavaScript("document.getElementById(\"title\").innerHTML = \"" + chartTitle + "\";");
	}

	private String getFilePath(String filePath) {
		String path = this.getClass().getResource(filePath).getPath();
		if (!path.startsWith("file:")) {
			path = "file://" + path;
		}
		if (path.contains(".jar!")) {
			path = "jar:" + path;
		}
		return path;
	}

}
