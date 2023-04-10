package memory;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/*********************************************************
Cette class génère un plateau de jeu, peut importe le niveau
**********************************************************/
public class Plateau {
	

	private final int MAX = 30;				//nombre max d'image pour le plateau
	public final int WIDTH_CARD = 100;		//largeur d'une carte
	public final int HEIGHT_CARD = 100;     //hauteur d'une carte
	public final int TICK_INTERVAL = 20;    //espace entre deux cartes
	private int X_MIN = 100;				//limite inférieure de mobilité de plateau sur x
	private int X_MAX;						//limite supérieure de mobilité de plateau sur x
	private int Y_MIN = 100;				//limite inférieure de mobilité de plateau sur y
	private int Y_MAX;						//limite supérieure de mobilité de plateau sur y
	private int height, width, level;		//nombre de ligne, de colonne et le niveau
	private String category; 
	private Card[][] grille;
	private Image img;
	private int x, y;						//coord du coin sup gauche du plateau
	private int widthWin, heightWin;		//taille de la fenetre
	private int lenPack;					//nombre de cartes pour la validation
	private boolean moving;					//plateau mobile ou pas
	
	public Plateau(int x, int y,int level, String category, boolean moving, int lenPack, GameContainer gc) throws SlickException {
		int u, v;
		this.x = x;
		this.y = y;
		this.lenPack = lenPack;
		X_MAX = gc.getWidth()-100;
		Y_MAX = gc.getHeight()-100;
		this.widthWin = gc.getWidth();
		this.heightWin = gc.getHeight();
		this.height = (level/2 + level%2)+2;
		this.width = (level/2 + level%2)*4;
		if(lenPack == 3) {
			if(this.height*this.width%3 != 0)
				this.width -= this.width%3;
		}
		this.category = category;
		grille = new Card[height][width];
		int temp;
		ArrayList accu = new ArrayList<Integer>(0);
		ArrayList accu2 =  new ArrayList<Integer>(0);
		int temp2, temp3, temp4;
		for(int i=0; i<width*height; i++) {
			accu2.add(i);
		}
		
		for(int i =0; i<MAX; i++) {
			accu.add(i);
		}
		
		//génération du plateau en mode complètement aléatoire à partir des images
		//d'un dossier de catégories
		
		if(lenPack == 2)
	 		for(int i=0; i<width*height/2; i++) {
				temp = (int)(Math.random()*accu.size());
				
				img = new Image(category + "/" + (int)accu.get(temp) + ".png");
				
				accu.remove(temp);
				
				do {
					temp2 = (int)(Math.random()*accu2.size());
					temp3 = (int)(Math.random()*accu2.size());
				}while(temp2==temp3);
				
				u =	(int)(accu2.get(temp2))/width;
				v = (int)(accu2.get(temp2))%width;
				grille[u][v] = new Card(img, i);
				
				u =	(int)(accu2.get(temp3))/width;
				v = (int)(accu2.get(temp3))%width;
				grille[u][v] = new Card(img, i);
				
				accu2.remove(temp2);
				if(temp2>temp3)
					accu2.remove(temp3);
				else
					accu2.remove(temp3-1);
	 		}
		else if(lenPack == 3)
			for(int i=0; i<width*height/3; i++) {
				temp = (int)(Math.random()*accu.size());
				
				img = new Image(category + "/" + (int)accu.get(temp) + ".png");
				
				accu.remove(temp);
				
				do {
					temp2 = (int)(Math.random()*accu2.size());
					temp3 = (int)(Math.random()*accu2.size());
					temp4 = (int)(Math.random()*accu2.size());
				}while(temp2==temp3 || temp3==temp4 || temp2 == temp4);
				
				u =	(int)(accu2.get(temp2))/width;
				v = (int)(accu2.get(temp2))%width;
				grille[u][v] = new Card(img, i);
				
				u =	(int)(accu2.get(temp3))/width;
				v = (int)(accu2.get(temp3))%width;
				grille[u][v] = new Card(img, i);
				
				u =	(int)(accu2.get(temp4))/width;
				v = (int)(accu2.get(temp4))%width;
				grille[u][v] = new Card(img, i);
				
				
				accu2.remove(temp2);
				if(temp2>temp3)
					accu2.remove(temp3);
				else
					accu2.remove(temp3-1);
				if(temp2>temp4 && temp3>temp4)
					accu2.remove(temp4);
				else if(temp2>temp4 || temp3>temp4)
					accu2.remove(temp4-1);
				else 
					accu2.remove(temp4-2);
	 		}
		
		//initialisation des vitesses
 		for(int i=0; i<height; i++) {
 			for(int j=0; j<width; j++) {
 				grille[i][j].setVitesse((float)(Math.random()*200)-100, (float)(Math.random()*200)-100);
 			}
 		}
 		//initialisation des positions
 		initPositionCard();
 		if(!moving)
 			changePosition();
	}
	
	/**************************************************
	affiche un rendu du plateau à l'ecran
	***************************************************/
	public void dessiner(Graphics g) throws SlickException {
		for(int i =0; i<width; i++) {
			for(int j = 0; j<height; j++) {
				grille[j][i].dessiner(g);
			}
		}
	}
	
	/************************************************************
	change la position du plateau tout entier à l'ecran
	*************************************************************/
	private void changePosition() {
		this.x = widthWin/2-(width*WIDTH_CARD + (width-1)*TICK_INTERVAL)/2;
		this.y = heightWin/2-(height*HEIGHT_CARD + (height-1)*TICK_INTERVAL)/2;
		initPositionCard();
	}
	
	/************************************************************
	initialise la position des cartes à l'écran
	*************************************************************/
	private void initPositionCard() {
		for(int i=0; i<height; i++) {
 			for(int j=0; j<width; j++) {
 				grille[i][j].mov(x+(WIDTH_CARD+TICK_INTERVAL)*j, y+(HEIGHT_CARD+TICK_INTERVAL)*i);
 			}
 		}	
	}
	
	/***********************************************
	gère le mouvement des cartes pour les parties
	où les cartes bougent à l'ecran
	************************************************/
	public void moveCard(float delta) {
		for(int i = 0; i<this.height; i++) {
			for(int j = 0; j<this.width; j++) {
				
				//à décommenter pour prendre en compte les collisions entre cartes
				/*for(int k = 0; k<this.height; k++) {
					for(int l = 0; l<this.width; l++) { 
						if((k!=i || l!=j))
							grille[i][j].collision(grille[k][l]);
					}
				}*/
				
				if(grille[i][j].ctrlLimitX(X_MAX, X_MIN))
					grille[i][j].setVitesse(grille[i][j].getVitesse()[0]*(-1), grille[i][j].getVitesse()[1]);
				if(grille[i][j].ctrlLimitY(Y_MAX, Y_MIN))
					grille[i][j].setVitesse(grille[i][j].getVitesse()[0], grille[i][j].getVitesse()[1]*(-1));
				grille[i][j].mov(grille[i][j].getX()+grille[i][j].getVitesse()[0]*delta/1000, grille[i][j].getY()+grille[i][j].getVitesse()[1]*delta/1000);
			}
		}
	}
	
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getLevel() {
		return level;
	}

 		
	public Card[][] getGrille() {
		return grille;
	}
	
	public String getCategory() {
		return category;
	}
	
	public int getLenPack() {
		return lenPack;
	}
	
	public boolean isMoving() {
		return moving;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setGrille(Card[][] grille) {
		this.grille = grille;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	public void setLenPack(int lenPack) {
		this.lenPack = lenPack;
	}
	

}
