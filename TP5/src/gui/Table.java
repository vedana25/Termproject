package gui;

import java.util.ArrayList;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import gui.Table;
import board.Battle;
import board.Board;
import board.BoardManager;
import board.BoardUtils;
import board.Player;
import entity.gameObject;


public final class Table extends JFrame{

    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(657, 640);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(200,240);
    private static final Dimension STORAGE_PANEL_DIMENSION = new Dimension(20, 20);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(20, 20);
   
    private Board battleBoard;
    private final BoardPanel boardPanel;
    private StoragePanel StoragePanel;

    private RandomShop randomFrame1;
    private RandomShop randomFrame2;
    private gameObject sourceObject;

    public int start;
    
    public static int p1 = 0;
    public static int p2 = 1;
    private static int isBattle =1;

    private Color upLightTileColor = Color.decode("#B2BABB");
    private Color upDarkTileColor = Color.decode("#CCD1D1");
    private Color downLightTileColor = Color.decode("#BFCDCA");
    private Color downDarkTileColor = Color.decode("#AAB8B8");
	
    private final JFrame gameFrame;

    public Table(Board game) {
    	this.battleBoard=game;
    	game.initialize();
		this.gameFrame = new JFrame("Auto-chess Game");
		this.gameFrame.setLayout(new BorderLayout());
		this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        setDefaultLookAndFeelDecorated(true);
        this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        center(this.gameFrame);
    	this.gameFrame.setVisible(true);
    	getBoardPanel().drawBoard(getGameBoard());
    	this.start = JOptionPane.showConfirmDialog(null, getGameBoard().popupScore(), "Start new game?", 0);
	 }
 
    
	private static void center(final JFrame frame) {
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        final int w = frame.getSize().width;
        final int h = frame.getSize().height;
        final int x = (dim.width - w) / 2;
        final int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
    }
    
    public void fight() {
    	Thread battle = new Battle(getGameBoard());
    	
    	Battle.isFinished = false;
        battle.start();
        
        while(true) {
        	
        	if(Battle.isFinished) {
        		battle.interrupt();
        		break;
        	}
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
        }
    }
    
    public class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;
        final List<StoragePanel> upperStorageTiles;
        final List<StoragePanel> lowerStorageTiles;


