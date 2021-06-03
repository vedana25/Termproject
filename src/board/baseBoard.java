package board;

import entity.gameObject;

public class baseBoard {
	
    public static final int MAX_LINE = 71;
    
    /* Battle Board (71 Lines)
    -----------------------------------------------------------------------
    CHARACTER ATK(***) HEALTH(***)^*** | CHARACTER ATK(***) HEALTH(***)^***
    CHARACTER ATK(***) HEALTH(***)^*** | <-------- DEAD---- DEAD---------->
    CHARACTER ATK(***) HEALTH(***)^*** | CHARACTER ATK(***) HEALTH(***)^***
    -----------------------------------------------------------------------
    */
    
    public static void drawBattleBoard(gameObject[][] entities, int round) {
    	StringBuffer buff = new StringBuffer();
    	
        buff.append("---------------P1------------------------------P2--------------------");
        
        // start placing entity
        for(int i = 0; i < (round+1)*4; i++) {
            if((i % 2) == 0) {
                buff.append("\n");
            } else {
            	buff.append("\t| ");
            }
            for(int j=0;j<10;j++) {
            	if((entities[i][j]!=null)&&(entities[i][j].getHealth() != 0)) {
                	buff.append(
                            entities[i][j].getName() + " ATK(" +(Integer.toString(entities[i][j].getAtk())) 
                            + ") HEALTH(" + (Integer.toString(entities[i][j].getHealth())) +")"
                    );
                } else {
                	buff.append("<----- DEAD---- DEAD------>");
                }
            }
            
        }
        // end placing entity
        buff.append("\n");        
        buff.append("---------------------------------------------------------------------");
        
        System.out.println(buff);
    }
    	
	
}
