package memory;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

/***************************************************
Cette class se charge du decor de la page de jeu
****************************************************/
public class DecorGame {

	private Controleur ctrl;
	private String[] perso = {"Red", "Blue", "Yellow", "Green"};	//des speudos des joueurs
	private String mode;						//Indique le mode de jeu
	private Font font;
	private TrueTypeFont ttf;
	private String[] listMode;
	private Image bande;						//Bande du haut pour écrire le mode
	
	public DecorGame(Controleur ctrl) throws SlickException {
		this.ctrl = ctrl;
		mode = new String("modes/m" + ctrl.getMode() + ".png");
		font = new Font("Snap ITC", Font.CENTER_BASELINE, 40);
		ttf = new TrueTypeFont(font, true);
		bande = new Image("bande.png");
		listMode = new String[4];
		listMode[0] = "Simple";
		listMode[1] = "Timer Challenger";
		listMode[2] = "Limited Tries";
		listMode[3] = "Remenber All";
	}

	
	/***********************************************************************
	Dessine de decor
	************************************************************************/
	public void dessiner(GameContainer gc, Graphics g) throws SlickException {
		g.drawImage(new Image("fondGame.jpg"), 0, 0);
		g.drawImage(bande, gc.getWidth()/2-200, 55);
		ttf.drawString(gc.getWidth()/2-(listMode[ctrl.getMode()].length()*25)/2, 55, listMode[ctrl.getMode()]);
		g.setColor(Color.black);
		if(ctrl.getJoueur() == 1) {
			g.drawString("Tries : " + String.format("%02d", ctrl.getTrie()[0]), 150, 40);
			g.drawString("Time : " + normalizeTime(ctrl.getTimes()[0]), gc.getWidth()-250, 40);
		}
		else {
			g.drawString("Tries : ", 150, 20);
			g.drawString("Time : ", 150, 40);
			for(int i = 0; i<ctrl.getJoueur(); i++) {
				if(i==0)
					g.setColor(Color.red);
				else if(i==1)
					g.setColor(Color.blue);
				else if(i==2)
					g.setColor(Color.yellow);
				else if(i==3)
					g.setColor(Color.green);
				if(ctrl.getPlaying() == i)
					g.fillRect(250+120*i, 0, 120, 60);
				else
					g.fillRect(250+120*i, 0, 120, 23);
				g.setColor(Color.black);
				System.out.println(i);
				g.drawString(perso[i], 250+120*i+30, 3);
				g.drawString(String.format("%02d", ctrl.getTrie()[i]), 250+120*i+50, 20);
				g.drawString(normalizeTime(ctrl.getTimes()[i]), 250+120*i+20, 40);
			}
		}
		ctrl.getButtonBack().dessiner(g);
	}
	
	/*******************************************
	Renvoie le temps sous forme normalisé
	********************************************/
	private String normalizeTime(float time) {
		return String.format("%02d", (int)((time/1000)/60)) + ":" + String.format("%02d", (int)((time/1000)%60));
	}
}
