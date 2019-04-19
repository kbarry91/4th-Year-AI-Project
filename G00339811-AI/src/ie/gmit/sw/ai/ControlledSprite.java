package ie.gmit.sw.ai;
/**
 * Represents a user Controlled Object.
 *  
 * @author Kevin Barry - Bachelor of Science (Honours) in Software Development
 *
 */
public class ControlledSprite extends Sprite{
	private double playerHealth;

	public ControlledSprite(int anger, String name, int frames, double playerHealth, String... images) throws Exception {
		super(anger, name, frames, images);
		this.playerHealth = playerHealth;
	}
	
	public ControlledSprite() {
		super();
	}
	
	public double getPlayerHealth() {
		return playerHealth;
	}

	

	public void setPlayerHealth(double playerHealth) {
		this.playerHealth = playerHealth;
	}

	public void setDirection(Direction d) {
		switch (d.getOrientation()) {
		case 0:
		case 1:
			super.setImageIndex(0); // UP or DOWN
			break;
		case 2:
			super.setImageIndex(1); // LEFT
			break;
		case 3:
			super.setImageIndex(2); // LEFT
		default:
			break; // Ignore...
		}
	}

	public boolean isAlive() {
		if(playerHealth > 0) 
			return true;
		else 
			return false;
	}

	
}