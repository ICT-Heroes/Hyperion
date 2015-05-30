package view.titan;

import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class TitanUtil{
	public static JButton buildImgButton(String actionCmd, ActionListener al, URL imgSrc){
		JButton btn = new JButton();
		btn.setActionCommand(actionCmd);
		btn.addActionListener(al);
		btn.setIcon(new ImageIcon(imgSrc));
		return btn;
	}
	
	public static JMenu buildMenu(String dispTxt, char mnemonic){
		JMenu mnu = new JMenu(dispTxt);
		mnu.setMnemonic(mnemonic);
		return mnu;
	}
	
	public static JMenuItem buildMenuItem(String dispTxt, ActionListener al, int keycode, int modifier){
		JMenuItem itm = new JMenuItem(dispTxt);
		itm.addActionListener(al);
		itm.setAccelerator(KeyStroke.getKeyStroke(keycode, modifier));
		return itm;
	}
	
	public static JMenuItem buildMenuItem(String dispTxt, ActionListener al){
		JMenuItem itm = new JMenuItem(dispTxt);
		itm.addActionListener(al);
		return itm;
	}
}