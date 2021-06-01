package board;

import java.util.ArrayList;

import entity.Archer1;
import entity.Archer2;
import entity.Archer3;
import entity.Warrior1;
import entity.Warrior2;
import entity.Warrior3;
import entity.gameObject;

public class BoardManager {
	public static ArrayList<gameObject> shopEntity= new ArrayList<gameObject>();
	
	//public static final gameObject[] ENTITIES_INITIALIZATION = new gameObject[36];
    public static final gameObject[][] ENTITIES_ONBOARD = new gameObject[10][10]; // coordinate -> object

    public static void addAllEntity(Board game) {
    	
    	ArrayList<gameObject> A1= new ArrayList<gameObject>(20);
    	ArrayList<gameObject> A2= new ArrayList<gameObject>(20);
    	ArrayList<gameObject> A3= new ArrayList<gameObject>(10);
    	ArrayList<gameObject> W1= new ArrayList<gameObject>(10);
    	ArrayList<gameObject> W2= new ArrayList<gameObject>(5);
    	ArrayList<gameObject> W3= new ArrayList<gameObject>(5);
    	
    	int i;
    	for(i=0; i<20; i++) {
        	A1.add(new Archer1());
        	W1.add(new Warrior1());
    	}
    	for(i=0; i<10; i++) {
        	A2.add(new Archer2());
        	W2.add(new Warrior2());
    	}
    	for(i=0; i<5; i++) {
        	A3.add(new Archer3());
        	W3.add(new Warrior3());
    	}
    	shopEntity.addAll(A1);
    	shopEntity.addAll(A1);
    	shopEntity.addAll(A2);
    	shopEntity.addAll(A3);
    	shopEntity.addAll(W1);
    	shopEntity.addAll(W2);
    	shopEntity.addAll(W3);
    }
    
    
}
