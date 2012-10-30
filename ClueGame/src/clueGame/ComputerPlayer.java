package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ComputerPlayer extends Player {
	private char lastRoomVisited;
	private List<Card> seenCards;

	public ComputerPlayer(String name, Color color) {
		super(name, color);
		seenCards = new ArrayList<Card>();
	}

	public ComputerPlayer(List<Card> myCards) {
		super(myCards);
		seenCards = new ArrayList<Card>();
	}

	public char getLastRoomVisited() {
		return lastRoomVisited;
	}

	public void setLastRoomVisited(char lastRoomVisited) {
		this.lastRoomVisited = lastRoomVisited;
	}

	public BoardCell pickLocation(Set<BoardCell> targets) {
		BoardCell bestChoice = null;
		BoardCell[] array = new BoardCell[targets.size()];
		targets.toArray(array);
		for (int i = 0; i < targets.size(); i++) {
			BoardCell cell = array[i];
			if (cell instanceof RoomCell) {
				if (((RoomCell) cell).getRoomInitial() == lastRoomVisited) {
					targets.remove(cell);
				} else {
					bestChoice = cell;
				}
			}
		}
		if (bestChoice == null) {
			array = new BoardCell[targets.size()];
			targets.toArray(array);
			int index = (int) (Math.random() * targets.size());
			bestChoice = array[index];
		}
		return bestChoice;
	}

	public Solution createSuggestion() {
		return null;
	}

	public void updateSeen(Card seen) {
		seenCards.add(seen);
	}

}
