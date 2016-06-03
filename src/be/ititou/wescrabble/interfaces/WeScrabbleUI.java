package be.ititou.wescrabble.interfaces;

import java.util.List;

import edu.vub.at.objects.natives.NATText;

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
	
	int getMyTeam();
	
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
	
	/**
	 * Called by the backend when the letters set has changed
	 * @param letters The new letters to show
	 */
	public void showMyLetters(List<NATText> letters);
	
	public void setPlayerRack(String playerName, List<NATText> letters);
	
	public void removePlayerRack(String playerName);
}
