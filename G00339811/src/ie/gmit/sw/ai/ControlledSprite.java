package ie.gmit.sw.ai;

import javax.imageio.*;
import java.awt.image.*;

public class ControlledSprite extends Sprite {
	public String weapon;
	public double health;
	public Weapon weaponO;
	private boolean helpIsActive;// boolean to check if help has been activated
	
	public ControlledSprite(String name, int frames, String... images) throws Exception {
		super(name, frames, images);
		this.setHealth(100);
		this.weaponO = new Weapon(); 
		this.helpIsActive = false;
	}

	public Weapon getWeaponO() {
		return weaponO;
	}

	public void setWeaponO(Weapon weaponO) {
		this.weaponO = weaponO;
	}

	public boolean isHelpIsActive() {
		return helpIsActive;
	}

	public void setHelpIsActive(boolean helpIsActive) {
		this.helpIsActive = helpIsActive;
	}

	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
		this.health = health;
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

	public String getWeapon() {
		return weapon;
	}

	public void setWeapon(String weapon) {
		this.weapon = weapon;
	}
}