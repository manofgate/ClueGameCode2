package test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import experiment.IntBoard;

public class IntBoardTest {

	IntBoard board;
	@Before
	public void init() {
		board = new IntBoard();
		board.calcAdjacencies();
	}
	
	@Test
	public void testCalcIndex() {
		/*
		 int expected = 6 ;
		 int actual = board.calcIndex(0, 6);
		 Assert.assertEquals(actual, expected);
		 expected = 76;
		 actual = board.calcIndex(3, 2);
		 Assert.assertEquals(expected, actual);
		 expected = 624;
		 actual = board.calcIndex(24, 24);
		 Assert.assertEquals(expected, actual);
		 expected = 341;
		 actual = board.calcIndex(13, 17);
		 Assert.assertEquals(expected, actual);
		 */ // test for actual board we have
		
		//tests for a 4x4 board
		int expected = 0;
		int actual = board.calcIndex(0, 0);
		Assert.assertEquals(actual, expected);
		expected = 6;
		actual = board.calcIndex(1, 2);
		Assert.assertEquals(actual, expected);
		expected = 13;
		actual = board.calcIndex(3, 1);
		Assert.assertEquals(actual, expected);
		expected = 15;
		actual = board.calcIndex(3, 3);
		Assert.assertEquals(actual, expected);
		 
	}
	
	@Test
	public void testAdjacencies0_0() {
		Set<Integer> testList = board.getAdjList(0);
		Assert.assertTrue(testList.contains(4));
		Assert.assertTrue(testList.contains(1));
		Assert.assertEquals(2, testList.size());
	}
	
	@Test
	public void testAdjacencies0_n() {
		//test a top edge
		Set<Integer> testList = board.getAdjList(1);
		Assert.assertTrue(testList.contains(0));
		Assert.assertTrue(testList.contains(2));
		Assert.assertTrue(testList.contains(5));
		Assert.assertEquals(3, testList.size());

		//test top right corner
		testList = board.getAdjList(3);
		Assert.assertTrue(testList.contains(2));
		Assert.assertTrue(testList.contains(7));
		Assert.assertEquals(2, testList.size());

		
	}
	
	@Test
	public void testAdjacenciesn_0() {
		//test left edge
		Set<Integer> testList = board.getAdjList(4);
		Assert.assertTrue(testList.contains(0));
		Assert.assertTrue(testList.contains(8));
		Assert.assertTrue(testList.contains(5));
		Assert.assertEquals(3, testList.size());
	}
	
	@Test
	public void testAdjacenciesn_n() {
		//test bot left edge
		Set<Integer> testList = board.getAdjList(15);
		Assert.assertTrue(testList.contains(11));
		Assert.assertTrue(testList.contains(14));
		Assert.assertEquals(2, testList.size());
	}
	
	@Test
	public void testAdjacencies1_1() {
		Set<Integer> testList = board.getAdjList(5);
		Assert.assertTrue(testList.contains(4));
		Assert.assertTrue(testList.contains(1));
		Assert.assertTrue(testList.contains(9));
		Assert.assertTrue(testList.contains(6));
		Assert.assertEquals(4, testList.size());
	}
	
	@Test
	public void testAdjacencies4_1() {
		Set<Integer> testList = board.getAdjList(13);
		Assert.assertTrue(testList.contains(12));
		Assert.assertTrue(testList.contains(14));
		Assert.assertTrue(testList.contains(9));
		Assert.assertEquals(3, testList.size());
	}
	
	@Test
	public void testAdjacencies4_0() {
		Set<Integer> testList = board.getAdjList(12);
		Assert.assertTrue(testList.contains(8));
		Assert.assertTrue(testList.contains(13));
		Assert.assertEquals(2, testList.size());
	}
	
	@Test
	public void testTargets0_3() {
		board.calcTargets(0, 3);
		Set<Integer> targets= board.getTargets();
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(12));
		Assert.assertTrue(targets.contains(9));
		Assert.assertTrue(targets.contains(1));
		Assert.assertTrue(targets.contains(6));
		Assert.assertTrue(targets.contains(3));
		Assert.assertTrue(targets.contains(4));
	}
	
	@Test
	public void testTargets0_1() {
		board.calcTargets(0, 1);
		Set<Integer> targets= board.getTargets();
		Assert.assertEquals(2, targets.size());
		Assert.assertTrue(targets.contains(1));
		Assert.assertTrue(targets.contains(4));
	}
	
	@Test
	public void testTargets6_2() {
		board.calcTargets(6, 2);
		Set<Integer> targets= board.getTargets();
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(1));
		Assert.assertTrue(targets.contains(3));
		Assert.assertTrue(targets.contains(4));
		Assert.assertTrue(targets.contains(3));
		Assert.assertTrue(targets.contains(9));
		Assert.assertTrue(targets.contains(11));
		Assert.assertTrue(targets.contains(14));
	}
	
	@Test
	public void testTargets15_4() {
		board.calcTargets(15, 4);
		Set<Integer> targets= board.getTargets();
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(2));
		Assert.assertTrue(targets.contains(5));
		Assert.assertTrue(targets.contains(7));
		Assert.assertTrue(targets.contains(8));
		Assert.assertTrue(targets.contains(10));
		Assert.assertTrue(targets.contains(13));
	
	}
	
	@Test
	public void testTargets5_3() {
		board.calcTargets(5, 3);
		Set<Integer> targets= board.getTargets();
		Assert.assertEquals(8, targets.size());
		Assert.assertTrue(targets.contains(1));
		Assert.assertTrue(targets.contains(3));
		Assert.assertTrue(targets.contains(4));
		Assert.assertTrue(targets.contains(6));
		Assert.assertTrue(targets.contains(9));
		Assert.assertTrue(targets.contains(11));
		Assert.assertTrue(targets.contains(12));
		Assert.assertTrue(targets.contains(14));
	
	}
	public void testTargets5_2() {
		board.calcTargets(5, 2);
		Set<Integer> targets= board.getTargets();
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(0));
		Assert.assertTrue(targets.contains(10));
		Assert.assertTrue(targets.contains(8));
		Assert.assertTrue(targets.contains(2));
		Assert.assertTrue(targets.contains(13));
		Assert.assertTrue(targets.contains(7));
		
		
	
	}
}
