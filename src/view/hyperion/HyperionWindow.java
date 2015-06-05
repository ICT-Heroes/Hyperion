package view.hyperion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;

import org.omg.CORBA.TCKind;

import service.DsmService;
import model.Data;
import model.Dsm;

import com.ezware.dialog.task.TaskDialogs;

import controller.DataController;
import controller.PartitionController;

/**
 * 타이탄 윈도우
 * 이 윈도우에 DSM과 클러스터링 정보를 표시하는 트리, 테이블이 추가되며
 * 이들을 제어할 수 있는 명령 도구들이 배치됩니다.  
 * @author Seok
 *
 */
public class HyperionWindow{
	//트리 컨테이너
	private TreeContainer		treeContainer;
	
	//테이블 컨테이너
	private TableContainer tblContainer;
	
	//스플리터(화면을 좌우로 나누고 양쪽에 트리와 테이블을 배치하는데 쓰임)
	private JSplitPane spltPane;

	//메뉴바
	private JMenuBar mnuBar;
	
	//툴바
	private JToolBar toolBar;
	
	//주 윈도우 프레임
	private JFrame frame;
	
	//윈도우 종료시 실행될 디폴트 액션(기본값 : VM까지 완전 종료)
	private int defCloseAction = WindowConstants.EXIT_ON_CLOSE;
	
	//새로 추가되는 엔터티 인덱스 번호
	private int entityIdxCounter = 0;

	//사용자가 선택한 DSM파일, 파일 선택을 취소 또는 선택하지 않은 경우에는 null
	private File dsmFile = null;

	//사용자가 선택한 CLSX파일, 파일 선택을 취소 또는 선택하지 않은 경우에는 null
	private File clsxFile = null;

	//프로그램 수정 여부, 새로운 파일을 여는 경우에만 false, 나머지 동작 수행시 true로 설정
	private boolean isModified = false;
	
	//클러스터링 로드 여부
	private boolean isClusterLoad = false;
	
	//데이터 컨트롤러
	private DataController dcon;
	
	private boolean partition = false;
	
	private Dsm		 parDSM = null;
	private PartitionController pc = null;
	
	//이벤트 처리기 인스턴스
	private EventHandler	evtObj;
	
