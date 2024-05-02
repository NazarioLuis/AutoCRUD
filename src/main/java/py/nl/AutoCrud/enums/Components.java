package py.nl.AutoCrud.enums;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import py.nl.AutoCrud.components.DateTextField;
import py.nl.AutoCrud.components.DecimalTextField;
import py.nl.AutoCrud.components.IntTextField;

public enum Components {
	String(JTextField.class) {
		@Override
		public Object getValue() {
			return ((JTextComponent) field).getText();
		}

		@Override
		public void setValue() {
			((JTextComponent) field).setText((java.lang.String) value);
		}
	},
	Boolean(JCheckBox.class) {
		@Override
		public Object getValue() {
			return ((JCheckBox) field).isSelected();
		}

		@Override
		public void setValue() {
			((JCheckBox) field).setSelected((boolean) value);
		}
	},
	Double(DecimalTextField.class) {
		@Override
		public Object getValue() {
			return ((DecimalTextField) field).getDoubleValue();
		}

		@Override
		public void setValue() {
			((DecimalTextField) field).setValue((double) value);
			
		}
	},
	Float(DecimalTextField.class) {
		@Override
		public Object getValue() {
			return ((DecimalTextField) field).getFloatValue();
		}

		@Override
		public void setValue() {
			((DecimalTextField) field).setValue((float) value);
		}
	},
	Integer(IntTextField.class) {
		@Override
		public Object getValue() {
			return ((IntTextField) field).getIntValue();
		}

		@Override
		public void setValue() {
			((IntTextField) field).setValue((int) value);
		}
	},
	Long(IntTextField.class) {
		@Override
		public Object getValue() {
			return ((IntTextField) field).getLongValue();
		}

		@Override
		public void setValue() {
			((IntTextField) field).setValue((long) value);
		}
	},
	Date(DateTextField.class) {
		@Override
		public Object getValue() {
			return ((DateTextField) field).getDate();
		}

		@Override
		public void setValue() {
			((DateTextField) field).setDate((java.util.Date) value);
		}
	};
	
	private final Class<?> inputType;
	protected Component field;
	protected Object value;

	private Components(Class<?> inputType) {
        this.inputType = inputType;
    }
    
    public Class<?> getInputType() {
        return this.inputType;
    }
    
    public abstract Object getValue();
    
    public Object getValue(Component campo){
    	this.field = campo;
    	return getValue();
    }
    
    public abstract void setValue();

	public void setValue(Component comp, Object valor) {
		if(valor!=null) {
			this.field = comp;
			this.value = valor;
			setValue();;
		}
	}
}
