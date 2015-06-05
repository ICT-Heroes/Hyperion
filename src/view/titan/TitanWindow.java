package view.titan;

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

import model.Data;

import com.ezware.dialog.task.TaskDialogs;

import controller.DataController;

/*
 * @date   : 2015-05-21
 * @author   : seok
 * @version   : 1  
 * TITAN 프로그램의 메인 윈도우 뼈대
 * 여기에 TreePane과 TablePane을 부착하여 기본 화면을 구성한다.
 * Fork 또는 Duplicate되는 경우 enableToolBar메서드를 호출하지 않는 것으로
 * 주 윈도우와 부 윈도우를 구분할 수 있다.
 * 
 */

public class TitanWindow implements ActionListener {
	private TreeContainer treeContainer;
	private TableContainer tblContainer;
	private JSplitPane spltPane;

	private JMenuBar mnuBar;
	private JToolBar toolBar;
	private JFrame frame;
	private int defCloseAction = WindowConstants.EXIT_ON_CLOSE;
	private Data currentData;
	private EventSurrogate surrogate;

	// 사용자가 선택한 DSM파일을 나타낸다.
	// 파일 선택을 취소 또는 선택하지 않은 경우에는 null
	private File dsmFile = null;

	// 사용자가 선택한 CLSX파일을 나타낸다.
	// 파일 선택을 취소 또는 선택하지 않은 경우에는 null
	private File clsxFile = null;

	// 프로그램 수정 여부, 새로운 파일을 여는 경우에만 false, 나머지 동작 수행시 true로 설정
	private boolean isModified = false;

	// 클래스가 생성되면서 초기화 수행
	{
		init();
		initSurrogate();
		initMenuBar();
		initToolBar();
	}

	/*
	 * 프레임의 종료 명령이 전달된 경우 닫기 동작에 대한 행동을 설정하는 메서드
	 */
	public void setCloseAction(int action) {
		defCloseAction = action;
		frame.setDefaultCloseOperation(defCloseAction);
	}

	/*
	 * 윈도우 메뉴바 부착 메서드
	 */
	public void attachMenuBar() {
		frame.setJMenuBar(mnuBar);
	}

	/*
	 * 윈도우 메뉴바 분리 메서드
	 */
	public void detachMenuBar() {
		frame.setJMenuBar(null);
	}

	/*
	 * 윈도우 툴바 부착 메서드
	 */
	public void attachToolBar() {
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
	}

	/*
	 * 윈도우 툴바 분리 메서드
	 */
	public void detachToolBar() {
		frame.getContentPane().remove(toolBar);
	}

	/*
	 * 툴바 초기화 메서드
	 */
	private void initToolBar() {
		toolBar = new JToolBar();

		toolBar.add(UIHelper.buildImgButton("Open DSM", this,
				TitanWindow.class.getResource("/res/open-dsm.png")));
		toolBar.add(UIHelper.buildImgButton("Redraw", this,
				TitanWindow.class.getResource("/res/redraw.png")));

		// 툴바 버튼 사이에 분리자 추가
		toolBar.addSeparator(new Dimension(2, 20));

		toolBar.add(UIHelper.buildImgButton("New Clustering", this,
				TitanWindow.class.getResource("/res/new-clsx.png")));
		toolBar.add(UIHelper.buildImgButton("Load Clustering", this,
				TitanWindow.class.getResource("/res/open-clsx.png")));
		toolBar.add(UIHelper.buildImgButton("Save Clustering", this,
				TitanWindow.class.getResource("/res/save-clsx.png")));
		toolBar.add(UIHelper.buildImgButton("Save Clustering As...", this,
				TitanWindow.class.getResource("/res/save-clsx-as.png")));
	}

