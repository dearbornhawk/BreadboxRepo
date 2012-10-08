package breadbox.util;

public interface WordListener extends BreadboxConstants {
	void newLetter(char letter);
	void endOfWord(String word);
	void endOfTweet(String message);
}
