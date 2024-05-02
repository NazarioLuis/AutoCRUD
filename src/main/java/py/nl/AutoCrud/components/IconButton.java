package py.nl.AutoCrud.components;

import java.awt.Dimension;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class IconButton extends JButton {
	public IconButton(String str) {
		setMaximumSize(new Dimension(100,80));
		setPreferredSize(new Dimension(100,80));
		setIconName(str);
	}
	@Override
	public void setText(String text) {
		setIconName(text);
	}
	
	public void setIconName(String name) {
		try {
			URL url = getClass().getClassLoader().getResource("img/"+name.toLowerCase()+".png");
			ImageIcon icon = new ImageIcon(url);
			setIcon(icon);
		} catch (Exception e) {
			super.setText(name);
		}
	}

}
