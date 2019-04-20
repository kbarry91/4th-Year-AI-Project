package ie.gmit.sw.ai.fuzzy;

import java.util.ArrayList;
import java.util.Random;

import ie.gmit.sw.ai.Sprite;
import ie.gmit.sw.ai.player;
import ie.gmit.sw.ai.nn.NeuralNetworkFight;
import ie.gmit.sw.ai.gui.Utils;
import ie.gmit.sw.ai.traversers.AStarTraversator;
import ie.gmit.sw.ai.traversers.Node;
import ie.gmit.sw.ai.traversers.Traversator;

/**
 * Fightable implements runnable to allow enemies to run on separate threads.
 * 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 *
 */
public class Fightable extends Sprite implements Runnable {

	// variables
	private int row;
	private int col;
	private int feature;

	private Node node = new Node(row, col, feature);

	// Lock is used to prevent deadlock in a scenario when multiple threads try to
	// access a function. 
	private Object lockThread ;

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
	
	// 
	// searches

	/**
	 * 
	 * @param row
	 * @param col
	 * @param feature
	 * @param lock    the lock for ythe executor
	 * @param maze    the game maze
	 * @param p       the player
	 * @param f       the neural netwprl
	 */
	public Fightable(int row, int col, int feature, Node[][] maze, player p,int damage,Object lockThread) {
		// TODO Auto-generated constructor stub
		this.row = row;
		this.col = col;
		this.feature = feature;
		// player
		this.player = p;
		this.fighterDamage = damage;

		node.setCol(col);
		node.setRow(row);
		node.setType(feature);

		this.maze = maze;

		//this.player.setPlayerHealth(100);
		this.keepThreadAlive = true;
		this.lockThread= lockThread;
		//this.nnfight = f;

		// ONLY GIVING 2 spiders follow
		if (feature == 6) {// fuzzy spider
			// assign a search
			System.out.println("Black spider searching for player using AStarTraversator");

			traverse = new AStarTraversator(this.player);
		} else if (feature == 11) {// nn spider
			System.out.println("Orange spider searching for player using AStarTraversator");
			traverse = new AStarTraversator(this.player);
		} else if (feature == 12) {// nn spider
			System.out.println("Red spider searching for player using AStarTraversator");

			traverse = new AStarTraversator(this.player);
		}

	}

