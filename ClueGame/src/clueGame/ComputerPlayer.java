package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import clueGame.Card.CardType;

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
		List<Card> allCards = Board.parseCards();
		allCards.removeAll(seenCards);
		List<Card> peopleCards = new ArrayList<Card>();
		List<Card> weaponCards = new ArrayList<Card>();
		List<Card> roomCards = new ArrayList<Card>();
		for(Card card : allCards) {
			if(card.getCardType() == CardType.PERSON) {
				peopleCards.add(card);
			}
			if(card.getCardType() == CardType.WEAPON) {
				weaponCards.add(card);
			}
			if(card.getCardType() == CardType.ROOM) {
				roomCards.add(card);
			}
		}
		return new Solution(peopleCards.get((int) (Math.random() * peopleCards.size())).getName(), 
				weaponCards.get((int) (Math.random() * weaponCards.size())).getName(), 
				roomCards.get((int) (Math.random() * roomCards.size())).getName());
	}

	public void updateSeen(Card seen) {
		seenCards.add(seen);
	}

}
