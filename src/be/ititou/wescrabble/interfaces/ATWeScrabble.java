package be.ititou.wescrabble.interfaces;

public interface ATWeScrabble {
	/**
	 * Return a letter from the Scrabble table
	 * @param row The letter's row
	 * @param column The letter's column
	 * @return A 1-length string
	 */
	public String getLetterAt(int row, int column);
		
	public Boolean addWord(String word, int row, int column, Boolean horizontally);
	
	public void addObserver(WeScrabbleObserver observer);
}
