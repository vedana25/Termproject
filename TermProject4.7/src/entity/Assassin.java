package entity;
import board.Board;

//common properties of all warriors
public class Assassin extends gameObject{
	int skilldamage; //varies along the level

	public Assassin() {
		setRange(1);
		setAtktime(1000);
		setSkillname("Sudden Death");
		setSkilltime(3);
	}

	public void skill(gameObject target) {
		target.setHealth(target.getHealth()-skilldamage);
		
		Board.printOnlog(this, target, skilldamage, this.getSkillname());
	}
	
}
