package view.hyperion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import view.titan.deprecated.TitanWindowDeprecated;
import model.Data;

import com.ezware.dialog.task.TaskDialogs;

import controller.DataController;
import factory.DataControllerFactory;


/**
 * 트리뷰 컨테이너<br/>
 * 트리와 관련된 모든 조작을 여기서 수행합니다.
 * @author Seok
 *
 */
public class TreeContainer{
	//컨테이너
	private JPanel				container;
	
	//툴바 컨테이너
	private JPanel				pnlToolbar;
	
	//스크롤 패널
	private JScrollPane		pnlTree;
	
	//트리 도구 메뉴
	private JToolBar			tbarTree;
	
	//트리
	private JTree				treeDSM;
	
	//팝업 메뉴
	private JPopupMenu		mnuTree;
	
	//이벤트 핸들러
	private EventHandler		evtObj;	
	
	/**
	 * 이벤트 핸들러
	 * @author Seok
	 *
	 */
	class EventHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent ae){
			switch(ae.getActionCommand()){
			case "Expand All":
				uiToolbarExpandAll(ae);
				break;
			case "Collapse All":
				uiToolbarCollapseAll(ae);
				break;
			case "Rename":
				uiToolbarRename(ae);
				break;
			case "Duplicate":
				uiMnuDuplicate(ae);
				break;
			case "Fork":
				uiMnuFork(ae);
				break;
			case "Delete":
				uiToolbarDelete(ae);
				break;
			case "Sort":
				uiToolbarSort(ae);
				break;
			case "Add New DSM Row":
				uiToolbarAddNewRow(ae);
				break;
			case "Move Up":
				uiToolbarMoveUp(ae);
				break;
			case "Move Down":
				uiToolbarMoveDown(ae);
				break;
			case "Group":
				uiToolbarGroup(ae);
				break;
			case "UnGroup":
				uiToolbarUnGroup(ae);
				break;
			}
		}		
	}
	{
		init();
	}
	
	/**
	 * 트리 컨테이너 초기화
	 */
	private void init(){		
		//이벤트 핸들러 초기화
		evtObj = new EventHandler();
		
		//컨테이너 생성(hgap : 5, vgap : 1)
		container = new JPanel();
		container.setLayout(new BorderLayout(5, 1));
		
		//툴바 컨테이너 생성
		pnlToolbar = new JPanel(new FlowLayout());
		pnlToolbar.setBorder(null);
		
		//툴바 컨테이너는 FlowLayout으로 설정, 항목간 Gap은 0, 좌측정렬
		FlowLayout flToolbar = (FlowLayout)pnlToolbar.getLayout();
		flToolbar.setHgap(0);
		flToolbar.setVgap(0);
		flToolbar.setAlignment(FlowLayout.LEFT);
		container.add(pnlToolbar, BorderLayout.NORTH);
			
		//툴바 생성, 부동 불가
		tbarTree = new JToolBar();
		tbarTree.setForeground(new Color(255, 255, 255));
		tbarTree.setFloatable(false);
		pnlToolbar.add(tbarTree);
		
		tbarTree.add(UIHelper.buildImgButton("Expand All", evtObj, HyperionWindow.class.getResource("/res/expand.png")));
		tbarTree.add(UIHelper.buildImgButton("Collapse All", evtObj, HyperionWindow.class.getResource("/res/collapse.png")));
		tbarTree.addSeparator(new Dimension(2, 20));
		
		tbarTree.add(UIHelper.buildImgButton("Group", evtObj, HyperionWindow.class.getResource("/res/group.png")));
		tbarTree.add(UIHelper.buildImgButton("Ungroup", evtObj, HyperionWindow.class.getResource("/res/ungroup.png")));
		tbarTree.addSeparator(new Dimension(2, 20));
		
		tbarTree.add(UIHelper.buildImgButton("Move Up", evtObj, HyperionWindow.class.getResource("/res/up.png")));
		tbarTree.add(UIHelper.buildImgButton("Move Down", evtObj, HyperionWindow.class.getResource("/res/down.png")));
		tbarTree.add(UIHelper.buildImgButton("Sort", evtObj, HyperionWindow.class.getResource("/res/sort.png")));
		tbarTree.addSeparator(new Dimension(2, 20));
		
		tbarTree.add(UIHelper.buildImgButton("Add New DSM Row", evtObj, HyperionWindow.class.getResource("/res/newrow.png")));
		tbarTree.add(UIHelper.buildImgButton("Rename", evtObj, HyperionWindow.class.getResource("/res/rename.png")));
		tbarTree.add(UIHelper.buildImgButton("Delete", evtObj, HyperionWindow.class.getResource("/res/delete.png")));
		
		//트리 패널 생성
		pnlTree = new JScrollPane();
		container.add(pnlTree, BorderLayout.CENTER);
		
		//트리 생성
		treeDSM = new JTree();
		
		//트리 패널(뷰포트 영역)에 트리 추가
		pnlTree.setViewportView(treeDSM);
		
		//트리에 팝업 메뉴 추가
		mnuTree = new JPopupMenu();
		addPopup(treeDSM, mnuTree);
		
		//트리 키 이벤트 리스너
		KeyListener kl = new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_F2){
					e.consume();
				}			
			}
		};
		
		//마우스 이벤트 리스너
		MouseListener ml = new MouseAdapter() {
		    public void mousePressed(MouseEvent e) {
		        int selRow = treeDSM.getRowForLocation(e.getX(), e.getY());
		        TreePath selPath = treeDSM.getPathForLocation(e.getX(), e.getY());
		        if(selRow != -1){
		        	//더블클릭일 때 항목명 수정할 수 있게 만듬
		        	//단, 루트 노드는 수정할 수 없게 만든다
		        	if(e.getClickCount() == 2){
		        		DefaultMutableTreeNode node = (DefaultMutableTreeNode)selPath.getLastPathComponent();
		        		if(node.isRoot() == false){
		        			editDSMNodeName((Data)node.getUserObject());
		        		}
		            }
		        }
		    }
		};
		treeDSM.addKeyListener(kl);
		treeDSM.addMouseListener(ml);
		
		//팝업 메뉴 설정
		mnuTree.add(UIHelper.buildMenuItem("Rename", evtObj));
		mnuTree.add(UIHelper.buildMenuItem("Duplicate", evtObj));
		mnuTree.add(UIHelper.buildMenuItem("Fork", evtObj));		
	}
	
	
	/**
	 * 루트 노드를 펼침
	 */
	void uiExpandRoot(){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		
		TreeNode[] nodes = dtm.getPathToRoot((TreeNode)dtm.getRoot());
		if(nodes == null || nodes.length == 0)
			return ;
		TreePath path = new TreePath(nodes);
		treeDSM.expandPath(path);		
	}
	
	/*
	 * Event Handlers
	 */
	
	/**
	 * 트리의 모든 노드를 확장시킨다
	 * @param ae
	 */
	private void uiToolbarExpandAll(ActionEvent ae){
		for(int i = 0 ; i < treeDSM.getRowCount(); i++){
			treeDSM.expandRow(i);
		}
	}
	
	/**
	 * 트리의 모든 노드를 축소시킨다
	 * @param ae
	 */
	private void uiToolbarCollapseAll(ActionEvent ae){
		for(int i = 0 ; i < treeDSM.getRowCount(); i++){
			treeDSM.collapseRow(i);
		}
	}
	
	/**
	 * DSM 또는 클러스터링 노드의 이름을 바꾼다
	 * @param ae
	 */
	private void uiToolbarRename(ActionEvent ae){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeDSM.getLastSelectedPathComponent();
		
		//선택된 항목이 없다면 변경할 수 없음		
		if(node == null) return;
		
		//DSM 노드명 변경
		editDSMNodeName((Data)node.getUserObject());
	}
	
	/**
	 * 타이탄 윈도우 복제
	 * @param ae
	 */
	private void uiMnuDuplicate(ActionEvent ae){
		EventSurrogate surrogate = EventSurrogateManager.selectSurrogate(this);
		DataController dcon = (DataController)surrogate.invoke("getDC");
		HyperionWindow dupWnd = new HyperionWindow();
		
		dupWnd.setTitle("Hyperion - duplicated");
		int idx = dcon.getRootData().getDataIndex(getSelectedNode());
		if(idx == -1) idx = 0;
		DataController dc = DataControllerFactory.getInstance(dupWnd.toString());
		
		
		dc.setData(dcon.duplicate(idx));
		dupWnd.setDataController(dc);
		dupWnd.attachToolBar();
		dupWnd.attachMenuBar();
		dupWnd.pack();
		dupWnd.setVisible(true);
		dupWnd.setCloseAction(WindowConstants.DISPOSE_ON_CLOSE);
	}

	/**
	 * 타이탄 윈도우 분기
	 * @param ae
	 */
	private void uiMnuFork(ActionEvent ae){
		EventSurrogate surrogate = EventSurrogateManager.selectSurrogate(this);
		DataController dcon = (DataController)surrogate.invoke("getDC");
		HyperionWindow dupWnd = new HyperionWindow();

		dupWnd.setTitle("Hyperion - edit");
		dupWnd.setDataController(dcon);
		dupWnd.attachToolBar();
		dupWnd.pack();
		dupWnd.setVisible(true);
		dupWnd.setCloseAction(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * 노드 삭제
	 * @param ae
	 */
	private void uiToolbarDelete(ActionEvent ae){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeDSM.getLastSelectedPathComponent();
		
		if(node == null || node.isRoot()) return;
		
		Data data = (Data)node.getUserObject();
		
		//트리에서 항목 삭제
		DefaultTreeModel model = (DefaultTreeModel)treeDSM.getModel();
		model.removeNodeFromParent(node);
		
		//데이터 컨트롤러에서도 항목 삭제
		EventSurrogate surrogate = EventSurrogateManager.selectSurrogate(this);
		DataController dcon = (DataController)surrogate.invoke("getDC");
		dcon.deleteItem(dcon.getRootData().getDataIndex(data));
	}
	
	/**
	 * 노드 사전순 정렬
	 * @param ae
	 */
	private void uiToolbarSort(ActionEvent ae){
		EventSurrogate surrogate = EventSurrogateManager.selectSurrogate(this);
		DataController dcon = (DataController)surrogate.invoke("getDC");
		int i = dcon.getRootData().getDataIndex(getSelectedNode());
		if(i == -1) i = 0;
		dcon.sort(i);//dcon.getRootData().getDataIndex(getSelectedNode()));
		surrogate.invoke("reloadDSM");
	}
	
	/**
	 * 새 DSM Row 추가
	 * @param ae
	 */
	void uiToolbarAddNewRow(ActionEvent ae){
		EventSurrogate surrogate = EventSurrogateManager.selectSurrogate(this);
		surrogate.invoke("newDSM", new Object[]{getSelectedNode(), "entity_" + (Integer)surrogate.invoke("getNewEntityCounter")});
		surrogate.invoke("reloadDSM");
	}
	
	/**
	 * 선택된 노드를 위로 이동
	 * @param ae
	 */
	void uiToolbarMoveUp(ActionEvent ae){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeDSM.getLastSelectedPathComponent();
		
		if(node == null) return;
		
		Data data = (Data)node.getUserObject();
		EventSurrogate surrogate = EventSurrogateManager.selectSurrogate(this);
		DataController dcon = (DataController)surrogate.invoke("getDC");
		dcon.moveUp(dcon.getRootData().getDataIndex(data));
		surrogate.invoke("reloadDSM");
		
		DefaultTreeModel model = (DefaultTreeModel)treeDSM.getModel();
		TreeNode[] nodes = model.getPathToRoot(_findNode(data));
		
		if(nodes == null || nodes.length == 0) return ;
		TreePath path = new TreePath(nodes);
		treeDSM.expandPath(path);
		treeDSM.setSelectionPath(new TreePath(nodes));
	}
	
	/**
	 * 선택된 노드를 아래로 이동
	 * @param ae
	 */
	void uiToolbarMoveDown(ActionEvent ae){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeDSM.getLastSelectedPathComponent();
		
		if(node == null)
			return;
		
		Data data = (Data)node.getUserObject();
		EventSurrogate s = EventSurrogateManager.selectSurrogate(this);
		DataController dc = (DataController)s.invoke("getDC");
		dc.moveDown(data);
		s.invoke("reloadDSM");
		
		DefaultTreeModel model = (DefaultTreeModel)treeDSM.getModel();
		TreeNode[] nodes = model.getPathToRoot(_findNode(data));
		
		if(nodes == null || nodes.length == 0) return ;
		TreePath path = new TreePath(nodes);
		treeDSM.expandPath(path);
		treeDSM.setSelectionPath(new TreePath(nodes));
	}

	/**
	 * 선택된 노드를 그룹으로 묶는다
	 * @param ae
	 */
	void uiToolbarGroup(ActionEvent ae){
		TreePath[] paths = treeDSM.getSelectionPaths();
		
		if(paths == null || paths.length == 0) return;
		
		EventSurrogate surrogate = EventSurrogateManager.selectSurrogate(this);
		DataController dcon = (DataController)surrogate.invoke("getDC");
		int pathIdx[] = new int[paths.length];
		int i = 0; 
		for(TreePath p : paths){
			DefaultMutableTreeNode no = ((DefaultMutableTreeNode)p.getLastPathComponent());
			pathIdx[i++] = dcon.getRootData().getDataIndex((Data)no.getUserObject());
		}
		String groupName = UIHelper.BuildInputDlg("Grouping", "Enter new group name", "");
		if(groupName == null || groupName.length() == 0){
			return ;
		}
		dcon.createGroup(pathIdx, groupName);
		surrogate.invoke("reloadDSM");
	}
	
	/**
	 * 그룹 풀기
	 * @param ae
	 */
	void uiToolbarUnGroup(ActionEvent ae){
		EventSurrogate s = EventSurrogateManager.selectSurrogate(this);
		DataController dc = (DataController)s.invoke("getDC");		
		dc.deleteGroup(dc.getRootData().getDataIndex(getSelectedNode()));
		s.invoke("reloadDSM");
	}
	
	/*
	 * Service Methods
	 */
	private void editDSMNodeName(Data dm){
		EventSurrogate s = EventSurrogateManager.selectSurrogate(this);
		DataController d = (DataController)s.invoke("getDC");
		String currentDSMName = dm.getName();
		boolean loopFlag = true;
		while(loopFlag){
			String input = TaskDialogs.input(null, "Edit DSM name", "Current : " + currentDSMName, "");
			
			if(input == null){
				//사용자가 취소를 눌렀음
				loopFlag = false;
			}else{
				//취소를 누르지 않은 경우 이름이 적절한지 확인하고 적절치 않다면 취소를 누르기까지 
				if(input.equals("")){
					TaskDialogs.error(null, "DSM name cannot be empty. Enter valid name.", "");
				}else{
					//중복된 아이템이 존재하는지 확인
					if(this.findNodeByName(input) != null){
						TaskDialogs.error(null, "DSM name must be unique. Try another name.", "");
					}else{
						d.setName(d.getRootData().getDataIndex(dm), input);
					
						//변경된 사항을 다시 그린다
						treeDSM.repaint();
						loopFlag = false;
					}
				}
			}
		}
	}

	private void popupEvtHandler(MouseEvent e, JPopupMenu popup){
		int x = e.getX();
		int y= e.getY();
		JTree target = (JTree)e.getSource();
		TreePath path = target.getPathForLocation(x, y);
		
		if(path == null)
			return ;
		
		target.setSelectionPath(path);
		popup.show(target, x, y);
	}
	
	
	
	private void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popupEvtHandler(e, popup);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popupEvtHandler(e, popup);
				}
			}
		});
	}
	
	protected JPanel getContainer(){
		return this.container;
	}
	

	
	/////////////////////////////////////
	//����� API
	/////////////////////////////////////
	private DefaultMutableTreeNode _findNodeByName(String s){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode)dtm.getRoot()).depthFirstEnumeration();
		
		while(e.hasMoreElements()){
			DefaultMutableTreeNode node = e.nextElement();
			if(s.equals(((Data)node.getUserObject()).getName()) == true){
				return node;
			}
		}
		return null;
	}


	private DefaultMutableTreeNode _findNode(Object o){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode)dtm.getRoot()).depthFirstEnumeration();
		
		while(e.hasMoreElements()){
			DefaultMutableTreeNode node = e.nextElement();
			if(o.equals(node.getUserObject()) == true){
				return node;
			}
		}
		return null;
	}
	
	/////////////////////////////////////
	//���� API
	/////////////////////////////////////
	public void setRoot(Object o){
		//get tree model
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		dtm.setRoot(new DefaultMutableTreeNode(o));
	}
	
	public Object getRoot(){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)dtm.getRoot();
		return root.getUserObject();
	}
	
	public void insertNode(Object parent, Object child){
		DefaultMutableTreeNode parentNode = _findNode(parent);
		parentNode.add(new DefaultMutableTreeNode(child));		
	}
	
	public void insertNodes(Object parent, Object[] childs){
		DefaultMutableTreeNode parentNode = _findNode(parent);
		
		for(Object c : childs){
			parentNode.add(new DefaultMutableTreeNode(c));
		}
	}
	
	public void removeNode(Object item){
		DefaultMutableTreeNode node = _findNode(item);
		node.removeFromParent();
	}
	
	public Object findNode(Object item){
		DefaultMutableTreeNode node = _findNode(item);		
		return node == null ? null : node.getUserObject();
	}
	
	public Object findNodeByName(String item){
		DefaultMutableTreeNode node = _findNodeByName(item);		
		return node == null ? null : node.getUserObject();
	}
	
	public int findNodeIndex(Object item){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode)dtm.getRoot()).depthFirstEnumeration();
		
		int i = 0;
		while(e.hasMoreElements()){
			DefaultMutableTreeNode node = e.nextElement();
			
			if(item.equals(node.getUserObject()) == true){
				return i;
			}
			if(((Data)node.getUserObject()).getChildLength() == 0)
				i++;
		}
		return -1;
	}
	
	public boolean isExpanded(Object item){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		DefaultMutableTreeNode target = _findNode(item);
		if(target == null)
			return false;
		TreeNode[] nodes = dtm.getPathToRoot(target);
		TreePath path = new TreePath(nodes);
		
		//부모노드가 접혀있으면 리프노드여도 false
		if(target.getParent() != null){
			Data dm = (Data)((DefaultMutableTreeNode)target.getParent()).getUserObject();
			if(isExpanded(dm) == true && target.isLeaf())
				return true;
		}
		//System.out.println("look for path, is expanded?" + treeDSM.isExpanded(path));
		return treeDSM.isExpanded(path);
	}
	
	public DefaultMutableTreeNode[] getVisibleTreeAllNodes(){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		Enumeration<DefaultMutableTreeNode> e = 
				((DefaultMutableTreeNode)dtm.getRoot()).preorderEnumeration();
		ArrayList<DefaultMutableTreeNode> vc = new ArrayList<DefaultMutableTreeNode>();
		
		while(e.hasMoreElements()){
			DefaultMutableTreeNode node = e.nextElement();
			
			if(node.isRoot() == false){
				if(isVisible(node.getUserObject()) != false){
					vc.add(node);
				}
			}
		}
		return vc.toArray(new DefaultMutableTreeNode[]{});
	}
	
	public boolean isNodeExpanded(DefaultMutableTreeNode node){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();		
		TreePath path = new TreePath(dtm.getPathToRoot(node));
		return treeDSM.isExpanded(path);
	}
	
	public boolean isVisible(Object item){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		DefaultMutableTreeNode target = _findNode(item);
		if(target == null)
			return false;
		TreeNode[] nodes = dtm.getPathToRoot(target);
		TreePath path = new TreePath(nodes);
		//System.out.println("PATH : " + ((Data)target.getUserObject()).getName() + ", vis : " + treeDSM.isVisible(path) + ", expand? " + treeDSM.isExpanded(path));
		return treeDSM.isVisible(path);
	}
	
	public boolean isVisibleNode(DefaultMutableTreeNode item){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		TreeNode[] nodes = dtm.getPathToRoot(item);
		TreePath path = new TreePath(nodes);
		//System.out.println("PATH : " + ((Data)target.getUserObject()).getName() + ", vis : " + treeDSM.isVisible(path) + ", expand? " + treeDSM.isExpanded(path));
		return treeDSM.isVisible(path);
	}
	
	public int getVisibleChildNodeCount(DefaultMutableTreeNode nm){
		int nCnt = nm.getChildCount();
		Enumeration<DefaultMutableTreeNode> e = nm.children();
		
		int visCnt = 0;
		while(e.hasMoreElements()){
			DefaultMutableTreeNode node = e.nextElement();
			
			if(isVisibleNode(node)){
				visCnt++;
			}
		}
		return visCnt;
	}
	
	public int getVisibleNodeCount(DefaultMutableTreeNode parent){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		Enumeration<DefaultMutableTreeNode> e = parent.depthFirstEnumeration();
		
		int count = 0;

		while(e.hasMoreElements()){
			DefaultMutableTreeNode node = e.nextElement();
			if(isVisible(node.getUserObject()) == false){
				//System.out.println("접혀진 노드 : " + ((Data)node.getUserObject()).getName());
			}else{
				//펼쳐진 모듈이면 카운트 안함
				//카운트 조건 : 보여지면서 펼쳐진 것이 아닌 것
				if(isExpanded(node.getUserObject()) == false || node.isLeaf()){
					//System.out.println("펼쳐진 노드 : " + ((Data)node.getUserObject()).getName());
					count++;
				}
			}
		}		
		return count;
	}
	
	public Data[] getVisibleNodes(){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode)dtm.getRoot()).depthFirstEnumeration();
		
		ArrayList<Data> vc = new ArrayList<Data>();
		int count = 0;

		while(e.hasMoreElements()){
			DefaultMutableTreeNode node = e.nextElement();
			if(isVisible(node.getUserObject()) != false){
				//펼쳐진 모듈이면 카운트 안함
				//카운트 조건 : 보여지면서 펼쳐진 것이 아닌 것
				if(isExpanded(node.getUserObject()) == false || node.isLeaf()){
					vc.add((Data)node.getUserObject());
				}
			}
		}		
		return vc.toArray(new Data[]{});
	}
	
	public DefaultMutableTreeNode[] getVisibleTreeNodes(){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode)dtm.getRoot()).depthFirstEnumeration();
		
		ArrayList<DefaultMutableTreeNode> vc = new ArrayList<DefaultMutableTreeNode>();
		int count = 0;

		while(e.hasMoreElements()){
			DefaultMutableTreeNode node = e.nextElement();
			if(isVisible(node.getUserObject()) != false){
				//펼쳐진 모듈이면 카운트 안함
				//카운트 조건 : 보여지면서 펼쳐진 것이 아닌 것
				if(isExpanded(node.getUserObject()) == false || node.isLeaf()){
					vc.add(node);
				}
			}
		}		
		return vc.toArray(new DefaultMutableTreeNode[]{});
	}
	
	//부모가 열려있으면서 비져블인 경우
	public int getVisibleLeafCount(){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode)dtm.getRoot()).depthFirstEnumeration();
		
		int count = 0;

		while(e.hasMoreElements()){
			DefaultMutableTreeNode node = e.nextElement();
			if(isVisible(node.getUserObject()) == false){
				//System.out.println("접혀진 노드 : " + ((Data)node.getUserObject()).getName());
			}else{
				//펼쳐진 모듈이면 카운트 안함
				//카운트 조건 : 보여지면서 펼쳐진 것이 아닌 것
				if(isExpanded(node.getUserObject()) == false || node.isLeaf()){
					//System.out.println("펼쳐진 노드 : " + ((Data)node.getUserObject()).getName());
					count++;
				}
			}
		}		
		return count;
	}
	
	public Data getSelectedNode(){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeDSM.getLastSelectedPathComponent();
		if(node == null)
			return null;
		Data dm = (Data)node.getUserObject();
		System.out.println(dm);
		return dm;
	}
	
	public String[] getItemText(){
		Vector<String> vc = new Vector<String>();
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode)dtm.getRoot()).depthFirstEnumeration();
		
		while(e.hasMoreElements()){
			DefaultMutableTreeNode node = e.nextElement();
			
			if(node.isLeaf()){
				vc.add(node.getUserObject().toString());
			}
		}
		return vc.toArray(new String[0]);
	}
	
	public int getLevel(DefaultMutableTreeNode node){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		TreeNode[] nodes = dtm.getPathToRoot(node);
		TreePath path = new TreePath(nodes);
		return nodes.length - 1;
	}
}
