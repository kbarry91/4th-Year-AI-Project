package ie.gmit.sw.ai;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

public class Fight implements Fightable {

	//public double getSalary(Weapon weapon, double expierence, double qualification, double screen) {
	public double getSalary( double expierence, double qualification, double screen) {

		FIS fis = FIS.load("resources/fight.fcl", true); // Load and parse the FCL
	//	fis.setVariable("weapon", weapon);
		fis.setVariable("expierence", expierence);
		fis.setVariable("qualification", qualification);
		fis.setVariable("screen", screen);
		fis.evaluate(); // Execute the fuzzy inference engine

		JFuzzyChart.get().chart(fis); // Display fuzzy charts

		JFuzzyChart.get().chart(fis.getVariable("salary").getDefuzzifier(), "Salary", true);
		return fis.getVariable("salary").getValue();
	}
}
