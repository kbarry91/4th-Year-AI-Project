package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutorService;

import javax.swing.*;

import ie.gmit.sw.ai.nn.NnFight;
import ie.gmit.sw.ai.traversers.AStarTraversator;
import ie.gmit.sw.ai.traversers.Node;
import ie.gmit.sw.ai.traversers.Traversator;

public class GameRunner implements KeyListener {
	private static final int MAZE_DIMENSION = 50;
	private static final int IMAGE_COUNT = 15;
	private ControlledSprite player;
	private GameView view;
	private Maze model;
	private int currentRow;
	private int currentCol;
	private NnFight nnfight;

	// Variables for exit stores position
	private int[] exitNode = new int[2];
	private Node goalNode;
	private Sprite exitSprite;

	// Variable for playerNode;
	private Node playerNode;
	private Sprite healthSprite;

	public GameRunner() throws Exception {
		model = new Maze(MAZE_DIMENSION);
		view = new GameView(model);

		Sprite[] sprites = getSprites();
		view.setSprites(sprites);

		placePlayer();
		// Set the exit node.
		placeExit();
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

	private void placePlayer() {
		currentRow = (int) (MAZE_DIMENSION * Math.random());
		currentCol = (int) (MAZE_DIMENSION * Math.random());
		model.set(currentRow, currentCol, '5'); // Player is at index 5
		System.out.println("Player Set to to [" + currentRow + "][" + currentCol + "]");

		updateView();
	}

	private void placeExit() {
		exitNode[0] = (int) (MAZE_DIMENSION * Math.random());
		exitNode[1] = (int) (MAZE_DIMENSION * Math.random());

		// Set exit values as a goalNode of Node type so ity can be used in a search
		goalNode = new Node(exitNode[0], exitNode[1]);

		model.set(exitNode[0], exitNode[1], '4'); // exit is at index 5
		System.out.println("Exit Node Set to to [" + exitNode[0] + "][" + exitNode[1] + "]---" + exitNode);
		updateView();
	}

	private void updateView() {
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
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
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			// - Display Player Stats in a popup.
			JOptionPane.showMessageDialog(null, "Player Stats:\n Health :" + player.getHealth() + "\n Weapon: "
					+ player.getWeaponO().getName() + "\n Path to goal activated: " + player.isHelpIsActive());
		} else if (e.getKeyCode() == KeyEvent.VK_H) {
			// 'H' Key selected to generate path to goal
			if (!player.isHelpIsActive()) {
				JOptionPane.showMessageDialog(null, "Path Help not activated collect a Help Pickup First!",
						"View Path To goal", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Path Help  activated ", "View Path To goal",
						JOptionPane.INFORMATION_MESSAGE);
				Traversator traverse = algorithm(0); // Try to make it do A*.
				playerNode = new Node(currentRow, currentCol);
				traverse.traverse(model.getMazeNode(), playerNode);
			}
		} else {
			return;
		}
		System.out.println("Above update view");
		updateView();
		System.out.println("UNder update view");

	}

	public void keyReleased(KeyEvent e) {
	} // Ignore

	public void keyTyped(KeyEvent e) {
	} // Ignore

