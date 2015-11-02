package com.belladati.charts.example.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.belladati.charts.example.loader.SdkLoader;
import com.belladati.sdk.report.Report;
import com.belladati.sdk.view.View;

/**
 * Dialog window used to login to SDK, select report and than view, that should be rendered.
 * @author Lubomir Elko
 */
public class ViewSelector extends JDialog {
	private static final long serialVersionUID = 6004737105858841534L;

	private final MainWindow mainWindow;
	private final JTextField fieldServiceUrl, fieldConsumerKey, fieldConsumerSecret, fieldUsername, fieldPassword;
	private final JButton buttonLogin, buttonLogout, buttonRender;
	private final MyJList<Report> listReports;
	private final MyJList<View> listViews;
	private SdkLoader loader;

	public ViewSelector(MainWindow parent) {
		super(parent, Dialog.ModalityType.APPLICATION_MODAL);

		// configure selector window
		setTitle("BellaDati Charts SDK Example");
		setSize(805, 425);
		setResizable(false);
		setLocationRelativeTo(null);
		mainWindow = parent;

		// setup content of main window
		JPanel content = new JPanel();
		this.add(content);
		content.setLayout(null);

		fieldServiceUrl = createFieldServiceUrl(content);
		fieldConsumerKey = createFieldConsumerKey(content);
		fieldConsumerSecret = createFieldConsumerSecret(content);
		fieldUsername = createFieldUsername(content);
		fieldPassword = createFieldPassword(content);
		content.add(buttonLogin = createLoginButton());
		content.add(buttonLogout = createLogoutButton());

		listReports = createListReports(content);
		listViews = createListViews(content);
		content.add(buttonRender = createRenderButton());

		// initialize state
		buttonLogin.setEnabled(true);
		buttonLogout.setEnabled(false);
		buttonRender.setEnabled(false);
	}

	private JTextField createFieldServiceUrl(JPanel content) {
		JLabel label = new JLabel("Service URL:");
		JTextField field = new JTextField("https://service.belladati.com/");
		label.setBounds(15, 10, 200, 30);
		field.setBounds(10, 32, 240, 30);
		content.add(label);
		content.add(field);
		return field;
	}

	private JTextField createFieldConsumerKey(JPanel content) {
		JLabel label = new JLabel("Consumer Key:");
		JTextField field = new JTextField("techKey");
		label.setBounds(15, 70, 200, 30);
		field.setBounds(10, 92, 240, 30);
		content.add(label);
		content.add(field);
		return field;
	}

	private JTextField createFieldConsumerSecret(JPanel content) {
		JLabel label = new JLabel("Consumer Secret:");
		JTextField field = new JTextField("techSecret");
		label.setBounds(15, 130, 200, 30);
		field.setBounds(10, 152, 240, 30);
		content.add(label);
		content.add(field);
		return field;
	}

	private JTextField createFieldUsername(JPanel content) {
		JLabel label = new JLabel("Username:");
		JTextField field = new JTextField("api-demo@belladati.com");
		label.setBounds(15, 190, 200, 30);
		field.setBounds(10, 212, 240, 30);
		content.add(label);
		content.add(field);
		return field;
	}

	private JTextField createFieldPassword(JPanel content) {
		JLabel label = new JLabel("Password:");
		JPasswordField field = new JPasswordField("apiDemo1");
		label.setBounds(15, 250, 200, 30);
		field.setBounds(10, 272, 240, 30);
		content.add(label);
		content.add(field);
		return field;
	}

	private JButton createLoginButton() {
		JButton button = new JButton("Login");
		button.setBounds(50, 325, 160, 30);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				// remove old views and reports
				buttonLogin.setEnabled(false);
				buttonLogout.setEnabled(true);
				listReports.clear();
				listViews.clear();

				try {
					// login user
					loader = new SdkLoader(fieldServiceUrl.getText(), fieldConsumerKey.getText(), fieldConsumerSecret.getText(),
						fieldUsername.getText(), fieldPassword.getText());

					// list new reports available for looged-in user
					for (Report report : loader.getReports()) {
						listReports.addElement(report);
					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(ViewSelector.this, "Cannot login to SDK: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
					buttonLogin.setEnabled(true);
					buttonLogout.setEnabled(false);
				}
			}
		});
		return button;
	}

	public SdkLoader getLoader() {
		return loader;
	}

	private JButton createLogoutButton() {
		JButton button = new JButton("Logout");
		button.setBounds(50, 360, 160, 30);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				// remove old views and reports
				listReports.clear();
				listViews.clear();
				buttonLogin.setEnabled(true);
				buttonLogout.setEnabled(false);
			}
		});
		return button;
	}

	private MyJList<Report> createListReports(JPanel content) {
		JLabel label = new JLabel("Reports:");
		label.setBounds(260, 10, 170, 30);
		content.add(label);

		MyJList<Report> list = new MyJList<Report>();
		JScrollPane scrollList = new JScrollPane(list);
		scrollList.setBounds(260, 35, 260, 350);
		scrollList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		content.add(scrollList);

		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				// remove old views and list new ones associated with selected report
				listViews.clear();
				Report selected = listReports.getSelectedValue();
				if (selected != null) {
					for (View view : loader.getViews(selected)) {
						listViews.addElement(view);
					}
				}
			}
		});
		return list;
	}

	private MyJList<View> createListViews(JPanel content) {
		JLabel label = new JLabel("Views:");
		label.setBounds(535, 10, 165, 30);
		content.add(label);

		MyJList<View> list = new MyJList<View>();
		JScrollPane scrollList = new JScrollPane(list);
		scrollList.setBounds(535, 35, 255, 320);
		scrollList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		content.add(scrollList);

		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				// enabled/disable render button
				View selected = listViews.getSelectedValue();
				buttonRender.setEnabled(selected != null);
			}
		});
		return list;
	}

	private JButton createRenderButton() {
		JButton button = new JButton("Render view");
		button.setBounds(590, 360, 160, 30);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				// render chart on main window
				Report selectedReport = listReports.getSelectedValue();
				View selectedView = listViews.getSelectedValue();
				loader.renderChart(mainWindow, selectedReport, selectedView);
				ViewSelector.this.setVisible(false);
			}
		});
		return button;
	}

	/**
	 * Common JList for Reports and Views.
	 */
	private final class MyJList<T> extends JList<T> {
		private static final long serialVersionUID = -1316106939236596642L;

		public MyJList() {
			super(new DefaultListModel<T>());
			setCellRenderer(new ListCellRenderer<T>() {

				@Override
				public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected,
					boolean cellHasFocus) {
					// get label for Report or View
					String label = "";
					if (value != null) {
						if (value instanceof Report) {
							label = ((Report) value).getName();
						}
						if (value instanceof View) {
							label = ((View) value).getName();
						}
					}
					JLabel item = new JLabel(label);

					// provide look & feel for selection
					if (isSelected) {
						item.setBackground(Color.LIGHT_GRAY);
					} else {
						item.setBackground(list.getBackground());
					}
					item.setOpaque(true);
					return item;
				}
			});
			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}

		/**
		 * Removes all of the elements from this list.
		 */
		public final void clear() {
			((DefaultListModel<T>) getModel()).clear();
		}

		/**
		 * Adds the specified element to the end of this list.
		 */
		public final void addElement(T element) {
			((DefaultListModel<T>) getModel()).addElement(element);
		}

	}

}
