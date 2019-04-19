package ie.gmit.sw.ai.traversers;

public interface Traversator {
	public void traverse(Node[][] maze, Node start);
	public Node getNextNode();
}
