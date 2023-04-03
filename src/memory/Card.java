package memory;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class Card {

	private final int deltaFrame = 0;	//padding de la carte
	private Image img;					//image de la carte
	private static Image imgDos;		//image du dos
	private static Image frame;			//image bordure
	private Image stepAnim;				//etape de l'animation en cours
	private static Image frameHover;	//image de l'ombre pour le hover
	private int id;
	private int etat;					//etat de la carte 0->caché 1->découvert 
	private int anim;					//pour les différentes animations (vibrer, retourner)
	private int timer;
	private int vibre;					//intensité du vibrement
	private int valide;					//carte validé ou non
	private float x, y, vx, vy;			//coord de la carte et sa vitesse
	private boolean hover;				//le curseur est sur la carte
	private static Music musicClic;		//son lors du clic
	
	
	public Card(Image img, int id) throws SlickException {
		this.img = img;
		this.id = id;
		this.etat = 0;
		this.x = 0;
		this.y = 0;
		vx = 0;
		vy = 0; 
		if(imgDos == null)
			imgDos = new Image("dos/dos0.png");
		if(frame == null)
			frame = new Image("frame.png");
		if(frameHover == null)
			frameHover = new Image("shadow.png");
		if(musicClic == null)
			musicClic = new Music("flipcard.wav");
		timer = 0;
		anim = 0;
		vibre = 1;
		valide = 0;
		hover = false;
		
	}
	
	public void collision(Card c) {
		if(Math.abs(this.x-c.x)<=img.getWidth() && Math.abs(this.y-c.y)<=img.getHeight()) {
			if(this.x-c.x<0) {
				this.vx = -1*Math.abs(this.vx);
				c.vx = Math.abs(c.vx);
			}
			else {
				c.vx = -1*Math.abs(c.vx);
				this.vx = Math.abs(this.vx);
			}
			
			if(this.y-c.y<0) {
				this.vy = -1*Math.abs(this.vy);
				c.vy = Math.abs(c.vy);
			}
			else {
				c.vy = -1*Math.abs(c.vy);
				this.vy = Math.abs(this.vy);
			}
		}
	}
	
	public void dessiner(Graphics g) throws SlickException {
		if(anim==1) {
			if(timer<66)
				stepAnim = new Image("firstRot.png");
			else if(timer<132)
				stepAnim = new Image("profil.png");
			else
				stepAnim = new Image("secondRot.png");
			g.drawImage(stepAnim, x-8, y-8);
		}
		else if(anim == -1) {
			if(timer < 500)
				stepAnim = img;
			else if(timer<566)
				stepAnim = new Image("firstRot.png");
			else if(timer<632)
				stepAnim = new Image("profil.png");
			else
				stepAnim = new Image("secondRot.png");
			
			if(timer<500) {
				g.drawImage(img, x, y);
				g.drawImage(frame, x-deltaFrame, y-deltaFrame);
			}
			else {
				g.drawImage(stepAnim, x-8, y-8);
			}
		}
		else if(anim == -2) {
			if(timer<66) {
				System.out.println("first");
				stepAnim = new Image("firstRot.png");
			}
			else if(timer<132)
				stepAnim = new Image("profil.png");
			else if(timer<198)
				stepAnim = new Image("secondRot.png");
			else if(timer<500)
				stepAnim = img;
			else if(timer<566)
				stepAnim = new Image("firstRot.png");
			else if(timer<632)
				stepAnim = new Image("profil.png");
			else
				stepAnim = new Image("secondRot.png");
		
			if(timer<500 && timer>=198) {
				g.drawImage(img, x, y);
				g.drawImage(frame, x-deltaFrame, y-deltaFrame);
			}
			else {
				g.drawImage(stepAnim, x-8, y-8);
			}
		}           
		else if(anim==2) {
			g.drawImage(img, x+vibre*5, y);
			g.drawImage(frame, x-deltaFrame+vibre*5, y-deltaFrame);
			vibre *= -1;
		}
	    else if(etat==1) {
			g.drawImage(img, x, y);
			g.drawImage(frame, x-deltaFrame, y-deltaFrame);
			if(valide == 1){
				g.setColor(new Color(50, 100, 100, 120));
				g.fillRect(x, y, 100, 100);
			}
		}
		else if(etat==0) {
			g.drawImage(imgDos, x, y);
			if(hover) 
				g.drawImage(frameHover, x-15, y-15);
		}
	}

	public int getEtat() {
		return etat;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

	public void clicked() {
		timer = 0;
		if(this.etat==0) {
			this.etat = 1;
			anim = 1;
			musicClic.play(5, 1);
			
		}
		else if(this.etat==1) {
			anim = 2;
		}
	}
	
	public boolean animation(int delta) {
		if((timer>700 && (anim == -1 || anim == -2)) || (timer>200 && anim != -1 && anim != -2) || anim == 0) {
			anim = 0;
			timer = 0;
			return false;
		}
		else {
			timer += delta;
			return true;
		}
	}
	
	public void reset() {
		timer = 0;
		etat = 0;
		anim = 0;
	}
	
	public void retourner() {
		this.etat = 0;
		timer = 0;
		if(anim == 0)
			anim = -1;
		else if(anim == 1)
			anim = -2;
	}
	
	public boolean pointIn(int x, int y, Plateau pl) {
		if(x>this.x && y>this.y && x<this.x+pl.WIDTH_CARD && y<this.y+pl.HEIGHT_CARD)
			return true;
		return false;
	}
	
	public void setHover(boolean b) {
		hover = b;
	}
	
	public boolean getHover() {
		return hover;
	}
	
	public int getValide() {
		return valide;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setValide(int valide) {
		this.valide = valide;
		anim = 2;
		
	}
	
	public boolean ctrlLimitX(int x_max, int x_min) {
		if((x+img.getWidth()>=x_max && vx>0) || (x<=x_min && vx<0))
			return true;
		return false;
	}
	
	public boolean ctrlLimitY(int y_max, int y_min) {
		if((y+img.getHeight()>=y_max && vy>0) || (y<=y_min && vy<0))
			return true;
		return false;
	}
	
	public void setVitesse(float vx, float vy) {
		this.vx = vx;
		this.vy = vy;
	}
	
	public float[] getVitesse() {
		float[] result = new float[2];
		result[0] = vx;
		result[1] = vy;
		return result;
	}
	public void mov(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public int getId() {
		return id;
	}

	public void setImg(Image img) {
		this.img = img;
	}

	public static void setImgDos(Image img) {
		imgDos = img;
	}

	public void setFrame(Image frame) {
		this.frame = frame;
	}

	public void setFrameHover(Image frameHover) {
		this.frameHover = frameHover;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static Music getMusicClic() {
		return musicClic;
	}
	
}
