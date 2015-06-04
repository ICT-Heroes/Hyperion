package test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import model.Clsx;

import org.junit.Before;
import org.junit.Test;

import service.ClsxService;
import controller.DataController;

public class ClsxTest {

	Clsx clsx;
	File readFile;
	String writeFilePath;
	DataController dataController;

	@Before
	public void setup() {
		clsx = new Clsx();
		dataController = new DataController();
		readFile = new File("src/res/testReadFile.clsx");
		writeFilePath = "src/res/testWriteFile.clsx";
		dataController.loadClsx(readFile);
	}

	@Test
	public void readClsxTest() {
		clsx = ClsxService.getInstance().readFile(readFile);
		// 원하는 데이터가 정확하게 들어갔는가
		assertThat("root", is(clsx.getName()));
		assertThat("First Group", is(clsx.item[0].getName()));
		assertThat("First Group's First Item",
				is(clsx.item[0].item[0].getName()));
		assertThat("Second Group", is(clsx.item[1].getName()));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void readClsxErrorTest() {
		clsx = ClsxService.getInstance().readFile(readFile);
		// 인덱스를 벗어난 이상한 데이터가 들어오지 않았는가
		clsx.item[3].getName();
		clsx.item[1].item[3].getName();
		// item의 item에 접근할 때 익셉션이 발생해야 정상
		clsx.item[0].item[0].item[0].getName();
	}

	@Test
	public void writeClsxTest() {

		clsx = ClsxService.getInstance().readFile(readFile);
		ClsxService.getInstance().WriteFile(writeFilePath + "2", clsx);

		dataController.saveClsx(writeFilePath);

	}

}
