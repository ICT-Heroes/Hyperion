package view.titan.deprecated;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.ListSelectionModel;

import view.hyperion.RowNumberTable;

public class TitanWindowDeprecated extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JTable tblDSMMatrix;


	private JMenuBar mnuBar;
	private JMenu mnFile;
	private JMenuItem mntmNewTitanDsm;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mnNewClusetring;
	private JMenuItem mntmLoadClustering;
	private JMenuItem mntmSaveClustering;
	private JMenuItem mntmSaveClusteringAs;
	private JMenuItem mntmNewMenuItem_1;
	private JMenu mnView;
	private JMenuItem mntmRedraw;
	private JSeparator separator_4;
	private JMenuItem mntmShowRowLabels;
	private JMenuItem mntmChangeLaf;
	private JMenu mnAbout;
	private JMenuItem mntmAbout;
	private JToolBar tbarTop;
	private JButton btnOpenDSM;
	private JButton btnRedrawTable;
	private JButton btnNewClustering;
	private JButton btnLoadClustering;
	private JButton btnSaveClustering;
	private JButton btnSaveClusteringAs;
	private JSplitPane spltPan;
	private JPanel blLftPnl;
	private JPanel flToolbarPnl;
	private JToolBar tbarTree = new JToolBar();
	private JTree treDSM = new JTree();
	private JButton btnExpandAll;
	private JButton btnCollapseAll;
	private JButton btnGroupNode;
	private JButton btnUngroupNode;
	private JButton btnMoveUpNode;
	private JButton btnMoveDownNode;
	private JButton btnSortNodes;
	private JButton btnNewDSMRow;
	private JButton btnRenameNode;
	private JButton btnDeleteNode;
	private JScrollPane scrpanTree;
	private JPopupMenu popupMenu;
	private JMenuItem mntmTest;
	public TitanWindowDeprecated() {
		setTitle("TITAN DSM");
		
		mnuBar = new JMenuBar();
		setJMenuBar(mnuBar);
		
		mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		mnuBar.add(mnFile);
		
		mntmNewTitanDsm = new JMenuItem("New DSM");
		mntmNewTitanDsm.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mntmNewTitanDsm.addActionListener(this);
		mnFile.add(mntmNewTitanDsm);
		mnFile.add(new JSeparator());
		
		mntmNewMenuItem = new JMenuItem("Open DSM");
		mntmNewMenuItem.addActionListener(this);
		mntmNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmNewMenuItem);
		mnFile.add(new JSeparator());
		
		mnNewClusetring = new JMenuItem("New Clustering");
		mnNewClusetring.addActionListener(this);
		mnNewClusetring.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnFile.add(mnNewClusetring);
		
		mntmLoadClustering = new JMenuItem("Load Clustering");
		mntmLoadClustering.addActionListener(this);
		mntmLoadClustering.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnFile.add(mntmLoadClustering);
		mnFile.add(new JSeparator());
		
		mntmSaveClustering = new JMenuItem("Save Clustering");
		mntmSaveClustering.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSaveClustering);
		
		mntmSaveClusteringAs = new JMenuItem("Save Clustering As...");
		mntmSaveClusteringAs.addActionListener(this);
		mntmSaveClusteringAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnFile.add(mntmSaveClusteringAs);
		mnFile.add(new JSeparator());
		
		mntmNewMenuItem_1 = new JMenuItem("Exit");
		mnFile.add(mntmNewMenuItem_1);
		
		mnView = new JMenu("View");
		mnView.setMnemonic('V');
		mnuBar.add(mnView);
		
		mntmRedraw = new JMenuItem("Redraw");
		mntmRedraw.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		mnView.add(mntmRedraw);
		
		separator_4 = new JSeparator();
		mnView.add(separator_4);
		
		mntmShowRowLabels = new JMenuItem("Show Row Lables");
		mntmShowRowLabels.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
		mnView.add(mntmShowRowLabels);
		
		mntmChangeLaf = new JMenuItem("Change UI Theme");
		mnView.add(mntmChangeLaf);
		mntmChangeLaf.addActionListener(this);
		
		mnAbout = new JMenu("Help");
		mnAbout.setMnemonic('H');
		mnuBar.add(mnAbout);
		
		mntmAbout = new JMenuItem("About");
		mnAbout.add(mntmAbout);
		
		tbarTop = new JToolBar();
		getContentPane().add(tbarTop, BorderLayout.NORTH);
		
		btnOpenDSM = new JButton("");
		btnOpenDSM.setToolTipText("Open DSM");
		btnOpenDSM.setActionCommand("Open DSM");
		btnOpenDSM.addActionListener(this);
		btnOpenDSM.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/open-dsm.png")));
		tbarTop.add(btnOpenDSM);
		
		btnRedrawTable = new JButton("");
		btnRedrawTable.setToolTipText("Redraw");
		btnRedrawTable.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/redraw.png")));
		tbarTop.add(btnRedrawTable);
		
		tbarTop.addSeparator(new Dimension(2, 20));
		
		btnNewClustering = new JButton("");
		btnNewClustering.addActionListener(this);
		btnNewClustering.setActionCommand("New Clustering");
		btnNewClustering.setToolTipText("New Clustering");
		btnNewClustering.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/new-clsx.png")));
		tbarTop.add(btnNewClustering);
		
		btnLoadClustering = new JButton("");
		btnLoadClustering.addActionListener(this);
		btnLoadClustering.setToolTipText("Load Clustering");
		btnLoadClustering.setActionCommand("Load Clustering");
		btnLoadClustering.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/open-clsx.png")));
		tbarTop.add(btnLoadClustering);
		
		btnSaveClustering = new JButton("");
		btnSaveClustering.setActionCommand("Save Clustering");
		btnSaveClustering.addActionListener(this);
		btnSaveClustering.setToolTipText("Save Clustering");
		btnSaveClustering.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/save-clsx.png")));
		tbarTop.add(btnSaveClustering);
		
		btnSaveClusteringAs = new JButton("");
		btnSaveClusteringAs.setActionCommand("Save Clustering As...");
		btnSaveClusteringAs.addActionListener(this);
		btnSaveClusteringAs.setToolTipText("Save Clustering As...");
		btnSaveClusteringAs.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/save-clsx-as.png")));
		tbarTop.add(btnSaveClusteringAs);
		
		spltPan = new JSplitPane();
		spltPan.setOneTouchExpandable(true);
		spltPan.setContinuousLayout(true);
		getContentPane().add(spltPan, BorderLayout.CENTER);
		
		blLftPnl = new JPanel();
		spltPan.setLeftComponent(blLftPnl);
		blLftPnl.setLayout(new BorderLayout(5, 1));
		
		flToolbarPnl = new JPanel();
		flToolbarPnl.setBorder(null);
		FlowLayout fl_flToolbarPnl = (FlowLayout) flToolbarPnl.getLayout();
		fl_flToolbarPnl.setVgap(0);
		fl_flToolbarPnl.setHgap(0);
		fl_flToolbarPnl.setAlignment(FlowLayout.LEFT);
		blLftPnl.add(flToolbarPnl, BorderLayout.NORTH);
		
		tbarTree.setForeground(new Color(255, 255, 255));
		tbarTree.setFloatable(false);
		flToolbarPnl.add(tbarTree);
		
		btnExpandAll = new JButton("");
		btnExpandAll.setToolTipText("Expand All");
		btnExpandAll.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/expand.png")));
		tbarTree.add(btnExpandAll);
		btnCollapseAll = new JButton("");
		btnCollapseAll.setToolTipText("Collapse All");
		btnCollapseAll.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/collapse.png")));
		tbarTree.add(btnCollapseAll);
		tbarTree.addSeparator(new Dimension(2, 20));
		
		btnGroupNode = new JButton("");
		btnGroupNode.setToolTipText("Group");
		tbarTree.add(btnGroupNode);
		btnGroupNode.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/group.png")));
		
		btnUngroupNode = new JButton("");
		btnUngroupNode.setToolTipText("Ungroup");
		btnUngroupNode.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/ungroup.png")));
		tbarTree.add(btnUngroupNode);
		
		tbarTree.addSeparator(new Dimension(2, 20));
		
		btnMoveUpNode = new JButton("");
		btnMoveUpNode.setActionCommand("Move Up");
		btnMoveUpNode.addActionListener(this);
		btnMoveUpNode.setToolTipText("Move Up");
		btnMoveUpNode.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/up.png")));
		tbarTree.add(btnMoveUpNode);
		
		btnMoveDownNode = new JButton("");
		btnMoveDownNode.setActionCommand("Move Down");
		btnMoveDownNode.addActionListener(this);
		btnMoveDownNode.setToolTipText("Move Down");		
		btnMoveDownNode.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/down.png")));
		tbarTree.add(btnMoveDownNode);
		
		btnSortNodes = new JButton("");
		btnSortNodes.setActionCommand("Sort");
		btnSortNodes.addActionListener(this);
		btnSortNodes.setToolTipText("Sort");
		btnSortNodes.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/sort.png")));
		tbarTree.add(btnSortNodes);
		
		tbarTree.addSeparator(new Dimension(2, 20));
		
		btnNewDSMRow = new JButton("");
		btnNewDSMRow.setToolTipText("Add New DSM Row");
		btnNewDSMRow.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/newrow.png")));
		tbarTree.add(btnNewDSMRow);
		
		btnRenameNode = new JButton("");
		btnRenameNode.addActionListener(this);
		btnRenameNode.setActionCommand("Rename");
		btnRenameNode.setToolTipText("Rename");		
		btnRenameNode.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/rename.png")));
		tbarTree.add(btnRenameNode);
		
		btnDeleteNode = new JButton("");
		btnDeleteNode.setToolTipText("Delete");
		btnDeleteNode.setIcon(new ImageIcon(TitanWindowDeprecated.class.getResource("/res/delete.png")));
		tbarTree.add(btnDeleteNode);
		
		scrpanTree = new JScrollPane();
		blLftPnl.add(scrpanTree, BorderLayout.CENTER);
		
		treDSM.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				System.out.println("is changed");
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)treDSM.getLastSelectedPathComponent();
				if(node == null)
					return;
				Object Node = node.getUserObject();
				
				if(node.isLeaf()){
					System.out.println(node.toString());
				}
			}
		});
		treDSM.setEditable(true);
		treDSM.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Demo") {
				{
					
				}
			}
		));
		scrpanTree.setViewportView(treDSM);
		
		popupMenu = new JPopupMenu();
		addPopup(treDSM, popupMenu);
		
		mntmTest = new JMenuItem("Rename");
		popupMenu.add(mntmTest);
		
		
		JScrollPane scrpanTbl = new JScrollPane();
		spltPan.setRightComponent(scrpanTbl);
		
		tblDSMMatrix = new JTable();
		tblDSMMatrix.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		tblDSMMatrix.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblDSMMatrix.setCellSelectionEnabled(true);
		tblDSMMatrix.setColumnSelectionAllowed(true);
		tblDSMMatrix.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"New column", "New column", "New column", "New column", "New column"
			}
		));
		JTable tblRowHdr = new RowNumberTable(tblDSMMatrix);
		scrpanTbl.setRowHeaderView(tblRowHdr);
		scrpanTbl.setViewportView(tblDSMMatrix);
		
		//If this program runs on windows, set LaF Windows(not the classic)
		//else, set LaF to Nimbus
		if(System.getProperty("os.name").contains("Windows") == true){
			changeLaF(this, "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}else{
			changeLaF(this, "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void changeLaF(Component c, String className){
		try{
			//����� ���� �� ������ ��� ������Ʈ�� �׸��� �缳�� �� �� ���� ����
			UIManager.setLookAndFeel(className);
			SwingUtilities.updateComponentTreeUI(c);
			this.pack();
		}catch(ClassNotFoundException | InstantiationException
				| IllegalAccessException
				| UnsupportedLookAndFeelException e1){
			e1.printStackTrace();
		}
	}
	
	public void uiMnuChangeLafHandler(ActionEvent e){
		//UI�����ڷκ��� ���� JREȯ�濡 ��ġ�� �⺻ LaFŬ������ ����
		UIManager.LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
		
		//LaF���� ���н� �ƹ��� ������ ���� �ʴ´�
		if(lafs == null || lafs.length == 0){
			return ;
		}
		
		//�޺��ڽ��� ǥ���� LaF�̸��� ����
		String[] lafName = new String[lafs.length];
		for(int i = 0; i < lafs.length; i++){
			lafName[i] = lafs[i].getName();
		}
		
		String selLaFName = (String)JOptionPane.showInputDialog(
								null, "�ý��� UI�׸��� �����ϼ���.", "UI �׸� ����", 
								JOptionPane.PLAIN_MESSAGE, null,
								lafName, lafName[0]);
		
		//���õ� LaF Name�� UI Manager���� �뺸�ϰ� ��ü �ý��� UI Theme ����
		for(UIManager.LookAndFeelInfo laf : lafs){
			if(laf.getName().equals(selLaFName) == true){
				changeLaF(this, laf.getClassName());
			}
		}
	}
	
	public void uiMnuNewDSM(ActionEvent e){
		//�ؽ�Ʈ �ʵ� ����
		final JTextField txt = new JTextField(10);
		
		//������� ����
		Object[] bodyAndTxtField = {"How many rows added?\n", txt};
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
		final JDialog dialog = new JDialog(this, "New DSM", true);
		dialog.setContentPane(optionPane);
		
		//���̾�αװ� �ڵ����� ������ �ʰ� ����
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		//���̾�αװ� ������ �ؽ�Ʈ�ʵ�� ��Ŀ���� �̵��ϵ��� �̺�Ʈ �ڵ鷯 ���� 
		dialog.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentShown(ComponentEvent ce){
				txt.requestFocusInWindow();
			}
		});
		
		//X��ư�� ������ �� ó���� ���� �̺�Ʈ �ڵ鷯 �߰� 
		dialog.addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent we){
		    	//X��ư�� �����ٸ� �׳� ����������
		    	dialog.dispose();
		    }
		});
		
		//�Ӽ�(���� ��� ��ư�� ���ȴٴ���)�� ����� ��츦 Ȯ��
		optionPane.addPropertyChangeListener(
		    new PropertyChangeListener() {
		        public void propertyChange(PropertyChangeEvent e) {
		            String prop = e.getPropertyName();
		
		            if (dialog.isVisible() 
		             && (e.getSource() == optionPane)
		             && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
		                //If you were going to check something
		                //before closing the window, you'd do
		                //it here.
		                //dialog.setVisible(false);
		            	Object value = optionPane.getValue();
		            	
		            	if(value == JOptionPane.UNINITIALIZED_VALUE){
		            		return;
		            	}
		            	
		            	//�� ���� �������� ������ �������� ������ �̺�Ʈ�� �߻���Ű����
		            	//������Ƽ ���� �̺�Ʈ�� �߻����� �ʰ� �ȴ�.
		            	optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
		            	
		            	if("OK".equals(value)){
		            		String typedText = txt.getText();
		            		int values = 0;
		            		
		            		try{
		            			values = Integer.parseInt(typedText);
		            		}catch(NumberFormatException nfe){
		            			JOptionPane.showMessageDialog(null, "Value must be positive integer, try again.");
		            		}
		            		System.out.println(values);
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
		dialog.setLocationRelativeTo(this);
		
		//���̾�α� ����
		dialog.setVisible(true);
		
		//���⿡ �ʿ��� �ڵ� �ۼ�
	}
	
	void uiMnuOpenDSM(ActionEvent e){
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Open DSM");
		FileNameExtensionFilter flt = new FileNameExtensionFilter("DSM File(*.dsm)", "dsm");
		jfc.setFileFilter(flt);
		if(jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			//File out = jfc.getSelectedFile();			
			//JOptionPane.showMessageDialog(null, "���� ���Ϸ� �����½��ϴ�.");
		}
	}
	
	void uiMnuLoadClustering(ActionEvent e){
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Load Clustering");
		FileNameExtensionFilter flt = new FileNameExtensionFilter("Clustering File(*.clsx)", "clsx");
		jfc.setFileFilter(flt);
		if(jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			//File out = jfc.getSelectedFile();			
			//JOptionPane.showMessageDialog(null, "���� ���Ϸ� �����½��ϴ�.");
		}
	}
	
	void uiMnuSaveClustering(ActionEvent e){
		JOptionPane.showMessageDialog(this, "Clustering saved...");
	}
	
	void uiMnuSaveClusteringAs(ActionEvent e){
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Save Clustering File As...");
		FileNameExtensionFilter flt = new FileNameExtensionFilter("Clustering File(*.clsx)", "clsx");
		jfc.setFileFilter(flt);
		if(jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
			//File out = jfc.getSelectedFile();			
			//JOptionPane.showMessageDialog(null, "���� ���Ϸ� �����½��ϴ�.");
		}
	}
	
	void uiMnuNewClustering(ActionEvent e){
		JOptionPane.showMessageDialog(this, "All grouped matrix are reverted...");
	}
	
	void uiToolbarMoveUp(ActionEvent e){
		//JOptionPane.showMessageDialog(this, "Item move up");
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treDSM.getLastSelectedPathComponent();
		
		
		if(node == null)
			return;
		
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		
		int nodeIdx = node.getParent().getIndex(node);
		System.out.println(nodeIdx);
		
		if(nodeIdx > 0){
			//moving up
			DefaultTreeModel tmo = (DefaultTreeModel)treDSM.getModel();
			//tmo.removeNodeFromParent(node);
			//tmo.nodeChanged(node);
			tmo.insertNodeInto(node,  parent, nodeIdx-1);
			tmo.nodeStructureChanged(parent);
			
			//make the tree select moved item
			TreeNode[] nodes = tmo.getPathToRoot(node);
			TreePath path = new TreePath(nodes);
			treDSM.setSelectionPath(path);
		}
	}
	
	void uiToolbarMoveDown(ActionEvent e){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treDSM.getLastSelectedPathComponent();
		
		
		if(node == null)
			return;
		
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		
		int nodeIdx = node.getParent().getIndex(node);
		System.out.println(nodeIdx);
		
		if(nodeIdx < parent.getChildCount() -1){
			//moving up
			DefaultTreeModel tmo = (DefaultTreeModel)treDSM.getModel();
			//tmo.removeNodeFromParent(node);
			//tmo.nodeChanged(node);
			tmo.insertNodeInto(node,  parent, nodeIdx+1);
			tmo.nodeStructureChanged(parent);
			
			//make the tree select moved item
			TreeNode[] nodes = tmo.getPathToRoot(node);
			TreePath path = new TreePath(nodes);
			treDSM.setSelectionPath(path);
		}
		
	}
	void uiToolbarSort(ActionEvent e){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treDSM.getLastSelectedPathComponent();
		
		//node�� ��Ʈ��� ���������� �ڽĳ�常 ��Ʈ		
		if(node == null)
			return;
		
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)node.getRoot();
		
		if(root.equals(node)){
			System.out.println("you select root node.");
			//��Ʈ�� �����ߴٸ� ��Ʈ�� �ڽ��� �ִ��� Ȯ��
			node = (DefaultMutableTreeNode)root.getFirstChild();
			
			if(node == null){
				System.out.println("there are no childs.");
				return ;
			}
			//�ڽĳ�尡 �ִٸ� �ش� ��带 �����ϰ� ���� ����....
		}
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		
		for(int i = 0; i < parent.getChildCount(); i++){
			DefaultMutableTreeNode f1 = (DefaultMutableTreeNode)parent.getChildAt(i);
			
			for(int j =0; j < i; j++){
				DefaultMutableTreeNode f2 = (DefaultMutableTreeNode)parent.getChildAt(j);
				if(f1.toString().compareTo(f2.toString()) < 0){
					parent.insert(f1, j);
					break;
				}
			}
		}
		
		DefaultTreeModel tmo = (DefaultTreeModel)treDSM.getModel();
		tmo.reload();
		
		//and set focused user selected items....
	}
	
	void uiToolbarRename(ActionEvent e){
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)treDSM.getLastSelectedPathComponent();
		
		//node�� ��Ʈ��� ���������� �ڽĳ�常 ��Ʈ		
		if(node == null)
			return;
		
		//make the tree select moved item
		DefaultTreeModel tmo = (DefaultTreeModel)treDSM.getModel();
		TreeNode[] nodes = tmo.getPathToRoot(node);
		TreePath path = new TreePath(nodes);
		treDSM.startEditingAtPath(path);
	}
	@Override
	public void actionPerformed(ActionEvent e){
		//�̺�Ʈ�� ���� UI ��Ҹ� �ľ��ϰ� ������ �̺�Ʈ �ڵ鷯 ȣ��
		System.out.println(e.getActionCommand());
		System.out.println("event triggered.");
		switch(e.getActionCommand()){
		case "Change UI Theme":
			uiMnuChangeLafHandler(e);
			break;
		case "New DSM":
			uiMnuNewDSM(e);
			break;
		case "Open DSM":
			uiMnuOpenDSM(e);
			break;
		case "New Clustering":
			uiMnuNewClustering(e);
			break;
		case "Load Clustering":
			uiMnuLoadClustering(e);
			break;
		case "Save Clustering":
			uiMnuSaveClustering(e);
			break;
		case "Save Clustering As...":
			uiMnuSaveClusteringAs(e);
			break;
		case "Move Up":
			uiToolbarMoveUp(e);
			break;
		case "Move Down":
			uiToolbarMoveDown(e);
			break;
		case "Sort":
			uiToolbarSort(e);
			break;
		case "Rename":
			uiToolbarRename(e);
			break;
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
}
