package memory;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

/*******************************************
Chaque methode de cette class a pour but
d'afficher une vue à l'ecran
********************************************/
public class Views {

	private Controleur ctrl;
	private Echantillon[] dosCards;
	private Image fondAc, imageMenu, category, fontChangeCard;
	private Image fontSetting, cadreSetting;
	private String[] labelCat;
	private Plateau pl;
	private DecorGame decor;
	private Image fondMenu, logoMenu, cateMenu;
	private Image gameHover, win, logoWin;
	private int cate;
	private Font fontTitle,fontSimple;
	private TrueTypeFont ttfTitle, ttfSimple;
	
	public Views(Controleur ctrl) throws SlickException {
		this.ctrl = ctrl;
		fondAc = new Image("accueil/accueil.png");
		labelCat = new String[10];
		labelCat[0] = "Food and drink";
		labelCat[1] = "Cats";
		labelCat[2] = "Fruits and vegetables";
		labelCat[3] = "Shapes";
		labelCat[4] = "Flags";
		labelCat[5] = "Numbers";
		labelCat[6] = "Letters";
		labelCat[7] = "Music";
		labelCat[8] = "Sport";
		labelCat[9] = "Attraction";
		fontTitle = new Font("Algerian", Font.BOLD, 46);
		ttfTitle = new TrueTypeFont(fontTitle, true);
		fontSimple = new Font("Snap ITC", Font.CENTER_BASELINE, 20);
		ttfSimple = new TrueTypeFont(fontSimple, true);
		fondMenu = new Image("menu/fond.jpg");
		fontSetting = new Image("setting/fontSetting.png");
		cadreSetting = new Image("setting/frameSetting.png");
		fontChangeCard = new Image("fontChangeCard.png");
		logoWin =  new Image("logoWin.png");
		logoMenu = new Image("menu/logo.png");
		cate = ctrl.getCategory();
		cateMenu = new Image("categories/"+ cate + ".png");
		gameHover = new Image("gameHover.png");
		win = new Image("win.png");
	}

	public void accueil(Graphics g, float timer) {
		g.drawImage(fondAc, 0, 0);
		ctrl.getButtonStart().dessiner(g);
	}
	
	public void help(Graphics g, GameContainer gc) {
		ctrl.getHelp().dessiner(g, gc);
	}
	
	public void menu(Graphics g, GameContainer gc) throws SlickException {
		g.drawImage(fondMenu, 0, 0);
		
		ttfTitle.drawString(gc.getWidth()/2 - 200, 0, "Memory", Color.orange);
		ttfTitle.drawString(gc.getWidth()/2+20, 0, "Game", Color.blue);
		
		if(cate!=ctrl.getCategory())
		{
			cate = ctrl.getCategory();
			cateMenu = new Image("categories/"+ cate + ".png");
		}
		g.drawImage(cateMenu, gc.getWidth()/2-250, 200);
		
		ctrl.getButtonD_g().dessiner(g);
		
		ctrl.getButtonD_r().dessiner(g);
		
		ctrl.getButtonParam().dessiner(g);
		
		ctrl.getButtonHelp().dessiner(g);
		
		g.setColor(Color.black);
		ttfSimple.drawString(gc.getWidth()/2-labelCat[ctrl.getCategory()].length()*10/2, 550, labelCat[ctrl.getCategory()], Color.black);
		
		ttfTitle.drawString( gc.getWidth()/2 - ctrl.getListMode()[ctrl.getMode()].length()*15, 100, ctrl.getListMode()[ctrl.getMode()]);


		ttfTitle.drawString(55, gc.getHeight()-195, "Joueur", Color.black);
		
		
		ttfTitle.drawString(100, gc.getHeight()-130, ctrl.getJoueur() + "");
		

		ttfTitle.drawString(gc.getWidth()-60-150, gc.getHeight()-195, "Level", Color.black);
		
		ttfTitle.drawString(gc.getWidth()-60-150+45, gc.getHeight()-130, ctrl.getLevel() + "");
		ctrl.getButtonPlay().dessiner(g);
		ctrl.getButtonExit().dessiner(g);
	}

	
	public void changeDosCard(GameContainer gc, Graphics g) throws SlickException {
		g.drawImage(fontChangeCard, gc.getWidth()/2 - fontChangeCard.getWidth()/2, gc.getHeight()/2 - fontChangeCard.getHeight()/2);
		ttfTitle.drawString(gc.getWidth()/2-250, gc.getHeight()/2 - fontChangeCard.getHeight()/2+50, "Change Style Card");
		
		for(int i =0; i<dosCards.length; i++) {
			dosCards[i].dessiner(g);
		}
	}
	
