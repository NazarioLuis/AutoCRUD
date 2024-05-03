package py.nl.AutoCrud.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import py.nl.AutoCrud.annotations.Relationship;
import py.nl.AutoCrud.interfaces.SearchViewListener;
import py.nl.AutoCrud.util.GetterAndSetterUtil;
import py.nl.AutoCrud.view.SearchView;

@SuppressWarnings("serial")
public class SearchTextField<T> extends JPanel implements SearchViewListener {
	private JTextField textField;
	private SearchView<T> seachView;
	private JButton btnOpenSearchView;
	private String dText;
	private Class<T> clazz;
	private T obj;

	@SuppressWarnings("unchecked")
	public SearchTextField(Field field) {
		this.clazz = (Class<T>) (field == null ? Object.class : field.getType());
		if (field != null) {
			Relationship relationship = field.getAnnotation(Relationship.class);
			dText = relationship.displayInForm();
		}
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		textField = new JTextField();
		add(textField);
		textField.setColumns(10);

		btnOpenSearchView = new JButton("");
		btnOpenSearchView.setIcon(new ImageIcon(SearchTextField.class.getResource("/img/search_16.png")));
		btnOpenSearchView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openSearchView();
			}
		});
		add(btnOpenSearchView);
	}

	private void openSearchView() {
		if (seachView == null) {
			seachView = new SearchView<T>(clazz);
			seachView.setListener(this);
		}
		seachView.setVisible(true);
	}

	public SearchView<T> getSeachView() {
		return seachView;
	}

	public void setValue(T entity) {
		obj = entity;
		try {
			textField.setText(getText(entity));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getText(T entity) throws Exception {
		String text = "";
		if (entity == null)
			return text;
		String[] words = dText.split("[^\\w:']+");
		for (String param : words) {
			if (param.startsWith(":")) {
				String fieldName = param.replaceAll(":", "");
				String value = (String) GetterAndSetterUtil.callGetter(entity, clazz.getDeclaredField(fieldName));
				text = dText.replaceAll(param, value);
			}
		}
		return text;
	}

	public T getValue() {
		return obj;
	}

	public void setEnabled(boolean e) {
		textField.setEnabled(e);
		btnOpenSearchView.setEnabled(e);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSelect(Object obj) {
		setValue((T) obj);
	}
}
