package ie.gmit.sw.ai.traversers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Implementation of A star search algorithim.
 * 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 *
 */
public class AStarTraversator implements Traversator{
	private Node goal;
	private LinkedList<Node> path = null;
	private List<Node> fullPath = null;
	
	/**
	 * Constructor
	 * 
	 * @param goal the goal node to traverse.
	 */
	public AStarTraversator(Node goal){
		this.goal = goal;
	}

	
	/**
	 * Traverse the maze to locate value to goal node.
	 * 
	 * @param maze the game maze. @param, the goal node to search.
	 * 
	 */
	public void traverse(Node[][] maze, Node node) {

		long time = System.currentTimeMillis();
		int visitCount = 0;

		// Assign open nodes top a priority queue.
		PriorityQueue<Node> open = new PriorityQueue<Node>(20,
				(Node current, Node next) -> (current.getPathCost() + current.getHeuristic(goal))
						- (next.getPathCost() + next.getHeuristic(goal)));
		java.util.List<Node> closed = new ArrayList<Node>();

		// Create a new path list.
		path = new LinkedList<>();

		open.offer(node);
		node.setPathCost(0);
		
		// Iterate while open queue is populated,
		while (!open.isEmpty()) {
			node = open.poll();
			closed.add(node);
			node.setVisited(true);
			visitCount++;

			// Check if current node is goal node.
			if (node.isGoalNode()) {
				System.out.println("IS GOAL NODE");
				
				 // Stop the clock
				time = System.currentTimeMillis() - time;
				
				// Print statistic.
				TraversatorStats.printStats(node, time, visitCount);
				break;
			}

			// Add adjacent nodes to node array.
			Node[] children = node.adjacentNodes(maze);
			
			
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
		
		// Send the path back.
		path.addFirst(closed.get(1));
		fullPath = closed;
	}

	@Override
	public Node getNextNode() {
		// TODO Auto-generated method stub
		return path.getFirst();
	}
}
