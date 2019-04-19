package ie.gmit.sw.ai;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ie.gmit.sw.ai.gui.Utils;
import ie.gmit.sw.ai.nn.NeuralNetworkFight;
import ie.gmit.sw.ai.traversers.Node;

/**
 * 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 *
 *
 *         GameRunner is the runner class of the application. Sets the view and
 *         sprites while also handling user input.
 */
public class GameRunner implements KeyListener {
	private static final int MAZE_DIMENSION = 50;
	private static final int IMAGE_COUNT = 16;
	public static boolean GAME_OVER = false;
	private ControlledSprite player;
	private GameView view;
	private Maze gameMaze;
	private int currentRow;
	private int currentCol;
	public int attack;
	private NeuralNetworkFight nnFight = new NeuralNetworkFight();
	int reply;// variable for JOPtionPane result

	/**
	 *  Main Runner method.
	 *  
	 * @param args, the command line argument.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new GameRunner();

	}
	
	/**
	 * Sets up the game view as well as all associated sprites.
	 * 
	 * @throws Exception
	 */
	public GameRunner() throws Exception {

		// Train the Neural Network at the very start to avoid the game lagging during
		// play.
		nnFight.train();

		gameMaze = new Maze(MAZE_DIMENSION, nnFight);
		view = new GameView(gameMaze);

		// Load the images from the resources and set them as sprites.
		Sprite[] sprites = getSprites();

		// Set the sprites in view.
		view.setSprites(sprites);

		// Set the player in view along and initialise current position.
		updateView();

		Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
		view.setPreferredSize(d);
		view.setMinimumSize(d);
		view.setMaximumSize(d);

		JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development)");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.addKeyListener(this);
		f.getContentPane().setLayout(new FlowLayout());
		f.add(view);
		f.setSize(1000, 1000);
		f.setLocation(100, 100);
		f.pack();
		f.setVisible(true);
	}

	/**
	 * Updates the view regarding the players position.
	 */
	private void updateView() {

		// Set the p-layer object in the maze.
		//view.setPlayer(gameMaze.getPlayer());
		currentRow = gameMaze.getPlayer().getRow();
		currentCol = gameMaze.getPlayer().getCol();

		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}

	/**
	 * Initialise an exit node in the maze for the end of the game and add it to the
	 * view.
	 */
	private void placeExit() {
		int[] exitNode = new int[2];
		exitNode[0] = (int) (MAZE_DIMENSION * Math.random());
		exitNode[1] = (int) (MAZE_DIMENSION * Math.random());
		Node goalNode = new Node(exitNode[0], exitNode[1], 15);

		// Set the exit node as a goal node.
		gameMaze.set(exitNode[0], exitNode[1], goalNode);

		System.out.println("[DEBUG INFO ]Exit Node Set to to [" + exitNode[0] + "][" + exitNode[1] + "]---" + exitNode);
		System.out.println("Exit node is : " + goalNode);
		updateView();
	}

	/**
	 * KeyPressed handles all keyboard inputs.
	 * 
	 * @param e, the key pressed.
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < MAZE_DIMENSION - 1) {
			if (isValidMove(currentRow, currentCol + 1)) {
				player.setDirection(Direction.RIGHT);
				currentCol++;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0) {
			if (isValidMove(currentRow, currentCol - 1)) {
				player.setDirection(Direction.LEFT);
				currentCol--;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0) {
			if (isValidMove(currentRow - 1, currentCol)) {
				player.setDirection(Direction.UP);
				currentRow--;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < MAZE_DIMENSION - 1) {
			if (isValidMove(currentRow + 1, currentCol)) {
				player.setDirection(Direction.DOWN);
				currentRow++;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_Z) {
			view.toggleZoom();
		} else if (e.getKeyCode() == KeyEvent.VK_H) {

			// IMPLEMENT SEARCH FUNCTION FOR PATH CAUSING ISSUES FIX AT END OF PROJECT
			/*
			 * Traversator traveser = new AStarTraversator(model.getP());
			 * //traveser.traverse(model.get, model.getGoalNode());
			 * traverseHelper(model.getGoalNode().getRow(),
			 * model.getGoalNode().getCol(),traveser);
			 */
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			// Display Player and game Statistics in a pop up.
			JOptionPane.showMessageDialog(null,
					"Player Stats:\n Health :" + Math.round(gameMaze.getPlayer().getPlayerHealth()) + "\n Weapon: "
							+ gameMaze.getPlayer().getWeapon().getName());
		} else {
			return;
		}

		updateView();
	}

	/*
	 * public void traverseHelper(int row, int col, Traversator t) {
	 * t.traverse(model.getMaze(), model.getGoalNode()); nextPosition =
	 * t.getNextNode(); if (nextPosition != null) { canMove = true; } else { canMove
	 * = false; } }
	 */
	public void keyReleased(KeyEvent e) {
	} // Ignore

	public void keyTyped(KeyEvent e) {
	} // Ignore

	/**
	 * Checks if a move is valid and if it is acts accordingly.
	 * 
	 * @param row, the row position that the player is attempting to move to.
	 * @param col, the column position that the player is attempting to move to.
	 * @return true if the player move is valid, false if invalid.
	 */
	private boolean isValidMove(int row, int col) {
		Weapon sword = new Weapon("sword", 20);
		Weapon bomb = new Weapon("bomb", 50);
		Weapon hbomb = new Weapon("hbomb", 75);

		if (row <= gameMaze.size() - 1 && col <= gameMaze.size() - 1 && gameMaze.get(row, col).getType() == -1) {
			gameMaze.set(currentRow, currentCol, gameMaze.get(row, col));
			gameMaze.set(row, col, gameMaze.getPlayer());
			return true;
		} else {
			// System.out.println("F Node is " + gameMaze.get(row, col));
			// System.out.println("F Node type is " + gameMaze.get(row, col).getType());

			// Switch on the type of node.
			// For each object detected show a prompt and allow user to select YES/NO to
			// pick up the item
			switch (gameMaze.get(row, col).getType()) {

			// Type 1 is a sword Weapon Object.
			case 1:
				System.out.println("Contact with sword ");
				if (Utils.displaySelectOption(1) == JOptionPane.YES_OPTION) {
					gameMaze.clearNode(row, col);
					gameMaze.getPlayer().setWeapon(sword);
					Utils.displayInfo(gameMaze.getPlayer().getWeapon().getName(), true);
				} else {
					Utils.displayInfo(sword.getName(), false);
				}
				break;

			// Type 3 is a bomb Weapon Object.
			case 3:
				if (Utils.displaySelectOption(3) == JOptionPane.YES_OPTION) {
					// System.out.println("You selected the " + sword + " weapon");
					gameMaze.clearNode(row, col);
					gameMaze.getPlayer().setWeapon(bomb);
					Utils.displayInfo(gameMaze.getPlayer().getWeapon().getName(), true);
				} else {
					Utils.displayInfo(bomb.getName(), false);
				}
				break;

			// Type 4 is a hbomb.
			case 4:
				if (Utils.displaySelectOption(4) == JOptionPane.YES_OPTION) {
					// System.out.println("You selected the " + sword + " weapon");
					gameMaze.clearNode(row, col);
					gameMaze.getPlayer().setWeapon(hbomb);
					Utils.displayInfo(gameMaze.getPlayer().getWeapon().getName(), true);
				} else {
					Utils.displayInfo(hbomb.getName(), false);
				}
				break;

			// Type 14 is a health pick up;
			case 14:
				// Restore the players health or increase it by 30
				if (gameMaze.getPlayer().getPlayerHealth() < 60)
					gameMaze.getPlayer().setPlayerHealth(gameMaze.getPlayer().getPlayerHealth() + 30);
				else
					gameMaze.getPlayer().restoreHealth();

				Utils.displayGeneralInfo("Health(+30) restored to : " + Math.round(gameMaze.getPlayer().getPlayerHealth()),
						"Health Pick up");

				gameMaze.clearNode(row, col);

				break;

			// Type 15 is The exit Node.
			case 15:
				System.out.println("Contact with Exit ");
				Utils.displayGeneralInfo("Hurray You escaped the spiders!!", "Game Complete");
				JOptionPane.showMessageDialog(null, "Hurray You escaped the spiders!!", "Game Complete",
						JOptionPane.INFORMATION_MESSAGE);
				// end the game.
				System.exit(0);
				break;

			// Type 613 are spider objects and must be destroyed after attack.
			case 6:
				System.out.println("Contact with Black ");
				gameMaze.clearSpiderNode(row, col);
				break;
			case 7:
				System.out.println("Contact with Blue ");
				gameMaze.clearSpiderNode(row, col);
				break;
			case 8:
				System.out.println("Contact with Brown ");
				gameMaze.clearSpiderNode(row, col);
				break;
			case 9:
				System.out.println("Contact with Green ");
				gameMaze.clearSpiderNode(row, col);
				break;
			case 10:
				System.out.println("Contact with Grey ");
				gameMaze.clearSpiderNode(row, col);
				break;
			case 11:
				System.out.println("Contact with Orange ");
				gameMaze.clearSpiderNode(row, col);
				break;
			case 12:
				System.out.println("Contact with Red ");
				gameMaze.clearSpiderNode(row, col);
				break;
			case 13:
				System.out.println("Contact with Yellow ");
				gameMaze.clearSpiderNode(row, col);
				break;

			}
		}

		// The player cannot move to the position
		return false;
	}

	/**
	 * Reads in all the images from the resource directory. Each image is assigned
	 * index that can be used to reference the sprite when attaching it to an obje
	 * 
	 * @return An array of Sprites representing game objects.
	 * @throws Exception
	 */
	private Sprite[] getSprites() throws Exception {

		player = new ControlledSprite(0, "Main Player", 3, 500, "resources/images/player/d1.png",
				"resources/images/player/d2.png", "resources/images/player/d3.png", "resources/images/player/l1.png",
				"resources/images/player/l2.png", "resources/images/player/l3.png", "resources/images/player/r1.png",
				"resources/images/player/r2.png", "resources/images/player/r3.png");
		Sprite healthSprite = new Sprite(0, "Health", 1, "resources/images/objects/health.png");
		Sprite exitSprite = new Sprite(0, "Exit", 1, "resources/images/objects/exit.png");

		Sprite[] sprites = new Sprite[IMAGE_COUNT];
		sprites[0] = new Sprite(0, "Hedge", 1, "resources/images/objects/hedge.png");
		sprites[1] = new Sprite(0, "Sword", 1, "resources/images/objects/sword.png");
		sprites[2] = new Sprite(0, "Help", 1, "resources/images/objects/help.png");
		sprites[3] = new Sprite(0, "Bomb", 1, "resources/images/objects/bomb.png");
		sprites[4] = new Sprite(0, "Hydrogen Bomb", 1, "resources/images/objects/h_bomb.png");
		sprites[5] = player;
		sprites[6] = new Sprite(70, "Black Spider", 2, "resources/images/spiders/black_spider_1.png",
				"resources/images/spiders/black_spider_2.png");
		sprites[7] = new Sprite(60, "Blue Spider", 2, "resources/images/spiders/blue_spider_1.png",
				"resources/images/spiders/blue_spider_2.png");
		sprites[8] = new Sprite(30, "Brown Spider", 2, "resources/images/spiders/brown_spider_1.png",
				"resources/images/spiders/brown_spider_2.png");
		sprites[9] = new Sprite(20, "Green Spider", 2, "resources/images/spiders/green_spider_1.png",
				"resources/images/spiders/green_spider_2.png");
		sprites[10] = new Sprite(40, "Grey Spider", 2, "resources/images/spiders/grey_spider_1.png",
				"resources/images/spiders/grey_spider_2.png");
		sprites[11] = new Sprite(50, "Orange Spider", 2, "resources/images/spiders/orange_spider_1.png",
				"resources/images/spiders/orange_spider_2.png");
		sprites[12] = new Sprite(45, "Red Spider", 2, "resources/images/spiders/red_spider_1.png",
				"resources/images/spiders/red_spider_2.png");
		sprites[13] = new Sprite(55, "Yellow Spider", 2, "resources/images/spiders/yellow_spider_1.png",
				"resources/images/spiders/yellow_spider_2.png");
		sprites[14] = healthSprite;// Health Object.
		sprites[15] = exitSprite;// Health Object.

		return sprites;
	}

}