	private boolean isValidMove(int row, int col) {
		Weapon sword = new Weapon("sword", 20);
		Weapon bomb = new Weapon("bomb", 50);
		Weapon hbomb = new Weapon("hbomb", 75);

		Fight fuzzyfight = new Fight();
		Double damagToInflict = 0.0;
		int reply;// variable for joptionpane choice
		System.out.println("Model.getmazw::" + model.getMaze());

		if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == ' ') {
			model.set(currentRow, currentCol, '\u0020');
			model.set(row, col, '5');
			System.out.println("Player moved to [" + row + "][" + col + "]");
			System.out.println("T Node  type is " + model.get(row, col));
			return true;
		} else {

			System.out.println("F Node  type is " + model.get(row, col));
			switch (model.get(row, col)) {
			case '1':
				System.out.println("Contact with sword ");
				reply = JOptionPane.showConfirmDialog(null, "Would you like to collect the " + "sword?",
						"Weapon Encountered", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					System.out.println("You selected the " + sword + " weapon");
					player.setWeaponO(sword);
					model.set(row, col, '0');// replace object with hedge
				} else {
					JOptionPane.showMessageDialog(null, "Weapon not collected");
					// System.exit(0);
				}

//				player.setWeapon("Sword");

				break;
			case '2':
				// player comes in contact with help symbol

				System.out.println("Contact with help ");
				reply = JOptionPane.showConfirmDialog(null, "Would you like to collect generate path to goal?",
						"Help Encountered", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {

					model.set(row, col, '0');// replace object with hedge
					player.setHelpIsActive(true);
					JOptionPane.showMessageDialog(null,
							"Help is activated, Press 'H' in map view to view best root to goal node");
				} else {
					JOptionPane.showMessageDialog(null, "Help not activated");
					// System.exit(0);
				}
			case '3':
				System.out.println("Contact with bomb ");
				player.setWeaponO(bomb);

				break;
			case '4':
				System.out.println("Contact with Exit ");
				JOptionPane.showMessageDialog(null, "Hurray You escaped the spiders!!", "Game Complete",
						JOptionPane.INFORMATION_MESSAGE);
				// end the game.
				System.exit(0);
				break;
			case '6':
				System.out.println("Contact with Black Spider ");
				reply = JOptionPane.showConfirmDialog(null, "Would you like to fight the " + "black" + "spider",
						"Spider Encountered", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					damagToInflict = fuzzyfight.getFightDamage(player.getWeaponO().getDamage(), 90);
					System.out.println("\n=======Fight========\n weapon:" + player.getWeaponO().getName()
							+ "\nSpider:Black\n Damage:" + damagToInflict + "\n=======Over======== ");
					System.out.println("Player health before :" + player.getHealth() + " after: "
							+ (player.getHealth() - damagToInflict));
					model.set(row, col, '\u0020');// replace object with empty
				} else {
					JOptionPane.showMessageDialog(null, "You didnt fight!");
					// System.exit(0);
				}
				/*
				 * damagToInflict = fuzzyfight.getFightDamage(player.getWeaponO().getDamage(),
				 * 90); System.out.println("\n=======Fight========\n weapon:" +
				 * player.getWeaponO().getName() + "\nSpider:Black\n Damage:" + damagToInflict +
				 * "\n=======Over======== "); System.out.println("Player health before :" +
				 * player.getHealth() + " after: " + (player.getHealth() - damagToInflict));
				 */
				break;
			case '7':
				System.out.println("Contact with Blue Spider ");
				damagToInflict = fuzzyfight.getFightDamage(player.getWeaponO().getDamage(), 50);
				System.out.println("\n=======Fight========\n weapon:" + player.getWeaponO().getName()
						+ "\nSpider:Blue\n Damage:" + damagToInflict + "\n=======Over======== ");
				System.out.println("Player health before :" + player.getHealth() + " after: "
						+ (player.getHealth() - damagToInflict));

				break;
			case '8':
				System.out.println("Contact with Brown Spider ");
				damagToInflict = fuzzyfight.getFightDamage(player.getWeaponO().getDamage(), 20);
				System.out.println("\n=======Fight========\n weapon:" + player.getWeaponO().getName()
						+ "\nSpider:Brown\n Damage:" + damagToInflict + "\n=======Over======== ");
				System.out.println("Player health before :" + player.getHealth() + " after: "
						+ (player.getHealth() - damagToInflict));

				break;
			case ':':
				System.out.println("Contact with Grey Spider ");
				reply = JOptionPane.showConfirmDialog(null, "Would you like to fight the " + "grey" + "spider",
						"Spider Encountered", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					damagToInflict = fuzzyfight.getFightDamage(player.getWeaponO().getDamage(), 40);
					fuzzyfight.getNNFightDamage(player.getHealth(), 40, player.getWeaponO().getDamage());
					System.out.println("\n=======Fight========\n weapon:" + player.getWeaponO().getName()
							+ "\nSpider:Grey\n Damage:" + damagToInflict + "\n=======Over======== ");
					System.out.println("Player health before :" + player.getHealth() + " after: "
							+ (player.getHealth() - damagToInflict));
					model.set(row, col, '\u0020');// replace object with empty
				} else {
					JOptionPane.showMessageDialog(null, "You didnt fight!");
					// System.exit(0);
				}
				/*
				 * damagToInflict = fuzzyfight.getFightDamage(player.getWeaponO().getDamage(),
				 * 90); System.out.println("\n=======Fight========\n weapon:" +
				 * player.getWeaponO().getName() + "\nSpider:Black\n Damage:" + damagToInflict +
				 * "\n=======Over======== "); System.out.println("Player health before :" +
				 * player.getHealth() + " after: " + (player.getHealth() - damagToInflict));
				 */
				break;
			case '>':
				// health pickup
				if (player.getHealth() < 60) {
					player.setHealth(player.getHealth() + 40);
				} else {
					player.setHealth(100);
				}
				JOptionPane.showMessageDialog(null, "Health restored to : " + player.getHealth(), "Health Pick up",
						JOptionPane.INFORMATION_MESSAGE);
				break;
			}

			player.setHealth(player.getHealth() - damagToInflict);
			return false; // Can't move
		}
	}

