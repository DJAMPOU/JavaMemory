package memory;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ShadowEffect;

/*************************************
 * Cette class est le cerveau du jeu.*
 * @author pedro		     *
 ************************************/
public class Controleur {

	      
	private ArrayList animCard;					//liste des cartes animées à un instant donné
	private ArrayList CardForValidation;				//liste des cartes en attente de validation(le joueur n'a pas encore fini de selectionner la pair ou le triplet)				
	private final int nbrJoueurMax = 4;  				//nombre de joueur max
	private int prevFrame;						//indique la page precedente lors d'une navigation
	private int timerTutoPairs, stepAnimTutoPairs;			//Utilisé pour gérer les animations du tuto des paramètres
	private int numDos;						//indique le dos actuel des cartes
	private int playing, timer;					//indique le joueur actuel
	private int timerSong;
	private int end;
	private int nbrCardValidation;					//Nombre de carte identique necessaire pour valider
	private boolean isChoosingNumcard;				
	private  boolean temp, animStartGame, prevTemp;
	private boolean pl_move;
	private int[] stepMenu;						//0 pour la categorie, 1 pour mode, [2]+1 pour le level et [3]+1 pour le nombre de joueur
	private int[] trieJoueurs;
	private int[] timeJoueurs;
	private int[] score;
	private int[] rangeJoueur;
	private Button backFromGame;
	private Button backFromSetting;
	private Button switchPair;
	private Button switchTriple;
	private Button buttonStart;
	private Button leftArrowMenu, rigthArrowMenu, continu, play, exit; 
	private Button setting;
	private Button changeCard;
	private Button buttonHome, buttonReplay, buttonNext;
	private Button buttonHelp;
	private String[] listMode;
	private List listForMode;
	private List listForPlayer;
	private List listForLevel;
	private Toggle toggleSetting;
	private Card[][] tutoPairs;
	private Echantillon[] dosCard;
	private Image mouse, mouseHand, mouseWait;
	private Plateau pl;
	private Music musicFont;
	private Aide help;
	
	
	
