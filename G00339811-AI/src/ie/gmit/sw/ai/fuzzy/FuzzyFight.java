package ie.gmit.sw.ai.fuzzy;

import net.sourceforge.jFuzzyLogic.FIS;

/**
 * Fuzzy fight is used to generate an outcome fo9r the damage to a player by
 * using fuzzy logic.
 * 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 *
 */
public class FuzzyFight {

	/**
	 * Generates a fight outcome using fuzzy logic.
	 * 
	 * @param weapon   the weapon used by the player.
	 * @param opponent the type of oponent the player is battling.
	 * 
	 * @return the value of health to take from the player.
	 */
	public double getPlayerDamage(int weapon, int opponent) {
		System.out.println("[FuzzyFight]getPlayerDamage() weapon:"+weapon+" Opponent:"+opponent);

		// Load and parse the fuzzy control logic file.
		FIS fis = FIS.load("resources/fuzzy/samplefuzzy.fcl", true);

		// Set the variable values for the Fuzzy inference system .
		fis.setVariable("weapon", weapon);
		fis.setVariable("opponent", opponent);

		// Evaluate fuzzy rules for all function blocks.
		fis.evaluate();

		// Return the value evaluated by the fis.
		return fis.getVariable("damage").getValue();
	}

}
