package clueGame;

public class Card {
	public enum CardType {
		PERSON, WEAPON, ROOM
	}
	private CardType cardType;
	private String name;
	public Card(CardType cardType, String name) {
		super();
		this.cardType = cardType;
		this.name = name;
	}
	public Card() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CardType getCardType() {
		return cardType;
	}
	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public boolean equals(Object o){
		Card c = (Card) o;
		if(name.equals(c.name) && cardType ==c.cardType){
			return true;
		}
		return false;
	}
}
