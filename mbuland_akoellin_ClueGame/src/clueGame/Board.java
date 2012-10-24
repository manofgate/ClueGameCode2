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
	protected Set<BoardCell> tgts;
	protected Map<Integer, List<Integer>> adjMap;
	//defined constants for the board
	protected static int ROWS;
	protected static int COLS;
	private List<BoardCell> cells;
	private Map<Character, String> rooms;
	private List<ComputerPlayer> comps;
	private List<Card> cards;
	private List<Human> human;
	public Board() {
		rooms = new HashMap<Character, String>();
		cells = new ArrayList<BoardCell>();
		comps = new ArrayList<ComputerPlayer>();
		cards = new ArrayList<Card>();
		human = new ArrayList<Human>();
		loadConfigFiles();
		calcAdjacencies();
	}
	
	public List<Card> getCards() {
		return cards;
	}

	public List<ComputerPlayer> getComps() {
		return comps;
	}

	public void setComps(List<ComputerPlayer> comps) {
		this.comps = comps;
	}

	public List<Human> getHuman() {
		return human;
	}

	public void setHuman(List<Human> human) {
		this.human = human;
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
						cells.add(new WalkwayCell());
					} else {
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
	
	public void loadConfigFiles() {
		parseLegend();
		parseLayout();
		parsePeople();
		parseCards();
	}
	public void parsePeople(){
		
	}
	
	public void parseCards(){
		
	}
	public BoardCell getCellAt(int row, int col) {
		return getCellAt(calcIndex(row, col));
	}
	
	public BoardCell getCellAt(int index) {
		return cells.get(index);
	}
	
	/**
	 * Create adjacency lists for each cell
	 */
	public void calcAdjacencies() {
		adjMap = new HashMap<Integer, List<Integer>>();
		for(int i = 0; i < ROWS*COLS; ++i) {
			List<Integer> adjList = new ArrayList<Integer>();
			if (getCellAt(i).isDoorway()) {
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
			//checks to see if and non doorway rooms were added and removes them.
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
	
	

	/**
	 * Calculate the targets that can be moved to from a certain cell with a certain number of moves
	 * @param start
	 * @param roll
	 */
	
	public void calcTargets(int start, int roll) {
		List<List<Integer>> alltgts = new ArrayList<List<Integer>>();
		List<Integer> emptyPath = new ArrayList<Integer>();
		emptyPath.add(start);
		//boolean is to not target roomcells inside the room, just the doorway. T means room and not a doorway 
		calcTargets(start, roll, emptyPath, alltgts, (getCellAt(start).isRoom() && !getCellAt(start).isDoorway()));
		
		// now, alltgts should contain every possible valid path
		tgts = new HashSet<BoardCell>();
		for (List<Integer> li : alltgts) {
			tgts.add(getCellAt(li.get(li.size()-1)));
		}
	}
	
	private void calcTargets(int from, int roll, List<Integer> pathsofar, List<List<Integer>> allpaths, boolean startRoom) {
		if (roll <= 0 || (getCellAt(from).isDoorway() && pathsofar.size() > 1)) {
			if (getCellAt(from).isDoorway())
				System.out.println("Cell " + from + " is a doorway. Ending");
			allpaths.add(pathsofar);
			return;
		}
		for (Integer dest : getAdjList(from)) {
			if(!startRoom){
				if (!pathsofar.contains(dest) &&  (getCellAt(dest).isWalkway() || getCellAt(dest).isDoorway() ) ) {
					List<Integer> cpPath = new ArrayList<Integer>(pathsofar);
					cpPath.add(dest);
					calcTargets(dest, roll-1,cpPath,allpaths, startRoom);
				}
			}
		}
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
	public void selectAnswer(){
		
	}
	public void deal(ArrayList<String> person){
		
	}
	public void deal(){
		
	}
	public boolean checkAccusation(String person, String weapon, String room){
		return false;
	}
	public void handleSuggestion(String person, String weapon, String room){
		
	}
}