	//이벤트 처리기 정의
	class EventHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent ae){
			// 액션커맨드에 따라 적절한 이벤트 핸들러 호출
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
			case "Partitioning":
				uiPartitioning(ae);
				break;
			}
		}		
	}

	//클래스 초기화
	{
		init();
		initSurrogate();
		initToolBar();
		initMenuBar();
	}
	
	/**
	 * 윈도우 초기화
	 */
	private void init(){
		evtObj = new EventHandler();
		
		frame = new JFrame("Hyperion");
		
		//스플리터 생성 및 속성 설정
		spltPane = new JSplitPane();
		spltPane.setOneTouchExpandable(true);
		spltPane.setContinuousLayout(true);

		//컨테이너 생성 및 스플리터에 연결
		treeContainer = new TreeContainer();
		tblContainer = new TableContainer();
		spltPane.setLeftComponent(treeContainer.getContainer());
		spltPane.setRightComponent(tblContainer.getContainer());

		
		//주 프레임에 추가
		frame.getContentPane().add(spltPane, BorderLayout.CENTER);

		//Look And Feel을 OS에 따라 결정 Windows 계열 : Windows 비 Windows 계열 : Nimbus
		if(System.getProperty("os.name").contains("Windows") == true){
			changeLaF(frame, "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}else{
			changeLaF(frame, "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}

		//소유하고 있는 모든 컨테이너의 크기 및 위치 조정
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(defCloseAction);
	}
	
	/**
	 * 이벤트 호출 대리자 초기화
	 */
	private void initSurrogate() {
		//새로운 대리자를 생성
		EventSurrogateManager.addSurrogate(this);
		
		//생성된 대리자를 선택
		EventSurrogate surrogate = EventSurrogateManager.selectSurrogate(this);
		
		//자식 윈도우와 대리자를 연결
		EventSurrogateManager.linkSurrogate(this.tblContainer, this);
		EventSurrogateManager.linkSurrogate(this.treeContainer, this);

		//메서드 연결		
		//새로운 DSM 추가
		surrogate.bind("addNewDSM", "newDSM", new Class[] { Data.class, String.class });
		
		//데이터 컨트롤러 조회
		surrogate.bind("getDataController", "getDC");
				
		//데이터 다시 불러오기
		surrogate.bind("loadData", "reloadDSM");
		
		//새 엔터티 카운터 가져오기
		surrogate.bind("getLastNewDSMEntityIndex", "getNewEntityCounter");
	}
	
	/**
	 * 툴바 초기화 메서드
	 */
	private void initToolBar() {
		toolBar = new JToolBar();

		toolBar.add(UIHelper.buildImgButton("Open DSM", evtObj,
				HyperionWindow.class.getResource("/res/open-dsm.png")));
		toolBar.add(UIHelper.buildImgButton("Redraw", evtObj,
				HyperionWindow.class.getResource("/res/redraw.png")));

		// 툴바 버튼 사이에 분리자 추가
		toolBar.addSeparator(new Dimension(2, 20));

		toolBar.add(UIHelper.buildImgButton("New Clustering", evtObj,
				HyperionWindow.class.getResource("/res/new-clsx.png")));
		toolBar.add(UIHelper.buildImgButton("Load Clustering", evtObj,
				HyperionWindow.class.getResource("/res/open-clsx.png")));
		toolBar.add(UIHelper.buildImgButton("Save Clustering", evtObj,
				HyperionWindow.class.getResource("/res/save-clsx.png")));
		toolBar.add(UIHelper.buildImgButton("Save Clustering As...", evtObj,
				HyperionWindow.class.getResource("/res/save-clsx-as.png")));
	}

	/**
	 * 메뉴바 생성 및 이벤트 핸들러 연결
	 */
	private void initMenuBar() {
		JMenu mnuTmp;

		mnuBar = new JMenuBar();

		mnuTmp = UIHelper.buildMenu("File", 'F');
		mnuBar.add(mnuTmp);

		mnuTmp.add(UIHelper.buildMenuItem("New DSM", evtObj, KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		mnuTmp.add(new JSeparator());

		mnuTmp.add(UIHelper.buildMenuItem("Open DSM", evtObj, KeyEvent.VK_O,
				InputEvent.CTRL_MASK));
		mnuTmp.add(new JSeparator());

		mnuTmp.add(UIHelper.buildMenuItem("New Clustering", evtObj,
				KeyEvent.VK_N, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnuTmp.add(UIHelper.buildMenuItem("Load Clustering", evtObj,
				KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnuTmp.add(new JSeparator());

		mnuTmp.add(UIHelper.buildMenuItem("Save Clustering", evtObj,
				KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnuTmp.add(UIHelper.buildMenuItem("Save Clustering As...", evtObj,
				KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnuTmp.add(new JSeparator());

		mnuTmp.add(UIHelper.buildMenuItem("Exit", evtObj, KeyEvent.VK_F4,
				InputEvent.ALT_MASK));
		mnuTmp = UIHelper.buildMenu("View", 'V');
		mnuBar.add(mnuTmp);
		mnuTmp.add(UIHelper.buildMenuItem("Redraw", evtObj, KeyEvent.VK_F5, 0));
		mnuTmp.add(new JSeparator());
		
		mnuTmp.add(UIHelper.buildMenuItem("Partitioning", evtObj, KeyEvent.VK_F8, 0));
		mnuTmp.add(new JSeparator());

		mnuTmp.add(UIHelper.buildMenuItem("Show Row Lables", evtObj,
				KeyEvent.VK_F6, 0));
		mnuTmp.add(UIHelper.buildMenuItem("Change UI Theme", evtObj));

		mnuTmp = UIHelper.buildMenu("Help", 'H');
		mnuBar.add(mnuTmp);
		mnuTmp.add(UIHelper.buildMenuItem("About", evtObj));
	}

	/**
	 * 프레임의 종료 명령이 전달된 경우 닫기 동작에 대한 행동을 설정하는 메서드
	 */
	public void setCloseAction(int action) {
		defCloseAction = action;
		frame.setDefaultCloseOperation(defCloseAction);
	}

	/**
	 * 윈도우 메뉴바 부착 메서드
	 */
	public void attachMenuBar() {
		frame.setJMenuBar(mnuBar);
	}

	/**
	 * 윈도우 메뉴바 분리 메서드
	 */
	public void detachMenuBar() {
		frame.setJMenuBar(null);
	}

	/**
	 * 윈도우 툴바 부착 메서드
	 */
	public void attachToolBar() {
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
	}

	/**
	 * 윈도우 툴바 분리 메서드
	 */
	public void detachToolBar() {
		frame.getContentPane().remove(toolBar);
	}
	
	/*
	 * Binding methods(without refresh)
	 * @SuppressWarning for unused method.(They can't resolve binding method)
	 */
	
	
	
	/**
	 * 데이터 컨트롤러 반환 메서드
	 * @return DataController
	 */
	@SuppressWarnings("unused")
	private DataController getDataController() {
		return dcon;
	}

	/**
	 * 새 DSM을 추가할 때 마지막에 추가된 인덱스
	 * @return
	 */
	@SuppressWarnings("unused")
	private int getLastNewDSMEntityIndex(){
		return this.entityIdxCounter;
	}
	
	
	/**
	 * 새 DSM 레코드 하나를 추가함
	 * @param parent
	 * @param name
	 */
	@SuppressWarnings("unused")
	private void addNewDSM(Data parent, String name) {
		int idx = dcon.getRootData().getDataIndex(parent);
		if(idx != -1){
			entityIdxCounter++;
			dcon.addItem(idx, name);
			loadData();
		}
	}
	
	/*
	 * EVENT Handler List
	 */
	
	/**
	 * 새 DSM 추가 이벤트 핸들러
	 * @param ae
	 */
	private void uiMnuNewDSM(ActionEvent ae) {
		//현재 트리뷰에서 선택된 노드 조회
		Data selNode = treeContainer.getSelectedNode();
		
		//선택된 노드가 없으면 새 DSM을 추가하지 않는다
		if(selNode == null){
			UIHelper.BuildWarnDlg("Error", "Select node first please. Try Again", "");
			return ;
		}
		
		//사용자가 취소 또는 올바른 값을 입력할때까지 루프 진행
		while(true){
			//사용자로부터 입력을 받는다(기대 : 양수)
			String value = UIHelper.BuildInputDlg("New DSM Row", "How many rows are added?", "");
			
			// 만약 value가 null이 아니면 어떠한 값이 입력된 것이므로
			if(value != null){
				try{
					int rowCount = 0;
					
					//숫자로 변환을 시도
					rowCount = Integer.parseInt(value);

					//양의 정수가 아니라면 루프 재시작
					if(rowCount < 1){
						UIHelper.BuildWarnDlg("Error", "Value must be integer. Try Again", "");
						continue;
					}

					//1이상의 양의 정수가 입력되면 추가
					for (int i = 0; i < rowCount; i++){
						dcon.addItem(dcon.getRootData().getDataIndex(treeContainer.getSelectedNode()), "entity_" + (++entityIdxCounter));
					}

					// DSM을 새로 로드한다
					loadData();
					break;
				}catch(NumberFormatException nfe){
					UIHelper.BuildWarnDlg("Error", "Value must be integer. Try Again", "");
				}
			} else {
				//요기는 사용자가 값을 입력하지 않은 경우, value가 null이면 cancel을 누른 것이기 때문이다
				break;
			}
		}
	}

	/**
	 * DSM파일을 열고 DataController에게 DSM을 불러오도록 지시
	 * @param ae
	 */
	private void uiMnuOpenDSM(ActionEvent ae) {
		//변경사항이 있으면 저장할 수 있도록 사용자에게 질문
		if(isModified){
			TaskDialogs.ask(null,
					"You have unsaved changes, save dsm before open?",
					"Your changes will be lost if you don't save them");
		}

		FileNameExtensionFilter flt = new FileNameExtensionFilter("DSM File(*.dsm)", "dsm");
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Open DSM");
		jfc.setFileFilter(flt);
		
		if(jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION){
			dsmFile = jfc.getSelectedFile();
			dcon.loadDsm(dsmFile);
			loadData();
			isClusterLoad = false;
		}
	}

	/**
	 * 클러스터링 초기화<br/>지금까지 수행한 DSM과 클러스터링 데이터를 초기화하고 DSM을 새로 불러온다.
	 * 이렇게하면 새로운 클러스터링을 만드는 것과 같다.
	 * @param ae
	 */
	void uiMnuNewClustering(ActionEvent ae){
		if(dsmFile != null){
			dcon.loadDsm(dsmFile);
			isClusterLoad = false;
			loadData();
		}
	}

	/**
	 * 클러스터링 로드<br/>
	 * DSM이 로드되어야 하며 그렇지 않은 경우 에러메시지를 사용자에게 표시한다.
	 * @param ae
	 */
	void uiMnuLoadClustering(ActionEvent ae) {
		if(dcon.getRootData().getChildLength() == 0){
			UIHelper.BuildWarnDlg("Error", "Cannot load clustering. Make sure load dsm file before load clustering.", "Reason : DSM not loaded.");
			return;
		}
		FileNameExtensionFilter flt = new FileNameExtensionFilter("Clustering File(*.clsx)", "clsx");
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Load Clustering");
		jfc.setFileFilter(flt);
		if(jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION){
			clsxFile = jfc.getSelectedFile();
			dcon.loadDsmClsx(dsmFile, clsxFile);
			isClusterLoad = true;
			loadData();
		}
	}

	/**
	 * 클러스터링 저장
	 * @param ae
	 */
	void uiMnuSaveClustering(ActionEvent ae){
		dcon.saveClsx(clsxFile.toString(), dcon.getRootData());
	}

	/**
	 * 클러스터링 정보를 다른 이름으로 저장
	 * @param ae
	 */
	void uiMnuSaveClusteringAs(ActionEvent ae) {
		FileNameExtensionFilter flt = new FileNameExtensionFilter("Clustering File(*.clsx)", "clsx");
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Save Clustering File As...");
		jfc.setFileFilter(flt);
		if(jfc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION){
			File clsxAs = jfc.getSelectedFile();
			dcon.saveClsx(clsxAs.toString()+".clsx", dcon.getRootData());
		}
	}

	/**
	 * 프로그램 종료
	 * @param ae
	 */
	void uiMnuExit(ActionEvent ae) {
		// 변경사항 감지
		if(isModified == true){
			// 종료 여부 확인
			TaskDialogs.ask(null,
					"You have unsaved changes, save unsaved changes before exit?",
					"Your changes will be lost if you don't save them");
		}

		//프로그램 종료
		System.exit(0);
	}

	/**
	 * 다시 그리기 수행
	 * @param ae
	 */
	void uiMnuRedraw(ActionEvent ae) {
		buildTable(tblContainer, dcon.getRootData());
	}
	
	/**
	 * 행 레이블 표시/숨기기
	 * @param ae
	 */
	void uiMnuShowRowLable(ActionEvent ae){
		//행 표시여부 토글 후 이전 상태 저장
		boolean beforeState = this.tblContainer.toggleRowHeader();
		
		//메뉴 아이템 텍스트 변경
		JMenuItem itm = (JMenuItem)ae.getSource();
		
		if(beforeState == true){
			//이전 상태가 표시였으면 현재 상태는 숨김이므로
			itm.setText("Show Row Lables");
		}else{
			itm.setText("Hide Row Lables");
		}
	}
	
	/**
	 * 룩앤필 변경 메서드
	 * @param ae
	 */
	private void uiMnuChangeLAF(ActionEvent ae) {
		//UI관리자로부터 현재 JRE환경에 설치된 기본 LaF클래스를 조사
		UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();

		//LaF조사 실패시 아무런 동작을 하지 않는다
		if(lafs == null || lafs.length == 0){
			return;
		}

		//콤보박스에 표시할 LAF이름을 조사하고 콤보박스에 추가
		String[] lafName = new String[lafs.length];
		for(int i = 0; i < lafs.length; i++){
			lafName[i] = lafs[i].getName();
		}

		//사용자가 선택한 LAF를 얻어낸다
		String selLaFName = (String)JOptionPane.showInputDialog(null,
				"Select System UI", "Change UI", JOptionPane.PLAIN_MESSAGE,
				null, lafName, lafName[0]);

		//선택된 LaF를 UI관리자에게 알린다
		for(UIManager.LookAndFeelInfo laf : lafs){
			if(laf.getName().equals(selLaFName) == true){
				changeLaF(frame, laf.getClassName());
			}
		}
	}
	
	/**
	 * About team dialogs
	 * @param ae
	 */
	void uiMnuAbout(ActionEvent ae){
		TaskDialogs.inform(null, "2015 Human ICT Software Engineering, Team 9", "남궁석-20101518\r\n노효빈-20101522\r\n김대훈-20114207\r\n하진-20135166\r\n");
	}
	
	/**
	 * 파티셔닝 테스트 메서드
	 * @param ae
	 */
	void uiPartitioning(ActionEvent ae){		
		if(partition == false){
			parDSM = DsmService.getInstance().readFromeFile(dsmFile);
			
			pc = new PartitionController();
			pc.setDsm(parDSM);
			
			pc.preProcessing();
			pc.pathSearching();
			partition = true;
			
			for (int i = 0; i < parDSM.getNumber(); i++) {
				System.out.println(parDSM.getName(i));
				for (int j = 0; j < parDSM.getNumber(); j++) {
					if (parDSM.getDependency(i, j))
						System.out.print("O ");
					else
						System.out.print("X ");
				}
				System.out.println("");
			}
			uiMnuRedraw(null);
			treeContainer.uiExpandRoot();
			UIHelper.BuildWarnDlg("Partition", "Partitioning is complete.", "");
		}else{
			partition = false;
		}
	}
	
	/*
	 * Window Property getter/setter 
	 */
	/**
	 * Window 타이틀 세트
	 * @param title
	 */
	public void setTitle(String title){
		frame.setTitle(title);
	}

	/**
	 * Window 가시성 설정
	 * @param isVisible
	 */
	public void setVisible(boolean isVisible){
		frame.setVisible(isVisible);
	}
	
	/**
	 * 배치된 컨트롤 크기 조정
	 */
	public void pack(){
		frame.pack();
	}
	
	/**
	 * 데이터 컨트롤러 설정
	 * @param dc
	 */
	public void setDataController(DataController dc){
		dcon = dc;
		loadData();
	}	
	
	/*
	 * Internal utility methods
	 */
	
	/**
	 * LAF 변경 메서드
	 * @param c
	 * @param clsName
	 */
	private void changeLaF(Component c, String clsName){
		try{
			//소유하고 있는 모든 컴포넌트의 룩앤필 속성 변경
			UIManager.setLookAndFeel(clsName);
			SwingUtilities.updateComponentTreeUI(c);
			frame.pack();
		}catch(ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex){
			TaskDialogs.showException(ex);
		}
	}
	
	/**
	 * 데이터 컨트롤러에서 데이터를 불러와 트리와 테이블에 설정
	 */
	private void loadData() {
		//변경사항이 없음을 알림
		isModified = false;

		//트리의 모든 요소 삭제 및 재구축
		treeContainer.setRoot(dcon.getRootData());
		buildTree(treeContainer, dcon.getRootData());

		//트리의 첫 번째 수준까지 expand
		treeContainer.uiExpandRoot();

		//의존도 정보를 셀에 표시
		buildTable(tblContainer, dcon.getRootData());
	}	
	
	void buildTree(TreeContainer tc, Data parent) {
		for (int i = 0; i < parent.getChildLength(); i++) {
			Data inner = parent.getChildData(i);

			if (inner.getChildLength() > 0) {
				// 현재 트리가 보여지는지 확인, 보여지지 않는다면 더 그리기를 중단
				tc.insertNode(parent, inner);
				buildTree(tc, inner);
			} else {
				tc.insertNode(parent, inner);
			}
		}
	}
	
	void buildParTree(TreeContainer tc){
		tc.setRoot(new Data("root"));
		Data d = (Data)tc.getRoot();
		
		
		for(int i = 0; i < parDSM.getNumber(); i++){
			tc.insertNode(d, new Data(parDSM.getName(i)));
		}
	}
    class GroupIndex{
 	   int start;
 	   int end;
 	   public GroupIndex(){
 	      start = 0;
 	      end = 0;
 	   }
   }

	private void buildPartitonTable(TableContainer tbl){
		buildParTree(treeContainer);
		boolean maps[][] = new boolean[parDSM.getNumber()][parDSM.getNumber()];
		 tbl.setColumnSize(parDSM.getNumber());
		
		for (int i = 0; i < parDSM.getNumber(); i++) {
			for (int j = 0; j < parDSM.getNumber(); j++) {
				if (parDSM.getDependency(i, j)){
					maps[i][j] = true;
				}else{
					maps[i][j] = false;
				}
			}
			System.out.println("");
		}
		String[] rowHdrNames = new String[maps.length];
	      int i = 0;

	      for (int k = 0; k < maps.length; k++) {
	         Vector<String> vc = new Vector<String>();

	         for (int j = 0; j < maps.length; j++) {
	            // 대각성분은 .으로 표시
	            if (k == j) {
	               vc.add(".");
	            } else {
	               if (maps[k][j])
	                  vc.add("x");
	               else
	                  vc.add("");
	            }
	         }
	         rowHdrNames[i] = parDSM.getName(i++);
	         tbl.addNewRow(vc);
	      }	      
	      
	      tbl.setColorMap(null);
	      tbl.setRowHeaderTxt(rowHdrNames);
	      tbl.setColumnSizePref();
	}
	void buildTable(TableContainer tbl, Data parent) {
		
		 if(partition){
				buildPartitonTable(tbl);
				return;
			}
		 
	      // 열 개수 설정
	      tbl.setColumnSize(treeContainer.getVisibleLeafCount());
	      
	     

	      // 현재 보여지는 아이템만 추려냄
	      Data[] dts = treeContainer.getVisibleNodes();
	      int[] dtsIdxs = new int[dts.length];

	      for (int i = 0; i < dts.length; i++) {
	         dtsIdxs[i] = dcon.getRootData().getDataIndex(dts[i]);
	         System.out.println(dtsIdxs[i]);
	      }

	      boolean maps[][] = dcon.getDependArray(dtsIdxs);

	      String[] rowHdrNames = new String[dts.length];
	      int i = 0;

	      for (int k = 0; k < dts.length; k++) {
	         Vector<String> vc = new Vector<String>();

	         for (int j = 0; j < dts.length; j++) {
	            // 대각성분은 .으로 표시
	            if (k == j) {
	               vc.add(".");
	            } else {
	               if (maps[k][j])
	                  vc.add("x");
	               else
	                  vc.add("");
	            }
	         }
	         rowHdrNames[i++] = dts[k].getName();
	         tbl.addNewRow(vc);
	      }
	      
	      if(isClusterLoad != false){
	    	 
	    
	      Color c1 = new Color(255, 255, 255);
	      Color c2 = new Color(102, 204, 255);
	      Color c3 = new Color(102, 255, 204);
	      Color c4 = new Color(255, 204, 102);
	      Color c5 = new Color(50, 140, 190);
	      Color c6 = new Color(100, 240, 90);
	      Color c7 = new Color(0, 40, 90);

	      Color[] cmap = new Color[] {c1, c1 ,c2, c3, c4, c5, c6, c7 };
	     
	      // 보여지는 노드를 가져옴
	      DefaultMutableTreeNode[] nodes = treeContainer.getVisibleTreeAllNodes();
	      int nCount = nodes.length;
	      Color color[][] = new Color[nCount][nCount];
	      int matrix[][] = new int[nCount][nCount];
	      

	      TreeContainer tc = treeContainer;
	      
	      ArrayList<GroupIndex> gi = new ArrayList<GroupIndex>();
	      int indexLength = dtsIdxs.length;
	      int dataLength = parent.countData();
	      int wholeMatrix[][] = new int[dataLength][dataLength];
	      
	      for(int d = 0 ; d < nCount ; d ++){
	         for(int e = 0 ; e < nCount ; e ++){
	            matrix[d][e] = 0;
	         }
	      }

	      for (int c = 0; c < dataLength; c++) {
	         if(parent.getData(c).getChildLength() != 0){
	            GroupIndex newgi = new GroupIndex();
	            newgi.start = c;
	            newgi.end = c + parent.getData(c).countData() - 1;
	            gi.add(newgi);
	         }
	      }
	      		   
	      for(int d = 0 ; d < gi.size() ; d++){
	         fillMatrix(wholeMatrix, gi.get(d).start, gi.get(d).end);
	      }
	      
	      wholeMatrix = removeMatrix(wholeMatrix, dtsIdxs[indexLength-1]+1, wholeMatrix.length);
	      for(int d = 0 ; d < indexLength-1 ; d++){
	         wholeMatrix = removeMatrix(wholeMatrix, dtsIdxs[indexLength-d-2]+1, dtsIdxs[indexLength-d-1]-1);
	      }
	      wholeMatrix = removeMatrix(wholeMatrix, 0, dtsIdxs[0]-1);
	      
	      for(int c = 0 ; c < nCount ; c++){
	         for(int d = 0 ; d < nCount ; d++){
	            color[c][d] = cmap[wholeMatrix[c][d]];
	         }
	      }
		  	tblContainer.setColorMap(color);
	      }else{
	    	  tblContainer.setColorMap(null);
	      }

	      

	      tbl.setRowHeaderTxt(rowHdrNames);
	      tbl.setColumnSizePref();
	   }
		   
	   private int[][] removeMatrix(int [][] map, int start, int end){
	      //int[][] ret = new int[map.length-(end-start+1)][map.length-(end-start+1)];
	      int[][] ret = new int[map.length][map.length];
	      for(int i = 0 ; i < start ; i++){
	         for(int j = 0 ; j < start ; j++){
	            ret[i][j] = map[i][j];
	         }
	      }
	      for(int i = end+1 ; i < map.length ; i++){
	         for(int j = 0 ; j < start ; j++){
	            ret[i-end+start-1][j] = map[i][j];
	         }
	      }
	      for(int i = 0 ; i < start ; i++){
	         for(int j = end+1; j < map.length ; j++){
	            ret[i][j-end+start-1] = map[i][j];
	         }
	      }
	      for(int i = end+1 ; i < map.length ; i++){
	         for(int j = end+1 ; j < map.length ; j++){
	            ret[i-end+start-1][j-end+start-1] = map[i][j];
	         }
	      }
	      
	      return ret;
	   }
	   
	   private void fillMatrix(int[][] map, int nBegin, int nCount){
	      for (int i = nBegin; i <= nCount; i++) {
	         for (int j = nBegin; j <= nCount; j++) {
	            map[i][j] ++;
	         }
	      }
	   }

	   public void fillColor(Color[][] map, int nBegin, int nCount, Color clr) {
	      for (int i = nBegin; i < nCount; i++) {
	         for (int j = nBegin; j < nCount; j++) {
	            map[i][j] = clr;
	         }
	      }
	   }
}