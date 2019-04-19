package ie.gmit.sw.ai.traversers;

import ie.gmit.sw.ai.*;
import java.util.*;

public class AStarTraversator extends ResetTraversal implements Traversator { // Adapted from the traversal labs.
	private Node goal;

	private Node pathGoal;
	private int stepsToExit;
	private boolean countSteps;
	private boolean foundGoal;
	private boolean enemySearch;

	public AStarTraversator(Node goal, boolean countSteps) {
		System.out.println("[]A* 2 construct");
		this.goal = goal;
		this.countSteps = countSteps;
	}

	public AStarTraversator(Node goal, boolean countSteps, boolean enemySearch) {
		System.out.println("[]A* 3 construct");

		this.goal = goal;
		this.countSteps = countSteps;
		this.enemySearch = enemySearch;
	}

	@Override
	public void traverse(Node[][] maze, Node node) {

		if (!countSteps && !enemySearch) {
			// When the player uses a special pickup item to find the shortest path
			System.out.println("Using A Star Traversator - Looking for Maze Exit Goal!\n");
			resetA(maze);
			System.out.println("Using A Star Traversator - After reset a!\n");

		} else if (enemySearch) {
			// When the enemy is actively searching for the player
			resetB(maze);
		} else {
			// When the game is computing the amount of steps to the exit point
			resetB(maze);
		}

		long time = System.currentTimeMillis();
		int visitCount = 0;

		PriorityQueue<Node> open = new PriorityQueue<Node>(20,
				(Node current, Node next) -> (current.getPathCost() + current.getHeuristic(goal))
						- (next.getPathCost() + next.getHeuristic(goal)));
		java.util.List<Node> closed = new ArrayList<Node>();

		open.offer(node);
		node.setPathCost(0);
		while (!open.isEmpty()) {
			node = open.poll();
			closed.add(node);
			node.setVisited(true);
			visitCount++;

			// When counting for steps to the goal or displaying path to goal node
			//if (node.isGoalNode() && node.getNodeType() != 'P' && !enemySearch) {
			System.out.println("[DEBUG A*]node.isGoalNode() = "+node.isGoalNode()+"\n node.getNodeType() :"+node.getNodeType());
			if (node.isGoalNode() && node.getNodeType() != 'P') {

				System.out.println("[a* if is]");
				time = System.currentTimeMillis() - time; // Stop the clock
				setStepsToExit(TraversatorStats.printStats(node, time, visitCount, countSteps));
				setFoundGoal(true);
				setPathGoal(node);
				break;

	//		} else if (node.isGoalNode() && node.getNodeType() == 'P' && enemySearch) {
			} else if (node.isGoalNode() && node.getNodeType() == 'P' ) {

				System.out.println("[a* if 1else");
				setFoundGoal(true);
				setPathGoal(node);
				break;
			}

			// Sleep for x amount of seconds
			// sleep(1);

			// Process adjacent nodes
			Node[] children = node.children(maze);
			for (int i = 0; i < children.length; i++) {
				Node child = children[i];
				int score = node.getPathCost() + 1 + child.getHeuristic(goal);
				int existing = child.getPathCost() + child.getHeuristic(goal);

				if ((open.contains(child) || closed.contains(child)) && existing < score) {
					continue;
				} else {
					open.remove(child);
					closed.remove(child);
					child.setParent(node);
					child.setPathCost(node.getPathCost() + 1);
					open.add(child);
				}
			}
		}
	}

	public int getStepsToExit() {
		System.out.println("[A*] getStepsToExit :" + stepsToExit);
		return stepsToExit;
	}

	public void setStepsToExit(int stepsToExit) {
		System.out.println("[A*] setStepsToExit :" + stepsToExit);

		this.stepsToExit = stepsToExit;
	}

	public boolean isFoundGoal() {
		System.out.println("[A*] isFoundGoal :" + foundGoal);

		return foundGoal;
	}

	public void setFoundGoal(boolean foundGoal) {
		System.out.println("[A*] setFoundGoal :" + foundGoal);

		this.foundGoal = foundGoal;
	}

	public Node getPathGoal() {
		System.out.println("[A*] getPathGoal :" + pathGoal);

		return pathGoal;
	}

	public void setPathGoal(Node pathGoal) {
		System.out.println("[A*] setPathGoal :" + pathGoal);

		this.pathGoal = pathGoal;
	}

	public boolean isEnemySearch() {
		System.out.println("[A*] isEnemySearch :" + enemySearch);

		return enemySearch;
	}

	public void setEnemySearch(boolean enemySearch) {
		System.out.println("[A*] setEnemySearch :" + enemySearch);

		this.enemySearch = enemySearch;
	}

}
