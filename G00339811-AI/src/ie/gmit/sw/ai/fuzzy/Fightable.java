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

// runnable thread each spiuder with player
public class Fightable extends Sprite implements Runnable {

	// variables
	private int row;
	private int col;
	private int feature;

	private Node node = new Node(row, col, feature);
	private Object executor;// name it lock(true when thread is running).Prevent deadlock... CAN CHNAGE TO
							// BOOLEAN
	private Node[][] maze;

	private Random random = new Random();
	private Node lastNode;
	private player p;
	private Traversator traverse;
	private Node nextPosition;
	private boolean canMove;
	private NeuralNetworkFight nnfight;
	private int outcome;

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
	public Fightable(int row, int col, int feature, Object lock, Node[][] maze, player p, NeuralNetworkFight f) {
		// TODO Auto-generated constructor stub
		this.row = row;
		this.col = col;
		this.feature = feature;
		// player
		this.p = p;

		node.setCol(col);
		node.setRow(row);
		node.setType(feature);

		this.executor = lock;
		this.maze = maze;

		p.setPlayerHealth(100);

		this.nnfight = f;

		// ONLY GIVING 2 spiders follow
		if (feature == 6) {// fuzzy spider
			// assign a search
			traverse = new AStarTraversator(p);
		} else if (feature == 11) {// nn spider
			traverse = new AStarTraversator(p);
		}else if (feature == 12) {// nn spider
			traverse = new AStarTraversator(p);
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Thread.sleep(1000 * feature / 2);

				// fuzzy stuff
				if (feature >= 6 && feature <= 9) {
// fuzzy spiders to fuzzy stuff
					if (feature == 6) {// black is searcjing
						traverse(node.getRow(), node.getCol(), traverse);
					}

					// fuzzy stuff
					// if player is touvhing
					if (node.getHeuristic(p) == 1) {
						System.out.println("The player is touching spider<=1s");
						System.out.println("[super.getanger<=1:] : "+super.getAnger());
						fight(super.getAnger());
					} else if (canMove && node.getHeuristic(p) < 10) {
						System.out.println("The Player is Near!! Follow Him");
						followPlayer();
					} else {
						move();
					}
				} else {
					// neural net stuff
// nn spiders do nn stuff
					// oranhe
					if (feature == 11 || feature == 12) {
						traverse(node.getRow(), node.getCol(), traverse);
					}
					if (node.getHeuristic(p) == 1) {
						System.out.println("The player is touching spiders11");
						System.out.println("[super.getanger==11:] : "+super.getAnger());
						fight(super.getAnger());
					} else if (canMove && node.getHeuristic(p) < 5) {
						System.out.println("Neural Network Training within Range");
						fightNn(p.getPlayerHealth(), super.getAnger(), 1);

					} else {
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
			synchronized (executor) {
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

		synchronized (executor) {
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
				
			}else {
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

	public void fight(int attack) {

		Fight f = new Fight();

		double health = p.getPlayerHealth();
		double newHealth = f.PlayerHealth(50, attack);
		double hello = health - newHealth;
		System.out.println(hello);
		p.setPlayerHealth(hello);
		System.out.println(p.getPlayerHealth());
		
		// shut her down
		if (p.getPlayerHealth()<0)
		{
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
