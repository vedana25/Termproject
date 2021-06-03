package board;
//storage where players keep their own objects;
import java.util.HashMap;

import entity.gameObject;

public class Storage {
	
	public HashMap<Integer,gameObject> cell= new HashMap<>();
	//maximum number of storage cells for an player:10
	public HashMap<String, Integer> numOfObjects = new HashMap<>(); 
	public HashMap<Integer,Boolean> isTaken= new HashMap<>(); //store the information about whether the cells are already taken.
	
	public int firstEmpty() {
		for(int i=0;i<10;i++) {
			if(!isTaken.containsKey(i)) {
				return i;
			}
		}
		return 99; //when it's full
	}
	
	
	public boolean isEmpty() {
		for(gameObject e: cell.values()) {
			if(e.isInstorage()==true) {
				return false;
			}
		}
		return true;
	}
	
	//move an object from the board to the storage.
	public void takeIn(int cellNumber, gameObject select){
		cell.put(cellNumber, select);
		if(!numOfObjects.containsKey(select.getName()))numOfObjects.put(select.getName(), 1);
		else numOfObjects.put(select.getName(), (numOfObjects.get(select.getName())+1));
		select.setCellNum(cellNumber);
		select.setInstorage(true);	
		isTaken.put(cellNumber,true);
	}
	
	//move an object from the storage to the board.
	public void takeOut(int cellNumber, int x, int y) {
		isTaken.remove(cellNumber);
		cell.get(cellNumber).setX(x);
		cell.get(cellNumber).setY(y);
		cell.get(cellNumber).setInstorage(false);

	}
	
	public gameObject get(int cellNumber) {
		return cell.get(cellNumber);
	}
	
	public int getSize() {
		return cell.size();
	}
	

	
	public void print() {
		System.out.println("---------------Storage------------------------------");
		for (int num: cell.keySet()) {
		    String value = cell.get(num).getName();
		    System.out.print(value+"  ");
		}
		System.out.println();
		System.out.println("----------------------------------------------------");

	}
}
