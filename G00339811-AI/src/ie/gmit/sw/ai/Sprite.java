package ie.gmit.sw.ai;

import javax.imageio.*;
import java.awt.image.*;
/**
 * Sprite class is the main class for a sprite object.
 * 
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 *
 */
public class Sprite {
	// SPrite name.
	private String name; 
	// Images to animate.
	private BufferedImage[][] images;
	// Direction sprite is facing
 	private int index = 0; 
 	// Index of image
 	private int frame = 0; 
 	// The damage value for sprite.
 	private int damage;
	
 	

	public Sprite() {
 		
 	}
 	
	/**
	 * Constructor to create sprite.
	 * 
	 * @param anger
	 * @param name
	 * @param frames
	 * @param files
	 * @throws Exception
	 */
	public Sprite(int damage, String name, int frames, String... files) throws Exception{
		this.name = name;
		this.damage= damage;
		// Set the starting index to 0.
		this.index = 0; 
		// Set the image frames.
		this.images = new BufferedImage[files.length / frames][frames]; 
		
		int row = 0;
		int col = 0;
		for (int i = 0; i < files.length; i++){
			// Read the file in as a bufferedImage and add to array.
			images[row][col] = ImageIO.read(new java.io.File(files[i]));

			col++;
			if (col % frames == 0){
				row++;
				col = 0;
			} 
		}
	}
	
	
	public BufferedImage getNext(){ //Returns the next image frame
		frame++;
		if (frame == images[index].length) frame = 0; 

		return images[index][frame]; 
	}
	
	public int getImageIndex(){
		return this.index;
	}
	
	public void setImageIndex(int idx){
		this.index = idx;
	}

	public String getName(){
		return this.name;
	}
	
	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
}