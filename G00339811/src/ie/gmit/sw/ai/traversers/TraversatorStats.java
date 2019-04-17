package ie.gmit.sw.ai.traversers;

import ie.gmit.sw.ai.*;

import java.awt.Color;

public class TraversatorStats {
	public static int printStats(Node node, long time, int visitCount, boolean countSteps){
		double depth = 0;
		
		int count = 0;
		
		while (node != null){			
			node = node.getParent();
			if (node != null){
				if(!countSteps){
					if(node.getNodeType() != 'P'){
						node.setColor(Color.YELLOW);
						node.setNodeType('T');
						node.setWalkable(true);
					}
				}
				count++;
			}
			depth++;		
		}
		
     //   System.out.println("Visited " + visitCount + " nodes in " + time + "ms.");
     //   System.out.println("Found goal at a depth of " + String.format("%.0f", depth));
     //   System.out.println("EBF = B* = k^(1/d) = " + String.format("%.2f", Math.pow((double) visitCount, (1.00d / depth))));           
      //  SoundEffects.ALARM.play();
        
        return count;
	}
}