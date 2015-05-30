package view.titan;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Data;

import com.ezware.dialog.task.TaskDialogs;

import controller.DataController;

/*
 * @date	: 2015-05-21
 * @author	: seok
 * @version	: 1  
 * TITAN ���α׷��� ���� ������ ����
 * ���⿡ TreePane�� TablePane�� �����Ͽ� �⺻ ȭ���� �����Ѵ�.
 * Fork �Ǵ� Duplicate�Ǵ� ��� enableToolBar�޼��带 ȣ������ �ʴ� ������
 * �� ������� �� �����츦 ������ �� �ִ�.
 * 
 */

public class TitanWindow implements ActionListener{
	private TitanSplitContainer	view;
	private JMenuBar				mnuBar;
	private JToolBar				toolBar;
	private JFrame					frame;
	
	
	//����ڰ� ������ DSM������ ��Ÿ����. 
	//���� ������ ��� �Ǵ� �������� ���� ��쿡�� null
	private File		dsmFile = null;
	
	//����ڰ� ������ CLSX������ ��Ÿ����.
	//���� ������ ��� �Ǵ� �������� ���� ��쿡�� null
	private File		clsxFile = null;
	
	//���α׷� ���� ����, ���ο� ������ ���� ��쿡�� false, ������ ���� ����� true�� ����
	private boolean	isModified = false;
	
	private DataController dc;
	//������ ���ÿ� �ʱ�ȭ ����
	{
		init();
		initMenuBar();
		initToolBar();
	}
	
	public void setDataController(Data dsmData){
		//������ �ε� ���� �� ��Ʈ ����
		TitanTreeContainer tc = this.getTitanTreeContainer();
		tc.setRoot(dsmData);
		
		//�� �о��
		Object o = tc.getRoot();
		for(int i = 0; i < dsmData.ItemCount(); i++){
			tc.insertNode(o, dsmData.child.get(i));
		}
		
		//�о�� �����ͷ� ���̺� �߰�
		this.getTitanTableContainer().setColumnSize(dsmData.child.size());
		
		
		//
		for(int i = 0; i < dsmData.ItemCount(); i++){
			Vector<String> vc = new Vector<String>();
			
			for(int j = 0; j < dsmData.ItemCount(); j++){
				if(i == j)
					vc.add(".");
				else
					vc.add("");
			}
			
			Data d = dsmData.child.get(i);
			for(Data target : d.depend){
				int idx = tc.findNodeIndex(target);
				if(idx != -1)
					vc.set(idx, "x");
			}
			this.getTitanTableContainer().addNewRow(vc);
		}
		this.getTitanTableContainer().setRowHeaderTxt(tc.getItemText());
		this.getTitanTableContainer().setColumnSizePref();	
	}
	
	/*
	 * �����쿡 �޴��� ����
	 */
	public void attachMenuBar(){
		frame.setJMenuBar(mnuBar);
	}
	
	/*
	 * �����쿡 �޴��� �и�
	 */
	public void detachMenuBar(){
		frame.setJMenuBar(null);
	}
	
	/*
	 * �����쿡 ���� ����
	 */
	public void attachToolBar(){
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
	}
	
	/*
	 * �����쿡 ���� �и�
	 */
	public void detachToolBar(){
		frame.getContentPane().remove(toolBar);
	}
	
