  package view.titan;

import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/*
 *	Use a JTable as a renderer for row snumbers of a given main table.
 *  This table must be added to the row header of the scrollpane that
 *  contains the main table.
 */
public class RowNumberTable extends JTable 	implements ChangeListener, PropertyChangeListener, TableModelListener{
	private static final long serialVersionUID = 1L;
	private JTable main;
	private static String[] hdrName = new String[]{"Root"};
	private static boolean showHeader = false;
	
	public void setHeaderName(String[] hdr){
		hdrName = hdr;
	}
	public void resizeColumnWidth(JTable table) {
	    final TableColumnModel columnModel = table.getColumnModel();
	    int width = 50; // Min width
	    for (int column = 0; column < table.getColumnCount(); column++) {
	        
	        for (int row = 0; row < table.getRowCount(); row++) {
	            TableCellRenderer renderer = table.getCellRenderer(row, column);
	            Component comp = table.prepareRenderer(renderer, row, column);
	            width = Math.max(comp.getPreferredSize().width, width);
	        }
	        columnModel.getColumn(column).setPreferredWidth(width);
	        setPreferredScrollableViewportSize(getPreferredSize());
	    }
	}
	public boolean toggleShowHeader(){
		showHeader = !showHeader;
		resizeColumnWidth(this);
		this.repaint();
		
		return !showHeader;
	}
	public RowNumberTable(JTable table)
	{
		main = table;
		main.addPropertyChangeListener( this );
		main.getModel().addTableModelListener( this );

		setFocusable( false );
		setAutoCreateColumnsFromModel( false );
		setSelectionModel( main.getSelectionModel() );
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


		TableColumn column = new TableColumn();
		column.setHeaderValue(" ");
		addColumn( column );
		column.setCellRenderer(new RowNumberRenderer());

		resizeColumnWidth(this);
		//getColumnModel().getColumn(0).setPreferredWidth(250);
		//setPreferredScrollableViewportSize(getPreferredSize());
	}

	@Override
	public void addNotify()
	{
		super.addNotify();

		Component c = getParent();

		//  Keep scrolling of the row table in sync with the main table.

		if (c instanceof JViewport)
		{
			JViewport viewport = (JViewport)c;
			viewport.addChangeListener( this );
		}
	}

	/*
	 *  Delegate method to main table
	 */
	@Override
	public int getRowCount()
	{
		return main.getRowCount();
	}

	@Override
	public int getRowHeight(int row)
	{
		int rowHeight = main.getRowHeight(row);

		if (rowHeight != super.getRowHeight(row))
		{
			super.setRowHeight(row, rowHeight);
		}

		return rowHeight;
	}

	/*
	 *  No model is being used for this table so just use the row number
	 *  as the value of the cell.
	 */
	@Override
	public Object getValueAt(int row, int column)
	{
		return Integer.toString(row + 1);
	}

	/*
	 *  Don't edit data in the main TableModel by mistake
	 */
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}

	/*
	 *  Do nothing since the table ignores the model
	 */
	@Override
	public void setValueAt(Object value, int row, int column) {}
//
//  Implement the ChangeListener
//
	public void stateChanged(ChangeEvent e)
	{
		//  Keep the scrolling of the row table in sync with main table

		JViewport viewport = (JViewport) e.getSource();
		JScrollPane scrollPane = (JScrollPane)viewport.getParent();
		scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
	}
//
//  Implement the PropertyChangeListener
//
	public void propertyChange(PropertyChangeEvent e)
	{
		//  Keep the row table in sync with the main table

		if ("selectionModel".equals(e.getPropertyName()))
		{
			setSelectionModel( main.getSelectionModel() );
		}

		if ("rowHeight".equals(e.getPropertyName()))
		{
			repaint();
		}

		if ("model".equals(e.getPropertyName()))
		{
			main.getModel().addTableModelListener( this );
			revalidate();
		}
	}

//
//  Implement the TableModelListener
//
	@Override
	public void tableChanged(TableModelEvent e)
	{
		revalidate();
	}

	/*
	 *  Attempt to mimic the table header renderer
	 */
	private static class RowNumberRenderer extends DefaultTableCellRenderer
	{
		private static final long serialVersionUID = 1L;

		public RowNumberRenderer()
		{
			setHorizontalAlignment(JLabel.CENTER);
		}

		public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			TableCellRenderer render = table.getTableHeader().getDefaultRenderer();
			DefaultTableCellRenderer rdr =
					(DefaultTableCellRenderer)render.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			if(isSelected)
				rdr.setFont(getFont().deriveFont(Font.BOLD));
			
			rdr.setHorizontalAlignment(SwingConstants.LEFT);
			//
			if(hdrName != null && showHeader == true){
				rdr.setText(row + " " + hdrName[row]);
			}else{
				rdr.setText(Integer.toString(row));
			}	
			return rdr;
			
			/*
			if (table != null)
			{
				JTableHeader header = table.getTableHeader();

				if (header != null)
				{
					setForeground(header.getForeground());
					setBackground(header.getBackground());
					setFont(header.getFont());
				}
			}

			if (isSelected)
			{
				setFont( getFont().deriveFont(Font.BOLD) );
			}

			setText((value == null) ? "" : value.toString());
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			
			return this;
			*/
		}
	}
}