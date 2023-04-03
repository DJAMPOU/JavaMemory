package memory;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

public class List {

	private int x, y, height, width, currentSelect;
	private Button rep;
	private Button[] listBout;
	private Button unRollUp, unRollDown;
	private String[] listLab;
	private boolean unRoll;
	private Font font;
	private TrueTypeFont ttf;
	private String label;
	
	
	
	public List(int x, int y, String label, String[] listLab) throws SlickException {
		this.x = x;
		this.y = y;
		this.label = label;
		currentSelect = 0;
		this.rep = rep;
		this.listLab = listLab;
		unRoll = false;
		this.listBout = new Button[listLab.length];
		rep = new Button(x, y, "setting/RepList.png", "setting/RepListHover.png");
		this.height = rep.getImg().getHeight()*listLab.length;
		this.width  = rep.getImg().getWidth();
		unRollDown = new Button(x + width, y, "setting/unRollDown.png", "setting/unRollDownHover.png");
		unRollUp = new Button(x + width, y, "setting/unRollUp.png", "setting/unRollUpHover.png");
		if(this.height + y<=800) 
			for(int i =0; i<listLab.length; i++)
				this.listBout[i] = new Button(x + width + unRollDown.getImg().getWidth() + 10, y+rep.getImg().getHeight()*(i), "setting/celluleList.png", "setting/celluleListHover.png");
		else
			for(int i =0; i<listLab.length; i++)
				this.listBout[i] = new Button(x + width + unRollDown.getImg().getWidth() + 10, y-rep.getImg().getHeight()*(i), "setting/celluleList.png", "setting/celluleListHover.png");
		
		font = new Font("Snap ITC", Font.CENTER_BASELINE, 20);
		ttf = new TrueTypeFont(font, true);
	}

	public void dessiner(Graphics g) {
		g.setColor(Color.blue);
		ttf.drawString(x, y-20, label + ":");
		if(unRoll)
			unRollUp.dessiner(g);
		else
			unRollDown.dessiner(g);
		rep.dessiner(g);
		ttf.drawString(x + rep.getImg().getWidth()/2 - listLab[currentSelect].length()*7, y+20, listLab[currentSelect]);
		if(unRoll)
			this.dessinerList(g);
	}
	
	public void dessinerList(Graphics g) {
		g.setColor(Color.blue);
		g.setLineWidth(10);
		for(int i= 0; i<listLab.length; i++) {
			listBout[i].dessiner(g);
			ttf.drawString(listBout[i].getX() + rep.getImg().getWidth()/2 - listLab[i].length()*7, listBout[i].getY() + 20, listLab[i]);
			g.drawLine(listBout[i].getX(), listBout[i].getY(), listBout[i].getX(), listBout[i].getY() + listBout[i].getImg().getHeight());
			g.drawLine(listBout[i].getX() + listBout[i].getImg().getWidth(), listBout[i].getY(), listBout[i].getX() + listBout[i].getImg().getWidth(), listBout[i].getY() + listBout[i].getImg().getHeight());
			if(i == 0 && height+y<=800 || i == listLab.length-1 && height+y>800)
				g.drawLine(listBout[i].getX(), listBout[i].getY(), listBout[i].getX()+listBout[i].getImg().getWidth(), listBout[i].getY());
			else if(i == listLab.length-1 && height+y<=800 || i == 0 && height+y>800)
				g.drawLine(listBout[i].getX(), listBout[i].getY() + listBout[i].getImg().getHeight(), listBout[i].getX()+listBout[i].getImg().getWidth(), listBout[i].getY() + listBout[i].getImg().getHeight());
		}
	}
	
	public int isSelected(int x, int y) {
		for(int i=0; i<listLab.length; i++)
			if(listBout[i].isHover(x, y)) {
				currentSelect = i;
				unRoll = false;
				return i;
			}
		return -1;
	}
	
	public boolean verifHover(int x, int y) {
		boolean temp = false;
		if(unRollDown.isHover(x, y) && !unRoll)
			temp = true;
		else if(unRollUp.isHover(x, y) && unRoll)
			temp = true;
		if(unRoll){
			for(int i =0; i<listLab.length; i++)
				listBout[i].isHover(x, y);
		}
		return temp;
	}
	
	public boolean isRepHover(int x, int y) {
		if(rep.isHover(x, y))
			return true;
		return false;
	}

	public int getCurrentSelect() {
		return currentSelect;
	}
	
	public boolean isUnRoll() {
		return this.unRoll;
	}
	
	public void setUnRoll(boolean unRoll) {
		this.unRoll = unRoll;
	}
	
	public void swicthUnRoll() {
		this.unRoll = !this.unRoll;
	}
	
	public void setCurrentSelect(int currentSelect) {
		this.currentSelect = currentSelect;
	}
	
}
