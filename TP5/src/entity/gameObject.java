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
import runGame.runGame;

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
	private boolean isInstorage=false;
	private int cellNumber; // storage room number.
	private boolean isAlive=true;
	private boolean isSelected = false;
	private gameObject presentObject=this;

	//constructor
	public gameObject() {
		
	}
	
	
	//methods
	
	//search the nearest enemy object
	public gameObject searchTarget() {
		gameObject target = null;
		int temp, closest=1000;
		try {
			for(int i=0;i<10;i++) {
				for(int j=0;j<10;j++) {
					if(BoardManager.ENTITIES_ONBOARD[i][j]!=this && BoardManager.ENTITIES_ONBOARD[i][j]!=null&&isEnemy(this,BoardManager.ENTITIES_ONBOARD[i][j])&&BoardManager.ENTITIES_ONBOARD[i][j].getHealth()>0) {
					temp=getDistance(this,BoardManager.ENTITIES_ONBOARD[i][j]);
						if(temp<closest){
							closest=temp;
							target=BoardManager.ENTITIES_ONBOARD[i][j];
						}
						else {}
					}
				}
				}
			
		}catch(NullPointerException e){
			System.out.println("null pointer");
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
			Board.printOnlog(this,target,this.getAtk());
			
	        
		}
		else {
			target.setHealth(0);
			Board.printOnlog(this,target,this.getAtk()); //print about it on the log
			target = null;

		}
        SwingUtilities.invokeLater
        (
             new Runnable()  {
                  public void run()     {
                	  runGame.getTable().getBoardPanel().drawBoard(runGame.getTable().getGameBoard());	
              }
             }
        );
		}
	}

	
	//move one step toward the target
	public void move(gameObject target) {
		int oldX, oldY;
		boolean u=false; boolean d=false; boolean l=false; boolean r =false;
		int cnt=0; 
		boolean available = false;
		oldX = this.getX(); 
		oldY = this.getY();
		int x=oldX; int y = oldY;
		int oldId = 10*oldX+oldY;
		int newId;

	while(available==false) {
		if(!u && this.getX()>target.getX()) {
			x = this.getX()-1; // up
			u=true; cnt++;
		}
		else {
			x = this.getX()+1; //down
			d=true; cnt++;
		}
		
		if(!l && this.getY()>target.getY()) {
			y = this.getY()-1; //left
			l=true; cnt++;
		}
		else {
			y = this.getY()+1; //right
			r=true; cnt++;
		}
		
		available = this.available(x, y);
		if(!available) {
			if(cnt==2) { //cannot go (up right, up left, down right, down left) then do just up or down only
				x = oldX; y = oldY; //reset
				if(u==true) {
					x = this.getX()-1; // up
				}
				else {
					x = this.getX()+1; //down
				}
				
				available = this.available(x, y);
				if(!available) { //cannot go up or down, then do left of right only
					x = oldX; y = oldY;// reset
					if(l==true) {
						y = this.getY()-1; //left
					}
					else {
						y = this.getY()+1; //right
					}
					available = this.available(x, y);
				}
			}
			else { // cannot go up, down, left, right
				if(u==true || d==true) {
					y= oldY-1; //left
					if(!this.available(x, y)) {y=oldY+1;}
				}
				else {
					x= oldX-1; //up
					if(!this.available(x, y)) {y=oldX+1;}	
				}
				available = this.available(x, y);
			}
		}
	}
		
		if (available == true) {
			this.setX(x); this.setY(y);
			newId = 10*x+y;
			this.drawMove(presentObject, oldId, newId); 
	        SwingUtilities.invokeLater
	        (
	             new Runnable()  {
	                  public void run()     {
	                	  runGame.getTable().getBoardPanel().drawBoard(runGame.getTable().getGameBoard());	
	              }
	             }
	        );
	        try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
			
	}
	
	public boolean available(int x, int y) {
		if(runGame.getTable().getBoardPanel().getTilePanel(x*10 + y).getTileObject()==null) return true;
		else return false;
	}
	
    public void drawMove(gameObject movingObject, int oldId, int newId) {    	
  	  runGame.getTable().getBoardPanel().getTilePanel(oldId).removeAll();
  	  runGame.getTable().getBoardPanel().getTilePanel(oldId).setTileObject(null);
  	  runGame.getTable().getBoardPanel().getTilePanel(newId).assignChampion(runGame.getTable().getGameBoard(), movingObject);
  	  runGame.getTable().getBoardPanel().getTilePanel(newId).setTileObject(movingObject);
  	  //BoardManager.ENTITIES_ONBOARD[oldId/10][oldId%10]=null;
  	  //BoardManager.ENTITIES_ONBOARD[newId/10][newId%10]=this;

    }
	
    public void whereToGo(int oldId, gameObject target) {
    	
    }
    
	//a thread
	public void run() {

		while(this.getHealth()>0) {
			int count=0; // count number of attacks; //need to be changed to global variable
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
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
		}
		if(this.getHealth()<=0) {
			this.setAlive(false);
			this.getPlayer().setnumOfobj(this.getPlayer().getnumOfobj()-1);
			//this.getPlayer().getStorage().isTaken.remove(this.getCellNumber()); ?
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
	
	public void setCellNum(int cell) {
		this.cellNumber = cell;
	}
	
	public int getCellNum() {
		return this.cellNumber;
	}
	
	public void setSelected(boolean selected) {
		this.isSelected = selected;
	}
	
	public boolean getSelected() {
		return this.isSelected;
	}
	
}
