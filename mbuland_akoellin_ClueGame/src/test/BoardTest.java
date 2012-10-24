package test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.RoomCell;

public class BoardTest {

	Board brd;
	@Before
	public void start() {
		brd = new Board();
		brd.loadConfigFiles();
	}
	
	@Test
	public void testRoomNum() {
		Assert.assertEquals(10, brd.getRooms().size());
	}
	
	@Test
	public void testRoomTypeMap() {
		Map<Character, String> brdMap = brd.getRooms();
		Assert.assertEquals("Walkway", brdMap.get('W'));
		Assert.assertEquals("IP Gateway", brdMap.get('A'));
		Assert.assertEquals("Domain Name Server", brdMap.get('B'));
		Assert.assertEquals("Server Closet", brdMap.get('C'));
		Assert.assertEquals("Subnet Mask", brdMap.get('D'));
		Assert.assertEquals("Traffic Monitor", brdMap.get('E'));
		Assert.assertEquals("The Loo", brdMap.get('F'));
		Assert.assertEquals("IP Address v6", brdMap.get('G'));
		Assert.assertEquals("MAC Address Filter", brdMap.get('H'));
		Assert.assertEquals("Study", brdMap.get('I'));
	}
	
	@Test
	public void testRowsAndCols() {
		Assert.assertEquals(25, brd.getNumRows());
		Assert.assertEquals(25, brd.getNumColumns());
	}
	
	@Test
	public void testDoorwayDirections() {
		Assert.assertEquals(RoomCell.DoorDirection.UP, ((RoomCell)brd.getCellAt(1, 0)).getDoorDirection());
		Assert.assertEquals(RoomCell.DoorDirection.DOWN, ((RoomCell)brd.getCellAt(4, 2)).getDoorDirection());
		Assert.assertEquals(RoomCell.DoorDirection.LEFT, ((RoomCell)brd.getCellAt(4, 8)).getDoorDirection());
		Assert.assertEquals(RoomCell.DoorDirection.LEFT, ((RoomCell)brd.getCellAt(3, 15)).getDoorDirection());
		Assert.assertEquals(RoomCell.DoorDirection.DOWN, ((RoomCell)brd.getCellAt(23, 1)).getDoorDirection());
		Assert.assertEquals(RoomCell.DoorDirection.DOWN, ((RoomCell)brd.getCellAt(23, 4)).getDoorDirection());
		Assert.assertEquals(RoomCell.DoorDirection.LEFT, ((RoomCell)brd.getCellAt(17, 6)).getDoorDirection());
		Assert.assertEquals(RoomCell.DoorDirection.UP, ((RoomCell)brd.getCellAt(7, 8)).getDoorDirection());
		Assert.assertEquals(RoomCell.DoorDirection.RIGHT, ((RoomCell)brd.getCellAt(10, 12)).getDoorDirection());
		Assert.assertEquals(RoomCell.DoorDirection.RIGHT, ((RoomCell)brd.getCellAt(24, 12)).getDoorDirection());
		Assert.assertEquals(RoomCell.DoorDirection.RIGHT, ((RoomCell)brd.getCellAt(6, 19)).getDoorDirection());
		Assert.assertEquals(RoomCell.DoorDirection.UP, ((RoomCell)brd.getCellAt(21, 24)).getDoorDirection());
		Assert.assertEquals(RoomCell.DoorDirection.LEFT, ((RoomCell)brd.getCellAt(24, 24)).getDoorDirection());
	}
	
