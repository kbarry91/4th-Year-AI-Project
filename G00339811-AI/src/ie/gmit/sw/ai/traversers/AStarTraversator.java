package ie.gmit.sw.ai.traversers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
public class AStarTraversator implements Traversator{
	private Node goal;
	private LinkedList<Node> path = null;
	private List<Node> fullPath = null;
	
	public AStarTraversator(Node goal){
		this.goal = goal;
	}

	

	public void traverse(Node[][] maze, Node node) {
		System.out.println("Runniung A* travester traverse");
		
        long time = System.currentTimeMillis();
    	int visitCount = 0;
    	
		PriorityQueue<Node> open = new PriorityQueue<Node>(20, (Node current, Node next)-> (current.getPathCost() + current.getHeuristic(goal)) - (next.getPathCost() + next.getHeuristic(goal)));
		java.util.List<Node> closed = new ArrayList<Node>();
		
		path = new LinkedList<>();
    	   	
		open.offer(node);
		node.setPathCost(0);		
		while(!open.isEmpty()){
			node = open.poll();		
			closed.add(node);
			node.setVisited(true);	
			visitCount++;
			
			if (node.isGoalNode()){
				System.out.println("IS GOAL NODE");
		        time = System.currentTimeMillis() - time; //Stop the clock
		        TraversatorStats.printStats(node, time, visitCount);
				break;
			}
			
//			try { //Simulate processing each expanded node
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			
			//Process adjacent nodes
			//Node[] children = node.children(maze);
			Node[] children = node.adjacentNodes(maze);// chan ged this
			for (int i = 0; i < children.length; i++) {
				Node child = children[i];
				int score = node.getPathCost() + 1 + child.getHeuristic(goal);
				int existing = child.getPathCost() + child.getHeuristic(goal);
				
				if ((open.contains(child) || closed.contains(child)) && existing < score){
					continue;
				}else{
					open.remove(child);
					closed.remove(child);
					child.setParent(node);
					child.setPathCost(node.getPathCost() + 1);
					open.add(child);
				}
			}									
		}
		// send path back to spider.. when player  oves set as goal node
		path.addFirst(closed.get(1));
		fullPath = closed;
	}

	@Override
	public Node getNextNode() {
		// TODO Auto-generated method stub
		return path.getFirst();
	}
}
