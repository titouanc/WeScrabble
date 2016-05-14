package be.ititou.wescrabble.interfaces;

/**
 * What our Ambient Talk application should expect from the UI
 * @author Titouan Christophe
 */
public interface WeScrabbleUI {
	/**
	 * Return the name of this player
	 */
	public String getMyName();
	
	/**
	 * Set the informative message content
	 * @param message The message content
	 */
	public void setMessage(String message);
	
	/**
	 * Set the application title bar
	 * @param title The new title
	 */
	public void setAppTitle(String title);
	
	/**
	 * Bind an AmbientTalk WeScrabble local interface to this UI
	 * @param backend The AmbientTalk WeScrabble local interface
	 */
	public void setBackend(ATWeScrabble backend);
}
