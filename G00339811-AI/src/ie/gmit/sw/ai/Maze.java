package ie.gmit.sw.ai;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ie.gmit.sw.ai.fuzzy.Fightable;
import ie.gmit.sw.ai.nn.NeuralNetworkFight;
import ie.gmit.sw.ai.traversers.Node;

/**
 * Maze is the game object that defines and populates the game map
 * 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 *
 */
public class Maze {
	// A 2-dimensional Node array that represents the game maze.
	private Node[][] maze;

	// Lock object used to run the while loop in runnable.
	private Object lock = new Object();

	// The goal node.
	public Node goalNode;

	// Creates a thread pool that creates new threads as needed.
	// Assign Thread pool to Executor service used to track tasks.
	private ExecutorService executor = Executors.newCachedThreadPool();

	// Player object.
	private player player;

	// Initialise the NeuralNetworkFight class.
	private NeuralNetworkFight nnFighter;

	/**
	 * Constructor to create the maze object. The NeuralNetworkFight is taken in as
	 * a parameter as it was ttrained at the start of the program.
	 * 
	 * @param dimension the size of the maze.
	 * @param           nfight, pass in the trained Neural Network.
	 */
	public Maze(int dimension, NeuralNetworkFight nfight) {

		this.nnFighter = nfight;

		// Set the size of the maze.
		maze = new Node[dimension][dimension];

		// Initialise a blank maze.
		init();
		buildMaze();

		// Set the amount of pick up objects to be added.
		// 0.01 sets it to 1 percent of maze.
		int featureNumber = (int) ((dimension * dimension) * 0.01);
		addFeature(1, 0, featureNumber); // 1 is a sword, 0 is a hedge
		addFeature(2, 0, featureNumber); // 2 is help, 0 is a hedge
		addFeature(3, 0, featureNumber); // 3 is a bomb, 0 is a hedge
		addFeature(4, 0, featureNumber); // 4 is a hydrogen bomb, 0 is a hedge
		addFeature(14, 0, featureNumber);// 14 is a health pickup , 0 is a hedge

		// Set the amount of enemy objects to be added.
		// 0.001 sets it to .1 percent of maze.
		featureNumber = (int) ((dimension * dimension) * 0.001);
		addFeature(6, -1, featureNumber); // 6 is a Black Spider, -1 is a blank background.
		addFeature(7, -1, featureNumber); // 7 is a Blue Spider, -1 is a blank background.
		addFeature(8, -1, featureNumber); // 8 is a Brown Spider, -1 is a blank background.
		addFeature(9, -1, featureNumber); // 9 is a Green Spider, -1 is a blank background.
		addFeature(10, -1, featureNumber); // : is a Grey Spider, -1 is a blank background.
		addFeature(11, -1, featureNumber); // ; is a Orange Spider,-1 is a blank background.
		addFeature(12, -1, featureNumber); // < is a Red Spider, -1 is a blank background.
		addFeature(13, -1, featureNumber); // = is a Yellow Spider,-1 is a blank background.
		addFeature(15, 0, 1);// 15 is a exit object

		// Instantiate the player.
		player(5, -1);
	}

	/**
	 * Initialise the maze with Blank nodes.
	 */
	private void init() {

		for (int row = 0; row < maze.length; row++) {

			for (int col = 0; col < maze[row].length; col++) {
				maze[row][col] = new Node(row, col, 0);
			}
		}
	}