	public Controleur() throws SlickException {
		animCard = new ArrayList<Card>(0);
		CardForValidation = new ArrayList<Card>(0);
		nbrCardValidation = 2;
		numDos = 0;
		Card.setImgDos(new Image("dos/dos" + numDos + ".png"));
		animStartGame = false;
		stepMenu = new int[4];
		trieJoueurs = new int[nbrJoueurMax];
		timeJoueurs = new int[nbrJoueurMax];
		timerTutoPairs = 0;
		stepAnimTutoPairs = 0;
		dosCard = new Echantillon[6];
		playing = -1;
		listMode = new String[4]; // 0 pour la liste des categories, 1 pour la liste des joueurs et 2 pour la liste des niveaux
		end = 0;
		score = new int[nbrJoueurMax];
		rangeJoueur = new int[nbrJoueurMax];
		timerSong = 0;
	}
	
	
	/*********************************************
	 * initialise le controleur
	 * @param gc
	 * @throws SlickException
	 */
	public void init(GameContainer gc) throws SlickException {
		musicFont = new Music("musicFont.ogg");
		musicFont.loop();
		leftArrowMenu = new Button(gc.getWidth()/2-400, 325, "menu/d_g.png", "menu/d_ghover.png");
	    rigthArrowMenu = new Button(gc.getWidth()/2+300, 325, "menu/d_r.png", "menu/d_rhover.png");
	    play = new Button(gc.getWidth()/2-250, gc.getHeight()-150, "menu/play1.png", "menu/play2.png");
		continu =  new Button(gc.getWidth()-120, gc.getHeight()/2-50, "continue.png", "continueHover.png");
		exit = new Button(gc.getWidth()/2+100, gc.getHeight()-150, "menu/exit1.png", "menu/exit2.png");
		
		setting = new Button(gc.getWidth() - 150, 50, "menu/setting.png", "menu/settingHover.png");
		buttonHome = new Button(200, gc.getHeight()-150, "houseHover.png", "house.png");
		buttonReplay = new Button(gc.getWidth()-300, gc.getHeight()-150, "replay.png", "replayHover.png");
		buttonNext = new Button(gc.getWidth()-300, gc.getHeight()-150, "next.png", "nextHover.png");
		buttonHelp = new Button(50, 50, "menu/help.png", "menu/helpHover.png");
		
		for(int i =0; i<6; i++) {
			dosCard[i] = new Echantillon(new Image("dos/dos" + i + ".png"), new Image("shadow.png"));
			dosCard[i].setLastX(800 + 150*(i%3));
			dosCard[i].setLastY(400 + 150*(i/3));
		}
		
		changeCard = new Button(gc.getWidth()-250, 320, "menu/changeCard.png", "menu/changeCardHover.png");
		
		backFromGame = new Button(50, gc.getHeight()-110, "back.png", "backHover.png");
		backFromSetting = new Button(50, 50, "back.png", "backHover.png");
		switchPair = new Button(gc.getWidth()-120, 350, "setting/switchOn.png", "setting/switchOn.png");
		switchTriple = new Button(gc.getWidth()-120, 460, "setting/switchOff.png", "setting/switchOff.png");
		
		buttonStart = new  Button(gc.getWidth()/2-100, gc.getHeight()-100, "accueil/start.png", "accueil/startHover.png");
		
		Button[] list1 = new Button[2];
		list1[0] = new Button(gc.getWidth()-250, 300, "menu/boutValidPair.png", "menu/boutValidPairHover.png");
		list1[1] = new Button(gc.getWidth()-250, 370, "menu/boutValidTriplet.png", "menu/boutValidTripletHover.png");
		
		mouse = new Image("mouse/mouse.png");
		mouseHand = new Image("mouse/mouseHand.png");
		mouseWait = new Image("mouse/mousewait.png");
		
		String[] list2 = {"Valid Card", "Theme card"};
		toggleSetting = new Toggle(800, 200, "setting/toggleOff.png", "setting/toggleOn.png", list2);
		
		gc.setMouseCursor(mouse, 0, 0);
		prevTemp = false;
		
		
		listMode[0] = "Simple";
		listMode[1] = "Timer Challenger";
		listMode[2] = "Limited Tries";
		listMode[3] = "Remenber All";
		
		tutoPairs = new Card[3][3];
		for(int  i= 0; i<3; i++) {
			for(int  j =0; j<3; j++) {
				if(i==0 && j ==0 || i ==1 && j==0 || i==1 && j==1)
					tutoPairs[i][j] = new Card(new Image("Letters/0.png"), -1);
				else if(i==0 && j ==1)
					tutoPairs[i][j] = new Card(new Image("Letters/1.png"), -1);
				else
					tutoPairs[i][j] = new Card(new Image("dos/dos" + this.getNumDos() + ".png"), -1);
				tutoPairs[i][j].mov(gc.getWidth()-350-(3-j)*110, gc.getHeight()-100-(3-i)*110);
			}
		}
		
		listForMode = new List(20, 200, "Mode", listMode);
		String[] list = {"1", "2", "3", "4", "5", "6"};
		listForLevel = new List(20, 200 + 220 + 220, "Level", list);
		list = new String[4];
		for(int i =0; i<4; i++)
			list[i] = i+1 + "";
		listForPlayer = new List(20, 200 + 220, "player", list);
		help = new Aide(gc, this.chargeHelp());
 	}
	
	/*******************************************
	Charge l'aide depuis un fichier
	********************************************/
	private String chargeHelp() {
		String help = "";
		try
	    {
	      // Le fichier d'entrée
	      File file = new File("help.txt");    
	      // Créer l'objet File Reader
	      FileReader fr = new FileReader(file);  
	      // Créer l'objet BufferedReader        
	      BufferedReader br = new BufferedReader(fr);  
	      StringBuffer sb = new StringBuffer();    
	      String line;
	      while((line = br.readLine()) != null)
	      {
	        // ajoute la ligne au buffer
	        sb.append(line);      
	        sb.append("\n");     
	      }
	      fr.close();    
	      help = sb.toString();  
	    }
	    catch(IOException e)
	    {
	      e.printStackTrace();
	    }
		return help;
	}
	
