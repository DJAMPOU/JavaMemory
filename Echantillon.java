package memory;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Echantillon {

	private Image img;
	private static Image frameHover;
	private int lastX, lastY;
	private boolean hover;
	
	public Echantillon(Image img, Image frame) {
		hover = false;
		this.img = img;
		if(frameHover == null)
			frameHover = frame;
		lastX = -1;
		lastY = -1;
	}
	
	public boolean verifHover(int x, int y) {
		if(x>lastX && y>lastY && x<lastX+img.getWidth() && y<lastY+img.getHeight()) {
			hover = true;
			return true;
		}
		else
			hover = false;
		return false;
	}

	public void dessiner(Graphics g) {
		g.drawImage(img, lastX, lastY);
		if(hover) 
			g.drawImage(frameHover, lastX-15, lastY-15);
	}

	public int getLastX() {
		return lastX;
	}

	public void setLastX(int lastX) {
		this.lastX = lastX;
	}

	public int getLastY() {
		return lastY;
	}

	public void setLastY(int lastY) {
		this.lastY = lastY;
	}
	
	
}
