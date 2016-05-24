package be.ititou.wescrabble.interfaces;

/**
 * What our Ambient Talk application should expect from the UI
 * @author Titouan Christophe
 */
public interface WeScrabbleUI {
	public static final int TeamA = 0;
	public static final int TeamB = 1;
	
	/**
	 * Return the name of this player
	 */
	public String getMyName();
	
	/**
	 * Set the informative message content
	 * @param message The message content
	 */
	public void showMessage(String message);
	
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
	
	/**
	 * Display the newly joined team
	 * @param team The joined team
	 */
	public void setTeam(int team);
}
