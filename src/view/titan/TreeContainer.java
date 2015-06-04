  package view.titan;

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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import view.titan.deprecated.TitanWindowDeprecated;
import model.Data;

import com.ezware.dialog.task.TaskDialogs;

import controller.DataController;


public final class TreeContainer{
	private JPanel				container;
	private JPanel				pnlToolbar;		//���� �����̳�
	private JScrollPane		pnlTree;		//Ʈ�� �����̳�
	private JToolBar			tbarTree;		//Ʈ���� �����ϴ� �� ����� ����
	private JTree				treeDSM;		//DSM Ʈ��
	private JPopupMenu		mnuTree;		//Ʈ�� �޴�
	private EventHandler		evtObj;			//�̺�Ʈ ó����
	private DataController	dc;
	
	void setDataController(DataController dc){
		this.dc = dc;
	}
	
	
	{
		init();
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	private void init(){		
		//�̺�Ʈ ó���� ����
		evtObj = new EventHandler();
		
		//�����̳� ����
		container = new JPanel();
		
		//���̾ƿ� ����(hgap : 5, vgap : 1)
		container.setLayout(new BorderLayout(5, 1));
		
		//���� ���̾ƿ� ����
		pnlToolbar = new JPanel(new FlowLayout());
		pnlToolbar.setBorder(null);
		
		FlowLayout flToolbar = (FlowLayout)pnlToolbar.getLayout();
		flToolbar.setHgap(0);
		flToolbar.setVgap(0);
		flToolbar.setAlignment(FlowLayout.LEFT);
		container.add(pnlToolbar, BorderLayout.NORTH);
			
		//���� ����
		tbarTree = new JToolBar();
		tbarTree.setForeground(new Color(255, 255, 255));
		tbarTree.setFloatable(false);
		pnlToolbar.add(tbarTree);
		
		JButton btnTmp;		
		btnTmp = new JButton("");
		btnTmp.setToolTipText("Expand All");
		btnTmp.setActionCommand("Expand All");
		btnTmp.addActionListener(evtObj);
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/expand.png")));
		tbarTree.add(btnTmp);
		btnTmp = new JButton("");
		btnTmp.setToolTipText("Collapse All");
		btnTmp.setActionCommand("Collapse All");
		btnTmp.addActionListener(evtObj);
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
		btnTmp.setActionCommand("Add New DSM Row");
		btnTmp.addActionListener(evtObj);
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
		btnTmp.addActionListener(evtObj);
		btnTmp.setActionCommand("Delete");
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/delete.png")));
		tbarTree.add(btnTmp);
		
		//Ʈ�� ���̾ƿ� ����
		pnlTree = new JScrollPane();
		container.add(pnlTree, BorderLayout.CENTER);
		
		//Ʈ�� ����
		treeDSM = new JTree();
		treeDSM.setEditable(true);

		treeDSM.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Root")));
		
		//����Ʈ�� Ʈ�� �߰�
		pnlTree.setViewportView(treeDSM);
		
		//Ʈ���� ����� �˾� �޴� ����
		mnuTree = new JPopupMenu();
		addPopup(treeDSM, mnuTree);
		

		KeyListener kl = new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				int keycode = e.getKeyCode();
				if(keycode == KeyEvent.VK_F2){
					//absorb the event and open dsm row editor
					e.consume();
				}
				
				
			}
		};
		treeDSM.addKeyListener(kl);
		MouseListener ml = new MouseAdapter() {
		    public void mousePressed(MouseEvent e) {
		        int selRow = treeDSM.getRowForLocation(e.getX(), e.getY());
		        TreePath selPath = treeDSM.getPathForLocation(e.getX(), e.getY());
		        if(selRow != -1){
		        	if(e.getClickCount() == 2){
		        		DefaultMutableTreeNode node = (DefaultMutableTreeNode)selPath.getLastPathComponent();
		        		if(node.isRoot() == false){
		        			editDSMNodeName((Data)node.getUserObject());
		        		}
		            }
		        }
		    }
		};
		treeDSM.addMouseListener(ml);
		
