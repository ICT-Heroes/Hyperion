package view.titan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


public final class TitanTreeContainer{
	private JPanel			container;
	private JPanel			pnlToolbar;		//툴바 컨테이너
	private JScrollPane	pnlTree;		//트리 컨테이너
	private JToolBar		tbarTree;		//트리를 제어하는 데 사용할 툴바
	private JTree			treeDSM;		//DSM 트리
	private JPopupMenu	mnuTree;		//트리 메뉴
	private EventHandler	evtObj;			//이벤트 처리기
	
	
	{
		init();
	}
	
	private void init(){
		//이벤트 처리기 생성
		evtObj = new EventHandler();
		
		//컨테이너 생성
		container = new JPanel();
		
		//레이아웃 설정(hgap : 5, vgap : 1)
		container.setLayout(new BorderLayout(5, 1));
		
		//툴바 레이아웃 생성
		pnlToolbar = new JPanel(new FlowLayout());
		pnlToolbar.setBorder(null);
		
		FlowLayout flToolbar = (FlowLayout)pnlToolbar.getLayout();
		flToolbar.setHgap(0);
		flToolbar.setVgap(0);
		flToolbar.setAlignment(FlowLayout.LEFT);
		container.add(pnlToolbar, BorderLayout.NORTH);
			
		//툴바 생성
		tbarTree = new JToolBar();
		tbarTree.setForeground(new Color(255, 255, 255));
		tbarTree.setFloatable(false);
		pnlToolbar.add(tbarTree);
		
		JButton btnTmp;		
		btnTmp = new JButton("");
		btnTmp.setToolTipText("Expand All");
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/expand.png")));
		tbarTree.add(btnTmp);
		btnTmp = new JButton("");
		btnTmp.setToolTipText("Collapse All");
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/collapse.png")));
		tbarTree.add(btnTmp);
		tbarTree.addSeparator(new Dimension(2, 20));
		
		btnTmp = new JButton("");
		btnTmp.setToolTipText("Group");
		tbarTree.add(btnTmp);
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/group.png")));
		
		btnTmp = new JButton("");
		btnTmp.setToolTipText("Ungroup");
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/ungroup.png")));
		tbarTree.add(btnTmp);
		
		tbarTree.addSeparator(new Dimension(2, 20));
		
		btnTmp = new JButton("");
		btnTmp.setActionCommand("Move Up");
		btnTmp.addActionListener(evtObj);
		btnTmp.setToolTipText("Move Up");
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/up.png")));
		tbarTree.add(btnTmp);
		
		btnTmp = new JButton("");
		btnTmp.setActionCommand("Move Down");
		btnTmp.addActionListener(evtObj);
		btnTmp.setToolTipText("Move Down");		
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/down.png")));
		tbarTree.add(btnTmp);
		
		btnTmp = new JButton("");
		btnTmp.setActionCommand("Sort");
		btnTmp.addActionListener(evtObj);
		btnTmp.setToolTipText("Sort");
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/sort.png")));
		tbarTree.add(btnTmp);
		
		tbarTree.addSeparator(new Dimension(2, 20));
		
		btnTmp = new JButton("");
		btnTmp.setToolTipText("Add New DSM Row");
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/newrow.png")));
		tbarTree.add(btnTmp);
		
		btnTmp = new JButton("");
		btnTmp.addActionListener(evtObj);
		btnTmp.setActionCommand("Rename");
		btnTmp.setToolTipText("Rename");		
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/rename.png")));
		tbarTree.add(btnTmp);
		
		btnTmp = new JButton("");
		btnTmp.setToolTipText("Delete");
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/delete.png")));
		tbarTree.add(btnTmp);
		
		//트리 레이아웃 생성
		pnlTree = new JScrollPane();
		container.add(pnlTree, BorderLayout.CENTER);
		
		//트리 생성
		treeDSM = new JTree();
		treeDSM.setEditable(true);
		treeDSM.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Root")));
		
		//뷰포트에 트리 추가
		pnlTree.setViewportView(treeDSM);
		
		//트리에 연결될 팝업 메뉴 생성
		mnuTree = new JPopupMenu();
		addPopup(treeDSM, mnuTree);
		
		//팝업 메뉴에 아이템 추가
		JMenuItem mntmTmp = TitanUtil.buildMenuItem("Rename", evtObj);
		mnuTree.add(mntmTmp);
		mntmTmp = TitanUtil.buildMenuItem("Duplicate", evtObj);
		mnuTree.add(mntmTmp);
		mntmTmp = TitanUtil.buildMenuItem("Fork", evtObj);
		mnuTree.add(mntmTmp);		
	}
	
	void uiToolbarRename(ActionEvent ae){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeDSM.getLastSelectedPathComponent();
		
		//node가 루트라면 자식노드만 소트		
		if(node == null)
			return;
		
		//노드를 편집가능 상태로 만듬
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		TreeNode[] nodes = dtm.getPathToRoot(node);
		TreePath path = new TreePath(nodes);
		treeDSM.startEditingAtPath(path);
	}
	
	void uiMnuDuplicate(ActionEvent ae){
		//새로운 TitanWindow를 생성
		TitanWindow dupWnd = new TitanWindow();
		dupWnd.setTitle("TITAN - Duplicated");
		//wnd.attachToolBar();
		dupWnd.pack();
		dupWnd.setVisible(true);
	}

	void uiMnuFork(ActionEvent ae){
		//새로운 TitanWindow를 생성하되 DataSource를 동일한 것으로 제공
		TitanWindow forkWnd = new TitanWindow();
		forkWnd.setTitle("TITAN - Fork");
		forkWnd.pack();
		forkWnd.setVisible(true);
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
	
	/*
	 * 이벤트 처리용 내부 클래스(불필요하게 노출된 Public 메서드 제거)
	 */
	class EventHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent ae){
			switch(ae.getActionCommand()){
			case "Rename":
				//트리메뉴와 툴바의 Rename 동작 수행
				uiToolbarRename(ae);
				break;
			case "Duplicate":
				uiMnuDuplicate(ae);
				break;
			case "Fork":
				uiMnuFork(ae);
				break;
			}
		}		
	}
	
	/////////////////////////////////////
	//비공개 API
	/////////////////////////////////////
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
	//공개 API
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
	
	public void clearTree(){
		
	}
}
