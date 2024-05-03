package py.nl.AutoCrud.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import py.nl.AutoCrud.controller.SearchController;
import py.nl.AutoCrud.controller.SearchViewListener;

public class SearchView <T> extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField searchField;
	private JTable table;
	private List<Field> fields;
	private GenericTableModel<T> tableModel;
	private Class<T> entityClass;
	private SearchController<T> controller;
	
	public void setListener(SearchViewListener listener) {
		controller.setListener(listener);
	}
	
	public SearchView(Class<T> entityClass) {
		this.entityClass = entityClass;
		fields = getAllFields(new ArrayList<Field>(), entityClass);
		buildView();
		addController();
	}

	public void buildView() {
		setModal(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int ancho = (int) Math.round(screenSize.getWidth() * 0.5);
		int alto = (int) Math.round(screenSize.getHeight() * 0.5);
		setBounds(100, 100, ancho, alto);
		setLocationRelativeTo(this);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel lblSearch = new JLabel("");
		lblSearch.setIcon(new ImageIcon(AutoCRUD.class.getResource("/img/search.png")));
		panel.add(lblSearch);
		
		searchField = new JTextField();
		panel.add(searchField);
		searchField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		tableModel = new GenericTableModel<T>(fields);
		table.setModel(tableModel);

	}

	public JTextField getSearchField() {
		return searchField;
	}

	public JTable getTable() {
		return table;
	}
	
	private List<Field> getAllFields(List<Field> fields, Class<?> type) {
		if (type.getSuperclass() != null) {
			getAllFields(fields, type.getSuperclass());
		}

		fields.addAll(Arrays.asList(type.getDeclaredFields()));
		return fields;
	}

	private void addController() {
		controller = new SearchController<T>(this);
	}

	public SearchController<T> getController() {
		return controller;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public List<Field> getFields() {
		return fields;
	}
}
