package ie.gmit.sw.ai.traversers;

import java.awt.Color;
public class TraversatorStats {
	public static void printStats(Node node, long time, int visitCount){
		System.out.println("Attempting to print traverstar stats");
		System.out.println("Attempting to print with node :"+ node);
		double depth = 0;
		
		while (node != null){			
			node = node.getParent();
			if (node != null) node.setColor(Color.YELLOW);
			depth++;			
		}
		
        System.out.println("Visited " + visitCount + " nodes in " + time + "ms.");
        System.out.println("Found goal at a depth of " + String.format("%.0f", depth));
        System.out.println("EBF = B* = k^(1/d) = " + String.format("%.2f", Math.pow((double) visitCount, (1.00d / depth))));           
        
	}
}