package ie.gmit.sw.ai;

import ie.gmit.sw.ai.traversers.Node;

/**
 * A Played object that extends the Node class.
 * 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 *
 */
public class player extends Node {

	private Node[][] maze;
	private double playerHealth;
	public Weapon weapon;
	private final double MAXHEALTH = 100;

	/**
	 * Constructor to initialise player object.
	 * 
	 * @param row the row position of player.
	 * @param col the column position of player.
	 * @param type the type of node.
	 * @param maze the Node maze.
	 */
	public player(int row, int col, int type, Node[][] maze) {
		super(row, col, type);
		this.maze = maze;
		// Create a new weapon object.
		this.weapon = new Weapon();

	}

	public double getPlayerHealth() {
		return playerHealth;
	}

	public void setPlayerHealth(double playerHealth) {
		this.playerHealth = playerHealth;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public void restoreHealth() {
		this.playerHealth = MAXHEALTH;
	}

}
