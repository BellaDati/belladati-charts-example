package com.belladati.charts.example.ui;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.Scene;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.List;

import org.w3c.dom.Document;

import com.belladati.charts.example.loader.SampleLoader;
import com.belladati.charts.example.loader.SdkLoader;
import com.belladati.sdk.dataset.Attribute;
import com.belladati.sdk.report.Report;
import com.belladati.sdk.view.View;

/**
 * Main application window used to show appropriate chart via {@link WebEngine}.
 * @author Lubomir Elko
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = -2299233958556243476L;

	private static final int WIDTH_DEFAULT = 700;
	private static final int WIDTH_FILTER = 930;
	private static final int HEIGHT = 480;

	private WebEngine webEngine;
	private ViewSelector selector;
	private JsonEditor jsonEditor;
	private final FilterPanel filterPanel;

	public MainWindow() throws InterruptedException {
		System.out.println("Initializing main window...");

		// configure main window
		setTitle("BellaDati Charts SDK Example");
		setSize(WIDTH_FILTER, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// setup content of main window
		JPanel content = new JPanel();
		this.add(content);
		content.setLayout(null);
		content.add(createWebPanel());
		content.add(createButtonPanel());
		content.add(filterPanel = new FilterPanel());

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
		hideFilterPanel();
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
		JFXPanel panel = new JFXPanel();
		panel.setBounds(170, 10, 520, 440);
		Platform.setImplicitExit(false);
		Platform.runLater(() -> {
			WebView webView = new WebView();
			webEngine = webView.getEngine();
			webEngine.setJavaScriptEnabled(true);
			panel.setScene(new Scene(webView));
		});
		return panel;
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		panel.setBounds(0, 5, 180, 440);
		panel.setLayout(null);
		panel.add(createSampleButton());
		panel.add(createCustomButton());
		panel.add(createApiButton());
		panel.add(createClearButton());
		panel.add(createQuitButton());
		panel.add(createBellaDatiIcon());
		panel.add(createBellaDatiLink());
		return panel;
	}

	private JButton createSampleButton() {
		JButton button = new JButton("Sample chart");
		button.setBounds(5, 5, 160, 30);
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
		button.setBounds(5, 45, 160, 30);
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
		button.setBounds(5, 85, 160, 30);
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
		button.setBounds(5, 125, 160, 30);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				clearBrowser();
			}
		});
		return button;
	}

	private JButton createQuitButton() {
		JButton button = new JButton("Quit");
		button.setBounds(5, 165, 160, 30);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		return button;
	}

	private JLabel createBellaDatiIcon() {
		JLabel label = new JLabel();
		label.setBounds(25, 275, 150, 150);

		ImageIcon icon = new ImageIcon(this.getClass().getResource("/com/belladati/charts/example/AppIcon60x60@2x.png"));
		label.setIcon(icon);

		return label;
	}

	private JLabel createBellaDatiLink() {
		JLabel label = new JLabel("www.belladati.com");
		label.setBounds(25, 415, 150, 30);
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				try {
					if (Desktop.isDesktopSupported()) {
						Desktop.getDesktop().browse(new URI("http://www.belladati.com"));
					}
				} catch (Exception e) {
					System.err.println("Cannot open default browser: " + e.getMessage());
					e.printStackTrace();
				}
			}
		});
		return label;
	}

	public final void hideFilterPanel() {
		filterPanel.clear();
		setSize(WIDTH_DEFAULT, HEIGHT);
		filterPanel.setVisible(false);
	}

	public final void showFiltersForView(SdkLoader loader, Report report, View view) {
		filterPanel.initForView(loader, report, view);
		setSize(WIDTH_FILTER, HEIGHT);
		filterPanel.setVisible(true);
	}

	private final class FilterPanel extends JPanel {
		private static final long serialVersionUID = 6329076606717798819L;

		private final JComboBox<Attribute> attributeComboBox;
		private final FiltersJList valueList;
		private final JButton buttonApplyFilter, buttonResetFilter;

		private SdkLoader loader;
		private Report report;
		private View view;

		public FilterPanel() {
			super();
			setBounds(700, 5, 220, 440);
			setLayout(null);

			JLabel labelAttribute = new JLabel("Filter by attribute:");
			labelAttribute.setBounds(0, 0, 220, 30);
			add(labelAttribute);
			attributeComboBox = new JComboBox<Attribute>();
			attributeComboBox.setBounds(0, 25, 220, 30);
			add(attributeComboBox);

			JLabel labelValues = new JLabel("Select values:");
			labelValues.setBounds(0, 50, 220, 30);
			add(labelValues);
			valueList = new FiltersJList();
			JScrollPane scrollList = new JScrollPane(valueList);
			scrollList.setBounds(0, 80, 220, 290);
			scrollList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			add(scrollList);

			buttonApplyFilter = new JButton("Apply filter");
			buttonApplyFilter.setBounds(25, 380, 170, 30);
			add(buttonApplyFilter);

			buttonResetFilter = new JButton("Reset filter");
			buttonResetFilter.setBounds(25, 415, 170, 30);
			add(buttonResetFilter);

			attributeComboBox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent event) {
					if (event.getStateChange() == ItemEvent.SELECTED) {
						Attribute attribute = (Attribute) event.getItem();
						clearValues();
						if (attribute != null) {
							for (String value : loader.getAttributeValues(attribute)) {
								addValue(value);
							}
						}
					}
				}
			});
			valueList.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent event) {
					// enabled/disable apply button
					List<String> filters = valueList.getSelectedValuesList();
					buttonApplyFilter.setEnabled(filters != null && !filters.isEmpty());
				}
			});
			buttonApplyFilter.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					// render chart with selected filters on main window
					Attribute selectedAttribute = (Attribute) attributeComboBox.getSelectedItem();
					List<String> selectedValues = valueList.getSelectedValuesList();
					loader.renderChart(MainWindow.this, report, view, selectedAttribute, selectedValues);
				}
			});
			buttonResetFilter.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					// render chart without filters on main window
					loader.renderChart(MainWindow.this, report, view, null, null);
				}
			});

			clear();
		}

		public final void clear() {
			attributeComboBox.removeAllItems();
			clearValues();
		}

		private void clearValues() {
			((DefaultListModel<String>) valueList.getModel()).clear();
			buttonApplyFilter.setEnabled(false);
		}

		public final void initForView(SdkLoader loader, Report report, View view) {
			clear();
			this.loader = loader;
			this.report = report;
			this.view = view;

			if (view != null && report != null && loader != null) {
				for (Attribute attribute : loader.getAttributes(report)) {
					addAttribute(attribute);
				}
			}
		}

		private final void addAttribute(Attribute attribute) {
			attributeComboBox.addItem(attribute);
		}

		private final void addValue(String value) {
			((DefaultListModel<String>) valueList.getModel()).addElement(value);
		}

		private final class FiltersJList extends JList<String> {
			private static final long serialVersionUID = -1316106939236596642L;

			public FiltersJList() {
				super(new DefaultListModel<String>());
				setCellRenderer(new ListCellRenderer<String>() {

					@Override
					public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
						boolean isSelected, boolean cellHasFocus) {
						JLabel item = new JLabel(value);
						if (isSelected) {
							item.setBackground(Color.LIGHT_GRAY);
						} else {
							item.setBackground(list.getBackground());
						}
						item.setOpaque(true);
						return item;
					}
				});
				setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			}
		}

	}

}
