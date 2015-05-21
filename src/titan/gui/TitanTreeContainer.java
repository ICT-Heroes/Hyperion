package titan.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
import javax.swing.tree.TreePath;


public final class TitanTreeContainer implements ActionListener{
	private JPanel			container;
	private JPanel			pnlToolbar;		//툴바 컨테이너
	private JScrollPane	pnlTree;		//트리 컨테이너
	private JToolBar		tbarTree;		//트리를 제어하는 데 사용할 툴바
	private JTree			treeDSM;		//DSM 트리
	private JPopupMenu	mnuTree;		//트리 메뉴
	
	
	{
		init();
	}
	
	private void init(){
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
		btnTmp.addActionListener(this);
		btnTmp.setToolTipText("Move Up");
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/up.png")));
		tbarTree.add(btnTmp);
		
		btnTmp = new JButton("");
		btnTmp.setActionCommand("Move Down");
		btnTmp.addActionListener(this);
		btnTmp.setToolTipText("Move Down");		
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/down.png")));
		tbarTree.add(btnTmp);
		
		btnTmp = new JButton("");
		btnTmp.setActionCommand("Sort");
		btnTmp.addActionListener(this);
		btnTmp.setToolTipText("Sort");
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/sort.png")));
		tbarTree.add(btnTmp);
		
		tbarTree.addSeparator(new Dimension(2, 20));
		
		btnTmp = new JButton("");
		btnTmp.setToolTipText("Add New DSM Row");
		btnTmp.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/newrow.png")));
		tbarTree.add(btnTmp);
		
		btnTmp = new JButton("");
		btnTmp.addActionListener(this);
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
		treeDSM.setModel(new DefaultTreeModel(
				new DefaultMutableTreeNode("Root"){
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					{
						DefaultMutableTreeNode node = new DefaultMutableTreeNode("Child1");
						add(node);
					}
				}//end of root
				)//end of new DefaultTreeModel
		);//end of setModel
		
		//뷰포트에 트리 추가
		pnlTree.setViewportView(treeDSM);
		
		//트리에 연결될 팝업 메뉴 생성
		mnuTree = new JPopupMenu();
		addPopup(treeDSM, mnuTree);
		
		//팝업 메뉴에 아이템 추가
		JMenuItem mntmTmp = new JMenuItem("Rename");
		mnuTree.add(mntmTmp);
		
		
	}
	@Override
	public void actionPerformed(ActionEvent arg0){
		// TODO Auto-generated method stub
		
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
}
