package clueGame;

import java.awt.Color;
import java.util.List;

public class HumanPlayer extends Player {

	public HumanPlayer(String name, Color color) {
		super(name, color);
	}
	
	public HumanPlayer(List<Card> myCards) {
		super(myCards);
	}
	
}
