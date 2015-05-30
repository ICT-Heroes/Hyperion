package view.titan;

import java.awt.event.ActionListener;

import javax.swing.JSplitPane;


public final class TitanSplitContainer{
	private TitanTreeContainer		treeContainer;
	private TitanTableContainer		tblContainer;
	private JSplitPane				spltPane;
	//�ʱ�ȭ
	{
		init();
	}
	
	protected void init(){
		spltPane = new JSplitPane();
		
		//��Ŭ������ �� pane�� ���� �ݱ� �����ϰ� ����
		spltPane.setOneTouchExpandable(true);
		
		//���Ե� �����̳ʵ��� ���ӵ� ��ġ�� ���̰� ����
		spltPane.setContinuousLayout(true);

		//�����̳� ���� �� �ʱ�ȭ
		treeContainer = new TitanTreeContainer();
		tblContainer = new TitanTableContainer();
		
		//splitpane�� ����
		spltPane.setLeftComponent(treeContainer.getContainer());
		spltPane.setRightComponent(tblContainer.getContainer());		
		
	}

	public void addActionListener(ActionListener listener){
	}
	
	JSplitPane getContainer(){
		return spltPane;
	}
	
	TitanTreeContainer getTreeContainer(){
		return treeContainer;
	}
	
	TitanTableContainer getTableContainer(){
		return tblContainer;
	}
}