	/*
	 * 메뉴바 생성 및 이벤트 핸들러 연결
	 */
	private void initMenuBar() {
		JMenu mnuTmp;

		mnuBar = new JMenuBar();

		mnuTmp = UIHelper.buildMenu("File", 'F');
		mnuBar.add(mnuTmp);

		mnuTmp.add(UIHelper.buildMenuItem("New DSM", this, KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		mnuTmp.add(new JSeparator());

		mnuTmp.add(UIHelper.buildMenuItem("Open DSM", this, KeyEvent.VK_O,
				InputEvent.CTRL_MASK));
		mnuTmp.add(new JSeparator());

		mnuTmp.add(UIHelper.buildMenuItem("New Clustering", this,
				KeyEvent.VK_N, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnuTmp.add(UIHelper.buildMenuItem("Load Clustering", this,
				KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnuTmp.add(new JSeparator());

		mnuTmp.add(UIHelper.buildMenuItem("Save Clustering", this,
				KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnuTmp.add(UIHelper.buildMenuItem("Save Clustering As...", this,
				KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnuTmp.add(new JSeparator());

		mnuTmp.add(UIHelper.buildMenuItem("Exit", this, KeyEvent.VK_F4,
				InputEvent.ALT_MASK));
		mnuTmp = UIHelper.buildMenu("View", 'V');
		mnuBar.add(mnuTmp);
		mnuTmp.add(UIHelper.buildMenuItem("Redraw", this, KeyEvent.VK_F5, 0));
		mnuTmp.add(new JSeparator());

		mnuTmp.add(UIHelper.buildMenuItem("Show Row Lables", this,
				KeyEvent.VK_F6, 0));
		mnuTmp.add(UIHelper.buildMenuItem("Change UI Theme", this));

		mnuTmp = UIHelper.buildMenu("Help", 'H');
		mnuBar.add(mnuTmp);
		mnuTmp.add(UIHelper.buildMenuItem("About", this));
	}

	private void initSurrogate() {
		EventSurrogateManager.addSurrogate(this);
		surrogate = EventSurrogateManager.selectSurrogate(this);
		EventSurrogateManager.linkSurrogate(this.tblContainer, this);
		EventSurrogateManager.linkSurrogate(this.treeContainer, this);

		// 메서드 서로게이트 추가
		surrogate.bind("addNewDSM", "newDSM", new Class[] { Data.class,
				String.class });
		surrogate.bind("getDataController", "getDC");
		surrogate.bind("getData", "getData");
		surrogate.bind("loadDSMFromData", "reloadDSM");
		// surrogate.invoke("newDSM", new Object[]{this.currentData, "tst"});
	}

	public void refresh() {
		loadDSMFromData();
		uiMnuRedraw(null);
	}

	private Data getData() {
		return this.currentData;
	}

	void setData(Data d) {
		this.currentData = d;
	}

	private DataController getDataController() {

		return this.dc;
	}

	/*
	 * 윈도우 초기화
	 */
	private void init() {
		frame = new JFrame();

		// 타이틀 설정
		frame.setTitle("TITAN");

		// 타이탄뷰어 생성
		spltPane = new JSplitPane();

		// ��Ŭ������ �� pane�� ���� �ݱ� �����ϰ� ����
		spltPane.setOneTouchExpandable(true);

		// ���Ե� �����̳ʵ��� ���ӵ� ��ġ�� ���̰� ����
		spltPane.setContinuousLayout(true);

		// �����̳� ���� �� �ʱ�ȭ
		treeContainer = new TreeContainer();
		tblContainer = new TableContainer();

		// splitpane�� ����
		spltPane.setLeftComponent(treeContainer.getContainer());
		spltPane.setRightComponent(tblContainer.getContainer());

		frame.getContentPane().add(spltPane, BorderLayout.CENTER);

		/*
		 * Look And Feel을 OS에 따라 결정 Windows 계열 : Windows 비 Windows 계열 : Nimbus
		 */
		if (System.getProperty("os.name").contains("Windows") == true) {
			changeLaF(frame,
					"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} else {
			changeLaF(frame, "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}

		// 소유하고 있는 모든 컨테이너의 크기 및 위치 조정
		frame.pack();

		// 현재 선택된 모니터의 중앙으로 이동
		frame.setLocationRelativeTo(null);

		// 설정된 창 닫기 옵션을 수행하도록 지정(기본값 : VM종료)
		frame.setDefaultCloseOperation(defCloseAction);
	}

	/*
	 * 새로운 DSM을 생성
	 */
	private void addNewDSM(Data parent, String name) {
		dc.addItem(0, name);
		loadDSMFromData();
	}

	Object invoke(String evtName, Object[] args) {
		return surrogate.invoke(evtName, args);
	}

	Object invoke(String evtName) {
		return surrogate.invoke(evtName);
	}

	public void uiMnuNewDSM(ActionEvent ae) {
		// 로직의 시작, 사용자가 취소 또는 올바른 값을 입력할때까지 루프 진행
		while (true) {
			// 사용자로부터 입력을 받는다
			String value = UIHelper.BuildInputDlg("New DSM Row",
					"How many rows are added?", "");
			int rowCount = 0;

			// 만약 value가 null이 아니면 어떠한 값이 입력된 것이므로
			if (value != null) {
				try {
					// 숫자로 변환을 시도한다
					rowCount = Integer.parseInt(value);

					// 양의 정수가 아니라면 루프 재시작
					if (rowCount < 1) {
						UIHelper.BuildWarnDlg("Error",
								"Value must be integer. Try Again", "");
						continue;
					}

					// 1이상의 양의 정수라면,
					String _entity = "entity_";
					for (int i = 0; i < rowCount; i++) {
						dc.addItem(currentData.getDataIndex(this.treeContainer
								.getSelectedNode()), _entity + (i + 1));
					}

					// DSM을 새로 로드한다
					loadDSMFromData();
					break;
				} catch (NumberFormatException nfe) {
					UIHelper.BuildWarnDlg("Error",
							"Value must be integer. Try Again", "");
				}
			} else {
				// 요기는 사용자가 값을 입력하지 않은 경우, value가 null이면 cancel을 누른 것이기 때문이다
				break;
			}
		}
	}

	int _insertChild(TreeContainer tc, Data parent) {
		Data local;
		// 아이템을 얻어온다
		int totalAdd = 0;
		for (int i = 0; i < parent.getChildLength(); i++) {
			// 추가한다.
			if (parent.getItemCount() == 1 && parent.getChildLength() > 0) {
				tc.insertNode(parent, parent);
				break;
			}

			// System.out.println(parent.ItemCount() + "@ i : " + i);
			local = parent.getChildData(i);
			tc.insertNode(parent, local);

			// 추가한 노드에 차일드가 존재하는지 확인한다.
			if (local.getChildLength() > 0) {
				int add = _insertChild(tc, local);
				i += local.getChildLength();
			}
			totalAdd = i;
		}
		return totalAdd;
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

	int depth = 0;

	void _buildTable(TableContainer tbl, Data parent) {
		for (int i = 0; i < parent.getChildLength(); i++) {
			Data inner = parent.getChildData(i);

			if (inner.getChildLength() > 0) {
				buildTable(tbl, inner);
			} else {
				Vector<String> vc = new Vector<String>();

				for (int j = 0; j < currentData.getItemCount(); j++) {
					// 대각성분은 .으로 표시
					if (depth == j) {
						vc.add(".");
					} else {
						vc.add("");
					}
				}

				// DSM 정보를 읽어서 배열에 채운다
				Data dsmRelation = inner;

				if (treeContainer.isExpanded(dsmRelation) == false) {
					System.out.println("접혀있습니다. 이 레코드는 축소될 것입니다."
							+ dsmRelation.getName());
				}
				for (int c = 0; c < dsmRelation.getDependencyLength(); c++) {
					int idx = treeContainer.findNodeIndex(dsmRelation
							.getDependencyData(c));
					if (idx != -1)
						vc.set(idx, "x");
				}
				// 새로운 행 추가
				tbl.addNewRow(vc);
				depth++;
			}
		}
	}

	/*
	 * 전략을 다르게 할 것이다. 어떻게 할 거냐면 트리에서 읽어서 데이터를 구축해야함 씨발 ㅠㅠ 와... 감격씨발
	 */
	void __buildTable(TableContainer tbl, Data parent) {
		System.out
				.println("LeafCount : " + treeContainer.getVisibleLeafCount());
		// 열 개수 설정
		tbl.setColumnSize(treeContainer.getVisibleLeafCount());

		// 현재 보여지는 아이템만 추려냄
		Data[] dts = treeContainer.getVisibleNodes();
		String[] rowHdrNames = new String[dts.length];
		int i = 0;
		Data d;
		for (int k = 0; k < dts.length; k++) {
			Vector<String> vc = new Vector<String>();

			for (int j = 0; j < currentData.getItemCount(); j++) {
				// 대각성분은 .으로 표시
				if (k == j) {
					vc.add(".");
				} else {
					vc.add("");
				}
			}
			for (int c = 0; c < dts[k].getDependencyLength(); c++) {
				int idx = treeContainer.findNodeIndex(dts[k].getDependencyData(c));
				if (idx != -1)
					vc.set(idx, "x");
			}
			rowHdrNames[i++] = dts[k].getName();
			tbl.addNewRow(vc);
		}

		tbl.setRowHeaderTxt(rowHdrNames);
		tbl.setColumnSizePref();
	}

	int recurGetMaxDepth(DefaultMutableTreeNode n, int nDepth) {
		// 새끼노드의 개수를 얻어옴
		int childLength = treeContainer.getVisibleChildNodeCount(n);

		// 일단 내 영역을 칠할 기본값
		int region = 1;
		DefaultMutableTreeNode node;

		// System.out.println("현재 탐색 중인 노드 : " + n.getUserObject());
		System.out.println("현재 탐색 중인 노드 : " + n.getUserObject() + ", 수준 : "
				+ nDepth);

		// 새끼노드가 하나라도 붙어있다면,
		boolean hasOnlyLeaves = true;
		if (childLength != 0) {
			// 자식노드가 하나라도 붙어있기 때문에 증가 시킨다.
			region++;

			// 새끼노드 탐색
			for (int i = 0; i < childLength; i++) {
				// 새끼노드 가져오기
				node = (DefaultMutableTreeNode) n.getChildAt(i);

				// 새끼노드가 잎사귀라면 카운팅하지 않는다(1로 간주)
				if (node.isLeaf() == false) {
					// 잎사귀가 아니라면 이게 확장됐는지 확인, 확장되지 않았다면 역시 1로 간주해야함
					if (treeContainer.isNodeExpanded(node)) {
						System.out.println("현재 탐색 중인 노드 : " + n.getUserObject()
								+ ", 수준 : " + nDepth);
						region = Math.max(region,
								recurGetMaxDepth(node, region));
					}
					hasOnlyLeaves = false;
				}
			}
		}
		// System.out.println("이 노드의 깊이는 다음과 같습니다 : " + region +
		// n.getUserObject());
		return region;
	}

	int findParentNodeIndex(DefaultMutableTreeNode[] k, int origin) {
		int level = treeContainer.getLevel(k[origin]);
		int n = origin;
		while (true) {
			if (treeContainer.getLevel(k[--n]) == (level - 1))
				break;
		}
		return n;
	}

	void buildTable(TableContainer tbl, Data parent) {
		System.out
				.println("LeafCount : " + treeContainer.getVisibleLeafCount());
		// 열 개수 설정
		tbl.setColumnSize(treeContainer.getVisibleLeafCount());

		// 현재 보여지는 아이템만 추려냄
		Data[] dts = treeContainer.getVisibleNodes();
		int[] dtsIdxs = new int[dts.length];

		for (int i = 0; i < dts.length; i++) {
			dtsIdxs[i] = currentData.getDataIndex(dts[i]);
			System.out.println(dtsIdxs[i]);
		}

		boolean maps[][] = dc.getDependArray(dtsIdxs);

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
		Color zero = new Color(255, 255, 255);
		Color skyblue = new Color(102, 204, 255);
		Color redone0 = new Color(102, 255, 204);
		Color redone1 = new Color(255, 204, 102);
		Color redone2 = new Color(50, 140, 190);
		Color redone3 = new Color(100, 240, 90);
		Color redone4 = new Color(0, 40, 90);

		Color[] cmap = new Color[] {zero, zero ,skyblue, redone0, redone1, redone2,
				redone3, redone4 };
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
		
		
		for(int c = 0 ; c < gi.size() ; c++){
			System.out.println("start : " + gi.get(c).start + ", end : " + gi.get(c).end);
		}

		//fillColor(color, 3, 5, cmap[depth[1]]);
	
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

		if (this.isClusterLoad)
			tblContainer.setColorMap(color);

		tbl.setRowHeaderTxt(rowHdrNames);
		tbl.setColumnSizePref();
	}
	
	private int[][] removeMatrix(int [][] map, int start, int end){
		int[][] ret = new int[map.length-(end-start)][map.length-(end-start)];
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

	public void loadDSMFromData() {
		// 변경사항이 없음을 알림
		isModified = false;

		// 트리 컨테이너 획득
		TreeContainer tc = this.treeContainer;

		// 테이블 컨테이너 획득
		TableContainer tbc = this.tblContainer;

		// 트리의 모든 요소 삭제
		tc.setRoot(currentData);

		// 차일드 아이템 설정
		Object root = tc.getRoot();

		buildTree(tc, currentData);

		// 트리의 첫 번째 수준까지 expand
		tc.uiExpandRoot();

		// 의존도 정보를 셀에 표시
		depth = 0;
		buildTable(tblContainer, currentData);
	}

	/*
	 * DSM파일의 경로를 반환
	 */
	private void uiMnuOpenDSM(ActionEvent ae) {
		// 변경사항이 있는지 확인한다
		if (isModified) {
			TaskDialogs.ask(null,
					"You have unsaved changes, save dsm before open?",
					"Your changes will be lost if you don't save them");
		}

		FileNameExtensionFilter flt = new FileNameExtensionFilter(
				"DSM File(*.dsm)", "dsm");
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Open DSM");
		jfc.setFileFilter(flt);
		if (jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			dsmFile = jfc.getSelectedFile();
			currentData = dc.loadDsm(dsmFile);
			loadDSMFromData();
		} else {
			dsmFile = null;
		}
	}

	/*
	 * 클러스터링 초기화
	 */
	void uiMnuNewClustering(ActionEvent ae) {
		this.currentData = dc.loadDsm(dsmFile);
		loadDSMFromData();
	}

	boolean isClusterLoad = false;

	/*
	 * 클러스터링 로드
	 */
	void uiMnuLoadClustering(ActionEvent ae) {
		FileNameExtensionFilter flt = new FileNameExtensionFilter(
				"Clustering File(*.clsx)", "clsx");
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Load Clustering");
		jfc.setFileFilter(flt);
		if (jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			clsxFile = jfc.getSelectedFile();
			Data clsx = dc.loadClsx(clsxFile);
			System.out.println("DSM : " + dsmFile.toString());
			System.out.println("CLSX : " + clsxFile.toString());
			Data newData = dc.loadDsmClsx(dsmFile, clsxFile);
			isClusterLoad = true;
			currentData = newData;
			loadDSMFromData();
		} else {
			clsxFile = null;
		}
	}

	/*
	 * 클러스터링 저장
	 */
	void uiMnuSaveClustering(ActionEvent ae) {
		// DsmService
		JOptionPane.showMessageDialog(frame, "Clustering saved...");
	}

	/*
	 * 클러스터링을 다른 이름으로 저장, 이 때 getCLSXFile 메소드로 새로운 CLSX파일을 알 수 있음
	 */
	void uiMnuSaveClusteringAs(ActionEvent ae) {
		FileNameExtensionFilter flt = new FileNameExtensionFilter(
				"Clustering File(*.clsx)", "clsx");
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Save Clustering File As...");
		jfc.setFileFilter(flt);
		if (jfc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
			clsxFile = jfc.getSelectedFile();
		} else {
			clsxFile = null;
		}
	}

	/*
	 * 프로그램 종료
	 */
	void uiMnuExit(ActionEvent ae) {
		// 변경사항 감지
		isModified = true;
		if (isModified == true) {
			// 종료 여부 확인
			int result = JOptionPane.showConfirmDialog(frame,
					"변경 사항이 있습니다. 저장하시겠습니까?");

			if (result == JOptionPane.YES_OPTION) {
				// save
			} else if (result == JOptionPane.CANCEL_OPTION) {
				return;
			}
		}

		// 프로그램 종료
		System.exit(0);
	}

	/*
	 * 다시 그리기
	 */
	void callTest() {
		buildTable(tblContainer, currentData);
	}

	void uiMnuRedraw(ActionEvent ae) {
		callTest();
		// loadDSMFromData();
		// loadDSMFromData();
	}

	/*
	 * 행 레이블 보이기 토글
	 */
	void uiMnuShowRowLable(ActionEvent ae) {
		boolean beforeState = this.tblContainer.toggleRowHeader();
		JMenuItem itm = (JMenuItem) ae.getSource();
		if (beforeState == true) {
			itm.setText("Show Row Lables");
		} else {
			itm.setText("Hide Row Lables");
		}
	}

	/*
	 * 어바웃 Team
	 */
	void uiMnuAbout(ActionEvent ae) {
		JOptionPane.showMessageDialog(frame, "EMPTY");
	}

	/*
	 * LAF를 선택할 수 있도록 전시
	 */
	private void uiMnuChangeLAF(ActionEvent ae) {
		// UI관리자로부터 현재 JRE환경에 설치된 기본 LaF클래스를 조사
		UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();

		// LaF조사 실패시 아무런 동작을 하지 않는다
		if (lafs == null || lafs.length == 0) {
			return;
		}

		// 콤보박스에 표시할 LAF이름을 조사하고 콤보박스에 추가
		String[] lafName = new String[lafs.length];
		for (int i = 0; i < lafs.length; i++) {
			lafName[i] = lafs[i].getName();
		}

		// 사용자가 선택한 LAF를 얻어낸다
		String selLaFName = (String) JOptionPane.showInputDialog(null,
				"Select System UI", "Change UI", JOptionPane.PLAIN_MESSAGE,
				null, lafName, lafName[0]);

		// 선택된 LaF를 UI관리자에게 알린다
		for (UIManager.LookAndFeelInfo laf : lafs) {
			if (laf.getName().equals(selLaFName) == true) {
				changeLaF(frame, laf.getClassName());
			}
		}
	}

	/*
	 * 툴바 및 메뉴에 관련된 이벤트를 처리하는 핸들러
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		// 액션커맨드에 따라 적절한 이벤트 핸들러 호출
		switch (ae.getActionCommand()) {
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

	private void changeLaF(Component c, String clsName) {
		try {
			// 소유하고 있는 모든 컴포넌트의 룩앤필 속성 변경
			UIManager.setLookAndFeel(clsName);
			SwingUtilities.updateComponentTreeUI(c);
			frame.pack();
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex) {
			TaskDialogs.showException(ex);
		}
	}

	public void setTitle(String title) {
		frame.setTitle(title);
	}

	public void setVisible(boolean isVisible) {
		frame.setVisible(isVisible);
	}

	public void pack() {
		frame.pack();
	}

	private DataController dc;

	public void setDataController(DataController dc) {
		this.dc = dc;
		this.currentData = dc.getRootData();// dc.GetRoot();
		this.treeContainer.setDataController(dc);
		/*
		 * currentData = dc.loadDsm(new File("f:\\dsm.dsm")); loadDSMFromData();
		 * isClusterLoad = true; currentData = dc.loadDsmClsx(currentData,
		 * dc.loadClsx(new File("f:\\dsm.clsx"))); loadDSMFromData();
		 */
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