package ie.gmit.sw.ai;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ie.gmit.sw.ai.fuzzy.Fightable;
import ie.gmit.sw.ai.nn.NeuralNetworkFight;
import ie.gmit.sw.ai.traversers.Node;

public class Maze {
	// first changed to node array
	private Node[][] maze; // An array does not lend itself to the type of mazge generation alogs we use in
							// the labs. There are no "walls" to carve...
	// Lock object uysed to run the while loop in runnable.
	private Object lock = new Object();
	public Node goalNode;

	private ExecutorService executor = Executors.newCachedThreadPool();
	// Player object.
	private player p;

	// Initilizing the neural network class here
	// train the NN
	// we dont train the nn each time the spider is running
	private NeuralNetworkFight f;

	public Maze(int dimension, NeuralNetworkFight nfight) {

		// Initilizing
		this.f = nfight;

		// second
		maze = new Node[dimension][dimension];
		init();
		buildMaze();

		// sets it to 1 percent of maze
		int featureNumber = (int) ((dimension * dimension) * 0.01); // Change this value to control the number of
																	// objects
//		addFeature('\u0031', '0', featureNumber); //1 is a sword, 0 is a hedge
//		addFeature('\u0032', '0', featureNumber); //2 is help, 0 is a hedge
//		addFeature('\u0033', '0', featureNumber); //3 is a bomb, 0 is a hedge
//		addFeature('\u0034', '0', featureNumber); //4 is a hydrogen bomb, 0 is a hedge

		// made into int rather than chars
		addFeature(1, 0, featureNumber); // 1 is a sword, 0 is a hedge
		addFeature(2, 0, featureNumber); // 2 is help, 0 is a hedge
		addFeature(3, 0, featureNumber); // 3 is a bomb, 0 is a hedge
		addFeature(4, 0, featureNumber); // 4 is a hydrogen bomb, 0 is a hedge
		addFeature(14, 0, featureNumber); // 4 is a health pickup , 0 is a hedge

		featureNumber = (int) ((dimension * dimension) * 0.001); // Change this value to control the number of spiders
//		addFeature('\u0036', '0', featureNumber); //6 is a Black Spider, 0 is a hedge
//		addFeature('\u0037', '0', featureNumber); //7 is a Blue Spider, 0 is a hedge
//		addFeature('\u0038', '0', featureNumber); //8 is a Brown Spider, 0 is a hedge
//		addFeature('\u0039', '0', featureNumber); //9 is a Green Spider, 0 is a hedge
//		addFeature('\u003A', '0', featureNumber); //: is a Grey Spider, 0 is a hedge
//		addFeature('\u003B', '0', featureNumber); //; is a Orange Spider, 0 is a hedge
//		addFeature('\u003C', '0', featureNumber); //< is a Red Spider, 0 is a hedge
//		addFeature('\u003D', '0', featureNumber); //= is a Yellow Spider, 0 is a hedge

		// player was inistiated in game runnner biut changed t here
		player(5, -1);

		// spiders
		addFeature(6, -1, featureNumber); // 6 is a Black Spider, 0 is a hedge
		addFeature(7, -1, featureNumber); // 7 is a Blue Spider, 0 is a hedge
		addFeature(8, -1, featureNumber); // 8 is a Brown Spider, 0 is a hedge
		addFeature(9, -1, featureNumber); // 9 is a Green Spider, 0 is a hedge
		addFeature(10, -1, featureNumber); // : is a Grey Spider, 0 is a hedge
		addFeature(11, -1, featureNumber); // ; is a Orange Spider, 0 is a hedge
		addFeature(12, -1, featureNumber); // < is a Red Spider, 0 is a hedge
		addFeature(13, -1, featureNumber); // = is a Yellow Spider, 0 is a hedge
		addFeature(15, 0, 1);// 15 is a exit object
	}

	private void init() {
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {
				// maze[row][col] = '0'; //Index 0 is a hedge...
				// here at the start of the game we are going to init the whole
				// maze with a value of 0
				maze[row][col] = new Node(row, col, 0);
			}
		}
	}

	// changed add feature so now it takes and int
	private void addFeature(int feature, int replace, int number) {

		int counter = 0;
		// this will draw the amount of spiders and hedges
		while (counter < number) { // Keep looping until feature number of items have been added
			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());
			//
			if (maze[row][col].getType() == replace) {
				// send spiders to threadpool class and the player aswell (Running on seperate
				// threwads)
				if (feature > 5 && feature < 14) {
					Fightable f = new Fightable(row, col, feature, lock, maze, p, this.f);

					// execute the thread pool
					executor.execute(f);
				}

				// change its type to whatever the name of the spider is
				maze[row][col].setType(feature);
				/*
				if (feature == 15) {
					//goalNode = maze[row][col];
					setGoalNode(maze[row][col]);
					maze[row][col].setGoalNode(true);
				}
				*/
				counter++;
			}
		}
	}

	public Node getGoalNode() {
		return goalNode;
	}

	public void setGoalNode(Node goalNode) {
		this.goalNode = goalNode;
	}

	private void buildMaze() {
		for (int row = 1; row < maze.length - 1; row++) {
			for (int col = 1; col < maze[row].length - 1; col++) {
				int num = (int) (Math.random() * 10);
				if (isRoom(row, col))
					continue;
				if (num > 5 && col + 1 < maze[row].length - 1) {
					// maze[row][col + 1] = '\u0020'; //\u0020 = 0x20 = 32 (base 10) = SPACE

					// space part of maze is now srt to type of -21
					maze[row][col + 1].setType(-1);
				} else {
//					if (row + 1 < maze.length - 1) maze[row + 1][col] = '\u0020';
					if (row + 1 < maze.length - 1)
						maze[row + 1][col].setType(-1);
				}
			}
		}
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
				p = new player(row, col, feature, maze);
				maze[row][col] = p;
				placed = true;
			}
		}
	}

	/**
	 *
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean isRoom(int row, int col) { // Flaky and only works half the time, but reduces the number of rooms
//		return row > 1 && maze[row - 1][col] == '\u0020' && maze[row - 1][col + 1] == '\u0020';
		return row > 1 && maze[row - 1][col].getType() == -1 && maze[row - 1][col + 1].getType() == -1;
	}

	/**
	 * 
	 * @return
	 */
	public Node[][] getMaze() {
		return this.maze;
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public Node get(int row, int col) {
		return this.maze[row][col];
	}

	/**
	 * 
	 * @param row
	 * @param col
	 * @param node
	 */
	public void set(int row, int col, Node node) {
		node.setCol(col);
		node.setRow(row);

		this.maze[row][col] = node;
	}

	public int size() {
		return this.maze.length;
	}

	public player getP() {
		return this.p;
	}

	public void setP(int row, int col) {
		this.maze[row][col] = this.p;
		this.p.setRow(row);
		this.p.setCol(col);
	}

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

	/**
	 * Set the node as type 0 to change it back to a hedge.
	 * 
	 * @param row
	 * @param col
	 */
	public void clearNode(int row, int col) {
		get(row, col).setType(0);
	}
	public void clearSpiderNode(int row, int col) {
		get(row, col).setType(-1);
	}
}