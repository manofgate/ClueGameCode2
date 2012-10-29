package test;

import java.awt.Color;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;

public class GameSetupTests {
	private Board brd;

	@Before
	public void setUp() throws Exception {
		brd = new Board();
		brd.loadConfigFiles();
		brd.deal();
	}

	@Test
	public void loadPeopletest() {
		List<ComputerPlayer> comps = brd.getComps();
		HumanPlayer hum = brd.getHuman();
		Assert.assertEquals("Mr. Custard", comps.get(0).getName());
		Assert.assertEquals("Mrs. Penguin", comps.get(comps.size() - 1));
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
		Assert.assertEquals(true, cards.contains(c));
		c.setName("Subnet Mask");
		Assert.assertEquals(true, cards.contains(c));
		c.setName("Mr. Green");
		Assert.assertEquals(true, cards.contains(c));
	}

	@Test
	public void dealCards() {
		brd.deal();
		HumanPlayer human = brd.getHuman();
		int cardCount = human.getMyCards().size();
		List<Card> cards = brd.getCards();
		for (Card card : human.getMyCards()) {
			if (!cards.remove(card)) {
				Assert.fail("Duplicate card!");
			}
		}
		List<ComputerPlayer> computers = brd.getComps();
		for (ComputerPlayer computer : computers) {
			Assert.assertEquals(
					Math.abs(computer.getMyCards().size() - cardCount) <= 1,
					true);
			for (Card card : computer.getMyCards()) {
				if (!cards.remove(card)) {
					Assert.fail("Duplicate card!");
				}
			}
		}
	}

}