	/**
	 * Adds the specified features to the maze.
	 * 
	 * @param feature, the feature (game object) to add.
	 * @param replace, the valuer to replace.
	 * @param number, the number of features to add.
	 */
	private void addFeature(int feature, int replace, int number) {

		int counter = 0;

		// Loop through adding features until the number of features specified has been
		// added.
		while (counter < number) {
			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());

			// Check if the node type is set to replace.
			if (maze[row][col].getType() == replace) {

				// Pass the objects to the class containing the Thread pool to run on separate
				// threads.
				if (feature > 5 && feature < 14) {
					Fightable fighter = new Fightable(row, col, feature, lock, maze, player, this.nnFighter);

					// Execute the thread pool with the fightable.
					executor.execute(fighter);
				}

				// Set node type to type of object.
				maze[row][col].setType(feature);
				counter++;
			}
		}
	}

	/**
	 * Builds the maze.
	 */
	private void buildMaze() {

		for (int row = 1; row < maze.length - 1; row++) {
			for (int col = 1; col < maze[row].length - 1; col++) {
				int num = (int) (Math.random() * 10);

				if (isRoom(row, col))
					continue;

				if (num > 5 && col + 1 < maze[row].length - 1) {
					// Set The blank paths in the maze.
					maze[row][col + 1].setType(-1);
				} else {
					if (row + 1 < maze.length - 1)
						maze[row + 1][col].setType(-1);
				}
			}
		}
	}

	/**
	 * Get the goal node.
	 * 
	 * @return Node the goal node.
	 */
	public Node getGoalNode() {
		return goalNode;
	}

	/**
	 * Set the goalNode.
	 * 
	 * @param goalNode the node to set.
	 */
	public void setGoalNode(Node goalNode) {
		this.goalNode = goalNode;
	}

	/**
	 * Puts the player ontpo the maze
	 * 
	 * @param feature
	 * @param replace
	 */
	public void player(int feature, int replace) {
		// check if player has been put onto maze
		boolean placed = false;
		while (!placed) {
			// get row and col values
			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());

			// if row and colvalues are not -1 meaning it not space rather than a hedge
			// Basically saying its not empty dont place it
			if (maze[row][col].getType() == replace) {

				// if row and col value are empty the n place the player
				player = new player(row, col, feature, maze);
				maze[row][col] = player;
				placed = true;
			}
		}
	}

	/**
	 * Check the location on maze to try reduce the number of rooms.
	 * 
	 * @param row , the row index.
	 * @param col , the column index.
	 * @return
	 */
	private boolean isRoom(int row, int col) {
		return row > 1 && maze[row - 1][col].getType() == -1 && maze[row - 1][col + 1].getType() == -1;
	}

	/**
	 * Get the maze.
	 * 
	 * @return a 2-dim node array representing the maze.
	 */
	public Node[][] getMaze() {
		return this.maze;
	}

	/**
	 * Retrieve a specific node.
	 * 
	 * @param row , the row index.
	 * @param col , the column index.
	 * @return
	 */
	public Node get(int row, int col) {
		return this.maze[row][col];
	}

	/**
	 * Set a node at the specified index.
	 * 
	 * @param row , the row index.
	 * @param col , the column index.
	 * @param     node, the node to put on index.
	 */
	public void set(int row, int col, Node node) {
		node.setCol(col);
		node.setRow(row);

		this.maze[row][col] = node;
	}

	/**
	 * Get the size of the maze.
	 * 
	 * @return the length of the maze.
	 */
	public int size() {
		return this.maze.length;
	}

	/**
	 * Get the player object.
	 * 
	 * @return the player object.
	 */
	public player getPlayer() {
		return this.player;
	}

	/**
	 * Set the player on the maze.
	 * 
	 * @param row , the row index.
	 * @param col , the column index.
	 */
	public void setPlayer(int row, int col) {
		this.maze[row][col] = this.player;
		this.player.setRow(row);
		this.player.setCol(col);
	}

	/**
	 * Set the node as type 0 to change it back to a hedge.
	 * 
	 * @param row , the row index.
	 * @param col , the column index.
	 */
	public void clearNode(int row, int col) {
		get(row, col).setType(0);
	}

	/**
	 * Set the node as type -1 to change it back to a blank.
	 * 
	 * @param row , the row index.
	 * @param col , the column index.
	 */
	public void clearSpiderNode(int row, int col) {
		get(row, col).setType(-1);
	}

	/**
	 * Return a string representation of the maze.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {
				sb.append(maze[row][col]);
				if (col < maze[row].length - 1)
					sb.append(",");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}