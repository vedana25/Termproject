package entity;
import board.Board;
import board.BoardManager;

public class Healer extends gameObject{
	int skilldamage; //varies along the level

	public Healer() {
		setRange(4);
		setAtktime(800);
		setSkillname("Blessing");
		setSkilltime(3);
	}

	public void skill(gameObject target) {
		try {
			gameObject friend;
			friend=searchFriendly();
			if(friend.getHealth()<friend.getMaxHealth()) {
				friend.setHealth(friend.getHealth()-skilldamage);
				Board.printOnlog(this, friend, skilldamage, this.getSkillname());
			}
		}catch(NullPointerException e) {}
		
	}
	public gameObject searchFriendly() {
		gameObject target = null;
		gameObject tempTarget= null;
		int temp, healthlowest=1000;
		try {
			for(int i=0;i<10;i++) {
				for(int j=0;j<10;j++) {
					tempTarget=BoardManager.ENTITIES_ONBOARD[i][j];
					if(tempTarget!=this &&tempTarget!=null&&(!isEnemy(this,tempTarget))&&tempTarget.getHealth()>0) {
					temp=tempTarget.getHealth();
						if(temp<healthlowest){
							healthlowest=temp;
							target=tempTarget;
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
	
}
