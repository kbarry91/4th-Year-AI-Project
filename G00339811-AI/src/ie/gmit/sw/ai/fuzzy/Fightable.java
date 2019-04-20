package ie.gmit.sw.ai.fuzzy;

import java.util.ArrayList;
import java.util.Random;

import ie.gmit.sw.ai.Sprite;
import ie.gmit.sw.ai.player;
import ie.gmit.sw.ai.nn.NeuralNetworkFight;
import ie.gmit.sw.ai.traversers.AStarTraversator;
import ie.gmit.sw.ai.traversers.Node;
import ie.gmit.sw.ai.traversers.Traversator;
import ie.gmit.sw.ai.utils.Utils;

/**
 * Fightable implements runnable to allow enemies to run on separate threads.
 * This is somewhat a God class for setting up searches, nn decisions and fuzzy
 * fights.
 * 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 *
 */
public class Fightable extends Sprite implements Runnable {

	// Variables that represent a Spider node.
	private int row;
	private int col;
	private int spiderType;
	private Node node = new Node(row, col, spiderType);

	// Lock is used to prevent deadlock in a scenario when multiple threads try to
	// access a function.
	private Object lockThread;

	// KeepThreadAlive is used to control the live/die state of a thread.
	private Boolean keepThreadAlive;

	private Node[][] maze;

	private Random random = new Random();
	private Node lastNode;
	private player player;
	private Traversator traverse;
	private Node nextPosition;
	private boolean canMove;
	private NeuralNetworkFight nnfight;
	private int outcome;
	
	// The damage a spider can inflict
	private int fighterDamage;

	/**
	 * Constructor Fightable initialise a spider as a fightable object. A fightable
	 * runs on its own seperate thread and allows selected spiders to perform
	 * actions such as, search for player, follow player, use neural network to
	 * decide to fight or run and calculate fight damage using fuzzy logic.
	 * 
	 * @param row, the row that the fightable is located in the maze.
	 * @param col, the row that the fightable is located in the maze.
	 * @param spiderType, the type of spider.
	 * @param maze, 2-Dim node array of the maze.
	 * @param p, the player object.
	 * @param damage, the damage that the spider can give.
	 * @param lockThread, Object to lock thread for synchronisation to avoid
	 *        deadlock.
	 */
	public Fightable(int row, int col, int spiderType, Node[][] maze, player p, int damage, Object lockThread) {
		// Set the spider.
		this.row = row;
		this.col = col;
		this.spiderType = spiderType;
		this.fighterDamage = damage;

		// Set the player.
		this.player = p;

		node.setCol(col);
		node.setRow(row);
		node.setType(spiderType);

		// Set the maze.
		this.maze = maze;

		// Set thread variables.
		this.keepThreadAlive = true;
		this.lockThread = lockThread;

		/*
		 * Allows spiders { 6: Black, 11: Orange, 12: Red} too search. Spiders search
		 * for player node using the A* search algorithim.
		 */
		if (spiderType == 6) {
			System.out.println("[INFO : Traverser] Black spider searching for player using AStarTraversator");
			traverse = new AStarTraversator(this.player);
		} else if (spiderType == 11) {// nn spider
			System.out.println("[INFO : Traverser] Orange spider searching for player using AStarTraversator");
			traverse = new AStarTraversator(this.player);
		} else if (spiderType == 12) {// nn spider
			System.out.println("[INFO : Traverser] Red spider searching for player using AStarTraversator");
			traverse = new AStarTraversator(this.player);
		}

	}