	/*******************************************
	 * gerer les animation de l'accueil
	 * @param gc
	 * @param x
	 * @param y
	 * @throws SlickException
	 */
	public void animAccueil(GameContainer gc, int x, int y) throws SlickException {
		if(buttonStart.isHover(x, y))
			temp = true;
		else 
			temp = false;
		if(temp && !prevTemp) {
			prevTemp = true;
			gc.setMouseCursor(mouseHand, 0, 0);
		}
		else if(!temp && prevTemp) {
			prevTemp = false;
			gc.setMouseCursor(mouse, 0, 0);
		}
	}
	
	
	/**********************************************
	 * gerer les evenements de l'accueil
	 * @param x
	 * @param y
	 * @return
	 */
	public int getEvenAccueil(int x, int y) {
		if(buttonStart.isHover(x, y)) {
			Button.getSong().play();
			return 1;
		}
		return 0;
	}
	
	
	/*********************************
	 * gerer le son
	 * @param delta
	 */
	public void manageSong(int delta) {
		timerSong += delta;
		if(!Button.getSong().playing() && !musicFont.playing() && !Card.getMusicClic().playing()) {
			musicFont.setPosition(timerSong);
			musicFont.play();
		}
	}

	
	/*******************************
	 * gerer les evenements du menu principale
	 * @param x
	 * @param y
	 * @param gc
	 * @return
	 */
	public int getEventMenu(int x, int y, GameContainer gc) {
		
		if(leftArrowMenu.isHover(x, y) && stepMenu[0]>0) {
			stepMenu[0]--;
			Button.getSong().play();
		}
		else if(rigthArrowMenu.isHover(x, y) && stepMenu[0]<9) {
			stepMenu[0]++;
			Button.getSong().play();
		}
		else if(leftArrowMenu.isHover(x, y) && stepMenu[0]==0) {
			stepMenu[0] = 9;
			Button.getSong().play();
		}
		else if(rigthArrowMenu.isHover(x, y) && stepMenu[0]==9) {
			stepMenu[0] = 0;
			Button.getSong().play();
		}
		
		if(setting.isHover(x, y)) {
			prevFrame = 1;
			Button.getSong().play();
			return 3;
		}
		
		if(play.isHover(x, y)) {
			this.reset();
			Button.getSong().play();
			return 1;
		}
		
		if(buttonHelp.isHover(x, y)) {
			return 5;
		}
		 
		else if(exit.isHover(x, y)) {
			Button.getSong().play();
			return -1;
		}
		return 0;
	}
	
	
	/*********************
	 * gerer les evenements du second menu
	 * @param x
	 * @param y
	 * @param gc
	 * @return
	 */
	public int getEventMenu2(int x, int y, GameContainer gc) {
		int i;
		i = this.getEventMenu(x, y, gc);
		if(i == 3)
			prevFrame = -1;
		if(i!=0)
			return i;
		if(continu.isHover(x, y))
			return 2;
		return 0;
	}
	
	
	private void reset() {
		playing = 0;
		for(int i = 0; i<4; i++) {
			trieJoueurs[i] = 0;
			timeJoueurs[i] = 0;
		}
	}
	
	
	/*****************
	 * animation du menu
	 * @param x
	 * @param y
	 * @param whichMenu
	 * @param gc
	 * @throws SlickException
	 */
	public void animHoverMenu(int x, int y, int whichMenu, GameContainer gc) throws SlickException {
		temp = false;

		if(leftArrowMenu.isHover(x, y) || rigthArrowMenu.isHover( x, y) || play.isHover(x, y) || whichMenu==-1&&continu.isHover(x, y) || exit.isHover(x, y))
			temp = true;
		
		if(changeCard.isHover(x, y))
			temp = true;
		
		if(setting.isHover(x, y))
			temp = true;
		if(buttonHelp.isHover(x, y))
			temp = true;
		
		if(temp && !prevTemp) {
			gc.setMouseCursor(mouseHand, 0, 0);
			prevTemp = true;
		}
		else if(!temp && prevTemp) {
			gc.setMouseCursor(mouse, 0, 0);
			prevTemp = false;
		}
	}
	
	
	/************
	 * manage even setting
	 * @param x
	 * @param y
	 * @return
	 * @throws SlickException
	 */
	public int getEvenSetting(int x, int y) throws SlickException {
		
		if(listForLevel.isUnRoll()) {
			if(listForLevel.isSelected(x, y)!=-1) {
				stepMenu[2]= listForLevel.isSelected(x, y);
				Button.getSong().play();
				prevFrame  = 1;
			}
		}
		
		if(listForPlayer.isUnRoll()) {
			if(listForPlayer.isSelected(x, y)!=-1) {
				stepMenu[3]= listForPlayer.isSelected(x, y);
				Button.getSong().play();
				prevFrame = 1;
			}
		}
		
		if(listForMode.isUnRoll()) {
			if(listForMode.isSelected(x, y)!=-1) {
				stepMenu[1] = listForMode.isSelected(x, y);
				Button.getSong().play();
				prevFrame = 1;
			}
		}
		
		if(listForLevel.verifHover(x, y)) {
			listForLevel.swicthUnRoll();
			Button.getSong().play();
		}
		else
			listForLevel.setUnRoll(false);
		if(listForPlayer.verifHover(x, y)) {
			listForPlayer.swicthUnRoll();
			Button.getSong().play();
		}
		else 
			listForPlayer.setUnRoll(false);
		if(listForMode.verifHover(x, y)) {
			listForMode.swicthUnRoll();
			Button.getSong().play();
		}
		else
			listForMode.setUnRoll(false);
		
		if(backFromSetting.isHover(x, y)) {
			Button.getSong().play();
			return prevFrame;
		}
		if(toggleSetting.swicth(x, y)) {
			resetAnimSetting();
			Button.getSong().play();
		}
		
		if(switchPair.isHover(x, y) && nbrCardValidation == 3) {
			Button.getSong().play();
			Image temp;
			temp = switchPair.getImg();
			switchPair.setImg(switchTriple.getImg());
			switchTriple.setImg(temp);
			temp = switchPair.getImgHover();
			switchPair.setImgHover(switchTriple.getImgHover());
			switchTriple.setImgHover(temp);
			nbrCardValidation = 2;
			resetAnimSetting();
		}
		
		if(switchTriple.isHover(x, y) && nbrCardValidation == 2) {
			Button.getSong().play();
			Image temp;
			temp = switchPair.getImg();
			switchPair.setImg(switchTriple.getImg());
			switchTriple.setImg(temp);
			temp = switchPair.getImgHover();
			switchPair.setImgHover(switchTriple.getImgHover());
			switchTriple.setImgHover(temp);
			nbrCardValidation = 3;
			resetAnimSetting();
		}
		
		if(toggleSetting.getIndex() == 1)
			for(int i =0; i<dosCard.length; i++) {
				if(dosCard[i].verifHover(x, y)) {
					numDos = i;
					Card.setImgDos(new Image("dos/dos" + numDos + ".png"));
				}
			}
		return 0;
	}
	
	
	/***************************
	 * reactualise l'animation tuto de carte du setting
	 */
	private void resetAnimSetting() {
		timerTutoPairs = 0;
		stepAnimTutoPairs = 0;
		tutoPairs[0][0].reset();
		tutoPairs[0][1].reset();
		tutoPairs[1][0].reset();
		tutoPairs[1][1].reset();
	}
	
	
	/**************************
	 * manage anim setting
	 * @param gc
	 * @param x
	 * @param y
	 * @param delta
	 * @throws SlickException
	 */
	public void animSetting(GameContainer gc, int x, int y, int delta) throws SlickException {
		if(listForLevel.verifHover(x, y))
			temp = true;
		
		else if(listForPlayer.verifHover(x, y))
			temp = true;

		else if(listForMode.verifHover(x, y))
			temp = true;
		
		else if(backFromSetting.isHover(x, y))
			temp = true;
		
		else if(toggleSetting.hover(x, y))
			temp = true;
		
		else if(switchPair.isHover(x, y) && nbrCardValidation == 3) 
			temp = true;
		
		else if(switchTriple.isHover(x, y) && nbrCardValidation == 2)
			temp = true;
			
		else 
			temp = false;
			
		if(temp && !prevTemp) {
			gc.setMouseCursor(mouseHand, 0, 0);
			prevTemp = true;
		}
		else if(!temp && prevTemp) {
			gc.setMouseCursor(mouse, 0, 0);
			prevTemp = false;
		}
		if(toggleSetting.getIndex()==0) {
			this.animModevalidation(delta);
		}
		
		else if(toggleSetting.getIndex() == 1) {
			for(int i =0; i<6; i++) {
				dosCard[i].verifHover(x, y);
			}
		}
		
	}
	
	
	/******************************
	 * animation du tuto
	 * @param delta
	 */
	public void animModevalidation(int delta) {

			if(stepAnimTutoPairs == 0) {
				tutoPairs[0][0].clicked();
				Card.getMusicClic().stop();
				stepAnimTutoPairs  = 1;
			}
			else if(stepAnimTutoPairs == 1) {
				if(!tutoPairs[0][0].animation(delta))
					stepAnimTutoPairs = 2;
			}
			else if(stepAnimTutoPairs == 2) {
				timerTutoPairs += delta;
				if(timerTutoPairs>200) {
					timerTutoPairs = 0;
					stepAnimTutoPairs = 3;
				}
			}
			else if(stepAnimTutoPairs == 3) {
				tutoPairs[0][1].clicked();
				Card.getMusicClic().stop();
				stepAnimTutoPairs  = 4;
			}
			else if(stepAnimTutoPairs == 4) {
				if(!tutoPairs[0][1].animation(delta)) {
					stepAnimTutoPairs = 5;
					if(this.getNbrCardValidation() == 3)
						stepAnimTutoPairs = -1;
				}	
			}
			else if(stepAnimTutoPairs == -1){
				tutoPairs[1][1].clicked(); 
				Card.getMusicClic().stop();
				stepAnimTutoPairs = -2;
			}
			else if(stepAnimTutoPairs == -2) {
				if(!tutoPairs[1][1].animation(delta))
					stepAnimTutoPairs = -3;
			}
			else if(stepAnimTutoPairs == -3) {
				timerTutoPairs += delta;
				if(timerTutoPairs>200) {
					timerTutoPairs = 0;
					stepAnimTutoPairs = 5;
				}
			}
			else if(stepAnimTutoPairs == 5) {
				timerTutoPairs += delta;
				if(timerTutoPairs>500) {
					timerTutoPairs = 0;
					stepAnimTutoPairs = 6;
				}
			}
			else if(stepAnimTutoPairs == 6) {
				tutoPairs[0][0].retourner();
				tutoPairs[0][1].retourner();
				if(this.getNbrCardValidation() == 3)
					tutoPairs[1][1].retourner();
				Card.getMusicClic().stop();
				stepAnimTutoPairs = 7;
			}
			else if(stepAnimTutoPairs == 7) {
				tutoPairs[0][0].animation(delta);
				tutoPairs[0][1].animation(delta);
				tutoPairs[1][1].animation(delta);
				if(!tutoPairs[0][0].animation(0) && !tutoPairs[0][1].animation(0) && !tutoPairs[1][1].animation(0)) {
					stepAnimTutoPairs = 8;
				}
			}
			else if(stepAnimTutoPairs == 8) {
				timerTutoPairs += delta;
				if(timerTutoPairs>200) {
					timerTutoPairs = 0;
					stepAnimTutoPairs = 9;
				}
			}
			else if(stepAnimTutoPairs == 9) {
				tutoPairs[0][0].clicked();
				Card.getMusicClic().stop();
				stepAnimTutoPairs = 10;
			}
			else if(stepAnimTutoPairs == 10) {
				if(!tutoPairs[0][0].animation(delta)) {
					stepAnimTutoPairs = 11;
				}
			}
			else if(stepAnimTutoPairs == 11) {
				tutoPairs[1][0].clicked();
				Card.getMusicClic().stop();
				stepAnimTutoPairs = 12;
			}
			else if(stepAnimTutoPairs == 12) {
				if(!tutoPairs[1][0].animation(delta)) {
					stepAnimTutoPairs = 13;
					if(this.getNbrCardValidation() == 3)
						stepAnimTutoPairs = -4;
				}
			}
			else if(stepAnimTutoPairs == -4) {
				tutoPairs[1][1].clicked();
				Card.getMusicClic().stop();
				stepAnimTutoPairs = -5;
			}
			else if(stepAnimTutoPairs == -5) {
				if(!tutoPairs[1][1].animation(delta))
					stepAnimTutoPairs = 13;
			}
			else if(stepAnimTutoPairs == 13) {
				timerTutoPairs += delta;
				if(timerTutoPairs>500) {
					timerTutoPairs = 0;
					stepAnimTutoPairs = 14;
				}
			}
			else if(stepAnimTutoPairs == 14) {
				tutoPairs[0][0].retourner();
				tutoPairs[1][0].retourner();
				if(this.getNbrCardValidation() == 3)
					tutoPairs[1][1].retourner();
				Card.getMusicClic().stop();
				stepAnimTutoPairs = 15;
			}
			else if(stepAnimTutoPairs == 15) {
				tutoPairs[0][0].animation(delta);
				tutoPairs[1][0].animation(delta);
				tutoPairs[1][1].animation(delta);
				if(!tutoPairs[0][0].animation(0) && !tutoPairs[1][0].animation(0) && !tutoPairs[1][1].animation(0)) {
					stepAnimTutoPairs = 0;
				}
			}
		this.manageSong(0);
	}
	
	
	/*******************************
	 * retourne toute les cartes du plateau
	 * @param i
	 */
	public void switchAll(int i) {
		for(int k = 0; k<pl.getWidth(); k++) {
			for(int l = 0; l<pl.getHeight(); l++) {
				pl.getGrille()[l][k].setEtat(i);
			}
		}
	}
	
	
	/**************************
	 * prepare une nouvelle partie
	 */
	public void newPart() {
		timer = 0;
		animStartGame = false;
		for(int i =0; i<nbrJoueurMax; i++) {
			timeJoueurs[i] = 0;
			trieJoueurs[i] = 0;
		}
		if(this.getMode()==3) {
			this.switchAll(1);
			animStartGame = true;
		}
		else if(this.getMode() == 2) {
			for(int i=0; i<nbrJoueurMax; i++)
				this.trieJoueurs[i] = this.getLevel()*15;
		}
		else if(this.getMode() == 1) {
			for(int i=0; i<nbrJoueurMax; i++) {
				this.timeJoueurs[i] = this.getLevel()*30000;
			}
		}
		if(this.getLevel()%2==0)
			pl_move = true;
		else
			pl_move = false;
	}
	
	
	/*******************************
	 * calcul du score
	 */
	public void calculScore() {
		for(int i =0; i<this.getJoueur(); i++) {
			score[i] = 0;
			if(this.getMode() == 2) {
				score[i] = (trieJoueurs[i] + this.getLevel()*15)*100-(timeJoueurs[i]/1000)*50;
			}
			else if(this.getMode() == 1) {
				score[i] = ((timeJoueurs[i] + this.getLevel()*30000)/1000)*100 - (trieJoueurs[i])*100;
			}
			else {
				score[i] = 10000 - trieJoueurs[i]*100-(timeJoueurs[i]/1000)*50;
			}
			if(score[i]<0)
				score[i] = 0;
		}
		for(int i =0; i<this.getJoueur(); i++) {
			rangeJoueur[i] = 0;
			for(int j =0; j<this.getJoueur(); j++) {
				if(score[i]<score[j] || score[i] == score[j] && i>j)
					rangeJoueur[i]++;
			}
		}
		
	}
	
	
	/********************************
	 * determiner la fin
	 * @return
	 */
	public boolean isEnd() {
		
		if(this.getMode() == 2) {
			for(int i =0; i<this.getJoueur(); i++)
				if(trieJoueurs[i] <= 0) {
					end = -1;
					return true;
				}
		}
		else if(this.getMode() == 1) {
			for(int i =0; i<this.getJoueur(); i++)
				if(timeJoueurs[i] <= 0) {
					end = -1;
					return true;
				}
		}
		for(int i =0; i<pl.getHeight(); i++) {
			for(int j =0; j<pl.getWidth(); j++) {
				if(pl.getGrille()[i][j].getValide() != 1)
					return false;
			}
		}
		end = 1;
		return true;
	}
	
	
	/*******************************
	 * manage even d'une partie
	 * @param x
	 * @param y
	 * @param pl
	 * @param gc
	 * @return
	 */
	public int getEventPlay(int x, int y, Plateau pl, GameContainer gc) {
		if(!animStartGame) {
			int i, j, len;
			i = -1;
			j = -1;
			for(int k = 0; k<pl.getWidth(); k++) {
				for(int l = 0; l<pl.getHeight(); l++) {
					if(pl.getGrille()[l][k].pointIn(x, y, pl)) {
						i = k;
						j = l;
						break;
					}
				}
			}
			if(i<pl.getWidth() && i>=0 && j<pl.getHeight() && j>=0) {
				
				if(pl.getGrille()[j][i].getValide()==0 && pl.getGrille()[j][i].getEtat()==0)
					CardForValidation.add(pl.getGrille()[j][i]);
				
				pl.getGrille()[j][i].clicked();
				animCard.add(pl.getGrille()[j][i]);
				
				if(CardForValidation.size()>=2) {
					len = CardForValidation.size();
					for(int k =0; k<len-1; k++) {
						if(((Card)(CardForValidation.get(k))).getId() != ((Card)(CardForValidation.get(CardForValidation.size()-1))).getId()) {
							for(int l = 0; l<len; l++) {
								((Card)(CardForValidation.get(0))).retourner();
								animCard.add(((Card)(CardForValidation.get(0))));
								CardForValidation.remove(0);
							}
							if(this.getMode()!=2)
								trieJoueurs[playing] += 1;
							else
								trieJoueurs[playing] -= 1;
							changePlayer();
							break; 
						}
						else if(k==CardForValidation.size()-2) {
							if(CardForValidation.size() == pl.getLenPack()) {
								for(int l = 0; l<len; l++) {
									((Card)(CardForValidation.get(0))).setValide(1);
									animCard.add(((Card)(CardForValidation.get(0))));
									CardForValidation.remove(0);
								}
								if(this.getMode()!=2)
									trieJoueurs[playing] += 1;
								else
									trieJoueurs[playing] -= 1;
							}
						}
					}
				}
			}	
		}
		if(backFromGame.isHover(x, y)) {
			Button.getSong().play();
			return -1;
		}
		return 0;
	}
	
	
	/**************************
	 * manage anim d'une partie
	 * @param x
	 * @param y
	 * @param delta
	 */
	public void manageAnimGame(int x, int y, int delta) {
		if(!animStartGame) {
			int k, l;
			k = -1;
			l = -1;
			for(int i = 0; i<pl.getWidth(); i++) {
				for(int j = 0; j<pl.getHeight(); j++) {
					if(pl.getGrille()[j][i].pointIn(x, y, pl)) {
						k = i;
						l = j;
						//break;
					}
					if(pl.getGrille()[j][i].getHover())
						pl.getGrille()[j][i].setHover(false);
				}
			}
			
			if(k!=-1)
				pl.getGrille()[l][k].setHover(true);
			
			for(int i = 0; i<animCard.size(); i++) {
				if(!((Card)(animCard.get(i))).animation(delta)) {
					animCard.remove(i);
					i=0;
				}
			}
			if(this.getMode() != 1)
				timeJoueurs[playing] += delta;
			else
				timeJoueurs[playing] -= delta;
			
			if(pl_move){
				pl.moveCard(delta);
			}
		}
		else {
			timer += delta;
		}
		if(timer >1500 && animStartGame) {
			timer = 0;
			animStartGame = false;
			this.switchAll(0);
		}
		backFromGame.isHover(x, y);
	}
	
