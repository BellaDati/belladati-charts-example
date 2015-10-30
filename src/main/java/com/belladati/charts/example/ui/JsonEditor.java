package com.belladati.charts.example.ui;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import com.belladati.charts.example.loader.SampleLoader;

/**
 * Dialog window used to enter custom JSON data of chart, that should be rendered.
 * @author Lubomir Elko
 */
public class JsonEditor extends JDialog {
	private static final long serialVersionUID = 571542550871222741L;

	private final MainWindow mainWindow;
	private final JTextComponent areaJson;
	private final JButton buttonRender;
	private SampleLoader loader;

	public JsonEditor(MainWindow parent) {
		super(parent, Dialog.ModalityType.APPLICATION_MODAL);

		// configure selector window
		setTitle("BellaDati Charts SDK Example");
		setSize(720, 560);
		setLocationRelativeTo(null);
		mainWindow = parent;

		// setup content of main window
		JPanel content = new JPanel();
		this.add(content);
		content.setLayout(null);

		areaJson = createAreaJson(content);
		content.add(buttonRender = createRenderButton());

		// initialize state
		buttonRender.setEnabled(false);
	}

	private JTextComponent createAreaJson(JPanel content) {
		JLabel label = new JLabel("JSON data:");
		label.setBounds(15, 5, 200, 30);
		content.add(label);

		JTextArea area = new JTextArea();
		JScrollPane scroll = new JScrollPane(area);
		scroll.setBounds(10, 30, 700, 470);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		content.add(scroll);
		area.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				refreshButton();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				refreshButton();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				refreshButton();
			}

			private void refreshButton() {
				buttonRender.setEnabled(area.getText() != null && !area.getText().isEmpty());
			}
		});
		return area;
	}

	private JButton createRenderButton() {
		JButton button = new JButton("Render view");
		button.setBounds(555, 503, 160, 30);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				// render chart on main window
				loader = new SampleLoader();
				loader.renderChart(mainWindow, areaJson.getText());
				JsonEditor.this.setVisible(false);
			}
		});
		return button;
	}

}
