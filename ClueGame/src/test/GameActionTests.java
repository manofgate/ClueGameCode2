package test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.Card.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.RoomCell;
import clueGame.RoomCell.DoorDirection;
import clueGame.Solution;
import clueGame.WalkwayCell;

public class GameActionTests {

	Board board;
	
	@Before
	public void setUp() throws Exception {
		board = new Board();
		board.loadConfigFiles();
		board.selectAnswer();
	}

	@Test
	public void checkAccuseTest() {
		Solution solution = board.getSolution();
		Assert.assertEquals(board.checkAccusation(solution.person, solution.weapon, solution.room), true);
		Assert.assertEquals(board.checkAccusation("wrong", solution.weapon, solution.room), false);
		Assert.assertEquals(board.checkAccusation(solution.person, "wrong", solution.room), false);
		Assert.assertEquals(board.checkAccusation(solution.person, solution.weapon, "wrong"), false);
		Assert.assertEquals(board.checkAccusation("wrong", "wrong", "wrong"), false);
	}
	
	@Test
	public void checkPickLocationWithRoom() {
		ComputerPlayer computer =  new ComputerPlayer("", Color.BLUE);
		
		Set<BoardCell> cells = new HashSet<BoardCell>();
		cells.add(new RoomCell('k', DoorDirection.DOWN));
		cells.add(new WalkwayCell());
		cells.add(new WalkwayCell());
		cells.add(new WalkwayCell());
		cells.add(new WalkwayCell());
		
		for(int i = 0; i < 12; i++) {
			Assert.assertEquals(computer.pickLocation(cells) instanceof RoomCell, true);
		}
	}
	
	@Test
	public void checkPickLocationFromRoom() {
		ComputerPlayer computer =  new ComputerPlayer("", Color.blue);
		computer.setLastRoomVisited('k');
		Set<BoardCell> cells = new HashSet<BoardCell>();
		cells.add(new RoomCell('k', DoorDirection.DOWN));
		cells.add(new WalkwayCell());
		cells.add(new WalkwayCell());
		cells.add(new WalkwayCell());
		cells.add(new WalkwayCell());
		
		for(int i = 0; i < 12; i++) {
			Assert.assertEquals(computer.pickLocation(cells) instanceof RoomCell, false);
		}
	}
	
	@Test
	public void checkPickLocationRandomly() {
		ComputerPlayer computer =  new ComputerPlayer("", Color.blue);
		computer.setLastRoomVisited('k');
		Set<BoardCell> cells = new HashSet<BoardCell>();
		WalkwayCell cell1 = new WalkwayCell();
		WalkwayCell cell2 = new WalkwayCell();
		WalkwayCell cell3 = new WalkwayCell();
		cells.add(cell1);
		cells.add(cell2);
		cells.add(cell3);
		
		int cell1Count = 0, cell2Count = 0, cell3Count = 0;
		
		for(int i = 0; i < 100; i++) {
			BoardCell cell = computer.pickLocation(cells);
			if(cell == cell1) {
				cell1Count++;
			}
			if(cell == cell2) {
				cell2Count++;
			}
			if(cell == cell3) {
				cell3Count++;
			}
		}
		
		Assert.assertEquals(cell1Count > 0, true);
		Assert.assertEquals(cell2Count > 0, true);
		Assert.assertEquals(cell3Count > 0, true);
	}
	
	@Test
	public void checkDisproveSuggestion() {
		Player player = new ComputerPlayer("", Color.blue);
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(CardType.PERSON, "a"));
		cards.add(new Card(CardType.PERSON, "b"));
		cards.add(new Card(CardType.WEAPON, "c"));
		cards.add(new Card(CardType.WEAPON, "d"));
		cards.add(new Card(CardType.ROOM, "e"));
		cards.add(new Card(CardType.ROOM, "f"));
		player.setMyCards(cards);
		Assert.assertEquals(player.disproveSuggestion("a", "a", "a"), cards.get(0));
		Assert.assertEquals(player.disproveSuggestion("c", "c", "c"), cards.get(2));
		Assert.assertEquals(player.disproveSuggestion("e", "e", "e"), cards.get(4));
	}
		
	@Test
	public void checkDisproveSuggestionOnePlayerMultipleCards() {
		Player player = new ComputerPlayer("", Color.blue);
		List<Card> cards = new ArrayList<Card>();
		cards.clear();
		cards.add(new Card(CardType.PERSON, "a"));
		cards.add(new Card(CardType.WEAPON, "c"));
		
		player.setMyCards(cards);
		int card1Count = 0, card2Count = 0;
		for(int i = 0; i < 100; i++) {
			Card card = player.disproveSuggestion("a", "BLARGH", "c");
			if(card == cards.get(0)) {
				card1Count++;
			}
			if(card == cards.get(1)) {
				card2Count++;
			}
		}
		Assert.assertEquals(card1Count > 0, true);
		Assert.assertEquals(card2Count > 0, true);
	}
	
	@Test
	public void checkDisproveSuggetionTwoPeopleOneCard() {
		List<ComputerPlayer> comps = new ArrayList<ComputerPlayer>();
		Card card1 = new Card(CardType.PERSON, "a");
		Card card2 = new Card(CardType.PERSON, "a");
		List<Card> comp1Cards = new ArrayList<Card>();
		comp1Cards.add(card1);
		List<Card> comp2Cards = new ArrayList<Card>();
		comp2Cards.add(card2);
		comps.add(new ComputerPlayer(comp1Cards));
		comps.add(new ComputerPlayer(comp2Cards));
		board.setComps(comps);
		board.setHuman(new HumanPlayer("", Color.blue));
		board.setTurn(0);
		int card1Count = 0;
		int card2Count = 0;
		for(int i = 0; i < 100; i++) {
			Card card = board.handleSuggestion("a", "b", "c");
			if(card == card1) {
				card1Count++;
			}
			if(card == card2) {
				card2Count++;
			}
		}
		Assert.assertEquals(card1Count > 0, true);
		Assert.assertEquals(card2Count > 0, true);
	}
	
	@Test
	public void checkDisproveSuggestionPlayerHasCard() {
		List<ComputerPlayer> comps = new ArrayList<ComputerPlayer>();
		comps.add(new ComputerPlayer("", Color.blue));
		comps.add(new ComputerPlayer("", Color.blue));
		List<Card> cards = new ArrayList<Card>();
		cards.add(new Card(CardType.PERSON, "a"));
		HumanPlayer human = new HumanPlayer(cards);
		board.setComps(comps);
		board.setHuman(human);
		Assert.assertEquals(board.handleSuggestion("a", "a", "a"), null);
	}
	
	@Test
	public void checkMakeSuggestion() {
		ComputerPlayer player = new ComputerPlayer("", Color.blue);
		Card card1 = new Card(CardType.PERSON, "a");
		Card card2 = new Card(CardType.ROOM, "b");
		Card card3 = new Card(CardType.WEAPON, "c");
		player.updateSeen(card1);
		player.updateSeen(card2);
		player.updateSeen(card3);
		Solution suggestion = player.createSuggestion();
		Assert.assertEquals(suggestion.person != card1.getName(), true);
		Assert.assertEquals(suggestion.room != card2.getName(), true);
		Assert.assertEquals(suggestion.weapon != card3.getName(), true);
	}

}
