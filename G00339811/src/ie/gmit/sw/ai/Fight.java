package ie.gmit.sw.ai;

import ie.gmit.sw.ai.nn.NnFight;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

public class Fight implements Fightable {

	// public double getSalary(Weapon weapon, double expierence, double
	// qualification, double screen) {
	// public double getFightDamage(Weapon weapon,Opponent opponent) {

	// TODO Make weapon and oponent objects
	public double getFightDamage(double weaponPower, double opponentStrength) {

		FIS fis = FIS.load("resources/fuzzy/fight.fcl", true); // Load and parse the FCL
		// fis.setVariable("weapon", weapon);
		fis.setVariable("weapon", weaponPower);
		fis.setVariable("opponent", opponentStrength);
		fis.evaluate(); // Execute the fuzzy inference engine

		JFuzzyChart.get().chart(fis); // Display fuzzy charts

		JFuzzyChart.get().chart(fis.getVariable("damage").getDefuzzifier(), "Damage", true);
		double damageToInflict =  fis.getVariable("damage").getValue();
		System.out.println("Fuzzy fight damage to inflict : "+ damageToInflict);
		return damageToInflict;
	}
	public void getNNFightDamage(double playerHealth,double angerLevel,double weapon) {
		 NnFight nnfight = new NnFight();
		 // Set health value to match NN input data
		 if(playerHealth<33) {
			 playerHealth = 0;
		 }else if (playerHealth<66) {
			 playerHealth = 1;
		 }else {
			 playerHealth=2;
		 }
		 try {
			double[] res = nnfight.action(playerHealth, weapon, angerLevel);
			System.out.println("==========Result from NN is =======\n playerhealth:"+res[0]+"\n weapon:"+res[1]+"\n angerlevel:"+res[2]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
}
