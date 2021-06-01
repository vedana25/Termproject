package board;

import entity.Archer1;
import entity.Archer2;
import entity.Archer3;
import entity.Warrior1;
import entity.Warrior2;
import entity.Warrior3;

import entity.gameObject;
import gui.Table;
import gui.Table.TilePanel;

import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities; 
public class Board {
	
	public Player player1;
	public Player player2;
	public Storage storage1;
	public Storage storage2;
	public static int round=0;
	public boolean isFinish=false;
	
	HashMap<Integer, gameObject> EntityMap;
	
	private static int finished=0;

	//write a line about an attack on the log
	public static void printOnlog(gameObject attacker, gameObject target, int damage) {
		System.out.println(attacker.getName()+" dealt "+damage+" damages to "+target.getName()+"(Hp: "+ target.getHealth()+")");
		if(target.getHealth()<=0) {
			System.out.println(attacker.getName()+" killed "+target.getName());
			baseBoard.drawBattleBoard(BoardManager.ENTITIES_ONBOARD, Board.round);

		}
	}
	//when an object attacked with a skill
	public static void printOnlog(gameObject attacker, gameObject target, int damage,String skillname) {
		System.out.println(attacker.getName()+" dealt "+damage+" damages to "+target.getName()+target.getName()+"(Hp: "+ target.getHealth()+")"+" with: "+attacker.getSkillname());
		if(target.getHealth()<=0) System.out.println(attacker.getName()+" killed "+target.getName());
	}	
	
	
	public int getRound() {
		return round;
	}
	
	public Storage getStorage1() {
		return this.storage1;
	}
	
	public Storage getStorage2() {
		return this.storage2;
	}
	public void setFinish(int isfinish) {
		finished=isfinish;
	}
	
	//Print ROUND # and Score
	public void printScore() {
		int presentRound = round+1;
		System.out.println("SCORE "+player1.getNumOfWin()+" : "+player2.getNumOfWin());
		System.out.println("Player1 HP: " + player1.getHp());
		System.out.println("Player2 HP: " + player2.getHp());
	}
	
	//GUI Version Print ROUND # and Score
	public String popupScore() {
		String message;
		int presentRound = round+1;
		message = "ROUND "+presentRound+
		"\nSCORE "+player1.getNumOfWin()+" : "+player2.getNumOfWin() +
		"\nPlayer1 HP: " + player1.getHp() +
		"\nPlayer2 HP: " + player2.getHp();
		return message;
	}
	
	//move all objects on the board to the storages after end of a round
	public void clearBoard(Table gameTable) {
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				gameObject e=BoardManager.ENTITIES_ONBOARD[i][j];
				if(e!=null) {
					if(e.getPlayer()==player1)  {
						gameTable.getBoardPanel().getUpperStoragePanel(e.getCellNumber()).assignChampion(gameTable.getGameBoard(), e);
						gameTable.getBoardPanel().getTilePanel(e.getX()*10+e.getY()).removeAll();
					}else {
						gameTable.getBoardPanel().getLowerStoragePanel(e.getCellNumber()).assignChampion(gameTable.getGameBoard(), e);
						gameTable.getBoardPanel().getTilePanel(e.getX()*10+e.getY()).removeAll();

					}
					e.setInstorage(true);
					e.setHealth(e.getMaxHealth());
				}
			}
		}
	}
	
	
	//judge which player won the game
	public boolean judge() {
		if(player1.getHp()==0 || player2.getHp()==0) {
			this.printScore();
			if(player1.getHp()==0) {
				System.out.println("Final Winner: Player2");
				return true;
			}
			else {
				System.out.println("Final Winner: Player1");
				return true;
			}
		}else {
			round++;
			System.out.println("PREPARE FOR THE NEXT ROUND!");
			JOptionPane.showMessageDialog(null, "PREPARE FOR THE NEXT ROUND!");
			return false;
		}
	}
	
	//reduce hp of lost player of an round
	public void endRound(int whoWin, Table gameTable) {
		
		if(whoWin==1) {
			player2.setHp(player2.getHp()-50);
			player1.setNumOfWin(player1.getNumOfWin()+1);
			JOptionPane.showMessageDialog(null, "Player1 Win!", round+"round Winner", 0);
			System.out.println("Player1 Win!");
			System.out.println("---------------------------------------------------------------------");
		}
		else if(whoWin==2) {
			player1.setHp(player1.getHp()-50);
			player2.setNumOfWin(player2.getNumOfWin()+1);		
			JOptionPane.showMessageDialog(null, "Player2 Win!", round+"round Winner", 0);
			System.out.println("Player2 Win!");
			System.out.println("---------------------------------------------------------------------");
		}
		else {
			player1.setHp(player1.getHp()-50);
			player2.setHp(player1.getHp()-50);
			System.out.println("Draw!");
			JOptionPane.showMessageDialog(null, "Draw!", round+"round Winner", 0);
			System.out.println("---------------------------------------------------------------------");	
		}
		
		this.clearBoard(gameTable);
		gameTable.getGameBoard().player1.setReady(false);
		gameTable.getGameBoard().player2.setReady(false);

	}
	
	public void initialize() {
		player1= new Player();
		player2= new Player();
		storage1= new Storage();
		storage2= new Storage();
		player1.setStorage(storage1);
		player2.setStorage(storage2);
		round=0;
		BoardManager.addAllEntity(this);
	}

	
	
}
