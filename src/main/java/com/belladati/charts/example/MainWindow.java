package com.belladati.charts.example;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.Scene;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.w3c.dom.Document;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = -2299233958556243476L;

	private WebEngine webEngine;

	public MainWindow() throws InterruptedException {
		System.out.println("Initializing main window...");

		// configure main window
		setTitle("BellaDati Charts SDK Example");
		setSize(750, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// setup content of main window
		JPanel content = new JPanel();
		this.add(content);
		content.setLayout(null);
		content.add(createQuitButton());
		content.add(createWebPanel());

		// initialize web engine
		while (webEngine == null) {
			System.out.println("Waiting to initialize JavaFX WebEngine...");
			Thread.sleep(1000);
		}
	}

	/**
	 * Loads a web page specified by given URL into this window. 
	 * @param url
	 */
	public void loadUrl(final String url) {
		System.out.println("Loading URL: " + url + "\n");
		Platform.runLater(() -> {
			webEngine.load(url);
		});
	}

	/**
	 * Executes given script in the context of the current page.
	 * @param javascript
	 */
	public void loadJavaScript(final String javascript) {
		System.out.println("Executing JavaScript: " + javascript + "\n");
		webEngine.documentProperty().addListener(new ChangeListener<Document>() {
			@Override
			public void changed(ObservableValue<? extends Document> observableValue, Document document, Document newDoc) {
				if (newDoc != null) {
					webEngine.documentProperty().removeListener(this);
					webEngine.executeScript(javascript);
				}
			}
		});
	}

	private JFXPanel createWebPanel() {
		JFXPanel htmlPanel = new JFXPanel();
		htmlPanel.setBounds(10, 10, 600, 550);
		Platform.setImplicitExit(false);
		Platform.runLater(() -> {
			WebView webView = new WebView();
			webEngine = webView.getEngine();
			webEngine.setJavaScriptEnabled(true);
			htmlPanel.setScene(new Scene(webView));
		});
		return htmlPanel;
	}

	private JButton createQuitButton() {
		JButton button = new JButton("Quit");
		button.setBounds(620, 10, 120, 30);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		return button;
	}

}
