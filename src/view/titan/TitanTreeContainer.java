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


public final class TitanTreeContainer{
	private JPanel			container;
	private JPanel			pnlToolbar;		//���� �����̳�
	private JScrollPane	pnlTree;		//Ʈ�� �����̳�
	private JToolBar		tbarTree;		//Ʈ���� �����ϴ� �� ����� ����
	private JTree			treeDSM;		//DSM Ʈ��
	private JPopupMenu	mnuTree;		//Ʈ�� �޴�
	private EventHandler	evtObj;			//�̺�Ʈ ó����
	
	
	{
		init();
	}
	
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
		
		//�˾� �޴��� ������ �߰�
		JMenuItem mntmTmp = TitanUtil.buildMenuItem("Rename", evtObj);
		mnuTree.add(mntmTmp);
		mntmTmp = TitanUtil.buildMenuItem("Duplicate", evtObj);
		mnuTree.add(mntmTmp);
		mntmTmp = TitanUtil.buildMenuItem("Fork", evtObj);
		mnuTree.add(mntmTmp);		
	}
	
	void uiToolbarRename(ActionEvent ae){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treeDSM.getLastSelectedPathComponent();
		
		//node�� ��Ʈ��� �ڽĳ�常 ��Ʈ		
		if(node == null)
			return;
		
		//��带 �������� ���·� ����
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		TreeNode[] nodes = dtm.getPathToRoot(node);
		TreePath path = new TreePath(nodes);
		treeDSM.startEditingAtPath(path);
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
	 * �̺�Ʈ ó���� ���� Ŭ����(���ʿ��ϰ� ����� Public �޼��� ����)
	 */
	class EventHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent ae){
			switch(ae.getActionCommand()){
			case "Rename":
				//Ʈ���޴��� ������ Rename ���� ����
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
	//����� API
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
	
	public int findNodeIndex(Object item){
		DefaultTreeModel dtm = (DefaultTreeModel)treeDSM.getModel();
		Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode)dtm.getRoot()).depthFirstEnumeration();
		
		int i = 0;
		while(e.hasMoreElements()){
			DefaultMutableTreeNode node = e.nextElement();
			if(item.equals(node.getUserObject()) == true){
				return i;
			}
			i++;
		}
		return -1;
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