        BoardPanel() {
            super(new GridLayout(12,10));
            this.upperStorageTiles = new ArrayList<>();
            for(int i=0; i<10; i++) {
            	StoragePanel = new StoragePanel(battleBoard.player1, i);
                this.upperStorageTiles.add(StoragePanel);
            	add(StoragePanel);

            }
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            this.lowerStorageTiles = new ArrayList<>();

            for(int i=0; i<10; i++) {
            	StoragePanel = new StoragePanel(battleBoard.player2, i);
                this.lowerStorageTiles.add(StoragePanel);
            	add(StoragePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(Color.decode("#273746"));
            validate();
        }

        public TilePanel getTilePanel(int id) {
        	return boardTiles.get(id);
        }
        
        public void drawBoard(final Board board) {
            removeAll();
            for (final StoragePanel upperStorageTile : upperStorageTiles) {
                upperStorageTile.drawTile(board);
                add(upperStorageTile);           	
            }
            for (final TilePanel boardTile : boardTiles) {
                boardTile.drawTile(board);
                add(boardTile);
            }
            for (final StoragePanel lowerStorageTile : lowerStorageTiles) {
                lowerStorageTile.drawTile(board);
                add(lowerStorageTile);           	
            }
            revalidate();
            repaint();
        }
        
        public StoragePanel getStoragePanel(Player player, int i) {
        	if (player == battleBoard.player1) return upperStorageTiles.get(i);
        	else return lowerStorageTiles.get(i);
        }


    }
	public class StoragePanel extends JPanel{
		private final int storageId;
		private gameObject EntityObject;
		private JPanel panel;
		private Player player;

		StoragePanel(Player player, final int storageId){
			super(new GridBagLayout());
			this.storageId = storageId;
			this.panel = this;
			this.player = player;
			
			setPreferredSize(STORAGE_PANEL_DIMENSION);
			if(player == getGameBoard().player1) setBorder(BorderFactory.createMatteBorder(0, 0, 10, 0, Color.decode("#273746")));
			else setBorder(BorderFactory.createMatteBorder(10, 0, 0, 0, Color.decode("#273746")));
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent event) {
        			if(player ==battleBoard.player1) EntityObject = getGameBoard().getStorage1().get(storageId);
        			else EntityObject = getGameBoard().getStorage2().get(storageId);
                    if (isRightMouseButton(event)) {
                        sourceObject = null;
                    } else if (isLeftMouseButton(event)) {
                    	sourceObject = EntityObject;
            			System.out.println(EntityObject);

       					//Remove from Storage
       					panel.removeAll();
       					panel.revalidate();
       					panel.repaint();
       					
                        }
                    
                
	                SwingUtilities.invokeLater
	                (
	                     new Runnable()  {
	                          public void run()     {
                        	    getBoardPanel().drawBoard(getGameBoard());

	                          }
	                      }
	                );
	                
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
		
		StoragePanel get() {
			return this;
		}
		
		
        void drawTile(final Board board) {
            assignTileColor();
            validate();
            repaint();
        }
        
        void assignTileColor() {
		    if(player == battleBoard.player1 )setBackground((this.storageId)%2==0 ? Color.decode("#85929E") : Color.decode("#5D6D7E"));
		    else setBackground((this.storageId)%2!=0 ? Color.decode("#85929E") : Color.decode("#5D6D7E"));
        }
        
        public void assignChampion(final Board board, final gameObject Entity) {
        	String p;
        	if(Entity.getPlayer()==board.player1) p = "p1";
        	else p = "p2";
        	
            try{
                final BufferedImage image = ImageIO.read(new File("art/champions/"+ p + Entity.getName()+".png"));
                add(new JLabel(new ImageIcon(image)));
            } catch(final IOException e) {
                e.printStackTrace();
            }
        	
            SwingUtilities.invokeLater
            (
                 new Runnable()  {
                      public void run()     {
                	    getBoardPanel().drawBoard(getGameBoard());

                      }
                  }
            );
        	
        }
        
        void removeChampion() {
        	removeAll();
        }
        
	}
    
    public class TilePanel extends JPanel {

        private final int tileId;
        private final TilePanel tilePanel;
        private gameObject tileObject;

        TilePanel(final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            this.tilePanel = this;
            
            int row = tileId/10;
            int col = tileId%10;
            
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent event) {
        			
                    if (isRightMouseButton(event)) {
                    } else if (isLeftMouseButton(event)) {

                    	//Assign Name Label to the tile
                    	tilePanel.assignChampion(battleBoard, sourceObject);
                    	tilePanel.setTileObject(sourceObject);
                    	
                    	//Taking out the gameObject to the board
        				int cellNum = sourceObject.getCellNumber();
        				if(sourceObject.getPlayer()==battleBoard.player1) { //Player 1
        					getGameBoard().storage1.takeOut(cellNum, row, col);

        					BoardManager.ENTITIES_ONBOARD[row][col] = getGameBoard().storage1.get(cellNum);
        					BoardManager.ENTITIES_ONBOARD[row][col].setPlayer(getGameBoard().player1);//temporary code
        					BoardManager.ENTITIES_ONBOARD[row][col].setInstorage(false);   		
        					        					
        					getGameBoard().player1.setnumOfobj(getGameBoard().player1.getnumOfobj()+1);

							//If the number of gameOject on board reach the limit, set ready
        					if((Board.round + 1)*2 == p1/2+1 ||getGameBoard().storage1.isEmpty()) getGameBoard().player1.setReady(true);
        					p1=p1+2;

        				}
        				else { //Player2
        					getGameBoard().storage2.takeOut(cellNum, row, col);
        					BoardManager.ENTITIES_ONBOARD[row][col] = getGameBoard().storage2.get(cellNum);
        					BoardManager.ENTITIES_ONBOARD[row][col].setPlayer(getGameBoard().player2);//temporary code
        					BoardManager.ENTITIES_ONBOARD[row][col].setInstorage(false);
        					getGameBoard().player2.setnumOfobj(getGameBoard().player2.getnumOfobj()+1);
       						
        					if((Board.round + 1)*2 == (p2-1)/2+1 || getGameBoard().storage2.isEmpty()) getGameBoard().player2.setReady(true);
        					p2=p2+2;
        				}
                     }
                    
	                SwingUtilities.invokeLater
	                (
	                     new Runnable()  {
	                          public void run()     {
	                        	  getBoardPanel().drawBoard(getGameBoard());			
          						          					
	              		        }
	                      }
	                );
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

        void drawTile(final Board board) {
            assignTileColor();
            if(tileObject!=null)damageColor();
            validate();
            repaint();
        }
        
        
        private void assignTileColor() {
        	if(this.tileId<50) {
	        	setBackground((this.tileId/10)%2==0 &&(this.tileId % 2 == 0) ? upLightTileColor : upDarkTileColor);
	        	if ((this.tileId/10)%2!=0 &&(this.tileId % 2 != 0)) {setBackground(upLightTileColor);}
        	}
        	else {
	        	setBackground((this.tileId/10)%2==0 &&(this.tileId % 2 == 0) ? downDarkTileColor : downLightTileColor);
	        	if ((this.tileId/10)%2!=0 &&(this.tileId % 2 != 0)) {setBackground(downDarkTileColor);}     		
        	}
        }
        public void assignChampion(final Board board, final gameObject Entity) {
        	String player="p1";
        	if(Entity==null)System.out.println("null");
        	if(Entity.getPlayer()==board.player1) player = "p1";
        	else player = "p2";
        	
            try{
                final BufferedImage image = ImageIO.read(new File("art/champions/"+ player + Entity.getName()+".png"));
                add(new JLabel(new ImageIcon(image)));
            } catch(final IOException e) {
                e.printStackTrace();
            }
        	
        }
        
        public void damageColor() {
        	int damaged=0;
        	if(this.getTileObject()!=null) {
        	if(this.getTileObject().getHealth()<this.getTileObject().getMaxHealth()*0.9 && this.getTileObject().getHealth()>this.getTileObject().getMaxHealth()*0.7) {
              	damaged=1;}
            	else if(this.getTileObject().getHealth()<=this.getTileObject().getMaxHealth()*0.7 && this.getTileObject().getHealth()>this.getTileObject().getMaxHealth()*0.5) {
            	damaged=2;}
            	else if(this.getTileObject().getHealth()<=this.getTileObject().getMaxHealth()*0.5 && this.getTileObject().getHealth()>this.getTileObject().getMaxHealth()*0.3) {
            	damaged=3;}
            	else if(this.getTileObject().getHealth()<=this.getTileObject().getMaxHealth()*0.3 && this.getTileObject().getHealth()>this.getTileObject().getMaxHealth()*0) {
            	damaged=4;}
            	else if(this.getTileObject().getHealth()<=0) {
            		this.getTileObject().setAlive(false);
            		this.removeAll();
            	}
        	}
        	if(damaged==1) setBackground(Color.decode("#E6B0AA"));
        	else if(damaged==2) setBackground(Color.decode("#CD6155"));
        	else if(damaged==3) setBackground(Color.decode("#A93226"));
        	else if(damaged==4) setBackground(Color.decode("#641E16"));
        }
        
        public gameObject getTileObject() {
        	return tileObject;
        }
        public void setTileObject(gameObject currentObject) {
        	this.tileObject = currentObject;
        }
    }
    
    
    public Board getGameBoard() {
        return this.battleBoard;
    }
    
    
    public BoardPanel getBoardPanel() {
        return this.boardPanel;
    }
    
    RandomShop getRandomFrame1() {
        return this.randomFrame1;
    }
 
    RandomShop getRandomFrame2() {
        return this.randomFrame2;
    }
    
    StoragePanel getStoragePanel(){
    	return this.StoragePanel;
    }
    

    
}
