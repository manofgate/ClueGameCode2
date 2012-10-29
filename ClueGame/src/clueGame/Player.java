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
	}
	
	public Player(List<Card> myCards) {
		this.myCards = myCards;
	}
	
	public Card disproveSuggestion(String person, String room, String weapon) {
		Card c = new Card();
		return c;
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
