package gui;
import static javax.swing.SwingUtilities.isLeftMouseButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import board.Board;
import board.BoardManager;
import board.Player;
import entity.gameObject;


public class RandomShop extends JFrame implements Runnable{
	
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 200);
    private static final Dimension BACK_PANEL_DIMENSION = new Dimension(600, 150);
    private static final Dimension CHAMPION_PANEL_DIMENSION = new Dimension(70, 70);
    
    private final RandomFrame randomFrame1,randomFrame2;
	static boolean isSelectFinished=false;
	
    Board board;
    private Table gameTable;
    
	public static ArrayList<gameObject> randomEntities= new ArrayList<gameObject>();//12 objects
	public static ArrayList<gameObject> randomSix= new ArrayList<gameObject>();//get 6 objects from randomEntities
	
	//constructor
    public RandomShop(Board board,Table gameTable) {
    	this.gameTable=gameTable;
        this.board=board;
        
        randomFrame1= new RandomFrame(-90,board.player1);
        randomFrame2= new RandomFrame(110,board.player2);

        setDefaultLookAndFeelDecorated(true);
    }
    
    //Thread
    public void run() {
    	initialize();
		openShop();
		while(!isSelectFinished) {
			if(randomFrame1.numOfChoice==3)randomFrame1.randomFrame.dispose();
			if(randomFrame2.numOfChoice==3)randomFrame2.randomFrame.dispose();
			if(randomFrame1.numOfChoice==3&&randomFrame2.numOfChoice==3) isSelectFinished=true;
			try {Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
	}
    public void initialize() {
    	isSelectFinished=false;
    	obtainRandom();
    	
    	randomFrame1.initialize();
    	randomSix.clear();
    	randomFrame2.initialize();
    	randomSix.clear();
    }
    
    public void openShop() {
    	this.randomFrame1.randomFrame.setVisible(true);
    	this.randomFrame2.randomFrame.setVisible(true);
    }
    
	private static void center(final JFrame frame, int i) {
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        final int w = frame.getSize().width;
        final int h = frame.getSize().height;
        final int x = (dim.width - w) / 2;
        final int y = (dim.height - h) / 2 + i;
        frame.setLocation(x, y);
    }
    public void obtainRandom() {//choose random object from shopEntity
    	int size = BoardManager.shopEntity.size();
    	
		for(int i=0;i<12;i++) {
			int item = new Random().nextInt(size); 
			randomEntities.add(BoardManager.shopEntity.get(item));
		}
    }
    
    
    //split 12 objects in randomEntities to 6 and 6
    public ArrayList<gameObject> getHalfObject(int num) {
    	randomSix.clear();
    	if(num==-90) {//for player1
    		for(int i=0;i<6;i++) {
        		randomSix.add(randomEntities.get(i));
        	}
    	}
    	else {//for player2
    		for(int i=0;i<6;i++) {
    			randomSix.add(randomEntities.get(i+6));
    		}
    	}
    	return randomSix;
    }
    
    
    //inner classes
    private class RandomFrame extends JPanel{
    	private final JFrame randomFrame;
    	private final JPanel backPanel;
    	private int numOfChoice=0;
    	
    	RandomFrame(int num,Player player){
    		
    		this.randomFrame= new JFrame("Random Champions");
    		this.randomFrame.setLayout(new BorderLayout());
			this.backPanel = new BackPanel(num, player, this);
	        this.randomFrame.add(this.backPanel, BorderLayout.CENTER);
	        this.randomFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	        this.randomFrame.setSize(OUTER_FRAME_DIMENSION);
    	    center(this.randomFrame, num);
    	}
    	public void initialize() {
    		numOfChoice=0;
    		((BackPanel) this.backPanel).initialize();
    	}
    }
    
    
	private class BackPanel extends JPanel {
		int num;
		private ArrayList<gameObject> randomSixEntities;
    	private ArrayList<ChampionPanel> championList= new ArrayList<>();
		
		BackPanel(int num, Player player,RandomFrame randomframe){
    		super(new GridLayout(1,6));
    		

			this.num=num;
			
        	for(int i=0;i<6;i++) {//don't write data in constructor!!
        		championList.add(new ChampionPanel(randomframe,num, board,player));
        		add(championList.get(i));
        	}
            setPreferredSize(BACK_PANEL_DIMENSION);
            setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 0));
            setBackground(Color.decode("#DC7633"));
            validate();
    	}
    	public void initialize() {//write new data for new round.
    		randomSixEntities = getHalfObject(num);
    		resetChampion(this.championList,this.randomSixEntities);
    	}
    	
    	//use instead of assignChampion
    	public void resetChampion(ArrayList<ChampionPanel> championList,ArrayList<gameObject> randomSixEntities) {
    		for(int i=0;i<6;i++) {
    			championList.get(i).setText(randomSixEntities.get(i).getName());
    			championList.get(i).Entity=randomSixEntities.get(i);
    		}
    	}
    }
   
    private class ChampionPanel extends JButton{
    	gameObject Entity;
        ChampionPanel(RandomFrame randomframe, int num, Board board, Player player) {
            setPreferredSize(CHAMPION_PANEL_DIMENSION);
            setBorder(BorderFactory.createMatteBorder(0, 0, 0, 7, Color.decode("#DC7633")));
            JLabel championName =new JLabel();
            add(championName);
            
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent event) {
                	if (isLeftMouseButton(event)) {
                    	//Put the selected champions in to the storage
                		int cellNumber=player.getStorage().firstEmpty();
                		player.getStorage().takeIn(cellNumber, Entity);
                		Entity.setInstorage(true);
                		if(player.equals(board.player2)) gameTable.getBoardPanel().getUpperStoragePanel(player.getStorage().firstEmpty()).assignChampion(board, Entity);
                		if(player.equals(board.player2)) gameTable.getBoardPanel().getLowerStoragePanel(player.getStorage().firstEmpty()).assignChampion(board, Entity);
    					player.getStorage().get(cellNumber).setPlayer(player);
    					randomframe.numOfChoice++;
    					BoardManager.shopEntity.remove(Entity); //delete the object from shopEntityList
                    }
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                }

                @Override
                public void mouseEntered(final MouseEvent e) {
                }

                @Override
                public void mouseReleased(final MouseEvent e) {
                }

                @Override
                public void mousePressed(final MouseEvent e) {
                }
            });
            
            validate();
        }   
    }
}
