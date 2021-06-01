package entity;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Math;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import board.Board;
import board.BoardManager;
import board.Player;
import gui.Table;

public class gameObject extends JFrame implements Runnable  {
	
	//properties
	private Player player;
	private String name;
	private int atk;
	private int range;
	private String skillname;
	private int skilltime;
	private long atktime; // in millisecond
	private int health;
	private int maxHealth;
	private int x;
	private int y;
	private boolean isInstorage;
	private int cellNumber; // storage room number.
	private boolean isAlive=true;

	private gameObject presentObject;

	//constructor
	public gameObject() {
		isInstorage=false;
		presentObject = this;
	}
	
	//methods
	
	//search the nearest enemy object
	public gameObject searchTarget() {
		gameObject target = null;
		int temp, closest=1000;
		try {
			for(int i=0;i<10;i++) {
				for(int j=0;j<10;j++) {
					temp=getDistance(this,BoardManager.ENTITIES_ONBOARD[i][j]);
					if(isEnemy(this,BoardManager.ENTITIES_ONBOARD[i][j])&&BoardManager.ENTITIES_ONBOARD[i][j].getHealth()>0) {
						if(temp<closest){
							closest=temp;
							target=BoardManager.ENTITIES_ONBOARD[i][j];
						}
						else {}
					}
				}
			}
		}catch(NullPointerException e){
		}
		return target;
	}
	
	//distinguish whether it's enemy or not
	public boolean isEnemy(gameObject from,gameObject to) {
		if(from.getPlayer()==to.getPlayer()) return false;
		else return true;
	}
	
	//calculate distance to a enemy
	public int getDistance(gameObject from,gameObject to) {
		int distance;
		distance=Math.abs(to.getX()-from.getX())+Math.abs(to.getY()-from.getY());
		return distance;
	}
	
	//attack the target
	public void attack(gameObject target) {//reduce the health of the target
		synchronized(target) { //target gets one attack at a time
		if(target.getHealth()-this.getAtk()>=0) {
			target.setHealth(target.getHealth()-this.getAtk());
		}
		else {
			target.setHealth(0);
			Board.printOnlog(this,target,this.getAtk()); //print about it on the log
		}
		/*
        SwingUtilities.invokeLater
        (
             new Runnable()  {
                  public void run()     {
                	  this.getDamage(target); 			
      		        }

                  public void getDamage(gameObject object) {
                	//getting attacked, tile gets darker by blood
               	
                  	getBoardPanel().drawBoard(Table.get().getGameBoard());

                  }
                  
              }
        );
		*/
		}
	}
	
	public int whichWay(gameObject target) {// decide which way to go
		int i=0;
		if(this.getX()>target.getX()) {
			if(this.getY()<target.getY()) {
				i=11;//left and up
			}
			else if(this.getY()>target.getY()) {
				i=12;//left and down
			}
			else {i=10;}//left
		}
		else if(this.getX()<target.getX()) {
			if(this.getY()<target.getY()) {
				i=21;//right and up
			}
			else if(this.getY()>target.getY()) {
				i=22;//right and down
			}
			else {i=20;}//right
		}
		else {
			if(this.getY()<target.getY()) {
				i=01;//up
			}
			else if(this.getY()>target.getY()) {
				i=02;//down
			}
			else {}
		}
		return i;
	}
	public void moveLeft() {
		this.setX(this.getX()-1);
	}
	public void moveRight() {
		this.setX(this.getX()+1);
	}
	public void moveUp() {
		this.setY(this.getY()+1);
	}
	public void moveDown() {
		this.setY(this.getY()-1);
	}
	public boolean isLeftBlocked() {
		if(board.BoardManager.ENTITIES_ONBOARD[x-1][y]!=null) return true;
		else return false;
	}
	public boolean isRightBlocked() {
		if(board.BoardManager.ENTITIES_ONBOARD[x+1][y]!=null) return true;
		else return false;
	}
	public boolean isUpBlocked() {
		if(board.BoardManager.ENTITIES_ONBOARD[x][y+1]!=null) return true;
		else return false;
	}
	public boolean isDownBlocked() {
		if(board.BoardManager.ENTITIES_ONBOARD[x][y-1]!=null) return true;
		else return false;
	}
	
	
	
