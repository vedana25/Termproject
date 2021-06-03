package entity;

import board.Board;
import board.BoardManager;

//common properties of all archers
public class Archer extends gameObject {
	int skilldamage;
	
	public Archer() {
		setRange(3);
		setAtktime(1000);
		setSkillname("Spread Shot");
		setSkilltime(4);
	}
	public void skill(gameObject target) {
		gameObject target1;
		gameObject target2;
		gameObject target3;
		gameObject target4;
		try {
			if(BoardManager.ENTITIES_ONBOARD[target.getX()-1][target.getY()]!=null) {
				target1=BoardManager.ENTITIES_ONBOARD[target.getX()-1][target.getY()];
				target1.setHealth(target.getHealth()-skilldamage);
				Board.printOnlog(this, target1, skilldamage, this.getSkillname());
			}
			if(BoardManager.ENTITIES_ONBOARD[target.getX()+1][target.getY()]!=null) {
				target2=BoardManager.ENTITIES_ONBOARD[target.getX()+1][target.getY()];
				target2.setHealth(target.getHealth()-skilldamage);
				Board.printOnlog(this, target2, skilldamage, this.getSkillname());
			}
			if(BoardManager.ENTITIES_ONBOARD[target.getX()][target.getY()+1]!=null) {
				target3=BoardManager.ENTITIES_ONBOARD[target.getX()][target.getY()+1];
				target3.setHealth(target.getHealth()-skilldamage);
				Board.printOnlog(this, target3, skilldamage, this.getSkillname());
			}
			if(BoardManager.ENTITIES_ONBOARD[target.getX()][target.getY()-1]!=null) {
				target4=BoardManager.ENTITIES_ONBOARD[target.getX()][target.getY()-1];
				target4.setHealth(target.getHealth()-skilldamage);
				Board.printOnlog(this, target4, skilldamage, this.getSkillname());
			}
		}catch (ArrayIndexOutOfBoundsException e) {}
		
		target.setHealth(target.getHealth()-skilldamage);
		Board.printOnlog(this, target, skilldamage, this.getSkillname());
	}
}
