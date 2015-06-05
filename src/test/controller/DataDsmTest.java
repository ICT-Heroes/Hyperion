package test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import model.Data;

import org.junit.Before;
import org.junit.Test;

import controller.DataController;

/**
 * Dsm만 읽었을 때 작동하는지에 대한 Test
 *
 */
public class DataDsmTest {

	DataController dataController;
	File readDsmFile;
	Data data;

	@Before
	public void setup() {
		dataController = new DataController();
		readDsmFile = new File("data/test.dsm");
		dataController.loadDsm(readDsmFile);
	}

	@Test
	public void duplicateTest() {
		// 아직 미구현 이 케이스가 통과 하도록 수정해야함
		String name = dataController.getRootData().getChildData(0).getName();
		data = dataController.duplicate(1);
		assertThat(name, is(data.getChildData(0).getName()));
	}

	@Test
	public void moveUpTest() {
		String name = dataController.data.getChildData(1).getName();
		// moveUp의 parameter는 root를 포함한 1부터 시작하기 때문에 2를 넣으면 root, 다음, 다음이 선택된다.
		// 즉 구조상으로 child의 1번이 올라가는셈.
		dataController.moveUp(2);
		assertThat(name, is(dataController.data.getChildData(0).getName()));
	}

	@Test
	public void moveDownTest() {
		String name = dataController.data.getChildData(0).getName();
		dataController.moveDown(1);
		assertThat(name, is(dataController.data.getChildData(1).getName()));
	}

	@Test
	public void changeNodeNameTest() {
		dataController.changeNodeName(1, "Test");
		assertThat("Test", is(dataController.data.getChildData(0).getName()));
	}

	@Test
	public void addDsmTest() {
		// DSM에서 item을 추가하는 기능은 원래 없다.
		// DSM 전용 메서드를 만들어야한다.
		String name = "addItem";
		dataController.addDsm(name);
		assertThat(name, is(dataController.data.getChildData(3).getName()));
	}

	@Test
	public void deleteDsmTest() {
		String name = dataController.data.getChildData(1).getName();
		dataController.deleteDsm(1);
		assertThat(name, is(dataController.data.getChildData(0).getName()));
	}

	@Test
	public void sort() {
		dataController.sort(0);
		assertThat("First Group's First Item", is(dataController.data
				.getChildData(0).getName()));
	}

}
