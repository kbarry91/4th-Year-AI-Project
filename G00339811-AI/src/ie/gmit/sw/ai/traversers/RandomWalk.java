package ie.gmit.sw.ai.traversers;

public class RandomWalk implements Traversator{
	public void traverse(Node[][] maze, Node node) {
        long time = System.currentTimeMillis();
    	int visitCount = 0;
    	   	
		int steps = (int) Math.pow(maze.length, 2) * 2;
		System.out.println("Number of steps allowed: " + steps);
		
		boolean complete = false;
		while(visitCount <= steps && node != null){		
			node.setVisited(true);	
			visitCount++;
			
			if (node.isGoalNode()){
		        time = System.currentTimeMillis() - time; //Stop the clock
		        TraversatorStats.printStats(node, time, visitCount);
		        complete = true;
				break;
			}
			
			try { //Simulate processing each expanded node
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Pick a random adjacent node
        	Node[] children = node.children(maze);
        	node = children[(int)(children.length * Math.random())];		
		}
		
		if (!complete) System.out.println("*** Out of steps....");
	}

	@Override
	public Node getNextNode() {
		// TODO Auto-generated method stub
		return null;
	}
}