package py.nl.AutoCrud.util;

import java.awt.Component;

import javax.swing.JOptionPane;

public class MessageUtil {

	private static String msg;
	private static int type;
	private static String title;
	private static Component source;

	public static void error(String msg,Component source) {
		MessageUtil.msg=msg;
		MessageUtil.source=source;
		MessageUtil.title="ERROR";
		MessageUtil.type=JOptionPane.ERROR_MESSAGE;
		show();
	}
	
	public static void warning(String msg,Component source) {
		MessageUtil.msg=msg;
		MessageUtil.source=source;
		MessageUtil.title="warning";
		MessageUtil.type=JOptionPane.WARNING_MESSAGE;
		show();
	}
	
	public static void success(String msg,Component source) {
		MessageUtil.msg=msg;
		MessageUtil.source=source;
		MessageUtil.title="EXITO";
		MessageUtil.type=JOptionPane.INFORMATION_MESSAGE;
		show();
	}
	
	public static int confirm(String msg,Component source) {
		return JOptionPane.showConfirmDialog(source, 
				msg,
				"Warning", JOptionPane.YES_NO_OPTION);
	}

	private static void show() {
		JOptionPane.showMessageDialog(source, msg, title, type);
	}

}