	//move one step toward the target
	public void move(gameObject target) {
		int oldX, oldY, newX, newY;
		oldX = this.getX(); 
		oldY = this.getY();
		int oldId = 10*oldX+oldY;
		/*
		int moveTo=whichWay(target);
		switch(moveTo) {
		case 01:{//when it has to go up
			if(isUpBlocked()) {
				if(isLeftBlocked()) {
					if(isRightBlocked()) {break;}
					else {moveRight();break;}
				}
				else {moveLeft();break;}
			}
			else {moveUp(); break;}
		}
		case 02:{//when it has to go down
			if(isDownBlocked()) {
				if(isLeftBlocked()) {
					if(isRightBlocked()) {break;}
					else {moveRight();break;}
				}
				else {moveLeft();break;}
			}
			else {moveDown(); break;}
		}
		case 10:{//when it has to go left
			if(moveTo==10) {
				if(isLeftBlocked()) {
					if(isUpBlocked()) {
						if(isDownBlocked()) {break;}
						else {moveUp();break;}
					}
					else {moveUp(); break;}
				}
			}
			else {moveLeft(); break;}
		}
		case 11:{//when it has to go left and up
			if(isLeftBlocked()&&isUpBlocked()) {break;}
			else if(isLeftBlocked()) {
				moveUp(); break;
			}
			else if(isUpBlocked()) {
				moveLeft(); break;
			}
			else {moveLeft(); moveUp(); break;}
		}
		case 12:{//when it has to go left and down
			if(isLeftBlocked()&&isDownBlocked()) {break;}
			else if(isLeftBlocked()) {
				moveUp(); break;
			}
			else if(isDownBlocked()) {
				moveLeft(); break;
			}
			else {moveLeft(); moveDown(); break;}
		}
		case 20:{//when it has to go right
			if(moveTo==10) {
				if(isRightBlocked()) {
					if(isUpBlocked()) {
						if(isDownBlocked()) {break;}
						else {moveUp();break;}
					}
					else {moveUp(); break;}
				}
			}
			else {moveRight(); break;}
		}
		case 21:{//when it has to go right and up
			if(isRightBlocked()&&isUpBlocked()) {break;}
			else if(isRightBlocked()) {
				moveUp(); break;
			}
			else if(isUpBlocked()) {
				moveLeft(); break;
			}
			else {moveRight(); moveUp(); break;}
		}
		case 22:{//when it has to go right and down
			if(isRightBlocked()&&isDownBlocked()) {break;}
			else if(isRightBlocked()) {
				moveUp(); break;
			}
			else if(isDownBlocked()) {
				moveLeft(); break;
			}
			else {moveRight(); moveDown(); break;}
		}
		}
		*/
		
		if(this.getX()>target.getX()) {
			this.setX(this.getX()-1);
		}
		else if(this.getX()<target.getX()) {
			this.setX(this.getX()+1);
		}
		else {}
		if(this.getY()>target.getY()) {
			this.setY(this.getY()-1);
		}
		else if(this.getY()<target.getY()) {
			this.setX(this.getY()+1);
		}
		else {}
		
		newX = this.getX();
		newY = this.getY();
		
		int newId = newX*10 + newY;
		BoardManager.ENTITIES_ONBOARD[oldX][oldY]=null;
		BoardManager.ENTITIES_ONBOARD[newX][newY]=this;
		
		
      	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
        SwingUtilities.invokeLater
        (
             new Runnable()  {
                  public void run()     {
                	  this.drawMove(presentObject, oldId, newId); 			

      		        }

                  public void drawMove(gameObject movingObject, int oldId, int newId) {    	
                  	Table.get().getBoardPanel().getTilePanel(oldId).removeAll();
                  	Table.get().getBoardPanel().getTilePanel(oldId).setTileObject(null);
                  	Table.get().getBoardPanel().getTilePanel(newId).assignChampion(Table.get().getGameBoard(), movingObject);
                  	Table.get().getBoardPanel().getTilePanel(newId).setTileObject(movingObject);
                  	Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());

                  }
                  
              }
        );
		*/
	}
	
	
	//a thread
	public void run() {
		
		while(this.getHealth()>0) {
			int count=0; // count number of attacks;
			gameObject target;
			target=this.searchTarget();//set a target
			if(target==null) {//stop this thread if there is no more enemy.
				break;
			}
			
			if(count==this.getSkilltime()) { //whenever it attacks certain amount of times, use the skill
				this.skill(target);
				count=0;
			}
			if(isInrange(target)) {
				attack(target);//attack the target
				count++;
				try {Thread.sleep(this.getAtktime());//rest for its attack speed
				} catch (InterruptedException e) {}
			}
			else {//move to target
				move(target);
				System.out.println(this.name+" moved to "+this.getX()+", "+this.getY());				 

				}
			
		}
		if(this.getHealth()<=0) {
			this.setAlive(false);
			this.getPlayer().setnumOfobj(this.getPlayer().getnumOfobj()-1);
			this.getPlayer().getStorage().isTaken.remove(this.getCellNumber());
		}
	}
	
	public void skill(gameObject target) {
	}

	//distinguish whether the target is in range.
	public boolean isInrange(gameObject target) {
		if(Math.abs(this.getX()-target.getX())+Math.abs(this.getY()-target.getY())<=this.getRange()) {
			return true;
		}
		else return false;
	}
	
	//getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHealth() {
		return health;
	}

	
	public void setHealth(int health) {
		this.health = health;
	}

	public void setMaxHealth(int max) {
		this.maxHealth = max;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	

	
	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isInstorage() {
		return isInstorage;
	}

	public void setInstorage(boolean isInstorage) {
		this.isInstorage = isInstorage;
	}

	public void setcellNumber(int cellNumber) {
		this.cellNumber = cellNumber;
	}

	public long getAtktime() {
		return atktime;
	}

	public void setAtktime(long atktime) {
		this.atktime = atktime;
	}

	public int getCellNumber() {
		return cellNumber;
	}

	public void setCellNumber(int cellNumber) {
		this.cellNumber = cellNumber;
	}

	public String getSkillname() {
		return skillname;
	}
	
	public void setSkillname(String skillname) {
		this.skillname = skillname;
	}

	public int getSkilltime() {
		return skilltime;
	}

	public void setSkilltime(int skilltime) {
		this.skilltime = skilltime;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
}
