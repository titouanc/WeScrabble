package be.ititou.wescrabble.interfaces;

import java.util.List;

public interface ATWeScrabble {
	/**
	 * Return a letter from the Scrabble table
	 * @param row The letter's row
	 * @param column The letter's column
	 * @return A 1-length string
	 */
	public String getLetterAt(int row, int column);
	
	public List<String> getMyLetters();
	
	public Boolean addWord(String word, int row, int column, Boolean horizontally);
	
	public void addObserver(TableObserver observer);
}
