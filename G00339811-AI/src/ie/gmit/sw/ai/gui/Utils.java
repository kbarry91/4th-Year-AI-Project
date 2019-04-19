package ie.gmit.sw.ai.gui;

import javax.swing.JOptionPane;

/**
 * 
 * 
 * @author Kevin Barry
 *
 */

/**
 * Utils contains static methods that can be used throughout the program.
 * 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 */
public class Utils {

	/**
	 * Display a YES/NO pop up dialog to confirm if user wnats to select an option.
	 * 
	 * @param nodeType the type of node to select.
	 * @return an integer value to represent yes or no selected.
	 */
	public static int displaySelectOption(int nodeType) {
		String weaponName = "";

		switch (nodeType) {
		case 1:
			weaponName = "Sword";
			break;
		case 3:
			weaponName = "Bomb";
			break;
		case 4:
			weaponName = "HydrogenBomb";
			break;
		}
		String strMessage = "Would you like to collect the " + weaponName + "?";
		return JOptionPane.showConfirmDialog(null, strMessage, "Weapon Encountered", JOptionPane.YES_NO_OPTION);
	}

	/**
	 * Display an information dialog box.
	 * 
	 * @param messageSubject the type of object to display information.
	 * @param collected      confirms if object was collected or not.
	 */
	public static void displayInfo(String messageSubject, boolean collected) {
		if (collected)
			messageSubject = " Weapon " + messageSubject + " collected !";
		else {
			messageSubject = " Weapon " + messageSubject + " Not collected !";
		}
		JOptionPane.showMessageDialog(null, messageSubject, "Game Update", JOptionPane.INFORMATION_MESSAGE);

	}
	
	/**
	 *  Display a general information message.
	 *  
	 * @param msgSubject the subject of the message.
	 * @param msgHeader the header of the dialog box.
	 */
	public static void displayGeneralInfo(String msgSubject, String msgHeader) {
		JOptionPane.showMessageDialog(null, msgSubject, msgSubject, JOptionPane.INFORMATION_MESSAGE);

	}
	
	
	/**
	 *  Display a general information message.
	 *  
	 * @param msgSubject the subject of the message.
	 * @param msgHeader the header of the dialog box.
	 */
	public static void displayWarningInfo(String msgSubject, String msgHeader) {
		JOptionPane.showMessageDialog(null, msgSubject, msgSubject, JOptionPane.WARNING_MESSAGE);

	}
}

