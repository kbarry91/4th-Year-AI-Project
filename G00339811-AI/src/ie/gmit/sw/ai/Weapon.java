package ie.gmit.sw.ai;

/**
 * Weapon represents weapon that the player may use.
 * 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 *
 */
public class Weapon {
	private String name;
	private int damage;

	/**
	 * Null constructor  to set default values. 
	 */
	public Weapon() {
		setName("Hands");
		setDamage(10);
	}

	/**
	 * Constructor to set weapon.
	 * 
	 * @param name the name of the weapon.
	 * @param damage the damage applied by the weapon.
	 */
	public Weapon(String name, int damage) {
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

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

}
