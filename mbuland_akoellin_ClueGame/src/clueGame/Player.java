package clueGame;

import java.awt.Color;
import java.util.ArrayList;

public abstract class Player {
	private String name;
	private ArrayList<Card> mycards;
	private Color color;
	public Card disproveSuggestion(String person, String room, String weapon){
		Card c = new Card();
		return c;
	}
	public String getName() {
		return name;
	}
	public ArrayList<Card> getMycards() {
		return mycards;
	}
	public Color getColor() {
		return color;
	}
	
}
