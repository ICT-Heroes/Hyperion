package main;
import java.io.File;

import model.Dsm;
import controller.Partitioner;
import controller.ReadDsmController;
import view.titan.TitanTreeContainer;
import view.titan.TitanWindow;

public class Main{
	public static void main(String[] args){
		/*TitanWindow wnd = new TitanWindow();
		wnd.attachMenuBar();
		wnd.attachToolBar();
		wnd.pack();
		wnd.setVisible(true);
		
		TitanTreeContainer tree = wnd.getTitanTreeContainer();
		tree.setRoot(new Object());
		Object item = new Object();
		tree.insertNode(tree.getRoot(), item);
		Object item2 = new Object();
		tree.insertNode(item, item2);
		tree.insertNodes(item, new Object[] {new Object(),new Object(),new Object(),new Object(),new Object(),new Object()});
		//tree.removeNode(item2);
		System.out.println(tree.getRoot().toString());*/
		
		Dsm dsm;
		ReadDsmController controller = new ReadDsmController();
		Partitioner partitioner;
		
		File file = new File("src/res/titan.dsm");
		dsm = controller.readFromeFile(file);
		partitioner = new Partitioner();
		partitioner.setDsm(dsm);
		
		partitioner.preProcessing();
		dsm.print();
	}
}
