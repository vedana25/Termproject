package runGame;
import gui.Table;

import javax.swing.SwingUtilities;

import board.Battle;
import board.Board;
import board.BoardManager;
import board.Preparation;
import gui.RandomShop;
import gui.Table;
public class runGame extends Thread{
	static Board game;
	Thread preparation;
	Thread battle;
	Thread randomShop;
	
	public static void main (String[] args) {
		Thread startgame=new runGame();
		startgame.start();
		
		try {
			startgame.join();
		} catch (Exception e) {
		}
		
		if (game.isFinish == true) {
			startgame.interrupt();
		}
	}
	public int wantTostart(Table gameTable) {
		return gameTable.start;
	}
	
	public void run() {
		
		game = new Board();
		game.initialize();
		Table gameTable = new Table(game);	
		
		if(wantTostart(gameTable)==0) {
			while(!game.isFinish) {
				
				randomShop = new Thread(new RandomShop(game,gameTable));
				preparation = new Preparation(game,gameTable);
				battle = new Battle(game);
				
				//shopping phase: choose random object
				randomShop.start();
				try {randomShop.join();
				} catch (Exception e) {}
				
				//preparation phase: move objects to the board
				preparation.start();
				try {preparation.join();
				} catch (Exception e) {}
				
				//battle phase
				Battle.isFinished = false;
		        battle.start();
		        while(true) {
		        	if(Battle.isFinished) {
		        		battle.interrupt();
		        		break;
		        	}
		        	try {Thread.sleep(1000);
					} catch (InterruptedException e) {}
		        }
				
		        game.endRound(Battle.whoWin,gameTable);
		        
		        if(game.judge()==true) {
		        	game.isFinish=true;
		        }          
			}
			System.out.println("game end");
		}
	}
}


