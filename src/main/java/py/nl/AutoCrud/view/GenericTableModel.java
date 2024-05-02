package py.nl.AutoCrud.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import py.nl.AutoCrud.annotations.Input;
import py.nl.AutoCrud.util.GetterAndSetterUtil;
import py.nl.AutoCrud.util.TextUtil;
import py.nl.AutoCrud.util.WraperUtil;


public class GenericTableModel<T> extends AbstractTableModel{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> ColumnNames = new ArrayList<>();
	
	private List<T> list = new ArrayList<>();

	private List<Field> fields = new ArrayList<>();
	
	
	public GenericTableModel(List<Field> fields) {
		for (Field field:fields) {
			Input inputAnnotation = field.getAnnotation(Input.class);
			boolean isColumn = (inputAnnotation!=null&&inputAnnotation.tableColumn());
			if(isColumn){
				this.fields.add(field);
				if(inputAnnotation!=null && !inputAnnotation.label().isEmpty()){
					this.ColumnNames.add(inputAnnotation.label().toUpperCase());
				} else {
					this.ColumnNames.add(TextUtil.generateLabel(field.getName()));
				}
			}
		}
	}
	
	public void setLista(List<T> lista) {
		this.list = lista;
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int i) {
		return ColumnNames.get(i);
	}
	
	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public int getColumnCount() {
		return ColumnNames.size();
	}
	
	@Override
	public Object getValueAt(int r, int c) {
		return getValue(r,c);
	}

	private Object getValue(int r, int c) {
		try {
			return GetterAndSetterUtil.callGetter(list.get(r), fields.get(c));
		} catch (Exception e) {
			e.printStackTrace();
			return new Object();
		}
	}

	@Override
	public Class<?> getColumnClass(int c) {
		try {
			return WraperUtil.wrap(fields.get(c).getType());
		} catch (Exception e) {
			return Object.class;
		}
	}
	
}