	/*
	 * ���� �ʱ�ȭ
	 */
	private void initToolBar(){
		toolBar = new JToolBar();
		
		JButton tmp;
		tmp = TitanUtil.buildImgButton("Open DSM", this, TitanWindow.class.getResource("/res/open-dsm.png"));
		tmp.setToolTipText("Open DSM");
		toolBar.add(tmp);
		
		tmp = TitanUtil.buildImgButton("Redraw", this, TitanWindow.class.getResource("/res/redraw.png"));
		tmp.setToolTipText("Redraw");
		toolBar.add(tmp);
		
		toolBar.addSeparator(new Dimension(2, 20));
		
		tmp = TitanUtil.buildImgButton("New Clustering", this, TitanWindow.class.getResource("/res/new-clsx.png"));
		tmp.setToolTipText("New Clustering");
		toolBar.add(tmp);
		
		tmp = TitanUtil.buildImgButton("Load Clustering", this, TitanWindow.class.getResource("/res/open-clsx.png"));
		tmp.setToolTipText("Load Clustering");
		toolBar.add(tmp);
		
		tmp = TitanUtil.buildImgButton("Save Clustering", this, TitanWindow.class.getResource("/res/save-clsx.png"));
		tmp.setToolTipText("Save Clustering");
		toolBar.add(tmp);
		
		tmp = TitanUtil.buildImgButton("Save Clustering As...", this, TitanWindow.class.getResource("/res/save-clsx-as.png"));
		tmp.setToolTipText("Save Clustering As...");
		toolBar.add(tmp);
	}
	/*
	 * �޴��� ���� �� �̺�Ʈ �ڵ鷯 ����
	 */
	private void initMenuBar(){
		mnuBar = new JMenuBar();
		
		JMenu mnuTmp;
		JMenuItem mntmTmp;
		
		mnuTmp = TitanUtil.buildMenu("File", 'F');
		mnuBar.add(mnuTmp);
		
		mntmTmp = TitanUtil.buildMenuItem("New DSM", this, KeyEvent.VK_N, InputEvent.CTRL_MASK);
		mnuTmp.add(mntmTmp);		
		mnuTmp.add(new JSeparator());
		
		mntmTmp = TitanUtil.buildMenuItem("Open DSM", this, KeyEvent.VK_O, InputEvent.CTRL_MASK);	
		mnuTmp.add(mntmTmp);
		mnuTmp.add(new JSeparator());
		
		mntmTmp = TitanUtil.buildMenuItem("New Clustering", this, KeyEvent.VK_N, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);
		mnuTmp.add(mntmTmp);
		
		mntmTmp = TitanUtil.buildMenuItem("Load Clustering", this, KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);
		mnuTmp.add(mntmTmp);
		mnuTmp.add(new JSeparator());		
		
		mntmTmp = TitanUtil.buildMenuItem("Save Clustering", this, KeyEvent.VK_S, InputEvent.CTRL_MASK);
		mnuTmp.add(mntmTmp);
		
		mntmTmp = TitanUtil.buildMenuItem("Save Clustering As...", this, KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK);
		mnuTmp.add(mntmTmp);
		mnuTmp.add(new JSeparator());
		
		mntmTmp = TitanUtil.buildMenuItem("Exit", this, KeyEvent.VK_F4, InputEvent.ALT_MASK);
		mnuTmp.add(mntmTmp);
		
		mnuTmp = TitanUtil.buildMenu("View", 'V');
		mnuBar.add(mnuTmp);
		
		mntmTmp = TitanUtil.buildMenuItem("Redraw", this, KeyEvent.VK_F5, 0);
		mnuTmp.add(mntmTmp);
		mnuTmp.add(new JSeparator());
		
		mntmTmp = TitanUtil.buildMenuItem("Show Row Lables", this, KeyEvent.VK_F6, 0);
		mnuTmp.add(mntmTmp);
		
		mntmTmp = TitanUtil.buildMenuItem("Change UI Theme", this);
		mnuTmp.add(mntmTmp);		
		
		mnuTmp = TitanUtil.buildMenu("Help", 'H');
		mnuBar.add(mnuTmp);		
		
		mntmTmp = TitanUtil.buildMenuItem("About", this);
		mnuTmp.add(mntmTmp);
	}
	
	/*
	 * ������ �ʱ�ȭ
	 */
	private void init(){
		frame = new JFrame();
		
		//Ÿ��Ʋ ����
		frame.setTitle("TITAN");
		
		//Ÿ��ź��� ����
		view = new TitanSplitContainer();
		frame.getContentPane().add(view.getContainer(), BorderLayout.CENTER);
		
		/*
		 * Look And Feel�� OS�� ���� ����
		 * Windows �迭 : Windows
		 * �� Windows �迭 : Nimbus
		 */
		if(System.getProperty("os.name").contains("Windows") == true){
			changeLaF(frame, "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}else{
			changeLaF(frame, "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		
		//�����ϰ� �ִ� ��� �����̳��� ũ�� �� ��ġ�� ������
		frame.pack();
		
		//���� ���õ� ������� �߾����� �̵�
		frame.setLocationRelativeTo(null);
		
		//�ݱ� ��ư ������ VM���� ����ǵ��� ����
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	/*
	 * ���ο� DSM�� ����
	 */
	public void uiMnuNewDSM(ActionEvent ae){
		//�ؽ�Ʈ �ʵ� ����
		final JTextField txtFld = new JTextField(10);
		
		//�������
		Object[] bodyAndTxtField = {"How many rows added?\n", txtFld};
		Object[] btnTxt = {"OK", "Cancel"};
		
		//�ɼ��г� ����
		final JOptionPane optionPane = new JOptionPane(
													bodyAndTxtField,
													JOptionPane.QUESTION_MESSAGE,
													JOptionPane.YES_NO_OPTION,
													null,
													btnTxt,
													btnTxt[0]
												);
		
		//���̾�α� ����
		final JDialog dialog = new JDialog(frame, "New DSM", true);
		dialog.setContentPane(optionPane);
		
		//���̾�αװ� �ڵ����� ������ �ʰ� ����
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		//���̾�αװ� ������ �ؽ�Ʈ�ʵ�� ��Ŀ���� �̵��ϵ��� �̺�Ʈ �ڵ鷯 ���� 
		dialog.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentShown(ComponentEvent ce){
				txtFld.requestFocusInWindow();
			}
		});
		
		//X��ư�� ������ �� ó���� ���� �̺�Ʈ �ڵ鷯 �߰� 
		dialog.addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent we){
		    	//X��ư�� �����ٸ� ����
		    	dialog.dispose();
		    }
		});
		