	public void sitting(GameContainer gc, Graphics g) throws SlickException {
		g.drawImage(fontSetting, 0, 0);
		ttfTitle.drawString(gc.getWidth()/2 - 100, 10, "Setting", Color.black);
		ctrl.getListForLevel().dessiner(g);
		ctrl.getListForMode().dessiner(g);
		ctrl.getListForPlayer().dessiner(g);
		if(ctrl.getListForLevel().isUnRoll())
			ctrl.getListForLevel().dessiner(g);
		else if(ctrl.getListForMode().isUnRoll())
			ctrl.getListForMode().dessiner(g);
		ctrl.getButtonBackFromSetting().dessiner(g);
		//g.drawImage(cadreSetting, 600, 300);
		
		ctrl.getToggleSetting().dessiner(g);
		if(ctrl.getToggleSetting().getIndex() == 0) {
			for(int i =0; i<3; i++) {
				for(int j = 0; j<3; j++) {
					ctrl.getTutoPairs()[i][j].dessiner(g);
				}
			}
			ctrl.getSwitchPair().dessiner(g);
			ctrl.getSwitchTriple().dessiner(g);
			g.drawImage(new Image("setting/fontNbrVal.png"), ctrl.getSwitchPair().getX()-200, ctrl.getSwitchPair().getY()-10);
			g.drawImage(new Image("setting/fontNbrVal.png"), ctrl.getSwitchTriple().getX()-200, ctrl.getSwitchTriple().getY()-10);
			ttfSimple.drawString(ctrl.getSwitchPair().getX()-130, ctrl.getSwitchPair().getY() + 10, "pairs");
			ttfSimple.drawString(ctrl.getSwitchTriple().getX()-140, ctrl.getSwitchTriple().getY() + 10, "triplets");
		}
		else if(ctrl.getToggleSetting().getIndex() == 1)
			for(int i =0; i<6; i++) {
				ctrl.getDosCards()[i].dessiner(g);
				if(i == ctrl.getNumDos())
					g.drawImage(new Image("setting/ok Card.png"), ctrl.getDosCards()[i].getLastX(), ctrl.getDosCards()[i].getLastY());
			}
		
		if(ctrl.getStepAnimTutoPairs() == 5) {
			g.drawImage(new Image("setting/x.png"), ctrl.getTutoPairs()[0][0].getX(), ctrl.getTutoPairs()[0][0].getY());
		}
		else if(ctrl.getStepAnimTutoPairs() == 13) {
			g.drawImage(new Image("setting/ok.png"), ctrl.getTutoPairs()[0][0].getX(), ctrl.getTutoPairs()[0][0].getY());
		}
	}
	
	public void menu2(Graphics g, GameContainer gc) throws SlickException {
		this.menu(g, gc);
		ctrl.getButtonContinu().dessiner(g);
	}
	
	public void drawParty(GameContainer gc, Graphics g) throws SlickException {
		decor.dessiner(gc, g);
		pl.dessiner(g);
	}
	
	public void gameHover(Graphics g) {
		g.drawImage(gameHover, 0, 0);
		ctrl.getButtonReplay().dessiner(g);
		ctrl.getButtonHome().dessiner(g);
	}
	
	public void win(Graphics g, GameContainer gc) {
		g.drawImage(win, 0, 0);
		if(ctrl.getJoueur() != 1)
			for(int i =0; i<ctrl.getJoueur(); i++) {
				ttfTitle.drawString(550, 200+ctrl.getRangeJoueur()[i]*100, "Joueur " + (i+1) + " : " +ctrl.getScore()[i], Color.black);
			}
		else {
			g.drawImage(logoWin, gc.getWidth()/2 - logoWin.getWidth()/2, 250);
			ttfTitle.drawString(600, 100, "Score : " +ctrl.getScore()[0], Color.black);
		}
		ctrl.getButtonNext().dessiner(g);
		ctrl.getButtonHome().dessiner(g);
	}
	
	public void setPl(Plateau pl) {
		this.pl = pl;
	}
	
	public void setDecor(DecorGame decor) {
		this.decor = decor;
	}
	
	public String getLabelCategory() {
		return labelCat[ctrl.getCategory()];
	}
}
