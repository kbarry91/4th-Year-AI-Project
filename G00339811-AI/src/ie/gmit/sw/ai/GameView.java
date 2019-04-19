package ie.gmit.sw.ai;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;
/**
 * 
 * @author Kevin Barry - BanodeTypeelor of Science (Honours) in Software Development
 *
 */
public class GameView extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_VIEW_SIZE = 800;
	
	private int cellspan = 5;	
	private int cellpadding = 2;
	private Maze maze;
	private Sprite[] sprites;
	private int enemy_state = 5;
	private Timer timer;
	private int currentRow;
	private int currentCol;
	private boolean zoomOut = false;
	private int imageIndex = -1;
	private int offset = 48; //The number 0 is ASCII 48.
	private Color[] reds = {new Color(255,160,122), new Color(139,0,0), new Color(255, 0, 0)}; //Animate enemy "dots" to make them easier to see
	private player p;
	
	public GameView(Maze maze) throws Exception{
		this.maze = maze;
		setBackground(Color.LIGHT_GRAY);
		setDoubleBuffered(true);
		timer = new Timer(300, this);
		timer.start();
	}
	
	public void setCurrentRow(int row) {
		if (row < cellpadding){
			currentRow = cellpadding;
		}else if (row > (maze.size() - 1) - cellpadding){
			currentRow = (maze.size() - 1) - cellpadding;
		}else{
			currentRow = row;
		}
	}

	public void setCurrentCol(int col) {
		if (col < cellpadding){
			currentCol = cellpadding;
		}else if (col > (maze.size() - 1) - cellpadding){
			currentCol = (maze.size() - 1) - cellpadding;
		}else{
			currentCol = col;
		}
	}
	
	public void setPlayer(player p) {
		this.p = p;
	}
	
	/**
	 * 
	 *
	 */
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
              
        cellspan = zoomOut ? maze.size() : 5;         
        final int size = DEFAULT_VIEW_SIZE/cellspan;
        g2.drawRect(0, 0, GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
        
        for(int row = 0; row < cellspan; row++) {
        	for (int col = 0; col < cellspan; col++){  
        		int x1 = col * size;
        		int y1 = row * size;
        		
        		// The type of node that the component is.
        		int nodeType = 0;
       		
        		// If game view is zoomed out set colour of square corresponding to node type.
        		if (zoomOut){
        			nodeType = maze.get(row, col).getType();
        			if (nodeType >= 5){
	        			if (row == currentRow && col == currentCol){
	        				g2.setColor(Color.YELLOW);
	        				
	        			}else if(nodeType==6) {
	        				g2.setColor(Color.BLACK);
	        			}
	        			else if(nodeType==7) {
	        				g2.setColor(Color.BLUE);
	        			}else if(nodeType==8) {
	        				g2.setColor(Color.LIGHT_GRAY);
	        			}else if(nodeType==9) {
	        				g2.setColor(Color.GREEN);
	        			}else if(nodeType==10) {
	        				g2.setColor(Color.GRAY);
	        			}
	        			else if(nodeType==11) {
	        				g2.setColor(Color.ORANGE);
	        			}else if(nodeType==12) {
	        				g2.setColor(Color.RED);
	        			}
	        			else if(nodeType==11) {
	        				g2.setColor(Color.white);
	        			}
        				g2.fillRect(x1, y1, size, size);
        			}
        		}else{
        			nodeType = maze.get(currentRow - cellpadding + row, currentCol - cellpadding + col).getType();
        		}
        		
        		imageIndex = (int) nodeType;
//        		imageIndex -= offset;
        		if (imageIndex < 0){
        			g2.setColor(Color.LIGHT_GRAY);//Empty cell
        			g2.fillRect(x1, y1, size, size);   			
        		}else{
        			g2.drawImage(sprites[imageIndex].getNext(), x1, y1, null);
        		}
        	}
        }
	}
	
	public void toggleZoom(){
		zoomOut = !zoomOut;		
	}

	public void actionPerformed(ActionEvent e) {	
		if (enemy_state < 0 || enemy_state == 5){
			enemy_state = 6;
		}else{
			enemy_state = 5;
		}
		this.repaint();
	}
	
	public void setSprites(Sprite[] sprites){
		this.sprites = sprites;
	}
	
	
}