		//�Ӽ�(���� ��� ��ư�� ���ȴٴ���)�� ����� ��츦 Ȯ��
		optionPane.addPropertyChangeListener(
		    new PropertyChangeListener(){
		        public void propertyChange(PropertyChangeEvent pcEvt){
		            String prop = pcEvt.getPropertyName();
		
		            if (dialog.isVisible() 
		             && (pcEvt.getSource() == optionPane)
		             && (prop.equals(JOptionPane.VALUE_PROPERTY))){
		            	Object value = optionPane.getValue();
		            	
		            	if(value == JOptionPane.UNINITIALIZED_VALUE){
		            		return;
		            	}
		            	
		            	//�� ���� �������� ������ �������� ������ �̺�Ʈ�� �߻���Ű����
		            	//������Ƽ ���� �̺�Ʈ�� �߻����� �ʰ� �ȴ�.
		            	optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
		            	
		            	if("OK".equals(value)){
		            		boolean isOK = false;
		            		String typedText = txtFld.getText();
		            		int values = 0;
		            		
		            		try{
		            			values = Integer.parseInt(typedText);
		            			if(values < 0){
		            				throw new Exception("Out of Range");
		            			}
		            			isOK = true;
		            		}catch(Exception e){
		            			JOptionPane.showMessageDialog(null, "Value must be positive integer, try again.");
		            			txtFld.setText("");
		            		}
		            		if(isOK == true)
		            			dialog.dispose();
		            		
		            	}else if("Cancel".equals(value)){
		            		dialog.dispose();
		            	}
		            }
		        }
		    });
		//���̾�α� ��Ҹ� �����ϰ� ��ġ
		dialog.pack();
		
		//�θ� �������� �߾ӿ� ������ ����
		dialog.setLocationRelativeTo(frame);
		
