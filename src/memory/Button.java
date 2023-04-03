package memory;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class Button {

	private int x, y;
	private int hover;
	private Image imgHover, img;
	private static Music song ;
	
	public Button(int x, int y, String img, String imgHover) throws SlickException {
		this.x = x;
		this.y = y;
		this.img = new Image(img);
		this.imgHover = new Image(imgHover);
		if(song == null)
			song = new Music("buttonSong.wav");
		hover = 0;
	}

	public boolean isHover(int x, int y) {
		if(x>this.x && x<this.x+img.getWidth() && y>this.y && y<this.y+img.getHeight()) {
			hover = 1;
			return true;
		}
		else {
			hover = 0;
			return false;
		}
	}
	
	public void dessiner(Graphics g) {
		if(hover == 0)
			g.drawImage(img, x, y);
		else if(hover == 1)
			g.drawImage(imgHover, x, y);
	}

	public void setX(int x) {
		if(x>0)
			this.x = x;
	}

	public void setY(int y) {
		if(y>0)
			this.y = y;
	}

	public Image getImgHover() {
		return imgHover;
	}

	public void setImgHover(Image imgHover) {
		this.imgHover = imgHover;
	}

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getHover() {
		return hover;
	}

	public static Music getSong() {
		return song;
	}
	
	
	
	
}
