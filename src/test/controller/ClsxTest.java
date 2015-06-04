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
	File readClsxFile;
	File writeClsxFile;
	String writeFilePath;
	DataController dataController;

	@Before
	public void setup() {
		clsx = new Clsx();
		dataController = new DataController();
		writeFilePath = "src/res/testWriteFile.clsx";
		readClsxFile = new File("src/res/testReadFile.clsx");
		writeClsxFile = new File(writeFilePath);
		clsx = ClsxService.getInstance().readFile(readClsxFile);
	}

	@Test
	public void readClsxTest() {
		// 원하는 데이터가 정확하게 들어갔는가
		assertThat("root", is(clsx.getName()));
		assertThat("First Group", is(clsx.item[0].getName()));
		assertThat("First Group's First Item",
				is(clsx.item[0].item[0].getName()));
		assertThat("Second Group", is(clsx.item[1].getName()));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void readClsxErrorTest() {
		// 인덱스를 벗어난 이상한 데이터가 들어오지 않았는가
		clsx.item[3].getName();
		clsx.item[1].item[3].getName();
		// item의 item에 접근할 때 익셉션이 발생해야 정상
		clsx.item[0].item[0].item[0].getName();
	}

	@Test
	public void writeClsxTest() {
		String name[] = new String[4];
		name[0] = clsx.getName();
		name[1] = clsx.item[0].getName();
		name[2] = clsx.item[0].item[0].getName();
		name[3] = clsx.item[1].getName();

		// clsx정보를 읽어오자마자 반환되는 Data를 즉석에서 바로 넣어줌으로써 테스트를 실시한다.(dsm을 먼저 읽지 않으면
		// data객체가 최신으로 바뀌지 않기 때문)
		dataController.saveClsx(writeFilePath,
				dataController.makeClsxToData(clsx));

		// 미리 저장해둔 clsx의 내용과 같은 정보를 가지고 있는지를 테스트 한다.
		clsx = ClsxService.getInstance().readFile(writeClsxFile);

		assertThat(name[0], is(clsx.getName()));
		assertThat(name[1], is(clsx.item[0].getName()));
		assertThat(name[2], is(clsx.item[0].item[0].getName()));
		assertThat(name[3], is(clsx.item[1].getName()));

	}
}
