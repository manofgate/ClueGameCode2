package clueGame;

public class BadConfigFormatException extends Exception { 
	public BadConfigFormatException(String cfgFileName, String problemMsg) {
		super("The config file " + cfgFileName + " has an issue: " + problemMsg);
	}
}
