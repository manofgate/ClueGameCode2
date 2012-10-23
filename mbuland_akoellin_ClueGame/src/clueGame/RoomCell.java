package clueGame;

public class RoomCell extends BoardCell {
	
	public enum DoorDirection {
		UP("U"), DOWN("D"), LEFT("L"), RIGHT("R"), NONE("");
		private String text;

		DoorDirection(String text) {
			this.text = text;
		}

		public String getText() {
			return this.text;
		}

		public static DoorDirection fromString(String text) {
			if (text != null) {
				for (DoorDirection b : DoorDirection.values()) {
					if (text.equalsIgnoreCase(b.text)) {
						return b;
					}
				}
			}
			return DoorDirection.NONE;
		}
	};
	
	private DoorDirection doorDirection;
	private char roomInitial;
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	public char getRoomInitial() {
		return roomInitial;
	}

	public RoomCell(char roomInitial, DoorDirection doorDirection) {
		super();
		this.doorDirection = doorDirection;
		this.roomInitial = roomInitial;
	}

	@Override
	public boolean isRoom() {
		return true;
	}
	
	public boolean isDoorway() {
		return doorDirection != DoorDirection.NONE;
	}
	
}