	private Sprite[] getSprites() throws Exception {
		// Read in the images from the resources directory as sprites. Note that each
		// sprite will be referenced by its index in the array, e.g. a 3 implies a
		// Bomb...
		// Ideally, the array should dynamically created from the images...

		// Workshop
		/**
		 * ExecutorService pool = new FixedSizeThreadPool(167);
		 */
		player = new ControlledSprite("Main Player", 3, "resources/images/player/d1.png",
				"resources/images/player/d2.png", "resources/images/player/d3.png", "resources/images/player/l1.png",
				"resources/images/player/l2.png", "resources/images/player/l3.png", "resources/images/player/r1.png",
				"resources/images/player/r2.png", "resources/images/player/r3.png");
		exitSprite = new Sprite("Exit Node", 1, "resources/images/objects/exit.png");
		healthSprite = new Sprite("Health", 1, "resources/images/objects/health.png");

		Sprite[] sprites = new Sprite[IMAGE_COUNT];
		sprites[0] = new Sprite("Hedge", 1, "resources/images/objects/hedge.png");
		sprites[1] = new Sprite("Sword", 1, "resources/images/objects/sword.png");
		sprites[2] = new Sprite("Help", 1, "resources/images/objects/help.png");
		sprites[3] = new Sprite("Bomb", 1, "resources/images/objects/bomb.png");
		// sprites[4] = new Sprite("Hydrogen Bomb", 1,
		// "resources/images/objects/h_bomb.png");
		sprites[4] = exitSprite;

		sprites[5] = player;
		sprites[6] = new Sprite("Black Spider", 2, "resources/images/spiders/black_spider_1.png",
				"resources/images/spiders/black_spider_2.png");
		sprites[7] = new Sprite("Blue Spider", 2, "resources/images/spiders/blue_spider_1.png",
				"resources/images/spiders/blue_spider_2.png");
		sprites[8] = new Sprite("Brown Spider", 2, "resources/images/spiders/brown_spider_1.png",
				"resources/images/spiders/brown_spider_2.png");
		sprites[9] = new Sprite("Green Spider", 2, "resources/images/spiders/green_spider_1.png",
				"resources/images/spiders/green_spider_2.png");
		sprites[10] = new Sprite("Grey Spider", 2, "resources/images/spiders/grey_spider_1.png",
				"resources/images/spiders/grey_spider_2.png");
		sprites[11] = new Sprite("Orange Spider", 2, "resources/images/spiders/orange_spider_1.png",
				"resources/images/spiders/orange_spider_2.png");
		sprites[12] = new Sprite("Red Spider", 2, "resources/images/spiders/red_spider_1.png",
				"resources/images/spiders/red_spider_2.png");
		sprites[13] = new Sprite("Yellow Spider", 2, "resources/images/spiders/yellow_spider_1.png",
				"resources/images/spiders/yellow_spider_2.png");
		sprites[14] = healthSprite;// Health Object.
		// sprites[14] = new Sprite("Exit Node", 1,
		// "resources/images/objects/exiiy.png");
		// Workshop
		/**
		 * for(Sprite s : sprites) { pool.execute(s); }
		 */
		return sprites;
	}

	private Traversator algorithm(int randNum) {
		// Selecting a random algorithm to be created and returned
		/*
		 * switch (randNum) { case 0: return new
		 * AStarTraversator(game.getModel().getGoalNode(), false); case 1: return new
		 * BeamTraversator(game.getModel().getGoalNode(), 10); case 2: // return new
		 * SimulatedAnnealingTraversator(game.getModel().getGoalNode()); case 3: return
		 * new BestFirstTraversator(game.getModel().getGoalNode()); case 4: return new
		 * BasicHillClimbingTraversator(game.getModel().getGoalNode()); case 5: //return
		 * new DepthLimitedDFSTraversator(game.getMaze().length); return new
		 * DepthLimitedDFSTraversator(5); // Only works sometimes. case 6: //return new
		 * IDAStarTraversator(game.getModel().getGoalNode()); case 7: //return new
		 * IDDFSTraversator(); default: return new
		 * AStarTraversator(game.getModel().getGoalNode(), false); }
		 */
		return new AStarTraversator(goalNode, false);
	}

	public static void main(String[] args) throws Exception {
		new GameRunner();
	}
}