	@Override
	public void run() {
		while (this.keepThreadAlive) {
			try {
				Thread.sleep(1000 * feature / 2);

				// 6 = Black Spider - 7 = Blue spider -  8 = Brown Spider - 9 = Greewn spider..
				if (feature >= 6 && feature <= 9) {
					// 6 is black searching spider
					if (feature == 6) {
						traverse(node.getRow(), node.getCol(), traverse);
					}

					/// If player is touching spider
					if (node.getHeuristic(this.player) == 1) {
						System.out.println("Spider has caught the player !");
						// start a fight.
						fightFuzzy(fighterDamage);
						
					} else if (canMove && node.getHeuristic(this.player) < 10) {
						System.out.println("Spider(fuzzy) is withen 10 squares and is now following player");
						// follow player.
						followPlayer();
					} else {
						// move randomly
						move();
					}

				} else {
					
					// 11 = Orange Spider - 12 = Red spider.
					if (feature == 11 || feature == 12) {
						traverse(node.getRow(), node.getCol(), traverse);
					}
					if (node.getHeuristic(this.player) == 1) {
						System.out.println("Spider has caught the player !");
						// start a fight
						fightFuzzy(fighterDamage);
					} else if (canMove && node.getHeuristic(player) < 5) {
						System.out.println("Spider(Neural Net) is withen 10 squares and is now following player canmove");
						fightNn(this.player.getPlayerHealth(),fighterDamage, 1);

					} else {
						// Move randomly
						move();
					}

				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/// move around

	private void followPlayer() {
		// TODO Auto-generated method stub
		if (nextPosition != null) {
			synchronized (this.lockThread) {
				// Figure out all the nodes around
				Node[] surroundingNodes = node.adjacentNodes(maze);
				// List of empty surrounding nodes
				ArrayList<Node> emptySurroundingNodes = new ArrayList<>();
				// Check if they are empty
				for (Node n : surroundingNodes) {
					if (n.getType() == -1) {
						emptySurroundingNodes.add(n);
					}
				}

				// Check if they are empty
				for (Node n : emptySurroundingNodes) {
					if (nextPosition.equals(n)) {
						// New position of the object
						int newPositionX, newPositionY;
						// Previous position of the object
						int previousPositonX = node.getRow(), previousPositionY = node.getCol();

						System.out.println();
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
				// Move to random in empty
				move();

				nextPosition = null;
				canMove = false;
				return;
			}
		} else {
			move();

			canMove = false;
		}
	}

	/**
	 * 
	 */
	public void move() {

		synchronized (this.lockThread) {
			// Figure out all the nodes around
			Node[] surroundingNodes = node.adjacentNodes(maze);
			// List of empty surrounding nodes
			ArrayList<Node> emptySurroundingNodes = new ArrayList<>();

			// Check if they are empty
			for (Node n : surroundingNodes) {
				if (n.getType() == -1 && n != lastNode) {
					emptySurroundingNodes.add(n);
				}
			}
// make some shit to stop player getting stuck
			if (emptySurroundingNodes.size() > 0) {

				// Move to random surrounding node
				int position = random.nextInt(emptySurroundingNodes.size());

				// New position of the object
				int newPositionX, newPositionY;
				// Previous position of the object
				int previousPositonX = node.getRow(), previousPositionY = node.getCol();
				newPositionX = emptySurroundingNodes.get(position).getRow();// nextPosition.getRow();
				newPositionY = emptySurroundingNodes.get(position).getCol();// nextPosition.getCol();
				node.setRow(newPositionX);
				node.setCol(newPositionY);

				lastNode = new Node(previousPositonX, previousPositionY, -1);
				maze[newPositionX][newPositionY] = node;
				maze[previousPositonX][previousPositionY] = lastNode;

			} else {
				// stop getting stuck ye cunts
				int newPositionX, newPositionY;
				// Previous position of the object
				int previousPositonX = lastNode.getRow(), previousPositionY = lastNode.getCol();
				newPositionX = lastNode.getRow();// nextPosition.getRow();
				newPositionY = lastNode.getCol();// nextPosition.getCol();
				node.setRow(newPositionX);
				node.setCol(newPositionY);

				lastNode = new Node(previousPositonX, previousPositionY, -1);
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

		//System.out.println("Fuzzy fight damage" + Math.round(damageToGive) + "player health now :" + Math.round(player.getPlayerHealth()));
		Utils.displayWarningInfo("You just lost "+ Math.round(damageToGive) +"health in that battle !", "Youve been attacked");
		
		this.keepThreadAlive=false;
		// shut her down
		if (player.getPlayerHealth() < 0) {
			Utils.displayWarningInfo("Game Over your Health reached 0 !", "GameOver");
			System.exit(0);
		}
	}

	public void traverse(int row, int col, Traversator t) {
		t.traverse(maze, maze[row][col]);
		nextPosition = t.getNextNode();
		if (nextPosition != null) {
			canMove = true;
		} else {
			canMove = false;
		}
	}

	public void fightNn(double health, double angerLevel, double weapon) {
		System.out.println("In fighnnt meythod in fightable");

// NORMAILIZE VALUES++=========================
		if (health < 30) {
			health = 0;
		} else if (health > 30 && health < 60) {
			health = 1;
		} else if (health > 60) {
			health = 2;
		}

		if (angerLevel < 30) {
			angerLevel = 0;
		} else if (angerLevel > 30 && angerLevel < 60) {
			angerLevel = 1;
		} else if (angerLevel > 60) {
			angerLevel = 2;
		}

		try {

			outcome = nnfight.action(health, weapon, angerLevel);
			// checkToRetrain =false;
			if (outcome == 1) {
				System.out.println("panic");
				Utils.displayWarningInfo("The closest spider has gone into a panic state !",
						"Neural Network Activated");
				move();
			} else if (outcome == 2) {
				System.out.println("attack");
				Utils.displayWarningInfo("The closest spider has gone into a attack state and is now following !",
						"Neural Network Activated");

				followPlayer();
			} else if (outcome == 3) {
				Utils.displayWarningInfo("The closest spider has gone into a hiding state !",
						"Neural Network Activated");

				// bang in some run away stuff
				move();
				System.out.println("hide");

			}

			else
				move();
		} catch (Exception e) {
		}
		
	}

}
