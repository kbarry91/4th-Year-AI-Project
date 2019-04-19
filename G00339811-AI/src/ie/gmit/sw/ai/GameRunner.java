package ie.gmit.sw.ai;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ie.gmit.sw.ai.gui.Utils;
import ie.gmit.sw.ai.nn.NnFight;
import ie.gmit.sw.ai.traversers.Node;
import ie.gmit.sw.ai.traversers.Traversator;

/**
 * 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 *
 *
 * GameRunner is the runner class of the application. Sets the view and sprites while also handling user input.
 */
public class GameRunner implements KeyListener {
	private static final int MAZE_DIMENSION = 50;
	private static final int IMAGE_COUNT = 16;
	public static boolean GAME_OVER = false;
	private static JFrame f;
	private ControlledSprite player;
	private GameView view;
	private Maze model;
	private int currentRow;
	private int currentCol;
	public int attack;
	private NnFight nfight = new NnFight();
	int reply;// variable for JOPtionPane result
	private Node nextPosition;

	/**
	 *  Sets up the game view as well as all associated sprites. 
	 *  
	 * @throws Exception
	 */
	public GameRunner() throws Exception {

		// Train the Neural Network at the very start to avoid the game lagging during play.
		nfight.train();

		model = new Maze(MAZE_DIMENSION, nfight);
		view = new GameView(model);

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

		// set the player
		view.setPlayer(model.getP());
		currentRow = model.getP().getRow();
		currentCol = model.getP().getCol();

		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);

	}

	/**
	 * Initialise an exit node in the maze for the end of the game and add it to the view. 
	 */
	private void placeExit() {
		int[] exitNode = new int[2];
		exitNode[0] = (int) (MAZE_DIMENSION * Math.random());
		exitNode[1] = (int) (MAZE_DIMENSION * Math.random());
		Node goalNode = new Node(exitNode[0], exitNode[1], 15);

		// Set the exit node as a goal node.
		model.set(exitNode[0], exitNode[1], goalNode);
		
		System.out.println("[DEBUG INFO ]Exit Node Set to to [" + exitNode[0] + "][" + exitNode[1] + "]---" + exitNode);
		System.out.println("Exit node is : " + goalNode);
		updateView();
	}

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
			// - Display Player Stats in a popup.
			JOptionPane.showMessageDialog(null, "Player Stats:\n Health :" + Math.round(model.getP().getPlayerHealth())
					+ "\n Weapon: " + model.getP().getWeapon().getName());
		} else {
			return;
		}

		updateView();
	}
/*
	public void traverseHelper(int row, int col, Traversator t) {
		t.traverse(model.getMaze(), model.getGoalNode());
		nextPosition = t.getNextNode();
		if (nextPosition != null) {
			canMove = true;
		} else {
			canMove = false;
		}
	}
*/
	public void keyReleased(KeyEvent e) {
	} // Ignore

	private static void closeGame() {

		// close the game
		f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
	}

	public void keyTyped(KeyEvent e) {
	} // Ignore

	private boolean isValidMove(int row, int col) {
		Weapon sword = new Weapon("sword", 20);
		Weapon bomb = new Weapon("bomb", 50);
		Weapon hbomb = new Weapon("hbomb", 75);

		if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getType() == -1) {
			model.set(currentRow, currentCol, model.get(row, col));
			model.set(row, col, model.getP());
			return true;
		} else {
			System.out.println("F Node   is " + model.get(row, col));
			System.out.println("F Node  type is " + model.get(row, col).getType());

			// Switch on the type of node.
			switch (model.get(row, col).getType()) {

			// Type 1 is a sword Weapon Object.
			case 1:
				System.out.println("Contact with sword ");
				if (Utils.displaySelectOption(1) == JOptionPane.YES_OPTION) {
					// System.out.println("You selected the " + sword + " weapon");
					model.clearNode(row, col);
					model.getP().setWeapon(sword);
					Utils.displayInfo(model.getP().getWeapon().getName(), true);
				} else {
					Utils.displayInfo(sword.getName(), false);
				}

				break;

			// Type 3 is a bomb Weapon Object.
			case 3:
				if (Utils.displaySelectOption(3) == JOptionPane.YES_OPTION) {
					// System.out.println("You selected the " + sword + " weapon");
					model.clearNode(row, col);
					model.getP().setWeapon(bomb);
					Utils.displayInfo(model.getP().getWeapon().getName(), true);
				} else {
					Utils.displayInfo(bomb.getName(), false);
				}
				break;

			// Type 4 is a hbomb.
			case 4:
				if (Utils.displaySelectOption(4) == JOptionPane.YES_OPTION) {
					// System.out.println("You selected the " + sword + " weapon");
					model.clearNode(row, col);
					model.getP().setWeapon(hbomb);
					Utils.displayInfo(model.getP().getWeapon().getName(), true);
				} else {
					Utils.displayInfo(hbomb.getName(), false);
				}
				break;

			// Type 14 is a health pick up;
			case 14:
				// health pickup
				if (model.getP().getPlayerHealth() < 60)
					model.getP().setPlayerHealth(model.getP().getPlayerHealth() + 30);
				else
					model.getP().restoreHealth();

				Utils.displayGeneralInfo("Health(+30) restored to : " + Math.round(model.getP().getPlayerHealth()),
						"Health Pick up");

				model.clearNode(row, col);

				break;
			case 15:
				System.out.println("Contact with Exit ");
				Utils.displayGeneralInfo("Hurray You escaped the spiders!!", "Game Complete");
				JOptionPane.showMessageDialog(null, "Hurray You escaped the spiders!!", "Game Complete",
						JOptionPane.INFORMATION_MESSAGE);
				// end the game.
				System.exit(0);
				break;
			case 6:
				System.out.println("Contact with Black ");
				model.clearSpiderNode(row, col);
				break;
			case 7:
				System.out.println("Contact with Blue ");
				model.clearSpiderNode(row, col);
				break;
			case 8:
				System.out.println("Contact with Brown ");
				model.clearSpiderNode(row, col);
				break;
			case 9:
				System.out.println("Contact with Green ");
				model.clearSpiderNode(row, col);
				break;
			case 10:
				System.out.println("Contact with Grey ");
				model.clearSpiderNode(row, col);
				break;
			case 11:
				System.out.println("Contact with Orange ");
				model.clearSpiderNode(row, col);
				break;
			case 12:
				System.out.println("Contact with Red ");
				model.clearSpiderNode(row, col);
				break;
			case 13:
				System.out.println("Contact with Yellow ");
				model.clearSpiderNode(row, col);
				break;

			}
		}

		return false;// player cant move
	}

	

	private Sprite[] getSprites() throws Exception {
		// Read in the images from the resources directory as sprites. Note that each
		// sprite will be referenced by its index in the array, e.g. a 3 implies a
		// Bomb...
		// Ideally, the array should dynamically created from the images...

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

	public static void main(String[] args) throws Exception {
		new GameRunner();

	}
}