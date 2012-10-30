package test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.Card.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;

public class GameSetupTests {
	private Board brd;

	@Before
	public void setUp() throws Exception {
		brd = new Board();
		brd.loadConfigFiles();
		//brd.deal();
	}

	@Test
	public void loadPeopletest() {
		List<ComputerPlayer> comps = brd.getComps();
		HumanPlayer hum = brd.getHuman();
		Assert.assertEquals("Mr. Custard", comps.get(0).getName());
		System.out.println(comps.get(comps.size()-1).getName());
		System.out.println(comps.get(1).getColor());
		Assert.assertEquals("Mrs. Penguin", comps.get(comps.size()-1).getName());
		Assert.assertEquals(Color.yellow, comps.get(0).getColor());
		
		Assert.assertEquals(Color.BLACK, comps.get(comps.size() - 1).getColor());

		Assert.assertEquals("Mr mober", hum.getName());
		Assert.assertEquals(Color.red, hum.getColor());
	}

	@Test
	public void loadCards() {
		List<Card> cards = brd.getCards();
		int person = 0;
		int weapon = 0;
		int room = 0;
		for (Card c : cards) {
			if (c.getCardType() == Card.CardType.PERSON)
				++person;
			if (c.getCardType() == Card.CardType.ROOM)
				++room;
			if (c.getCardType() == Card.CardType.WEAPON)
				++weapon;
		}
		Card c = new Card();
		Assert.assertEquals(21, cards.size());
		Assert.assertEquals(6, person);
		Assert.assertEquals(6, weapon);
		Assert.assertEquals(9, room);
		c.setName("Pipe");
		c.setCardType(CardType.WEAPON);
		Assert.assertEquals(true, cards.contains(c));
		c.setName("Subnet Mask");
		c.setCardType(CardType.ROOM);
		Assert.assertEquals(true, cards.contains(c));
		c.setName("Mr. Green");
		c.setCardType(CardType.PERSON);
		Assert.assertEquals(true, cards.contains(c));
	}

	@Test
	public void dealCards() {
		brd.deal((ArrayList<Card>) brd.getCards());
		HumanPlayer human = brd.getHuman();
		int cardCount = human.getMyCards().size();
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.addAll(brd.getCards());
		for (Card card : human.getMyCards()) {
			if (!cards.remove(card)) {
				Assert.fail("Duplicate card!");
			}
		}
		List<ComputerPlayer> computers = brd.getComps();
		System.out.println("size of Computers " + computers.size());
		for (ComputerPlayer computer : computers) {
		//	System.out.println("CardCount " + cardCount);
			//System.out.println(computer.getMyCards().size() - cardCount);
			Assert.assertEquals(
					Math.abs(computer.getMyCards().size() - cardCount) <= 1,
					true);
			for(Card card: cards){
				//System.out.println("Card: " + card.getName());
			}
			for (Card card : computer.getMyCards()) {
				//System.out.println("compCard " + card.getName());
				if (!cards.remove(card)) {
					System.out.println("gello" + !cards.remove(card));
					Assert.fail("Duplicate card!");
				}
			}
		}
	}

}
