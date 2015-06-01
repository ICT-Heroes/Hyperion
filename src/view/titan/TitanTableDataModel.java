package view.titan;

import javax.swing.table.DefaultTableModel;

public class TitanTableDataModel extends DefaultTableModel{
	public boolean isCellEditable(int row, int col){
        return false;
    }
}
