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
 * TITAN 프로그램의 메인 윈도우 뼈대
 * 여기에 TreePane과 TablePane을 부착하여 기본 화면을 구성한다.
 * Fork 또는 Duplicate되는 경우 enableToolBar메서드를 호출하지 않는 것으로
 * 주 윈도우와 부 윈도우를 구분할 수 있다.
 * 
 */

public class TitanWindow implements ActionListener{
	private TitanSplitContainer	view;
	private JMenuBar				mnuBar;
	private JToolBar				toolBar;
	private JFrame					frame;
	private int					defCloseAction = WindowConstants.EXIT_ON_CLOSE;
	private Data					currentData;
	
	
	//사용자가 선택한 DSM파일을 나타낸다. 
	//파일 선택을 취소 또는 선택하지 않은 경우에는 null
	private File		dsmFile = null;
	
	//사용자가 선택한 CLSX파일을 나타낸다.
	//파일 선택을 취소 또는 선택하지 않은 경우에는 null
	private File		clsxFile = null;
	
	//프로그램 수정 여부, 새로운 파일을 여는 경우에만 false, 나머지 동작 수행시 true로 설정
	private boolean	isModified = false;
	
	//클래스가 생성되면서 초기화 수행
	{
		init();
		initMenuBar();
		initToolBar();
	}
	
	/*
	 * 프레임의 종료 명령이 전달된 경우 닫기 동작에 대한 행동을 설정하는 메서드
	 */
	public void setCloseAction(int action){
		defCloseAction = action;
	}
	
	/*
	 * 윈도우 메뉴바 부착 메서드
	 */
	public void attachMenuBar(){
		frame.setJMenuBar(mnuBar);
	}
	
	/*
	 * 윈도우 메뉴바 분리 메서드
	 */
	public void detachMenuBar(){
		frame.setJMenuBar(null);
	}
	
	/*
	 * 윈도우 툴바 부착 메서드
	 */
	public void attachToolBar(){
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
	}
	
	/*
	 * 윈도우 툴바 분리 메서드
	 */
	public void detachToolBar(){
		frame.getContentPane().remove(toolBar);
	}
	
	/*
	 * 툴바 초기화 메서드
	 */
	private void initToolBar(){
		toolBar = new JToolBar();
		
		toolBar.add(TitanUtil.buildImgButton("Open DSM", this, TitanWindow.class.getResource("/res/open-dsm.png")));		
		toolBar.add(TitanUtil.buildImgButton("Redraw", this, TitanWindow.class.getResource("/res/redraw.png")));
		
		//툴바 버튼 사이에 분리자 추가
		toolBar.addSeparator(new Dimension(2, 20));
		
		toolBar.add(TitanUtil.buildImgButton("New Clustering", this, TitanWindow.class.getResource("/res/new-clsx.png")));
		toolBar.add(TitanUtil.buildImgButton("Load Clustering", this, TitanWindow.class.getResource("/res/open-clsx.png")));
		toolBar.add(TitanUtil.buildImgButton("Save Clustering", this, TitanWindow.class.getResource("/res/save-clsx.png")));
		toolBar.add(TitanUtil.buildImgButton("Save Clustering As...", this, TitanWindow.class.getResource("/res/save-clsx-as.png")));
	}
	
