package entity;

import board.Board;

//common properties of all archers
public class Archer extends gameObject {
	int skilldamage;
	
	public Archer() {
		setRange(3);
		setAtktime(1000);
		setSkillname("Spread Shot");
		setSkilltime(8);
	}
	
	public void skill(gameObject target) {
		target.setHealth(target.getHealth()-skilldamage);
		Board.printOnlog(this, target, skilldamage, this.getSkillname());
	}
	
}