	public int getEvenHelp(int x, int y) {
		return help.getEvenAide(x, y) ;
	}
	
	public void mangeHoverHelp(int x, int y) {
		help.manageHover(x, y);
	}
	
	
	/***************************
	 * manage even end
	 * @param x
	 * @param y
	 * @return
	 */
	public int getEvenEnd(int x, int y) {
		if(buttonHome.isHover(x, y)) {
			end = 0;
			Button.getSong().play();
			return 1;
		}
		else if(buttonReplay.isHover(x, y) && end == -1) {
			Button.getSong().play();
			return 2;
		}
		else if(buttonNext.isHover(x, y) && end == 1) {
			Button.getSong().play();
			return 2;
		}
		return 0;
	}
	
	
	/****************************
	 * manage animation de la fin
	 * @param gc
	 * @param x
	 * @param y
	 * @throws SlickException
	 */
	public void animGameEnd(GameContainer gc, int x, int y) throws SlickException {
		if(buttonHome.isHover(x, y)) {
			temp = true;
		}
		else if(buttonReplay.isHover(x, y) && end == -1) {
			temp = true;
		}
		else if(buttonNext.isHover(x, y) && end == 1) {
			temp = true;
		}
		else
			temp = false;
		
		if(temp && !prevTemp) {
			gc.setMouseCursor(mouseHand, 0, 0);
			prevTemp = true;
		}
		else if(!temp && prevTemp) {
			gc.setMouseCursor(mouse, 0, 0);
			prevTemp = false;
		}
	}
	
	
	/****************************
	 * changer de joueur
	 */
	private void changePlayer() {
		if(playing+1 == getJoueur())
			playing = 0;
		else
			playing ++;
	}
	
	
	public void setPl(Plateau pl) {
		this.pl = pl;
	}
	
	
	public int getCategory() {
		return stepMenu[0];
	}
	
	
	public int getMode() {
		return stepMenu[1];
	}
	
	
	public int getLevel() {
		return stepMenu[2] + 1;
	}
	