	@Test
	public void testRoomInitials() {
		Assert.assertEquals('A', ((RoomCell)brd.getCellAt(1, 0)).getRoomInitial());
		Assert.assertEquals('A', ((RoomCell)brd.getCellAt(4,2)).getRoomInitial());
		Assert.assertEquals('B', ((RoomCell)brd.getCellAt(4,8)).getRoomInitial());
		Assert.assertEquals('G', ((RoomCell)brd.getCellAt(3,15)).getRoomInitial());
		Assert.assertEquals('E', ((RoomCell)brd.getCellAt(23,1)).getRoomInitial());
		Assert.assertEquals('D', ((RoomCell)brd.getCellAt(23,4)).getRoomInitial());
		Assert.assertEquals('F', ((RoomCell)brd.getCellAt(17,6)).getRoomInitial());
		Assert.assertEquals('F', ((RoomCell)brd.getCellAt(7,8)).getRoomInitial());
		Assert.assertEquals('F', ((RoomCell)brd.getCellAt(12,10)).getRoomInitial());
		Assert.assertEquals('F', ((RoomCell)brd.getCellAt(24,10)).getRoomInitial());
		Assert.assertEquals('H', ((RoomCell)brd.getCellAt(7,19)).getRoomInitial());
		Assert.assertEquals('I', ((RoomCell)brd.getCellAt(21,24)).getRoomInitial());
		Assert.assertEquals('I', ((RoomCell)brd.getCellAt(24,24)).getRoomInitial());
	}
	@Test
	public void testCalcIndex() {
		Assert.assertEquals(347, brd.calcIndex(13, 22));
		Assert.assertEquals(45, brd.calcIndex(1, 20));
		Assert.assertEquals(437, brd.calcIndex(17, 12));
		Assert.assertEquals(2, brd.calcIndex(0, 2));
		Assert.assertEquals(600, brd.calcIndex(24, 0));
		Assert.assertEquals(624, brd.calcIndex(24, 24));
		Assert.assertEquals(472, brd.calcIndex(18, 22));
		Assert.assertEquals(42, brd.calcIndex(1, 17));
		Assert.assertEquals(23, brd.calcIndex(0, 23));
		Assert.assertEquals(276, brd.calcIndex(11, 1));
		Assert.assertEquals(121, brd.calcIndex(4, 21));
	}
	
	@Test
	public void testAdjInRoom() {
		List<Integer> testList = brd.getAdjList(brd.calcIndex(7, 6));
		assertEquals(0, testList.size());
	}
	
	@Test
	public void testAdjInWalkway() {
		List<Integer> testList = brd.getAdjList(brd.calcIndex(17, 14));
		assertEquals(4, testList.size());
		assertTrue(testList.contains(brd.calcIndex(17,13)));
		assertTrue(testList.contains(brd.calcIndex(17,15)));
		assertTrue(testList.contains(brd.calcIndex(18,14)));
		assertTrue(testList.contains(brd.calcIndex(16,14)));
		//one next to room
		testList = brd.getAdjList(brd.calcIndex(3, 13));
		System.out.println("sixe " + testList.size());
		assertEquals(2, testList.size());
		assertTrue(testList.contains(brd.calcIndex(3,12)));
		assertTrue(testList.contains(brd.calcIndex(3,14)));
	}
	
	@Test
	public void testAdjInDoorway() {
		List<Integer> testList = brd.getAdjList(brd.calcIndex(7, 8));
		assertEquals(1, testList.size());
		assertTrue(testList.contains(brd.calcIndex(6, 8)));
		
		testList = brd.getAdjList(brd.calcIndex(23, 4));
		assertEquals(1, testList.size());
		assertTrue(testList.contains(brd.calcIndex(24, 4)));
	}
	
	@Test
	public void testTargetsOneStep() {
		brd.calcTargets(brd.calcIndex(17, 14), 1);
		Set<BoardCell> testList = brd.getTargets();
		assertEquals(4, testList.size());
		assertTrue(testList.contains(brd.getCellAt(brd.calcIndex(17,13))));
		assertTrue(testList.contains(brd.getCellAt(brd.calcIndex(17,15))));
		assertTrue(testList.contains(brd.getCellAt(brd.calcIndex(18,14))));
		assertTrue(testList.contains(brd.getCellAt(brd.calcIndex(16,14))));
		//one next to room
		brd.calcTargets(brd.calcIndex(3, 13), 1);
		testList = brd.getTargets();
		assertEquals(2, testList.size());
		assertTrue(testList.contains(brd.getCellAt(brd.calcIndex(3,12))));
		assertTrue(testList.contains(brd.getCellAt(brd.calcIndex(3,14))));
	}
	
	@Test
	public void testTargetsTwoSteps() {
		brd.calcTargets(brd.calcIndex(17, 14), 2);
		Set<BoardCell> testList = brd.getTargets();
		assertEquals(7, testList.size());
		assertTrue(testList.contains(brd.getCellAt(brd.calcIndex(15, 14))));
		assertTrue(testList.contains(brd.getCellAt(brd.calcIndex(16, 13))));
		assertTrue(testList.contains(brd.getCellAt(18, 13)));
		assertTrue(testList.contains(brd.getCellAt(brd.calcIndex(19, 14))));
		assertTrue(testList.contains(brd.getCellAt(18, 15)));
		assertTrue(testList.contains(brd.getCellAt(17, 16)));
		assertTrue(testList.contains(brd.getCellAt(16, 15)));
		
		brd.calcTargets(brd.calcIndex(14, 5), 2);
		testList = brd.getTargets();
		assertEquals(2, testList.size());
		assertTrue(testList.contains(brd.getCellAt(12,5)));
		assertTrue(testList.contains(brd.getCellAt(16,5)));
	}
	
