package ie.gmit.sw.ai;

public class Weapon {
	private String name;
	private double damage;

	public Weapon() {
		setName("Hands");
		setDamage(10);
	}

	public Weapon(String name, double damage) {
		super();
		this.name = name;
		this.damage = damage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

}
