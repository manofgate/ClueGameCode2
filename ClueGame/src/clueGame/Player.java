package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public abstract class Player {
	private String name;
	private List<Card> myCards;
	private Color color;

	public Player(String name, Color color) {
		this.name = name;
		this.color = color;
		myCards = new ArrayList<Card>();
	}
	
	public Player(List<Card> myCards) {
		this.myCards = myCards;
	}
	
	public Card disproveSuggestion(String person, String room, String weapon) {
		List<Card> cards = new ArrayList<Card>();
		for(Card card : myCards) {
			if(card.getName().equals(person) || card.getName().equals(room) || card.getName().equals(weapon)) {
				cards.add(card);
			}
		}
		if(cards.size() == 0) {
			return null;
		}
		Card returnCard = cards.get((int) (Math.random() * cards.size()));
		return returnCard;
	}

	public String getName() {
		return name;
	}

	public List<Card> getMyCards() {
		return myCards;
	}

	public void setMyCards(List<Card> cards) {
		this.myCards = cards;
	}

	public Color getColor() {
		return color;
	}

}
