package clueGame;

import java.awt.Color;
import java.util.List;
import java.util.Set;

public class ComputerPlayer extends Player {
	private char lastRoomVisited;

	public ComputerPlayer(String name, Color color) {
		super(name, color);
	}
	
	public ComputerPlayer(List<Card> myCards) {
		super(myCards);
	}

	public char getLastRoomVisited() {
		return lastRoomVisited;
	}

	public void setLastRoomVisited(char lastRoomVisited) {
		this.lastRoomVisited = lastRoomVisited;
	}
	public BoardCell pickLocation(Set<BoardCell> targets){
		return null;
	}
	public Solution createSuggestion(){return null;}
	public void updateSeen(Card seen){}

}
