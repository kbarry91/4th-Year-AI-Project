package ie.gmit.sw.ai;

import javax.imageio.*;
import java.awt.image.*;
public class ControlledSprite extends Sprite{	
	public ControlledSprite(String name, int frames, String... images) throws Exception{
		super(name, frames, images);
	}
	
	public void setDirection(Direction d){
		switch(d.getOrientation()) {
		case 0: case 1:
			super.setImageIndex(0); //UP or DOWN
			break;
		case 2:
			super.setImageIndex(1);  //LEFT
			break;
		case 3:
			super.setImageIndex(2);  //LEFT
		default:
			break; //Ignore...
		}		
	}
}