		//�˾� �޴��� ������ �߰�
		JMenuItem mntmTmp = UIHelper.buildMenuItem("Rename", evtObj);
		mnuTree.add(mntmTmp);
		mntmTmp = UIHelper.buildMenuItem("Duplicate", evtObj);
		mnuTree.add(mntmTmp);
		mntmTmp = UIHelper.buildMenuItem("Fork", evtObj);
		mnuTree.add(mntmTmp);		
	}
	
	void uiExpandRoot(){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		
		TreeNode[] nodes = dtm.getPathToRoot((TreeNode)dtm.getRoot());
		TreePath path = new TreePath(nodes);
		treeDSM.expandPath(path);
	}
	void uiToolbarExpandAll(ActionEvent ae){
		for(int i = 0 ; i < treeDSM.getRowCount(); i++){
			treeDSM.expandRow(i);
		}
	}
	
	void uiToolbarCollapseAll(ActionEvent ae){
		for(int i = 0 ; i < treeDSM.getRowCount(); i++){
			treeDSM.collapseRow(i);
		}
	}
	
	private void editDSMNodeName(Data dm){
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
						dc.setName(dm, currentDSMName, input);
					
						//변경된 사항을 다시 그린다
						treeDSM.repaint();
						loopFlag = false;
					}
				}
			}
		}
	}
	
	void uiToolbarRename(ActionEvent ae){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeDSM.getLastSelectedPathComponent();
		
		//node�� ��Ʈ��� �ڽĳ�常 ��Ʈ		
		if(node == null)
			return;
		
		//��带 �������� ���·� ����
		editDSMNodeName((Data)node.getUserObject());
	}
	
	void uiMnuDuplicate(ActionEvent ae){
		//���ο� TitanWindow�� ����
		TitanWindow dupWnd = new TitanWindow();
		dupWnd.setTitle("TITAN - Duplicated");
		//wnd.attachToolBar();
		dupWnd.pack();
		dupWnd.setVisible(true);
	}

	void uiMnuFork(ActionEvent ae){
		//���ο� TitanWindow�� �����ϵ� DataSource�� ������ ������ ����
		TitanWindow forkWnd = new TitanWindow();
		forkWnd.setTitle("TITAN - Fork");
		forkWnd.pack();
		forkWnd.setVisible(true);
	}
	
	void uiToolbarAddNewRow(ActionEvent ae){
		EventSurrogate s = EventSurrogateManager.selectSurrogate(this);
		Data d = (Data)s.invoke("getData");
		s.invoke("newDSM", new Object[]{d, "test"});
		this.treeDSM.repaint();
	}
	
	void uiToolbarDelete(ActionEvent ae){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeDSM.getLastSelectedPathComponent();
		
		if(node == null)
			return;
		
		Data data = (Data)node.getUserObject();
		
		if(node.isLeaf()){
			DefaultTreeModel model = (DefaultTreeModel)treeDSM.getModel();
			node.removeFromParent();			
			model.reload();
			dc.deleteItem(data, data.toString());		
			
			treeDSM.repaint();
		}else{
			
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
	
	void uiToolbarMoveUp(ActionEvent ae){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeDSM.getLastSelectedPathComponent();
		
		if(node == null)
			return;
		
		Data data = (Data)node.getUserObject();
		EventSurrogate s = EventSurrogateManager.selectSurrogate(this);
		DataController dc = (DataController)s.invoke("getDC");
		Data root = (Data)s.invoke("getData");
		dc.moveUp(root, data.getName());
		s.invoke("reloadDSM");
	}
	
	void uiToolbarMoveDown(ActionEvent ae){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeDSM.getLastSelectedPathComponent();
		
		if(node == null)
			return;
		
		Data data = (Data)node.getUserObject();
		EventSurrogate s = EventSurrogateManager.selectSurrogate(this);
		DataController dc = (DataController)s.invoke("getDC");
		Data root = (Data)s.invoke("getData");
		dc.moveDown(root, data.getName());
		s.invoke("reloadDSM");
	}
	/*
	 * �̺�Ʈ ó���� ���� Ŭ����(���ʿ��ϰ� ����� Public �޼��� ����)
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
				
			}
		}		
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
	public void uiToolbarSort(ActionEvent ae){	
		//정렬을 어떻게하려나...
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeDSM.getLastSelectedPathComponent();
		
		//선택한 노드가 없다
		if(node == null)
			return;
		
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)node.getRoot();
		
		if(root.equals(node)){
			//��Ʈ�� �����ߴٸ� ��Ʈ�� �ڽ��� �ִ��� Ȯ��
			node = (DefaultMutableTreeNode)root.getFirstChild();
			
			if(node == null){
				return ;
			}
		}
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		EventSurrogate s = EventSurrogateManager.selectSurrogate(this);
		DataController dc = (DataController)s.invoke("getDC");
		//데이터
		
		for(int i = 0; i < parent.getChildCount(); i++){
			DefaultMutableTreeNode f1 = (DefaultMutableTreeNode)parent.getChildAt(i);
			
			for(int j =0; j < i; j++){
				DefaultMutableTreeNode f2 = (DefaultMutableTreeNode)parent.getChildAt(j);
				if(f1.toString().compareTo(f2.toString()) < 0){
					dc.moveUp((Data)parent.getUserObject(), ((Data)f2.getUserObject()).getName());
					break;
				}
			}
		}
		s.invoke("reloadDSM");
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
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)dtm.getRoot();
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
	
	public boolean isVisible(Object item){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		DefaultMutableTreeNode target = _findNode(item);
		if(target == null)
			return false;
		TreeNode[] nodes = dtm.getPathToRoot(target);
		TreePath path = new TreePath(nodes);
		System.out.println("PATH : " + ((Data)target.getUserObject()).getName() + ", vis : " + treeDSM.isVisible(path) + ", expand? " + treeDSM.isExpanded(path));
		return treeDSM.isVisible(path);
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
	
	
	//부모가 열려있으면서 비져블인 경우
	public int getVisibleLeafCount(){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode)dtm.getRoot()).depthFirstEnumeration();
		
		int count = 0;

		while(e.hasMoreElements()){
			DefaultMutableTreeNode node = e.nextElement();
			if(isVisible(node.getUserObject()) == false){
				System.out.println("접혀진 노드 : " + ((Data)node.getUserObject()).getName());
			}else{
				//펼쳐진 모듈이면 카운트 안함
				//카운트 조건 : 보여지면서 펼쳐진 것이 아닌 것
				if(isExpanded(node.getUserObject()) == false || node.isLeaf()){
					System.out.println("펼쳐진 노드 : " + ((Data)node.getUserObject()).getName());
					count++;
				}
			}
		}		
		return count;
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
	public void clearTree(){
		
	}
}