	public int getJoueur() {
		return stepMenu[3] + 1;
	}
	
	public Button getButtonD_g() {
		return leftArrowMenu;
	}
	
	public Button getButtonD_r() {
		return rigthArrowMenu;
	}
	
	public Button getButtonParam() {
		return setting;
	}
	
	public Button getButtonPlay() {
		return play;
	}
	
	public Button getButtonExit() {
		return exit;
	}
	
	public Button getButtonContinu() {
		return continu;
	}
	
	public String[] getListMode() {
		return listMode;
	}
	

	public Button getButtonBack() {
		return backFromGame;
	}
	
	public Button getButtonBackFromSetting() {
		return backFromSetting;
	}
	
	public Button getChangeCard() {
		return changeCard;
	}
	
	public boolean isChoosingNumcard() {
		return isChoosingNumcard;
	}
	
	public int[] getTrie() {
		return trieJoueurs;
	}
	
	public int[] getTimes() {
		return timeJoueurs;
	}
	
	public int getPlaying() {
		return playing;
	}
	
	public Echantillon[] getDosCards() {
		return dosCard;
	}
	

	public Toggle getToggleSetting() {
		return toggleSetting;
	}

	public int[] getStepMenu() {
		return stepMenu;
	}


	public int[] getRangeJoueur() {
		return rangeJoueur;
	}

