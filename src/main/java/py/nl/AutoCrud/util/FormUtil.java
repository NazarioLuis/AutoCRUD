package py.nl.AutoCrud.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class FormUtil {
	public static void clearForm(Container cont){
		Component[] comps = cont.getComponents();
		for (int i = 0; i < comps.length; i++) {
			clear(comps[i]);
		}
	}
	@SuppressWarnings("unchecked")
	private static void clear(Component comp) {
		String className = comp.getClass().getName();
		String nombre_super= comp.getClass().getSuperclass().getName();
		if (className.equals("javax.swing.JTextField")||nombre_super.equals("javax.swing.JTextField")) {
			((javax.swing.JTextField) comp).setText("");
		} else if(className.equals("py.nl.AutoCrud.components.SearchTextField")) {
			((py.nl.AutoCrud.components.SearchTextField<Object>) comp).setValue(null);
		} else if (className.equals("javax.swing.JComboBox")||nombre_super.equals("javax.swing.JComboBox")) {
			((javax.swing.JComboBox<?>) comp).setSelectedIndex(0);
		} else if (className.equals("javax.swing.JTextArea")||nombre_super.equals("javax.swing.JTextArea")) {
			((javax.swing.JTextArea) comp).setText("");
		} 
		else if (className.equals("javax.swing.JPasswordField")||nombre_super.equals("javax.swing.JPasswordField")) {
			((javax.swing.JPasswordField) comp).setText("");
		}
		else if (className.equals("javax.swing.JFormattedTextField")||nombre_super.equals("javax.swing.JFormattedTextField")) {
			((javax.swing.JFormattedTextField) comp).setValue(null);;
		}
		else if (className.equals("javax.swing.JCheckBox")||nombre_super.equals("javax.swing.JCheckBox")) {
			((javax.swing.JCheckBox) comp).setSelected(false);
		}
		
	}
	
	public static void manageFocus(Container cont){
		Component[] comps = cont.getComponents();
		for (int a = 0; a < comps.length; a++) {
			if (!(comps[a] instanceof javax.swing.JLabel)) {
				JComponent component = (JComponent) comps[a];
				component.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							e.consume();
						}
					}
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							JComponent component = (JComponent) e.getSource();
							component.transferFocus();
						}
					}
				});
			}
		}
	}
	
	public static void enableInputs(Container cont,Boolean b) {
		Component[] comps = cont.getComponents();
		for (int a = 0; a < comps.length; a++) {
			if(JScrollPane.class==comps[a].getClass()) 
				((JScrollPane) comps[a]).getViewport().getView().setEnabled(b);
			else
				comps[a].setEnabled(b);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static boolean isEmpty(Component comp) {
		String className = comp.getClass().getName();
		String nombre_super = comp.getClass().getSuperclass().getName();
		if (className.equals("javax.swing.JTextField")||nombre_super.equals("javax.swing.JTextField")) {
			return ((javax.swing.JTextField) comp).getText().isEmpty();
		} else if (className.equals("javax.swing.JTextArea")||nombre_super.equals("javax.swing.JTextArea")) {
			return ((javax.swing.JTextArea) comp).getText().isEmpty();
		} 
		else if (className.equals("javax.swing.JPasswordField")||nombre_super.equals("javax.swing.JPasswordField")) {
			return new String(((javax.swing.JPasswordField) comp).getPassword()).isEmpty();
		}
		else if (className.equals("javax.swing.JFormattedTextField")||nombre_super.equals("javax.swing.JFormattedTextField")) {
			return ((javax.swing.JFormattedTextField) comp).getValue() == null;
		}else if (className.equals("py.nl.AutoCrud.components.SearchTextField")) {
			return ((py.nl.AutoCrud.components.SearchTextField<Object>) comp).getValue() == null;
		}
		return false;
	}

}
