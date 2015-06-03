package view.titan;

import java.awt.Component;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;


public final class TitanTableContainer{
	private JScrollPane container;	//�����̳�
	private JTable tblDSM;				//DSM ���̺�
	private JTable tblRowHdr;			//Row Header�� ���̺�
	
	{
		init();
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	private void init(){
		container = new JScrollPane();
		
		//���̺� ����
		tblDSM = new JTable(){
			public boolean isCellEditable(int i, int i1) {
				return false; //To change body of generated methods, choose Tools | Templates.
			}
	    };
		
		//���̺� �⺻ ������ �� �߰�
		tblDSM.setModel(new DefaultTableModel(
				new Object[][]{{"."},},
				new String[]{"1",}
		));
		
		tblDSM.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);	
		
		//��� ���̺� ����
		tblRowHdr = new TitanRowNumberTable(tblDSM);
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
	
	public void addNewRow(Vector<?> d){
		DefaultTableModel model = (DefaultTableModel)tblDSM.getModel();
		model.addRow(d);
	}
	
	public void setRowHeaderTxt(String[] o){
		((TitanRowNumberTable)tblRowHdr).setHeaderName(o);
	}
	
	/*
	 * 반환값 : 토글 이전의 표시 여부를 반환
	 */
	public boolean toggleRowHeader(){
		return ((TitanRowNumberTable)tblRowHdr).toggleShowHeader();
	}
}