	public int getNbrCardValidation() {
		return nbrCardValidation;
	}
	
	public int getNumDos() {
		return numDos;
	}

	public Card[][] getTutoPairs() {
		return tutoPairs;
	}
	
	public Button getButtonHome() {
		return buttonHome;
	}

	public Button getButtonReplay() {
		return buttonReplay;
	}

	public Button getButtonNext() {
		return buttonNext;
	}

	public boolean isPl_move() {
		return pl_move;
	}

	public ArrayList getCardForValidation() {
		return CardForValidation;
	}

	public List getListForMode() {
		return listForMode;
	}

	public List getListForPlayer() {
		return listForPlayer;
	}

	public List getListForLevel() {
		return listForLevel;
	}

	public int getStepAnimTutoPairs() {
		return stepAnimTutoPairs;
	}

	public Button getSwitchPair() {
		return switchPair;
	}

	public Button getSwitchTriple() {
		return switchTriple;
	}

	public Button getButtonStart() {
		return buttonStart;
	}

	public Button getButtonHelp() {
		return buttonHelp;
	}

	public int getEnd() {
		return end;
	}

	public int[] getScore() {
		return score;
	}

	public Aide getHelp() {
		return help;
	}

	public void setLevel(int level) {
		stepMenu[2] = level-1;
	}
	
	public void setEnd(int end) {
		this.end = end;
	}

	public void setStepMenu(int[] stepMenu) {
		this.stepMenu = stepMenu;
	}


	public void setNbrCardValidation(int nbrCardValidation) {
		this.nbrCardValidation = nbrCardValidation;
	}

	public void setCardForValidation(ArrayList cardForValidation) {
		CardForValidation = cardForValidation;
	}

	public void setTrieJoueurs(int[] trieJoueurs) {
		this.trieJoueurs = trieJoueurs;
	}

	public void setTimeJoueurs(int[] timeJoueurs) {
		this.timeJoueurs = timeJoueurs;
	}

	public void setPlaying(int playing) {
		this.playing = playing;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public void setPl_move(boolean pl_move) {
		this.pl_move = pl_move;
	}

	public void setTemp(boolean temp) {
		this.temp = temp;
	}

	public void setPrevTemp(boolean prevTemp) {
		this.prevTemp = prevTemp;
	}

	public void setAnimStartGame(boolean animStartGame) {
		this.animStartGame = animStartGame;
	}

}
