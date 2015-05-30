package view.titan;

import java.awt.Component;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import model.Data;


public final class TitanTableContainer{
	private JScrollPane container;	//컨테이너
	private JTable tblDSM;				//DSM 테이블
	private JTable tblRowHdr;			//Row Header용 테이블
	
	{
		init();
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
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
						"1",
				}
		));
		
		tblDSM.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);	
		
		//헤더 테이블 생성
		tblRowHdr = new RowNumberTable(tblDSM);
		tblRowHdr.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		
		container.setViewportView(tblDSM);
		container.setRowHeaderView(tblRowHdr);
	}
	
	JScrollPane getContainer(){
		return container;
	}
	
	public void setColumnSize(int size){
		Integer[] colHeader = new Integer[size];
		
		for(int i = 0; i < size; i++){
			colHeader[i] = i + 1;
		}
		
		tblDSM.setModel(new DefaultTableModel(
				null,
				colHeader
				));
	}
	
	public void setColumnSizePref(){
		final TableColumnModel columnModel = tblDSM.getColumnModel();
		int totalWidth = 0;
		int width = 25; // Min width
	    for (int column = 0; column < tblDSM.getColumnCount(); column++) {
	            TableCellRenderer renderer = tblDSM.getCellRenderer(0, column);
	            Component comp = tblDSM.prepareRenderer(renderer, 0, column);
	            width = Math.max(comp.getPreferredSize().width, width);
	            totalWidth += width;
	        columnModel.getColumn(column).setPreferredWidth(width);
	    }
	    
	    tblDSM.setSize(tblDSM.getHeight(), totalWidth);
	}
	
	public void addNewRow(Vector d){
		DefaultTableModel model = (DefaultTableModel)tblDSM.getModel();
		model.addRow(d);
	}
	
	public void setRowHeaderTxt(String[] o){
		((RowNumberTable)tblRowHdr).setHeaderName(o);
	}
}