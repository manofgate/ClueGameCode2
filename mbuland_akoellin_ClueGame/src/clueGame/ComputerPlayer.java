package clueGame;

import java.util.Set;

public class ComputerPlayer extends Player {
	private String lastRoomVisited;

	public ComputerPlayer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLastRoomVisited() {
		return lastRoomVisited;
	}

	public void setLastRoomVisited(String lastRoomVisited) {
		this.lastRoomVisited = lastRoomVisited;
	}
	public void pickLocation(Set<BoardCell> targets){
		
	}
	public void createSuggestion(){}
	public void updateSeen(Card seen){}

}
