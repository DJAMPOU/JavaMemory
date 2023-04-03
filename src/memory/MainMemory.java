package memory;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class MainMemory {

	public static void main(String[] args) throws SlickException {
		// TODO Auto-generated method stub
		Game memo = new Game("Memory Game");
		AppGameContainer app = new AppGameContainer(memo);
		app.setShowFPS(false);
		app.setTargetFrameRate(500);
		app.setDisplayMode(1500, 800, false);;
		app.start();
	}

}
