  package view.titan;

import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.ezware.dialog.task.TaskDialog;
import com.ezware.dialog.task.TaskDialog.StandardCommand;
import com.ezware.dialog.task.TaskDialog.StandardIcon;

public class UIHelper{
	/**
	 * 
	 * @param actionCmd
	 * @param al
	 * @param imgSrc
	 * @return
	 */
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
	
	static private TaskDialog buildDlg(String title, String text, String instruction, StandardIcon ico){
		TaskDialog dlg = new TaskDialog(null, title);
		dlg.setText(text);
		dlg.setIcon(ico);
		dlg.setInstruction(instruction);
		return dlg;
	}
	
	static public <T> T BuildInputDlg(String title, String text, String instruction){
		JTextField txtFld = new JTextField(10);
		TaskDialog dlg = buildDlg(title, text, instruction, StandardIcon.QUESTION);
		dlg.setCommands(StandardCommand.OK, StandardCommand.CANCEL);
		dlg.setFixedComponent(txtFld);
		
		if(dlg.show().equals(StandardCommand.OK)){
			return (T)txtFld.getText();
		}else{
			return null;
		}
	}
	
	static public void BuildWarnDlg(String title, String text, String instruction, StandardCommand... cmds){
		TaskDialog dlg = buildDlg(title, text, instruction, StandardIcon.WARNING);
		
		if(cmds == null){
			dlg.setCommands(StandardCommand.OK);
		}else{
			dlg.setCommands(cmds);
		}
		dlg.show();
	}
}