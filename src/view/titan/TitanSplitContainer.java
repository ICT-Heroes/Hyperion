package view.titan;

import java.awt.event.ActionListener;

import javax.swing.JSplitPane;


public final class TitanSplitContainer{
	private TitanTreeContainer		treeContainer;
	private TitanTableContainer		tblContainer;
	private JSplitPane				spltPane;
	//초기화
	{
		init();
	}
	
	protected void init(){
		spltPane = new JSplitPane();
		
		//원클릭으로 각 pane을 열고 닫기 가능하게 만듬
		spltPane.setOneTouchExpandable(true);
		
		//포함된 컨테이너들을 연속된 배치로 보이게 만듬
		spltPane.setContinuousLayout(true);

		//컨테이너 생성 및 초기화
		treeContainer = new TitanTreeContainer();
		tblContainer = new TitanTableContainer();
		
		//splitpane에 부착
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
