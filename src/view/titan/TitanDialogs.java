package view.titan;

import javax.swing.JTextField;

import com.ezware.dialog.task.TaskDialog;
import com.ezware.dialog.task.TaskDialog.StandardCommand;
import com.ezware.dialog.task.TaskDialog.StandardIcon;

public class TitanDialogs{
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
