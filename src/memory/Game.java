package memory;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

/*******************************************
C'est ici que le jeu tourne. 
********************************************/
public class Game extends BasicGame {

	private Views vue;           //class charger de gerer toute les vues
	private Controleur ctrl;	//controleur du jeu
	private DecorGame decor;	//gere le decors du game
	private int num_frame;			//indique le frame current
	private int timer, temp;		// timer pour le temps ici et temp est un tampon
	private int x_pl, y_pl, dimCard;  //coords du plateau et dimension de la card
	private Plateau pl;							//plateau du jeu
		
	public Game(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	/*********************************************
	Elle s'exécute une seule fois au lancement du jeu
	**********************************************/
	@Override
	public void init(GameContainer gc) throws SlickException {
		x_pl = 100;
		y_pl = 100;
		dimCard = 100;
		ctrl = new Controleur();
		ctrl.init(gc);
		vue = new Views(ctrl);
		decor = new DecorGame(ctrl);
		vue.setDecor(decor);
		num_frame = 0;
		timer = 0;
	}
	
	/**************************************************
	Elle affiche à chaque fois un rendu (elle tourne toute
	seule, mais on peut définir sa vitesse dans le main
	comme je l'ai fait)
	***************************************************/
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		
		//page d'accueil
		if(num_frame==0) {
			vue.accueil(g, timer);
		}
		
		//page du menu principal
		else if(num_frame==1) {
			vue.menu(g, gc);
		}
		
		//page du menu suite a une partie
		else if(num_frame==-1) {
			vue.menu2(g, gc);
		}
		
		//page de jeu
		else if(num_frame == 2) {
			vue.drawParty(gc, g);
		}
		
		//page du setting
		else if(num_frame == 3) {
			vue.sitting(gc, g);
		}
		
		//page de l'aide
		else if(num_frame == 5) {
			vue.help(g, gc);
		}
		
		//page de fin de partie
		else if(num_frame == 4) {
			if(ctrl.getEnd() == -1)
				vue.gameHover(g);
			else if(ctrl.getEnd() == 1)
				vue.win(g, gc);
		}
	}

	/********************************************************************
	Elle met les informations à jour dans des intervales de temps comme
	la methode render. les infos mis à jour permettent aussi de changer le
	rendu suivant. Donc Basic Game à jugé bon de séparer les calculs graphiques
	et les calculs de traitement proprement dit dans deux lignes d'exécutions
	différents pour un résultat optimal. 
	*********************************************************************/
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		// TODO Auto-generated method stub
		Input inp = gc.getInput();
		timer += delta;
		ctrl.manageSong(delta);
			
		//gestion de accueil	
		if(num_frame==0) {
			ctrl.animAccueil(gc, inp.getMouseX(), inp.getMouseY());
			if(inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				num_frame = ctrl.getEvenAccueil(inp.getMouseX(), inp.getMouseY());
			}
		}
		
		//gestion des menus	
		if(num_frame==1 || num_frame == -1) {
			if(inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				if(num_frame == 1)
					temp = ctrl.getEventMenu(inp.getMouseX(), inp.getMouseY(), gc);
				else
					temp = ctrl.getEventMenu2(inp.getMouseX(), inp.getMouseY(), gc);
				
				if(temp == 1) {
					num_frame = 2;
					pl = new Plateau(x_pl, y_pl, ctrl.getLevel(), vue.getLabelCategory(), false, ctrl.getNbrCardValidation(), gc);
					vue.setPl(pl);
					ctrl.setPl(pl);
					ctrl.newPart();
					decor = new DecorGame(ctrl);
					vue.setDecor(decor);
				}
				
				else if(temp == 3) {
					num_frame = 3;  //setting
					ctrl.getListForLevel().setCurrentSelect(ctrl.getLevel()-1);
				}
				else if(temp == -1)
					gc.exit();
				else if(temp == 2)
					num_frame = 2;
				else if(temp==5)
					num_frame = 5;
			}
			else {
				ctrl.animHoverMenu(inp.getMouseX(), inp.getMouseY(), num_frame, gc);
			}
		}
		
		//gestion du setting
		if(num_frame == 3) {
			ctrl.animSetting(gc, inp.getMouseX(), inp.getMouseY(), delta);
			if(inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				temp = ctrl.getEvenSetting(inp.getMouseX(), inp.getMouseY());
				if(temp!=0)
					num_frame = temp;
			}
		}
		
		//gestion de la fin de partie
		if(num_frame == 4) {
	
				if(inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					temp = ctrl.getEvenEnd(inp.getMouseX(), inp.getMouseY());
					if(temp == 1) {
						num_frame = 1;
						ctrl.setEnd(0);
					}
					else if(temp == 2) {
						if(ctrl.getEnd() == 1) {
							if(ctrl.getLevel() != 6)
								ctrl.setLevel(ctrl.getLevel() +1);
						}
						ctrl.setEnd(0);
						num_frame = 2;
						pl = new Plateau(x_pl, y_pl, ctrl.getLevel(), vue.getLabelCategory(), false, ctrl.getNbrCardValidation(), gc);
						vue.setPl(pl);
						ctrl.setPl(pl);
						ctrl.newPart();
						decor = new DecorGame(ctrl);
						vue.setDecor(decor);
					}
				}
				ctrl.animGameEnd(gc, inp.getMouseX(), inp.getMouseY());

		}
		
		//gestion de la page d'aide
		if(num_frame == 5) {
			
			if(inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				temp = ctrl.getEvenHelp(inp.getMouseX(), inp.getMouseY());
				if(temp != 0)
					num_frame = temp;
			}
			ctrl.mangeHoverHelp(inp.getMouseX(), inp.getMouseY());
		}
		
		//gestion de la partie	
		if(num_frame==2) {
			if(inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				if(ctrl.getEventPlay(inp.getMouseX(), inp.getMouseY(), pl, gc) == -1) {
					num_frame = -1;
				}
			}
			if(ctrl.isEnd())
				{
					ctrl.calculScore();
					num_frame = 4;
				}

			ctrl.manageAnimGame(inp.getMouseX(), inp.getMouseY(), delta);
		}
		
		
		//sortie express
		if(inp.isKeyPressed(Input.KEY_Q))
			gc.exit();
	}
	
}
