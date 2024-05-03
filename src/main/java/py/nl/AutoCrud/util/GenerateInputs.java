package py.nl.AutoCrud.util;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import py.nl.AutoCrud.annotations.HiddenInput;
import py.nl.AutoCrud.annotations.Input;
import py.nl.AutoCrud.annotations.Relationship;
import py.nl.AutoCrud.annotations.RequiredInput;
import py.nl.AutoCrud.components.SearchTextField;
import py.nl.AutoCrud.components.TextPrompt;
import py.nl.AutoCrud.enums.Components;
import py.nl.AutoCrud.view.AutoCRUD;
import py.nl.AutoCrud.view.GenericTableModel;

public class GenerateInputs<T> {

	private AutoCRUD<T> crud;
	private List<Field> fields;
	private GridBagLayout gbl_form;
	private GenericTableModel<T> tableModel;

	public GenerateInputs(AutoCRUD<T> crud) {
		this.crud = crud;
		fields = crud.getFields();
	}

	@SuppressWarnings("unchecked")
	public void build() {
		String search = "";
		int row = 0;
		int column = 0;
		int columnCount = crud.getColumnCount();
		for (Field field : fields) {
			Input inputAnnotation = field.getAnnotation(Input.class);
			HiddenInput hiddenInput = field.getAnnotation(HiddenInput.class);
			RequiredInput requiredInput = field.getAnnotation(RequiredInput.class);
			Relationship relationship = field.getAnnotation(Relationship.class);

			if (!field.getName().equals("id") && hiddenInput == null) {
				Class<?> oComp = null;

				if (inputAnnotation != null && inputAnnotation.data().length > 1) {
					oComp = JComboBox.class;
				} else if (inputAnnotation != null && inputAnnotation.longText()) {
					if (field.getType() == String.class)
						oComp = JTextArea.class;
					else {
						System.err.println(
								"The 'longText' prop can only be used with a String. Please verify the entities.");
					}
				} else if (relationship != null) {
					oComp = SearchTextField.class;
				} else {
					oComp = Components.valueOf(WraperUtil.wrap(field.getType()).getSimpleName()).getInputType();
				}

				JLabel label = new JLabel();
				label.setBorder(new EmptyBorder(3, 5, 3, 3));
				label.setHorizontalAlignment(SwingConstants.RIGHT);

				if (inputAnnotation == null || inputAnnotation.label().isEmpty()) {
					label.setText(TextUtil.generateLabel(field.getName()));
				} else {
					label.setText(inputAnnotation.label().toUpperCase());
				}

				if (search.isEmpty() && field.getType() == String.class)
					search = label.getText();
				else if (field.getType() == String.class)
					search += ", " + label.getText();

				if (requiredInput != null) {
					label.setText(label.getText() + "(*)");
				}

				JComponent campo = null;
				if (oComp == SearchTextField.class) {
					campo = new SearchTextField<>(field);
					campo.setName(field.getName());
				} else {
					try {
						campo = (JComponent) oComp.getDeclaredConstructor().newInstance();
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
						return;
					}
					campo.setName(field.getName());

					if (oComp == JTextArea.class) {
						row++;
						column = 0;
						((JTextArea) campo).setRows(3);
						((JTextArea) campo).setLineWrap(true);
						JScrollPane sc = new JScrollPane(campo);
						campo = sc;
					} else if (oComp == JComboBox.class) {
						for (int i = 0; i < inputAnnotation.data().length; i++) {
							((JComboBox<Object>) campo).addItem(inputAnnotation.data()[i]);
						}
					}
				}

				addInput(row, column, 0, 1, label);
				column++;
				int ancho = (inputAnnotation != null && inputAnnotation.longText()) ? columnCount - 1 : 1;

				addInput(row, column, 1, ancho, campo);
				column += ancho;

				if (column == columnCount) {
					row++;
					column = 0;
				}
			}
		}

		tableModel = new GenericTableModel<T>(fields);
		crud.getTable().setModel(tableModel);

		addSearchFieldPlaceholder(search);
	}

	private void addSearchFieldPlaceholder(String text) {
		TextPrompt placeholder = new TextPrompt(text, crud.getSearchField());
		placeholder.changeAlpha(0.75f);
		placeholder.changeStyle(Font.ITALIC);
	}

	public void addInput(int row, int column, double weight, int columnCount, Component component) {
		if (gbl_form == null) {
			gbl_form = new GridBagLayout();
			this.crud.getForm().setLayout(gbl_form);
		}

		double[] rw = new double[row + 1];
		for (int i = 0; i <= row; i++) {
			rw[i] = (i < row) ? 0 : Double.MIN_VALUE;
		}
		gbl_form.rowWeights = rw;
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = column;
		gbc.gridy = row;
		gbc.gridwidth = columnCount;
		gbc.gridheight = 1;
		gbc.weightx = weight;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl_form.setConstraints(component, gbc);
		crud.getForm().add(component);
	}

}