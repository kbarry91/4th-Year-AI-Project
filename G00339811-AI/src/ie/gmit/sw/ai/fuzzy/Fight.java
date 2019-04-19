package ie.gmit.sw.ai.fuzzy;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

public class Fight {
	
	public double PlayerHealth(int weapon , int opponent) {
		FIS fis = FIS.load("resources/fuzzy/fight.fcl", true); //Load and parse the FCL
		//JFuzzyChart.get().chart(fis);
		
		//fis.chart(); //Display the linguistic variables and terms
		fis.setVariable("weapon", weapon); //Apply a value to a variable
		fis.setVariable("opponent", opponent);
		fis.evaluate(); //Execute the fuzzy inference engine
		//JFuzzyChart.get().chart(fis.getVariable("risk").getDefuzzifier(),"Risk",true);
		return fis.getVariable("damage").getValue(); //Output end result
	}

}
