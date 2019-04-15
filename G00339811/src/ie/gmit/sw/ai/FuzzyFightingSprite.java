package ie.gmit.sw.ai;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

//Idea
public class FuzzyFightingSprite extends Sprite implements Runnable, Fightable, fleeable {
	private Fight fuzzyfight;
	private double damageToInflict;

	public FuzzyFightingSprite(String name, int frames, String[] files) throws Exception {
		super(name, frames, files);
		// TODO Auto-generated constructor stub
	}

	public void fight() {
		// Fuzzy mandani.... COG
	}

	@Override
	public double getFightDamage(double weaponPower, double opponentStrength) {
		fuzzyfight = new Fight();
		damageToInflict = fuzzyfight.getFightDamage(weaponPower, opponentStrength);
		System.out.println("FuzzyFightingSprite damageToInflict:" + damageToInflict);
		return damageToInflict;
		// TODO Auto-generated method stub

	}

}
// Ideas
