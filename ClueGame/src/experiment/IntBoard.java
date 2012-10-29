package experiment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class IntBoard {
	
	protected Set<Integer> tgts;
	protected Map<Integer, Set<Integer>> adjMap;
	//defined constants for the board
	protected static final int ROWS = 4;
	protected static final int COLS = 4;
	
	public IntBoard() {
		
	}
	
	/**
	 * Create adjacency lists for each cell
	 */
	public void calcAdjacencies() {
		adjMap = new HashMap<Integer, Set<Integer>>();
		for(int i = 0; i < ROWS*COLS; ++i) {
			Set<Integer> adjList = new TreeSet<Integer>();
			// Checks the cell above
			if ( (i/ COLS) >= 1)
				adjList.add(i-COLS);
			
			// Checks the cell below
			if (i < (COLS *(ROWS -1)))
				adjList.add(i+COLS);
			
			// Checks the cell to the left
			if (i % COLS > 0)
				adjList.add(i-1);
			
			// Checks the cell to the right
			if (i % COLS < COLS - 1)
				adjList.add(i+1);
			
			adjMap.put(i, adjList);
		}
	}

	/**
	 * Calculate the targets that can be moved to from a certain cell with a certain number of moves
	 * @param start
	 * @param roll
	 */
	public void calcTargets(int start, int roll) {
		System.out.println("----------------------------------------");
		Point startRC = calcRowColumn(start);
		System.out.println("At " + startRC.x + "," + startRC.y + " moving " + roll);
		tgts = new TreeSet<Integer>();
		for(int i = startRC.x-roll; i<= startRC.x+roll; ++i) {
			for(int j = startRC.y-roll; j <= startRC.y+roll; ++j) {
				System.out.println("(" + i + "," + j + ") : " + isOnBoard(i,j)  + " : " + canMoveTo(i,j,startRC.x,startRC.y,roll));
				if (isOnBoard(i,j)) {
					if (canMoveTo(i, j, startRC.x, startRC.y, roll)) {
						tgts.add(calcIndex(i, j));
					}
				}
			}
		}
	}
	
	/**
	 * Checks an ABSOLUTE position to see if it's on the board
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isOnBoard(int row, int col) {
		return 0 <= row && row < ROWS && 0 <= col && col < COLS;
	}
	
	/**
	 * Checks an the ABSOLUTE positions of a row,col to see if it can be moved to from startRow,startCol
	 * @param row
	 * @param col
	 * @param startRow
	 * @param startCol
	 * @param move
	 * @return
	 */
	public boolean canMoveTo(int row, int col, int startRow, int startCol, int move) {
		if (row == startRow && col == startCol) {
			System.out.println("Can't move to self");
			return false;
		}
		
		if (Math.abs(startRow-row) + Math.abs(startCol-col) > move) {
			System.out.println(Math.abs(startRow-row) + "," + Math.abs(startCol-col) + "  Can't move further than move");
			return false;
		}
		
		if ((Math.abs(startRow-row) + Math.abs(startCol-col))%2 != move%2) {
			System.out.println("Can't move to an opposite odd/even");
			return false;
		}
		return true;
	}
	
	
	/**
	 * Get the set of all targets that were calculated in calcTargets
	 * @return
	 */
	public Set<Integer> getTargets() {
		return tgts;
	}
	
	/**
	 * Get the adjacency list for the specified cell
	 * @param cell
	 * @return
	 */
	public Set<Integer> getAdjList(int cell) {
		return adjMap.get(cell);
	}
	
	/**
	 * Given a grid position, returns the position on the board, returns -1 if you try to refrence a point outside the board
	 * @param row
	 * @param col
	 * @return
	 */
	public int calcIndex(int row, int col) {
		if (row >= ROWS || col >= COLS) {
			return -1;
		}
		return (COLS*row + col);
	}
	
	public Point calcRowColumn(int index) {
		Point p = new Point(index/COLS, index%COLS);
		return p;
	}
}
