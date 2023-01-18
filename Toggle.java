package memory;

import java.awt.Font;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

public class Toggle {

	private String[] list;
	private Image none, select;
	private int isHover;
	private int x, y;
	private final int ecart = 20;
	private final Font font = new Font("Snap ITC", Font.CENTER_BASELINE, 14);
	private final TrueTypeFont ttf = new TrueTypeFont(font, true);
	private int index;
	
	public Toggle(int x, int y, String none, String select, String[] list) throws SlickException {
		this.list = list;
		this.x = x;
		this.y = y;
		this.none = new Image(none);
		this.select = new Image(select);
		index = 0;
		isHover = -1;
	}

	public int getIndex() {
		return index;
	}
	
	public boolean swicth(int x, int y) {
		for(int i =0; i<list.length; i++) {
			if(i!= index && x>this.x+(none.getWidth()+ecart)*i && x<this.x+(none.getWidth()+ecart)*i+none.getWidth() && y>this.y && y<this.y+none.getHeight()) {
				index = i;
				return true;
			}
		}
		return false;
	}
	
	public boolean hover(int x, int y) {
		for(int i =0; i<list.length; i++) {
			if(i!= index && x>this.x+(none.getWidth()+ecart)*i && x<this.x+(none.getWidth()+ecart)*i+none.getWidth() && y>this.y && y<this.y+none.getHeight()) {
				isHover = i;
				return true;
			}
		}
		isHover = -1;
		return false;
	}
	
	public void dessiner(Graphics g) throws SlickException {
		for(int i = 0; i<list.length; i++) {
			if(i==isHover) {
				g.drawImage(new Image("setting/toggleOffHover.png"), this.x + (none.getWidth()+ecart)*i, this.y);
			}
			else if(i!=index) {
				g.drawImage(none, this.x + (none.getWidth()+ecart)*i, this.y);

			}
			else {
				g.drawImage(select, this.x+ (none.getWidth()+ecart)*i, this.y);
				
			}
			ttf.drawString(this.x+(none.getWidth()+ecart)*i+20, this.y+20, list[i]);
		}
	}
}
