package py.nl.AutoCrud.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import py.nl.AutoCrud.dao.AutoDAO;
import py.nl.AutoCrud.view.GenericTableModel;
import py.nl.AutoCrud.view.SearchView;

public class SearchController<T> implements ActionListener, MouseListener{

	private SearchView<T> searchView;
	private AutoDAO<T> dao;
	private GenericTableModel<T> tableModel;
	private List<Field> filtrableFields;
	private List<T> list;
	private SearchViewListener listener;
	public void setListener(SearchViewListener listener) {
		this.listener = listener;
	}

	@SuppressWarnings("unchecked")
	public SearchController(SearchView<T> searchView) {
		this.searchView = searchView;
		this.dao = new AutoDAO<T>(searchView.getEntityClass());
		this.tableModel = (GenericTableModel<T>) searchView.getTable().getModel();
		filtrableFields = getFiltrableFields();
		filter();
		
		addEvents();
	}
	
	private void addEvents() {
		searchView.getSearchField().addActionListener(this);
		searchView.getTable().addMouseListener(this);
	}

	private List<Field> getFiltrableFields() {
		List<Field> fields = searchView.getFields();
		List<Field> filtrableFields = new ArrayList<>();
		for (int i = 0; i < fields.size(); i++) {
			if(fields.get(i).getType() == String.class) filtrableFields.add(fields.get(i));
		}
		return filtrableFields;
	}
	
	private void filter() {
		list = dao.filter(searchView.getSearchField().getText(),filtrableFields);
		tableModel.setLista(list);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == searchView.getSearchField()) {
			filter();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == searchView.getTable() && e.getClickCount() == 2) {
			if (searchView.getTable().getSelectedRow()<0) return;
			listener.onSelect(list.get(searchView.getTable().getSelectedRow()));
			searchView.dispose();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
