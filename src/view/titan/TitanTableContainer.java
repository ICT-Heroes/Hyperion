package view.titan;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public final class TitanTableContainer{
	private JScrollPane container;	//컨테이너
	private JTable tblDSM;				//DSM 테이블
	private JTable tblRowHdr;			//Row Header용 테이블
	
	
	{
		init();
	}
	
	private void init(){
		container = new JScrollPane();
		
		//테이블 생성
		tblDSM = new JTable();
		
		//테이블 기본 데이터 모델 추가
		tblDSM.setModel(new DefaultTableModel(
				new Object[][]{
						{null},
						{null},
				},new String[]{
						"1", "2"
				}
		));
		
		//헤더 테이블 생성
		tblRowHdr = new RowNumberTable(tblDSM);
		
		container.setViewportView(tblDSM);
		container.setRowHeaderView(tblRowHdr);
	}
	
	JScrollPane getContainer(){
		return container;
	}
}