	@Test
	public void testTargetsFourSteps() {
		brd.calcTargets(brd.calcIndex(4, 10), 4);
		Set<BoardCell> testList = brd.getTargets();
		assertEquals(7, testList.size());
		assertTrue(testList.contains(brd.getCellAt(0, 10)));
		assertTrue(testList.contains(brd.getCellAt(3, 13)));
		assertTrue(testList.contains(brd.getCellAt(6, 8)));
		assertTrue(testList.contains(brd.getCellAt(5, 11)));
		assertTrue(testList.contains(brd.getCellAt(6, 12)));
		assertTrue(testList.contains(brd.getCellAt(5, 13)));
		
		brd.calcTargets(brd.calcIndex(10, 13), 4);
		testList = brd.getTargets();
		assertEquals(10, testList.size());
		assertTrue(testList.contains(brd.getCellAt(10, 12)));
		assertTrue(testList.contains(brd.getCellAt(8, 13)));
		assertTrue(testList.contains(brd.getCellAt(6, 13)));
		assertTrue(testList.contains(brd.getCellAt(7, 14)));
		assertTrue(testList.contains(brd.getCellAt(9, 14)));
		assertTrue(testList.contains(brd.getCellAt(11, 14)));
		assertTrue(testList.contains(brd.getCellAt(13, 14)));
		assertTrue(testList.contains(brd.getCellAt(12, 13)));
		assertTrue(testList.contains(brd.getCellAt(14, 13)));
		assertTrue(testList.contains(brd.getCellAt(11, 16)));
	}
	
	@Test
	public void testRoomExit(){
		brd.calcTargets(brd.calcIndex(17, 6), 2);
		Set<BoardCell> testList = brd.getTargets();	
		assertEquals(2, testList.size());
		assertTrue(testList.contains(brd.getCellAt(16,5)));
		assertTrue(testList.contains(brd.getCellAt(18,5)));
	}

	@Test
	public void testTargetsSixSteps() {
		brd.calcTargets(brd.calcIndex(24, 0), 6);
		Set<BoardCell> testList = brd.getTargets();
		assertEquals(2, testList.size());
		assertTrue(testList.contains(brd.getCellAt(18,0)));
		assertTrue(testList.contains(brd.getCellAt(23,1)));
		

		brd.calcTargets(brd.calcIndex(11, 14), 6);
		testList = brd.getTargets();
		assertEquals(22, testList.size());
		assertTrue(testList.contains(brd.getCellAt(9, 14)));
		assertTrue(testList.contains(brd.getCellAt(13, 14)));
		assertTrue(testList.contains(brd.getCellAt(15, 14)));
		assertTrue(testList.contains(brd.getCellAt(17, 14)));
		assertTrue(testList.contains(brd.getCellAt(16, 13)));
		assertTrue(testList.contains(brd.getCellAt(16, 15)));
		assertTrue(testList.contains(brd.getCellAt(15, 16)));
		assertTrue(testList.contains(brd.getCellAt(14, 15)));
		assertTrue(testList.contains(brd.getCellAt(14, 13)));
		assertTrue(testList.contains(brd.getCellAt(12, 13)));
		assertTrue(testList.contains(brd.getCellAt(10, 13)));
		assertTrue(testList.contains(brd.getCellAt(8, 13)));
		assertTrue(testList.contains(brd.getCellAt(6, 13)));
		assertTrue(testList.contains(brd.getCellAt(5, 14)));
		assertTrue(testList.contains(brd.getCellAt(7, 14)));
		assertTrue(testList.contains(brd.getCellAt(9, 14)));
		assertTrue(testList.contains(brd.getCellAt(11, 18)));
		assertTrue(testList.contains(brd.getCellAt(11, 20)));
		assertTrue(testList.contains(brd.getCellAt(12, 17)));
		assertTrue(testList.contains(brd.getCellAt(12, 19)));
		assertTrue(testList.contains(brd.getCellAt(13, 18)));
		assertTrue(testList.contains(brd.getCellAt(10, 12)));
	}
	

}
