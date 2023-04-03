package memory;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

public class Aide {
	private String[] contenu;
	private final int nbrLineMax = 10;
	private int cur;
	private Button house;
	private Button up, down;
	private Image imgFont;
	private Font font;
	private TrueTypeFont ttf;
	
	public Aide(GameContainer gc, String contenu) throws SlickException {
		this.contenu = contenu.split("\n");
		cur = 0;
		up = new Button(gc.getWidth()-150, gc.getHeight()/2-150, "aide/up.png", "aide/upHover.png");
		down = new Button(gc.getWidth()-150, gc.getHeight()/2+50, "aide/down.png", "aide/downHover.png");
		house = new Button(50, gc.getHeight()-150, "houseHover.png", "house.png");
		imgFont = new Image("aide/font.png");
		font = new Font("Kristen ITC", Font.CENTER_BASELINE, 24);
		ttf = new TrueTypeFont(font, true);
	}
	
	public void dessiner(Graphics g, GameContainer gc) {
		g.drawImage(imgFont, 0, 0);
		house.dessiner(g);
		up.dessiner(g);
		down.dessiner(g);
		g.setColor(Color.white);
		g.fillRect(150, 50, 900, 600);
		for(int  i= cur; i<nbrLineMax+cur; i++) {
			ttf.drawString(gc.getWidth()/2 - (contenu[i].length()*13)/2-120, 100+50*(i-cur), contenu[i], Color.black);
		}
		g.setColor(Color.lightGray);
		g.fillRect(1050, 50, 50, 600);
		g.setColor(Color.black);
		g.fillRect(1050, 50+550*cur/(contenu.length-10), 50, 50);
	}
	
	public int getEvenAide(int x, int y) {
		if(house.isHover(x, y)) {
			return 1;
		}
		else if(up.isHover(x, y)) {
			if(cur>0)
				cur --;
		}
		else if(down.isHover(x, y))
			if(cur+nbrLineMax<contenu.length)
				cur++;
		return 0;
	}
	
	public void manageHover(int x, int y) {
		house.isHover(x, y);
		up.isHover(x, y);
		down.isHover(x, y);
	}

}
