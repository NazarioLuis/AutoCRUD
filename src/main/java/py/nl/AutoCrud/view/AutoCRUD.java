package py.nl.AutoCrud.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import py.nl.AutoCrud.annotations.EntityCRUD;
import py.nl.AutoCrud.components.IconButton;
import py.nl.AutoCrud.controller.AutoController;
import py.nl.AutoCrud.util.GenerateInputs;

public class AutoCRUD<T> extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private IconButton btnRemove;
	private IconButton btnEdit;
	private IconButton btnAdd;
	private IconButton btnSave;
	private IconButton btnCancel;
	private JPanel form;
	private JPanel toolbarB;
	private JPanel toolbarA;
	private JTextField searchField;
	private Class<T> entityClass;
	private EntityCRUD entityCRUD;
	private AutoController<T> controller;
	private List<Field> fields;

	public AutoCRUD(Class<T> entityClass) {
		this.entityClass = entityClass;
		entityCRUD = entityClass.getAnnotation(EntityCRUD.class);
		fields = getAllFields(new ArrayList<Field>(), entityClass);
		buildView();
		addController(this);
	}

	private void buildView() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int ancho = (int) Math.round(screenSize.getWidth() * ((double) entityCRUD.width() / 100));
		int alto = (int) Math.round(screenSize.getHeight() * ((double) entityCRUD.height() / 100));
		setBounds(100, 100, ancho, alto);

		setTitle(entityCRUD.title());

		setLocationRelativeTo(this);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 10, 400, 0, 400, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 10, 0, 0, 0, 5, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 3;
		gbc_scrollPane.gridy = 1;
		getContentPane().add(scrollPane, gbc_scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);

		toolbarA = new JPanel();
		GridBagConstraints gbc_toolbarA = new GridBagConstraints();
		gbc_toolbarA.insets = new Insets(0, 0, 5, 5);
		gbc_toolbarA.fill = GridBagConstraints.BOTH;
		gbc_toolbarA.gridx = 2;
		gbc_toolbarA.gridy = 1;
		getContentPane().add(toolbarA, gbc_toolbarA);
		toolbarA.setLayout(new BoxLayout(toolbarA, BoxLayout.PAGE_AXIS));

		btnAdd = new IconButton("Add");
		btnAdd.setBackground(new Color(234, 255, 213));
		toolbarA.add(btnAdd);
		
		JLabel sep1 = new JLabel("");
		sep1.setMaximumSize(new Dimension(0, 10));
		toolbarA.add(sep1);

		btnEdit = new IconButton("Edit");
		btnEdit.setBackground(new Color(198, 227, 227));
		toolbarA.add(btnEdit);
		
		JLabel sep2 = new JLabel("");
		sep2.setMaximumSize(new Dimension(0, 10));
		toolbarA.add(sep2);

		btnRemove = new IconButton("Remove");
		btnRemove.setBackground(new Color(255, 100, 100));
		toolbarA.add(btnRemove);

		form = new JPanel();
		form.setBorder(
				new TitledBorder(null, entityCRUD.formTitle(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		form.setLayout(null);
		GridBagConstraints gbc_form = new GridBagConstraints();
		gbc_form.insets = new Insets(0, 0, 5, 5);
		gbc_form.fill = GridBagConstraints.BOTH;
		gbc_form.gridx = 1;
		gbc_form.gridy = 1;
		getContentPane().add(form, gbc_form);

		toolbarB = new JPanel();
		GridBagConstraints gbc_toolbarB = new GridBagConstraints();
		gbc_toolbarB.fill = GridBagConstraints.VERTICAL;
		gbc_toolbarB.insets = new Insets(0, 0, 5, 5);
		gbc_toolbarB.gridx = 1;
		gbc_toolbarB.gridy = 2;
		getContentPane().add(toolbarB, gbc_toolbarB);
		toolbarB.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

		btnSave = new IconButton("Save");
		btnSave.setBackground(new Color(234, 255, 213));
		toolbarB.add(btnSave);

		btnCancel = new IconButton("Cancel");
		btnCancel.setBackground(new Color(224, 224, 224));
		toolbarB.add(btnCancel);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.NORTH;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 3;
		gbc_panel.gridy = 2;
		getContentPane().add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel lblSearch = new JLabel("");
		lblSearch.setIcon(new ImageIcon(AutoCRUD.class.getResource("/img/search.png")));
		panel.add(lblSearch);
		
				searchField = new JTextField();
				panel.add(searchField);
				searchField.setColumns(10);

		new GenerateInputs<T>(this).build();
	}

	private List<Field> getAllFields(List<Field> fields, Class<?> type) {
		if (type.getSuperclass() != null) {
			getAllFields(fields, type.getSuperclass());
		}

		fields.addAll(Arrays.asList(type.getDeclaredFields()));
		return fields;
	}

	private void addController(AutoCRUD<T> autoCRUD) {
		controller = new AutoController<T>(this);
	}

	public AutoController<T> getController() {
		return controller;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public int getColumnCount() {
		return entityCRUD.columnCount() * 2;
	}

	public JTable getTable() {
		return table;
	}

	public JButton getBtnRemove() {
		return btnRemove;
	}

	public JButton getBtnEdit() {
		return btnEdit;
	}

	public JButton getBtnAdd() {
		return btnAdd;
	}

	public JButton getBtnSave() {
		return btnSave;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	public JPanel getForm() {
		return form;
	}

	public JPanel getToolbarB() {
		return toolbarB;
	}

	public JPanel getToolbarA() {
		return toolbarA;
	}

	public JTextField getSearchField() {
		return searchField;
	}

	public List<Field> getFields() {
		return fields;
	}

	public Field getField(String name) {
		for (int i = 0; i < fields.size(); i++) {
			if (name.equals(fields.get(i).getName()))
				return fields.get(i);
		}
		return null;
	}
}
