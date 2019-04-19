package ie.gmit.sw.ai;

import ie.gmit.sw.ai.traversers.Node;

public class player extends Node{
	
	private Node[][] maze;
	private double playerHealth;
	public Weapon weapon;
 private final double MAXHEALTH=100;
	public player(int row, int col, int type, Node[][] maze) {
		super(row, col, type);
		this.maze = maze;
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
	
	public void restoreHealth(){
		this.playerHealth = MAXHEALTH;
	}

}
