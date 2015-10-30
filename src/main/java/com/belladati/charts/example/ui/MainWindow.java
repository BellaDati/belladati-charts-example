package com.belladati.charts.example.ui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

import com.belladati.charts.example.loader.SampleLoader;

/**
 * Main application window used to show appropriate chart via {@link WebEngine}.
 * @author Lubomir Elko
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = -2299233958556243476L;

	private WebEngine webEngine;
	private ViewSelector selector;
	private JsonEditor jsonEditor;

	public MainWindow() throws InterruptedException {
		System.out.println("Initializing main window...");

		// configure main window
		setTitle("BellaDati Charts SDK Example");
		setSize(700, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// setup content of main window
		JPanel content = new JPanel();
		this.add(content);
		content.setLayout(null);
		content.add(createWebPanel());
		content.add(createSampleButton());
		content.add(createCustomButton());
		content.add(createApiButton());
		content.add(createClearButton());
		content.add(createQuitButton());
		content.add(createBellaDatiIcon());

		// initialize web engine
		while (webEngine == null) {
			System.out.println("Waiting to initialize JavaFX WebEngine...");
			Thread.sleep(1000);
		}
		clearBrowser();
	}

	/**
	 * Clears content of HTML panel in this window.
	 */
	public void clearBrowser() {
		loadStaticHtmlCode("<html><body><center>No chart yet</center></body></html>");
	}

	/**
	 * Loads given static HTML code into this window. 
	 * @param htmlCode
	 */
	public void loadStaticHtmlCode(final String htmlCode) {
		System.out.println("Loading static HTML code: " + htmlCode + "\n");
		Platform.runLater(() -> {
			webEngine.loadContent(htmlCode);
		});
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
		htmlPanel.setBounds(10, 10, 520, 440);
		Platform.setImplicitExit(false);
		Platform.runLater(() -> {
			WebView webView = new WebView();
			webEngine = webView.getEngine();
			webEngine.setJavaScriptEnabled(true);
			htmlPanel.setScene(new Scene(webView));
		});
		return htmlPanel;
	}

	private JButton createSampleButton() {
		JButton button = new JButton("Sample chart");
		button.setBounds(535, 10, 160, 30);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				SampleLoader loader = new SampleLoader();
				loader.renderChart(MainWindow.this);
			}
		});
		return button;
	}

	private JButton createCustomButton() {
		JButton button = new JButton("Custom JSON data");
		button.setBounds(535, 50, 160, 30);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				if (jsonEditor == null) {
					jsonEditor = new JsonEditor(MainWindow.this);
				}
				jsonEditor.setVisible(true);
			}
		});
		return button;
	}

	private JButton createApiButton() {
		JButton button = new JButton("Chart from server");
		button.setBounds(535, 90, 160, 30);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				if (selector == null) {
					selector = new ViewSelector(MainWindow.this);
				}
				selector.setVisible(true);
			}
		});
		return button;
	}

	private JButton createClearButton() {
		JButton button = new JButton("Clear");
		button.setBounds(535, 130, 160, 30);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				clearBrowser();
			}
		});
		return button;
	}

	private JButton createQuitButton() {
		JButton button = new JButton("Quit");
		button.setBounds(535, 170, 160, 30);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		return button;
	}

	private JLabel createBellaDatiIcon() {
		JLabel label = new JLabel();
		label.setBounds(555, 310, 150, 150);

		ImageIcon icon = new ImageIcon(this.getClass().getResource("/com/belladati/charts/example/AppIcon60x60@2x.png"));
		label.setIcon(icon);

		return label;
	}

}
