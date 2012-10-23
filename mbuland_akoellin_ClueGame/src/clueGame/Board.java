package clueGame;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clueGame.RoomCell.DoorDirection;

public class Board {
	
	private String legendFile = "legend.cfg";
	private String boardFile = "board.cfg";
	public Board() {
		rooms = new HashMap<Character, String>();
		cells = new ArrayList<BoardCell>();
		loadConfigFiles();
		calcAdjacencies();
	}
	
	public List<BoardCell> getCells() {
		return cells;
	}
	
	public Map<Character, String> getRooms() {
		return rooms;
	}
	
	public void parseLegend() {
		FileReader legendReader = null;
		try{
			legendReader = new FileReader(legendFile);
			Scanner reader = new Scanner(legendReader);
			Pattern legPat = Pattern.compile("([A-Za-z]), (.+?)\n?");
			while (reader.hasNext()){
				String line = reader.nextLine();
				Matcher lineMatcher = legPat.matcher(line);
				lineMatcher.find();
				if (lineMatcher.matches()) {
					if (!lineMatcher.group(1).isEmpty() && !lineMatcher.group(2).isEmpty()) {
						rooms.put(lineMatcher.group(1).charAt(0), lineMatcher.group(2));
					} else {
						System.err.println("Unexpected syntax with line " + line);
					}
				} else {
					System.err.println("No syntax match with line " + line);
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("NO LEGEND FOUND");//should never happen, since the function will never be called for a character that isn't in the legend
		}
		System.out.println("Rooms: " + rooms);
	}

	public void parseLayout() {
		FileReader configReader = null;
		try {
			configReader = new FileReader(boardFile);
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		Scanner reader = new Scanner(configReader);
		int rows = 0;
		int cols = 99;
		while (reader.hasNext()){
			String[] space = reader.next().split(",");
			if (space == null)
				continue;
			rows++;
			int lCol = 0;
			for (int i = 0; i < space.length; ++i) {
				lCol++;
				if (lCol > cols)
					System.err.println("Line " + rows + " has more columns than the row before it.");
				if (space[i].length() != 1 && space[i].length() != 2) {
					System.err.println("Found unknown value: " + space[i]);
				}
				
				if (rooms.containsKey(space[i].charAt(0))) {
					// all is good in the world
					if (space[i].charAt(0) == 'W') {
						System.out.println("Found walkway");
						cells.add(new WalkwayCell());
					} else {
						System.out.println("Checking space:" + space[i]);
						if (space[i].length() == 1) 
							cells.add(new RoomCell(space[i].charAt(0), DoorDirection.fromString("")));
						else
							cells.add(new RoomCell(space[i].charAt(0), DoorDirection.fromString(Character.toString(space[i].charAt(1)))));
					}
				} else {
					System.err.println("This room was not found in the legend: " + space[i].charAt(0));
				}
			}
			cols = lCol;
		}
		ROWS = rows;
		COLS = cols;
	}

	public String getFromLegend(char x){
		FileReader legendReader = null;
		try{
			legendReader = new FileReader("CR-ClueLegend.cfg");
			Scanner reader = new Scanner(legendReader);
			while (reader.hasNext()){
				String line = reader.next();
				if (line.charAt(0) == x)
					return line.substring(3);//everything after the whitespace char after the comma in the legend
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		return "NO LEGEND FOUND";//should never happen, since the function will never be called for a character that isn't in the legend
		
	}
	
	public int getNumRows() {
		return ROWS;
	}

	public int getNumColumns() {
		return COLS;
	}

	private List<BoardCell> cells;
	private Map<Character, String> rooms;
	
	public void loadConfigFiles() {
		parseLegend();
		parseLayout();
	}
	
	public BoardCell getCellAt(int row, int col) {
		return getCellAt(calcIndex(row, col));
	}
	
	public BoardCell getCellAt(int index) {
		return cells.get(index);
	}
	protected Set<BoardCell> tgts;
	protected Map<Integer, List<Integer>> adjMap;
	//defined constants for the board
	protected static int ROWS;
	protected static int COLS;

	/**
	 * Create adjacency lists for each cell
	 */
	public void calcAdjacencies() {
		adjMap = new HashMap<Integer, List<Integer>>();
		for(int i = 0; i < ROWS*COLS; ++i) {
			List<Integer> adjList = new ArrayList<Integer>();
			if (getCellAt(i).isDoorway()) {
				System.out.println("Cell " + i + " is a doorway with direction " + ((RoomCell)getCellAt(i)).getDoorDirection());
				switch (((RoomCell)getCellAt(i)).getDoorDirection()) {
				case DOWN:
					adjList.add(i+COLS); break;
				case UP:
					adjList.add(i-COLS); break;
				case LEFT:
					adjList.add(i-1); break;
				case RIGHT:
					adjList.add(i+1); break;
				default:
					break;
				}
			} else if (getCellAt(i).isWalkway()) {
				System.out.println("Cell " + i + " is a walkway");
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
			}
			int j=0;
			while (j<adjList.size()) {
				if (getCellAt(adjList.get(j)).isRoom() && !getCellAt(adjList.get(j)).isDoorway()) {
					adjList.remove(j);
					j--;
				}
				j++;
			}
			adjMap.put(i, adjList);
		}
	}
	
	public String getIndex(BoardCell cell) {
		String s = "(";
		int index = cells.indexOf(cell);
		Point p = calcRowColumn(index);
		s += p.x;
		s += ",";
		s += p.y;
		s += ")";
		return s;
	}

	/**
	 * Calculate the targets that can be moved to from a certain cell with a certain number of moves
	 * @param start
	 * @param roll
	 */
	
	public void calcTargets(int start, int roll) {
//		System.out.println("----------------------------------------");
//		Point startRC = calcRowColumn(start);
//		System.out.println("At " + startRC.x + "," + startRC.y + " moving " + roll);
//		List<Integer> targs = new ArrayList<Integer>();
//		for(int i = startRC.x-roll; i<= startRC.x+roll; ++i) {
//			for(int j = startRC.y-roll; j <= startRC.y+roll; ++j) {
//				System.out.println("(" + i + "," + j + ") : " + isOnBoard(i,j)  + " : " + canMoveTo(i,j,startRC.x,startRC.y,roll));
//				if (isOnBoard(i,j)) {
//					if (canMoveTo(i, j, startRC.x, startRC.y, roll)) {
//						targs.add(calcIndex(i, j));
//					}
//				}
//			}
//			int j=0;
//			while (j<targs.size()) {
//				if (getCellAt(targs.get(j)).isRoom() && !getCellAt(targs.get(j)).isDoorway()) {
//					targs.remove(j);
//					j--;
//				}
//				j++;
//			}
//		}
//		tgts = new HashSet<BoardCell>();
//		for (Integer i : targs) {
//			tgts.add(getCellAt(i));
//			
//		}
		List<List<Integer>> alltgts = new ArrayList<List<Integer>>();
		List<Integer> emptyPath = new ArrayList<Integer>();
		emptyPath.add(start);
		calcTargets(start, roll, emptyPath, alltgts, (getCellAt(start).isRoom() && !getCellAt(start).isDoorway()));
		
		// now, alltgts should contain every possible valid path
//		Set<Integer> noDupTgts = new HashSet<Integer>();
		tgts = new HashSet<BoardCell>();
		for (List<Integer> li : alltgts) {
			tgts.add(getCellAt(li.get(li.size()-1)));
		}
//		
//		// noDupTgts has no duplicate targets
//		tgts = new ArrayList<BoardCell>();
//		for (Integer i : noDupTgts) {
//			tgts.add(getCellAt(i));
//		}
	}
	
	private void calcTargets(int from, int roll, List<Integer> pathsofar, List<List<Integer>> allpaths, boolean startRoom) {
		if (roll <= 0 || (getCellAt(from).isDoorway() && pathsofar.size() > 1)) {
			if (getCellAt(from).isDoorway())
				System.out.println("Cell " + from + " is a doorway. Ending");
			allpaths.add(pathsofar);
			return;
		}
		for (Integer dest : getAdjList(from)) {
			if (!pathsofar.contains(dest) && ( ( ( (getCellAt(dest).isWalkway() || getCellAt(dest).isDoorway() ) && !startRoom) ) || ((getCellAt(dest).isRoom() && !getCellAt(dest).isWalkway() ) ) ) ) {
				List<Integer> cpPath = new ArrayList<Integer>(pathsofar);
				cpPath.add(dest);
				calcTargets(dest, roll-1,cpPath,allpaths, startRoom);
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
	public Set<BoardCell> getTargets() {
		return tgts;
	}
	
	/**
	 * Get the adjacency list for the specified cell
	 * @param cell
	 * @return
	 */
	public List<Integer> getAdjList(int cell) {
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
