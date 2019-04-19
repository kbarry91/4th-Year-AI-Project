package ie.gmit.sw.ai.traversers;

import java.awt.Color;
/**
 * Node is a class to represent a node implementation of and object and can be used to traverse a path in the maze.
 * 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 *
 */
public class Node {
	public enum Direction {North, South, East, West};
	private Node parentNode;
	private Color color = Color.BLACK;
	private Direction[] paths = null;
	public boolean visited =  false;
	public boolean goal;
	private int row = -1;
	private int col = -1;
	private int distance;
	private int type;

	/**
	 * Constructor for a Node object
	 * 
	 * @param row the row location of node.
	 * @param col the column location of the node.
	 * @param type the type of node.
	 */
	public Node(int row, int col, int type) {
		this.row = row;
		this.col = col;
		this.type = type;
	}

	/**
	 * Get the row.
	 * 
	 * @return row value for node.
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Get the column.
	 * 
	 * @return column value for node.
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Get the parent node.
	 * 
	 * @return the parent of the current node.
	 */
	public Node getParent() {
		return parentNode;
	}
	
	/**
	 * 
	 * Set the parent node.
	 * 
	 * @param parent the node to set as parent.
	 */
	public void setParent(Node parent) {
		this.parentNode = parent;
	}

	/**
	 * Get the colour of the node.
	 * 
	 * @return the node colour.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Set the colour for the node.
	 * 
	 * @param color the node colour.
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Get the node type.
	 * 
	 * @return the type of node.
	 */
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	/**
	 * Checks if the node has a direction.
	 * 
	 * @param directionthe direction to check
	 * 
	 * @return true or false.
	 */
	public boolean hasDirection(Direction direction){	
		for (int i = 0; i < paths.length; i++) {
			if (paths[i] == direction) return true;
		}
		return false;
	}
	
	/**
	 * Retrieves the children of the node
	 * 
	 * @param maze the maze object.
	 * 
	 * @return an array of children Nodes.
	 */
	public Node[] children(Node[][] maze){		
		java.util.List<Node> children = new java.util.ArrayList<Node>();
				
		if (row > 0 && maze[row - 1][col].hasDirection(Direction.South)) children.add(maze[row - 1][col]); //Add North
		if (row < maze.length - 1 && maze[row + 1][col].hasDirection(Direction.North)) children.add(maze[row + 1][col]); //Add South
		if (col > 0 && maze[row][col - 1].hasDirection(Direction.East)) children.add(maze[row][col - 1]); //Add West
		if (col < maze[row].length - 1 && maze[row][col + 1].hasDirection(Direction.West)) children.add(maze[row][col + 1]); //Add East
		
		return (Node[]) children.toArray(new Node[children.size()]);
	}

	/**
	 * Retrieves the adjacent nodes of the node
	 * 
	 * @param maze the maze object.
	 * 
	 * @return an array of adjacent Nodes.
	 */
	public Node[] adjacentNodes(Node[][] maze){
		java.util.List<Node> adjacents = new java.util.ArrayList<Node>();
		
		if (row > 0) adjacents.add(maze[row - 1][col]); //Add North
		if (row < maze.length - 1) adjacents.add(maze[row + 1][col]); //Add South
		if (col > 0) adjacents.add(maze[row][col - 1]); //Add West
		if (col < maze[row].length - 1) adjacents.add(maze[row][col + 1]); //Add East
		
		return (Node[]) adjacents.toArray(new Node[adjacents.size()]);
	}
	
	public Direction[] getPaths() {
		return paths;
	}

	/**
	 * Adds a direction to the path.
	 * 
	 * @param direction.
	 */
	public void addPath(Direction direction) {
		int index = 0;
		if (paths == null){
			paths = new Direction[index + 1];		
		}else{
			index = paths.length;
			Direction[] temp = new Direction[index + 1];
			for (int i = 0; i < paths.length; i++) temp[i] = paths[i];
			paths = temp;
		}
		
		paths[index] = direction;
	}

	
	public boolean isVisited() {
		return visited;
	}

	/**
	 * Set the node as visited and its colour to blue.
	 * 
	 * @param visited
	 */
	public void setVisited(boolean visited) {
		this.color = Color.BLUE;
		this.visited = visited;
	}

	public boolean isGoalNode() {
		return goal;
	}

	public void setGoalNode(boolean goal) {
		this.goal = goal;
	}
	
	/**
	 * Calculates the heuristic value.
	 * 
	 * @param goal, the goal to check
	 * @return the heuristic value.
	 */
	public int getHeuristic(Node goal){
		double x1 = this.col;
		double y1 = this.row;
		double x2 = goal.getCol();
		double y2 = goal.getRow();
		return (int) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
	
	public int getPathCost() {
		return distance;
	}

	public void setPathCost(int distance) {
		this.distance = distance;
	}
	
	/**
	 * Returns a string representation of the node.
	 */
	public String toString() {
		return "[" + row + "/" + col + "]";
	}
}