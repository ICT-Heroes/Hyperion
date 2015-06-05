package test.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import model.Dsm;

import org.junit.Before;
import org.junit.Test;

import service.DsmService;

public class DsmTest {
	Dsm dsm;
	DsmService service = new DsmService();

	@Before
	public void setup() {
		File file = new File("data/test.dsm");
		dsm = service.readFromeFile(file);
	}

	@Test
	public void readDsmTest() {
		// 이름 정보
		assertThat("First Group's First Item", is(dsm.getName(0)));
		assertThat("First Group's Second Item", is(dsm.getName(1)));
		// dependency 정보
		assertThat(false, is(dsm.getDependency(0, 2)));
		assertThat(true, is(dsm.getDependency(0, 1)));
	}

	@Test
	public void writeDsmTest() {
		// write가 정상 작동한다는 전제조건 하에
		String writePath = "src/res/writetest.dsm";
		service.WriteFile(writePath, dsm);
		File file2 = new File(writePath);
		Dsm dsmTest = service.readFromeFile(file2);
		assertThat(dsm.equals(dsmTest), is(true));
	}

}