	@Override
	public void run() {
		// Keep thread running while true.
		while (this.keepThreadAlive) {
			try {
				Thread.sleep(1000 * spiderType / 4);

				// 6 = Black Spider - 7 = Blue spider - 8 = Brown Spider - 9 = Green spider.
				if (spiderType >= 6 && spiderType <= 9) {

					// 6 is black searching spider.
					if (spiderType == 6) {
						traverse(node.getRow(), node.getCol(), traverse);
					}

					/// If player is touching spider.
					if (node.getHeuristic(this.player) == 1) {
						System.out.println("Spider has caught the player !");

						// start a fight.
						fightFuzzy(fighterDamage);

					} else if (canMove && node.getHeuristic(this.player) < 10) {
						System.out.println(
								"[INFO : Heuristic Search] Spider(fuzzy) is withen 10 squares and is now following player");
						// follow player.
						followPlayer();
					} else {
						// Move randomly.
						move();
					}

				} else {

					// 11 = Orange Spider - 12 = Red spider.
					if (spiderType == 11 || spiderType == 12) {
						traverse(node.getRow(), node.getCol(), traverse);
					}
					if (node.getHeuristic(this.player) == 1) {
						System.out.println("Spider has caught the player !");
						// start a fight
						fightFuzzy(fighterDamage);
					} else if (canMove && node.getHeuristic(player) < 5) {
						System.out.println(
								"[INFO : Heuristic Search] Spider(Neural Net) is withen 5 squares and is now following player canmove");
						getNextAction(this.player.getPlayerHealth(), fighterDamage, 1);

					} else {
						// Move randomly.
						move();
					}
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * followPlayer calculates the next move and checks if a player can move to that
	 * node position.
	 */
	private void followPlayer() {

		// If next position can be moved to
		if (nextPosition != null) {

			// Check if threasd is locked.
			synchronized (this.lockThread) {

				// Get the adjacent nodes.
				Node[] surroundingNodes = node.adjacentNodes(maze);

				// List of empty surrounding nodes.
				ArrayList<Node> emptySurroundingNodes = new ArrayList<>();

				// iterate the surrounding nodes.
				for (Node n : surroundingNodes) {

					// Check if node is blank.
					if (n.getType() == -1) {
						emptySurroundingNodes.add(n);
					}
				}

				// iterate the empty surrounding nodes.
				for (Node n : emptySurroundingNodes) {
					if (nextPosition.equals(n)) {

						// New position of the object
						int newPositionX, newPositionY;

						// Previous position of the object
						int previousPositonX = node.getRow(), previousPositionY = node.getCol();

						newPositionX = nextPosition.getRow();
						newPositionY = nextPosition.getCol();

						node.setRow(newPositionX);
						node.setCol(newPositionY);

						maze[newPositionX][newPositionY] = node;
						maze[previousPositonX][previousPositionY] = new Node(previousPositonX, previousPositionY, -1);

						nextPosition = null;
						canMove = false;
						return;
					}
				}
				// Move to node.
				move();

				// Set variables for next iteration.
				nextPosition = null;
				canMove = false;
				return;
			}
		} else {
			// Move to node.
			move();
			canMove = false;
		}
	}

	/**
	 * 
	 */
	public void move() {

		// Ensure thread is not locked.
		synchronized (this.lockThread) {

			// Get adjacent nodes.
			Node[] surroundingNodes = node.adjacentNodes(maze);

			// Get empty surrounding nodes.
			ArrayList<Node> emptySurroundingNodes = new ArrayList<>();

			// Iterate surrounding nodes
			for (Node n : surroundingNodes) {

				// check if its a blank node and not last node.
				if (n.getType() == -1 && n != lastNode) {
					emptySurroundingNodes.add(n);
				}
			}

			// Check if there is empty nodes surrounding.
			if (emptySurroundingNodes.size() > 0) {

				// Move to random surrounding node.
				int position = random.nextInt(emptySurroundingNodes.size());


				// Previous position of the object
				int previousPositonX = node.getRow(), previousPositionY = node.getCol();

				// Set new position for node.
				node.setRow(emptySurroundingNodes.get(position).getRow());
				node.setCol(emptySurroundingNodes.get(position).getCol());

				// Set the last node.
				lastNode = new Node(previousPositonX, previousPositionY, -1);

				// Set node in maze.
				maze[emptySurroundingNodes.get(position).getRow()][emptySurroundingNodes.get(position).getCol()] = node;
				maze[previousPositonX][previousPositionY] = lastNode;

			} else {
				// Get node Position.
				int newPositionX, newPositionY;
				int previousPositonX = lastNode.getRow(), previousPositionY = lastNode.getCol();

				newPositionX = lastNode.getRow();
				newPositionY = lastNode.getCol();

				// Set the node position.
				node.setRow(newPositionX);
				node.setCol(newPositionY);

				// Set the last node.
				lastNode = new Node(previousPositonX, previousPositionY, -1);

				// Set node in maze.
				maze[newPositionX][newPositionY] = node;
				maze[previousPositonX][previousPositionY] = lastNode;
			}
		}

	}

	/**
	 * Start a fuzzy fight.
	 * 
	 * 
	 * @param spiderPower the power ovger the spiders attack.
	 */
	public void fightFuzzy(int spiderPower) {
		FuzzyFight fuzzyFight = new FuzzyFight();

		// Carry out a fuzzy attack.
		double damageToGive = fuzzyFight.getPlayerDamage(player.getWeapon().getDamage(), spiderPower);

		// Remove attack damage from players health.
		player.setPlayerHealth(player.getPlayerHealth() - damageToGive);

		// System.out.println("Fuzzy fight damage" + Math.round(damageToGive) + "player
		// health now :" + Math.round(player.getPlayerHealth()));
		Utils.displayWarningInfo("You just lost " + Math.round(damageToGive) + "health in that battle !",
				"Youve been attacked");
		this.keepThreadAlive = false;
		// shut her down
		if (player.getPlayerHealth() < 0) {
			Utils.displayWarningInfo("Game Over your Health reached 0 !", "GameOver");
			System.exit(0);
		}
	}

	/**
	 * Retrieves the next move using the specified traversator algorithim.
	 * 
	 * @param row, current row of node.
	 * @param col, current location of node.
	 * @param t, the traversator algorithm to use.
	 */
	public void traverse(int row, int col, Traversator t) {

		// Traverse the maze.
		t.traverse(maze, maze[row][col]);

		// Get the next position.
		nextPosition = t.getNextNode();

		// Check if position is free to move to.
		if (nextPosition != null) {
			canMove = true;
		} else {
			canMove = false;
		}
	}

	/**
	 * getNextActio uses previously trained neural network to determine the action
	 * of the spider.
	 * 
	 * @param        health, The players health.
	 * @param        spiderPower, The power of the spider.
	 * @param weapon the players current weapon.
	 */
	public void getNextAction(double health, double spiderPower, double weapon) {

		System.out.println("[INFO : Neural Network] A spider is calculating its next action." + weapon);

		// Must Normalise the values before feeding them into the neural network.

		// Normalise health.z
		if (health < 30) {
			health = 0;
		} else if (health > 30 && health < 60) {
			health = 1;
		} else if (health > 60) {
			health = 2;
		}

		// Normalise spiders power.
		if (spiderPower < 30) {
			spiderPower = 0;
		} else if (spiderPower > 30 && spiderPower < 60) {
			spiderPower = 1;
		} else if (spiderPower > 60) {
			spiderPower = 2;
		}

		// Generate next outcome from nerual net.
		// 1: panic (move randomly)
		// 2: attack (start following player)
		// 3: hide (move away from player)
		try {

			outcome = nnfight.action(health, weapon, spiderPower);
			// checkToRetrain =false;
			if (outcome == 1) {
				System.out.println("[INFO : Neural Network] The spiders next astion will be panic.");
				Utils.displayWarningInfo("The closest spider has gone into a panic state !",
						"Neural Network Activated");
				move();
			} else if (outcome == 2) {
				System.out.println("[INFO : Neural Network] The spiders next astion will be attack.");
				Utils.displayWarningInfo("The closest spider has gone into a attack state and is now following !",
						"Neural Network Activated");
				followPlayer();
			} else if (outcome == 3) {
				System.out.println("[INFO : Neural Network] The spiders next astion will be hide.");
				Utils.displayWarningInfo("The closest spider has gone into a hiding state !",
						"Neural Network Activated");

				// Can adapt to make spider run away.
				move();
			} else {
				System.out.println("[INFO : Neural Network] The spiders next action will be move.");
				move();
			}
		} catch (Exception e) {
			// If an exception occurs print it.
			//e.printStackTrace();
		}

	}

}