		//���̾�α� ����
		dialog.setVisible(true);
	}
	
	/*
	 * DSM������ ��θ� ��ȯ
	 */
	private void uiMnuOpenDSM(ActionEvent ae){
		FileNameExtensionFilter flt = new FileNameExtensionFilter("DSM File(*.dsm)", "dsm");
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Open DSM");
		jfc.setFileFilter(flt);
		if(jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION){
			dsmFile = jfc.getSelectedFile();
		}else{
			dsmFile = null;
		}
	}
	
	/*
	 * ���ο� Ŭ�����͸� ����
	 */
	void uiMnuNewClustering(ActionEvent ae){
		JOptionPane.showMessageDialog(frame, "All grouped matrix are reverted...");
	}
	
	/*
	 * Ŭ�����͸� �ε�
	 */
	void uiMnuLoadClustering(ActionEvent ae){
		FileNameExtensionFilter flt = new FileNameExtensionFilter("Clustering File(*.clsx)", "clsx");
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Load Clustering");		
		jfc.setFileFilter(flt);
		if(jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION){
			clsxFile = jfc.getSelectedFile();
		}else{
			clsxFile = null;
		}
	}
	
	/*
	 * Ŭ�����͸� ����
	 */
	void uiMnuSaveClustering(ActionEvent ae){
		JOptionPane.showMessageDialog(frame, "Clustering saved...");
	}
	
	/*
	 * Ŭ�����͸��� �ٸ� �̸����� ����,
	 * �� �� getCLSXFile �޼ҵ�� ���ο� CLSX������ �� �� ����
	 */	
	void uiMnuSaveClusteringAs(ActionEvent ae){
		FileNameExtensionFilter flt = new FileNameExtensionFilter("Clustering File(*.clsx)", "clsx");
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Save Clustering File As...");
		jfc.setFileFilter(flt);
		if(jfc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){
			clsxFile = jfc.getSelectedFile();
		}else{
			clsxFile = null;
		}
	}
	
	/*
	 * ���α׷� ����
	 */	
	void uiMnuExit(ActionEvent ae){
		//������� ����
		isModified = true;
		if(isModified == true){
			//���� ���� Ȯ��
			int result = JOptionPane.showConfirmDialog(frame, "���� ������ �ֽ��ϴ�. �����Ͻðڽ��ϱ�?");
			
			if(result == JOptionPane.YES_OPTION){
				//save
			}else if(result == JOptionPane.CANCEL_OPTION){
				return ;
			}
		}
		
		//���α׷� ����
		System.exit(0);
	}
	
	/*
	 * �ٽ� �׸���
	 */	
	void uiMnuRedraw(ActionEvent ae){
		JOptionPane.showMessageDialog(frame, "redrawed...");
	}
	
	/*
	 * �� ���̺� ���̱� ���
	 */	
	void uiMnuShowRowLable(ActionEvent ae){
		JOptionPane.showMessageDialog(frame, "Toggled.");
	}
	
	/*
	 * ��ٿ� Team
	 */	
	void uiMnuAbout(ActionEvent ae){
		JOptionPane.showMessageDialog(frame, "EMPTY");
	}
	
	/*
	 * LAF�� ������ �� �ֵ��� ����
	 */
	private void uiMnuChangeLAF(ActionEvent ae){
		//UI�����ڷκ��� ���� JREȯ�濡 ��ġ�� �⺻ LaFŬ������ ����
		UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
		
		//LaF���� ���н� �ƹ��� ������ ���� �ʴ´�
		if(lafs == null || lafs.length == 0){
			return ;
		}
		
		//�޺��ڽ��� ǥ���� LAF�̸��� �����ϰ� �޺��ڽ��� �߰�
		String[] lafName = new String[lafs.length];
		for(int i = 0; i < lafs.length; i++){
			lafName[i] = lafs[i].getName();
		}
		
		//����ڰ� ������ LAF�� ����
		String selLaFName = (String)JOptionPane.showInputDialog(
								null, "Select System UI", "Change UI", 
								JOptionPane.PLAIN_MESSAGE, null,
								lafName, lafName[0]);
		
		//���õ� LaF�� UI�����ڿ��� �˸���
		for(UIManager.LookAndFeelInfo laf : lafs){
			if(laf.getName().equals(selLaFName) == true){
				changeLaF(frame, laf.getClassName());
			}
		}
	}
	/*
	 * ���� �� �޴��� ���õ� �̺�Ʈ�� ó���ϴ� �ڵ鷯
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent ae){
		//�׼�Ŀ�ǵ忡 ���� ������ �̺�Ʈ �ڵ鷯 ȣ��
		switch(ae.getActionCommand()){
		case "New DSM":
			uiMnuNewDSM(ae);
			break;
		case "Open DSM":
			uiMnuOpenDSM(ae);
			break;
		case "New Clustering":
			uiMnuNewClustering(ae);
			break;
		case "Load Clustering":
			uiMnuLoadClustering(ae);
			break;
		case "Save Clustering":
			uiMnuSaveClustering(ae);
			break;
		case "Save Clustering As...":
			uiMnuSaveClusteringAs(ae);
			break;
		case "Exit":
			uiMnuExit(ae);
			break;
		case "Redraw":
			uiMnuRedraw(ae);
			break;
		case "Show Row Lables":
			uiMnuShowRowLable(ae);
			break;
		case "Change UI Theme":
			uiMnuChangeLAF(ae);
			break;
		case "About":
			uiMnuAbout(ae);
			break;
		}
	}
	
	private void changeLaF(Component c, String clsName){
		try{
			//�����ϰ� �ִ� ��� ������Ʈ�� ����� �Ӽ� ����
			UIManager.setLookAndFeel(clsName);
			SwingUtilities.updateComponentTreeUI(c);
			frame.pack();
		}catch(ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ex){
			TaskDialogs.showException(ex);
		}
	}
	
	public TitanTreeContainer getTitanTreeContainer(){
		return view.getTreeContainer();
	}
	
	public TitanTableContainer getTitanTableContainer(){
		return view.getTableContainer();
	}
	
	public void setTitle(String title){
		frame.setTitle(title);
	}
	
	public void setVisible(boolean isVisible){
		frame.setVisible(isVisible);
	}
	
	public void pack(){
		frame.pack();
	}
	
	/*
	 * DSM ���� ����
	 */
	public File getDSMFile(){
		return dsmFile;
	}
	
	/*
	 * CLSX ���� ����
	 */
	public File getCLSXFile(){
		return clsxFile;
	}
	
}
