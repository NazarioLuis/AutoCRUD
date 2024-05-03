package py.nl.AutoCrud.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.hibernate.exception.ConstraintViolationException;

import py.nl.AutoCrud.annotations.Input;
import py.nl.AutoCrud.annotations.RequiredInput;
import py.nl.AutoCrud.components.SearchTextField;
import py.nl.AutoCrud.dao.AutoDAO;
import py.nl.AutoCrud.enums.Components;
import py.nl.AutoCrud.util.FormUtil;
import py.nl.AutoCrud.util.GetterAndSetterUtil;
import py.nl.AutoCrud.util.MessageUtil;
import py.nl.AutoCrud.util.TextUtil;
import py.nl.AutoCrud.util.WraperUtil;
import py.nl.AutoCrud.view.AutoCRUD;
import py.nl.AutoCrud.view.GenericTableModel;


public class AutoController <T> implements ActionListener, ListSelectionListener {
	private AutoCRUD<T> crud;
	private T obj;
	private List<Component> inputs;
	private AutoDAO<T> dao;
	private List<T> list;
	private GenericTableModel<T> tableModel;
	private List<Field> filtrableFields;

	
	@SuppressWarnings("unchecked")
	public AutoController(AutoCRUD<T> crud) {
		this.crud = crud;
		this.inputs = getInputsOnly(crud.getForm().getComponents());
		this.dao = new AutoDAO<T>(crud.getEntityClass());
		this.tableModel = (GenericTableModel<T>) crud.getTable().getModel();
		filtrableFields = getFiltrableFields();

		addEvents();
		initState(true);
		filter();
	}

	
	private List<Component> getInputsOnly(Component[] componentes) {
		List<Component> inputsOnly = new ArrayList<>();
		for (int i = 0; i < componentes.length; i++) {
			if (componentes[i].getClass() == JScrollPane.class) {
				inputsOnly.add(((JScrollPane) componentes[i]).getViewport().getView());
			} else if (componentes[i].getClass() != JLabel.class) {
				inputsOnly.add(componentes[i]);
			}
		}
		return inputsOnly;
	}

	private void initState(boolean b) {
		FormUtil.enableInputs(crud.getForm(), !b);
		FormUtil.enableInputs(crud.getToolbarA(), b);
		FormUtil.enableInputs(crud.getToolbarB(), !b);
		
	}

	
	private void addEvents() {
		FormUtil.manageFocus(crud.getForm());
		crud.getBtnAdd().addActionListener(this);
		crud.getBtnEdit().addActionListener(this);
		crud.getBtnRemove().addActionListener(this);
		crud.getBtnSave().addActionListener(this);
		crud.getBtnCancel().addActionListener(this);
		crud.getSearchField().addActionListener(this);
		crud.getTable().getSelectionModel().addListSelectionListener(this);
	}

	private void add() {
		clear();
		initState(false);
		try {
			fillForm(crud.getEntityClass().getDeclaredConstructor().newInstance());
		} catch (Exception e) {
		}
	}

	private void edit() {
		initState(false);
	}

	private void remove() {
		obj = list.get(crud.getTable().getSelectedRow());
		
		int respuesta = MessageUtil.confirm("Are you sure you want to delete the selected item?", crud);
		if (respuesta == JOptionPane.YES_OPTION) {
			try {
				dao.remove(obj);
				filter();
				clear();
			} catch (Exception e) {
				MessageUtil.error("The item cannot be deleted; it may be in use.", crud);
			}
		}
	}

	private void save() {
		
		try {
			obj = fillData(obj);
			System.out.println(obj.toString());
		} catch (Exception e1) {
			if(e1.getClass()!=RuntimeException.class) e1.printStackTrace();
			return;
		}
		
		try {
			dao.save(obj);
			filter();
			clear();
		} catch (Exception e) {
			e.printStackTrace();
			Throwable exc = e.getCause();
			if (exc.getClass() == ConstraintViolationException.class) {
				MessageUtil.error(exc.getCause().getMessage(), crud);
			}else{
				MessageUtil.error("An error occurred while trying to save", crud);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private T fillData(T obj) throws Exception {
		for (int i = 0; i < inputs.size(); i++) {
			Field field = crud.getField(inputs.get(i).getName());
			Input inputAnnotation = field.getAnnotation(Input.class);
			RequiredInput requiredInput = field.getAnnotation(RequiredInput.class);
			
			
			String label;
			if(inputAnnotation==null || inputAnnotation.label().isEmpty()){
				label = TextUtil.generateLabel(field.getName());
			}else {
				label = inputAnnotation.label().toUpperCase();
			}
			
			if (requiredInput!=null && FormUtil.isEmpty(inputs.get(i))) {
				MessageUtil.error(label+" is required", crud);
				throw new RuntimeException();
			}
			
			Object value = null;
			if (inputs.get(i).getClass()==JComboBox.class) {
				value = ((JComboBox<Object>)inputs.get(i)).getSelectedItem();
			}else if (inputs.get(i).getClass()==SearchTextField.class){
				value = ((SearchTextField<Object>)inputs.get(i)).getValue();
			}else {
				Components oComp = Components.valueOf(WraperUtil.wrap(field.getType()).getSimpleName());
				value = oComp.getValue(inputs.get(i));
			}
			
			GetterAndSetterUtil.callSetter(obj, field, value);
			
		}
		return obj;
	}

	
	private void clear() {
		initState(true);
		FormUtil.clearForm(crud.getForm());
	}

	@SuppressWarnings("unchecked")
	private void fillForm(T obj) throws Exception {
		this.obj = obj;
		
		for (int i = 0; i < inputs.size(); i++) {
			Field field = crud.getField(inputs.get(i).getName());
			
			Object valor = GetterAndSetterUtil.callGetter(obj, field);
			
			if (inputs.get(i).getClass()==JComboBox.class) {
				((JComboBox<Object>)inputs.get(i)).setSelectedItem(valor);
			}else if (inputs.get(i).getClass()==SearchTextField.class){
				((SearchTextField<Object>)inputs.get(i)).setValue(valor);
			} else{
				Components oComp = Components.valueOf(WraperUtil.wrap(field.getType()).getSimpleName());
				oComp.setValue(inputs.get(i),valor);
			}
		}
	}
	
	private void filter() {
		list = dao.filter(crud.getSearchField().getText(),filtrableFields);
		tableModel.setLista(list);
	}
	
	
	private List<Field> getFiltrableFields() {
		List<Field> fields = crud.getFields();
		List<Field> filtrableFields = new ArrayList<>();
		for (int i = 0; i < fields.size(); i++) {
			if(fields.get(i).getType() == String.class) filtrableFields.add(fields.get(i));
		}
		return filtrableFields;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == crud.getBtnAdd())
			add();
		if (e.getSource() == crud.getBtnEdit())
			edit();
		if (e.getSource() == crud.getBtnRemove())
			remove();
		if (e.getSource() == crud.getBtnSave())
			save();
		if (e.getSource() == crud.getBtnCancel())
			clear();
		if (e.getSource() == crud.getSearchField())
			filter();
	}

	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting() && crud.getTable().getSelectedRow() >= 0) {
			try {
				fillForm(list.get(crud.getTable().getSelectedRow()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