	/*
	 * 메뉴바 생성 및 이벤트 핸들러 연결
	 */
	private void initMenuBar(){
		JMenu mnuTmp;
		
		mnuBar = new JMenuBar();	
		
		mnuTmp = TitanUtil.buildMenu("File", 'F');
		mnuBar.add(mnuTmp);
		
		mnuTmp.add(TitanUtil.buildMenuItem("New DSM", this, KeyEvent.VK_N, InputEvent.CTRL_MASK));		
		mnuTmp.add(new JSeparator());
		
		mnuTmp.add(TitanUtil.buildMenuItem("Open DSM", this, KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnuTmp.add(new JSeparator());
		
		mnuTmp.add(TitanUtil.buildMenuItem("New Clustering", this, KeyEvent.VK_N, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnuTmp.add(TitanUtil.buildMenuItem("Load Clustering", this, KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnuTmp.add(new JSeparator());

		mnuTmp.add(TitanUtil.buildMenuItem("Save Clustering", this, KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnuTmp.add(TitanUtil.buildMenuItem("Save Clustering As...", this, KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnuTmp.add(new JSeparator());
		
		mnuTmp.add(TitanUtil.buildMenuItem("Exit", this, KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mnuTmp = TitanUtil.buildMenu("View", 'V');
		mnuBar.add(mnuTmp);
		mnuTmp.add(TitanUtil.buildMenuItem("Redraw", this, KeyEvent.VK_F5, 0));
		mnuTmp.add(new JSeparator());
		
		mnuTmp.add(TitanUtil.buildMenuItem("Show Row Lables", this, KeyEvent.VK_F6, 0));
		mnuTmp.add(TitanUtil.buildMenuItem("Change UI Theme", this));
		
		mnuTmp = TitanUtil.buildMenu("Help", 'H');
		mnuBar.add(mnuTmp);
		mnuTmp.add(TitanUtil.buildMenuItem("About", this));
	}
	
	/*
	 * 윈도우 초기화
	 */
	private void init(){
		frame = new JFrame();
		
		//타이틀 설정
		frame.setTitle("TITAN");
		
		//타이탄뷰어 생성
		view = new TitanSplitContainer();
		frame.getContentPane().add(view.getContainer(), BorderLayout.CENTER);
		
		/*
		 * Look And Feel을 OS에 따라 결정
		 * Windows 계열		: Windows
		 * 비 Windows 계열	: Nimbus
		 */
		if(System.getProperty("os.name").contains("Windows") == true){
			changeLaF(frame, "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}else{
			changeLaF(frame, "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		
		//소유하고 있는 모든 컨테이너의 크기 및 위치 조정
		frame.pack();
		
		//현재 선택된 모니터의 중앙으로 이동
		frame.setLocationRelativeTo(null);
		
		//설정된 창 닫기 옵션을 수행하도록 지정(기본값 : VM종료)
		frame.setDefaultCloseOperation(defCloseAction);
	}

	/*
	 * 새로운 DSM을 생성
	 */
	public void uiMnuNewDSM(ActionEvent ae){
		//텍스트 필드 생성
		final JTextField txtFld = new JTextField(10);
		
		//구성요소
		Object[] bodyAndTxtField = {"How many rows added?\n", txtFld};
		Object[] btnTxt = {"OK", "Cancel"};
		
		//옵션패널 생성
		final JOptionPane optionPane = new JOptionPane(
													bodyAndTxtField,
													JOptionPane.QUESTION_MESSAGE,
													JOptionPane.YES_NO_OPTION,
													null,
													btnTxt,
													btnTxt[0]
												);
		
		//다이얼로그 생성
		final JDialog dialog = new JDialog(frame, "New DSM", true);
		dialog.setContentPane(optionPane);
		
		//다이얼로그가 자동으로 닫히지 않게 설정
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		//다이얼로그가 열리면 텍스트필드로 포커스가 이동하도록 이벤트 핸들러 설정 
		dialog.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentShown(ComponentEvent ce){
				txtFld.requestFocusInWindow();
			}
		});
		
		//X버튼을 눌렀을 때 처리를 위해 이벤트 핸들러 추가 
		dialog.addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent we){
		    	//X버튼을 눌렀다면 종료
		    	dialog.dispose();
		    }
		});
		
		//속성(예를 들어 버튼이 눌렸다던지)이 변경된 경우를 확인
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
		            	
		            	//이 값을 설정하지 않으면 다음번에 유저가 이벤트를 발생시키더라도
		            	//프로퍼티 변경 이벤트가 발생하지 않게 된다.
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
		//다이얼로그 요소를 적절하게 배치
		dialog.pack();
		
		//부모 윈도우의 중앙에 오도록 조정
		dialog.setLocationRelativeTo(frame);
		
		//다이얼로그 전시
		dialog.setVisible(true);
	}
	
	private void loadDSMFromData(){
		//변경사항이 없음을 알림
		isModified = false;
		
		//트리 컨테이너 획득
		TitanTreeContainer tc = this.getTitanTreeContainer();
		
		//테이블 컨테이너 획득
		TitanTableContainer tbc = this.getTitanTableContainer();
		
		//트리의 모든 요소 삭제
		tc.setRoot(currentData);
		
		//차일드 아이템 설정
		Object root = tc.getRoot();
		for(int i = 0; i < currentData.ItemCount(); i++){
			tc.insertNode(root, currentData.child.get(i));
		}
		
		//열 개수 설정
		tbc.setColumnSize(currentData.child.size());
		
		//의존도 정보를 셀에 표시
		for(int i = 0; i < currentData.ItemCount(); i++){
			Vector<String> vc = new Vector<String>();
			
			for(int j = 0; j < currentData.ItemCount(); j++){
				//대각성분은 .으로 표시
				if(i == j){
					vc.add(".");
				}else{
					vc.add("");
				}
			}
			
			//DSM 정보를 읽어서 배열에 채운다
			Data dsmRelation = currentData.child.get(i);
			
			for(Data target : dsmRelation.depend){
				int idx = tc.findNodeIndex(target);
				if(idx != -1)
					vc.set(idx, "x");
			}
			
			//새로운 행 추가
			tbc.addNewRow(vc);
		}
		tbc.setRowHeaderTxt(tc.getItemText());
		tbc.setColumnSizePref();
	}
	
	/*
	 * DSM파일의 경로를 반환
	 */
	private void uiMnuOpenDSM(ActionEvent ae){
		//변경사항이 있는지 확인한다
		if(isModified){
			TaskDialogs.ask(null, "You have unsaved changes, save dsm before open?", "Your changes will be lost if you don't save them");
		}
		
		FileNameExtensionFilter flt = new FileNameExtensionFilter("DSM File(*.dsm)", "dsm");
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Open DSM");
		jfc.setFileFilter(flt);
		if(jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION){
			dsmFile = jfc.getSelectedFile();
			currentData = dc.LoadDsm(dsmFile);
			loadDSMFromData();
		}else{
			dsmFile = null;
		}
	}
	
	/*
	 * 새로운 클러스터링 생성
	 */
	void uiMnuNewClustering(ActionEvent ae){
		JOptionPane.showMessageDialog(frame, "All grouped matrix are reverted...");
	}
	
	/*
	 * 클러스터링 로드
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
	 * 클러스터링 저장
	 */
	void uiMnuSaveClustering(ActionEvent ae){
		JOptionPane.showMessageDialog(frame, "Clustering saved...");
	}
	
	/*
	 * 클러스터링을 다른 이름으로 저장,
	 * 이 때 getCLSXFile 메소드로 새로운 CLSX파일을 알 수 있음
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
	 * 프로그램 종료
	 */	
	void uiMnuExit(ActionEvent ae){
		//변경사항 감지
		isModified = true;
		if(isModified == true){
			//종료 여부 확인
			int result = JOptionPane.showConfirmDialog(frame, "변경 사항이 있습니다. 저장하시겠습니까?");
			
			if(result == JOptionPane.YES_OPTION){
				//save
			}else if(result == JOptionPane.CANCEL_OPTION){
				return ;
			}
		}
		
		//프로그램 종료
		System.exit(0);
	}
	
	/*
	 * 다시 그리기
	 */	
	void uiMnuRedraw(ActionEvent ae){
		JOptionPane.showMessageDialog(frame, "redrawed...");
	}
	
	/*
	 * 행 레이블 보이기 토글
	 */	
	void uiMnuShowRowLable(ActionEvent ae){
		boolean beforeState = this.getTitanTableContainer().toggleRowHeader();
		JMenuItem itm = (JMenuItem)ae.getSource();
		if(beforeState == true){
			itm.setText("Show Row Lables");
		}else{
			itm.setText("Hide Row Lables");
		}
	}
	
	/*
	 * 어바웃 Team
	 */	
	void uiMnuAbout(ActionEvent ae){
		JOptionPane.showMessageDialog(frame, "EMPTY");
	}
	
	/*
	 * LAF를 선택할 수 있도록 전시
	 */
	private void uiMnuChangeLAF(ActionEvent ae){
		//UI관리자로부터 현재 JRE환경에 설치된 기본 LaF클래스를 조사
		UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
		
		//LaF조사 실패시 아무런 동작을 하지 않는다
		if(lafs == null || lafs.length == 0){
			return ;
		}
		
		//콤보박스에 표시할 LAF이름을 조사하고 콤보박스에 추가
		String[] lafName = new String[lafs.length];
		for(int i = 0; i < lafs.length; i++){
			lafName[i] = lafs[i].getName();
		}
		
		//사용자가 선택한 LAF를 얻어낸다
		String selLaFName = (String)JOptionPane.showInputDialog(
								null, "Select System UI", "Change UI", 
								JOptionPane.PLAIN_MESSAGE, null,
								lafName, lafName[0]);
		
		//선택된 LaF를 UI관리자에게 알린다
		for(UIManager.LookAndFeelInfo laf : lafs){
			if(laf.getName().equals(selLaFName) == true){
				changeLaF(frame, laf.getClassName());
			}
		}
	}
	/*
	 * 툴바 및 메뉴에 관련된 이벤트를 처리하는 핸들러
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent ae){
		//액션커맨드에 따라 적절한 이벤트 핸들러 호출
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
		case "Hide Row Lables":
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
			//소유하고 있는 모든 컴포넌트의 룩앤필 속성 변경
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
	 * DSM 파일 정보
	 */
	public File getDSMFile(){
		return dsmFile;
	}
	
	/*
	 * CLSX 파일 정보
	 */
	public File getCLSXFile(){
		return clsxFile;
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

	private DataController		dc;
	
	public void setDataController(DataController dc){
		this.dc = dc;
	}
	
}
