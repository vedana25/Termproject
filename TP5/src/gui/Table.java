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

import javax.swing.*;

import gui.Table;
import board.Battle;
import board.Board;
import board.BoardManager;
import board.BoardUtils;
import entity.gameObject;


public final class Table extends JFrame{

    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(657, 640);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(200,240);
    private static final Dimension STORAGE_PANEL_DIMENSION = new Dimension(20, 20);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(20, 20);
   
    private Board battleBoard;
    private final BoardPanel boardPanel;
    private UpperStoragePanel upperStoragePanel;
    private LowerStoragePanel lowerStoragePanel;

    private RandomShop randomFrame1;
    private RandomShop randomFrame2;
    private gameObject sourceObject;
    //private String movingObjectName;
    public int start;
    
    public static int p1 = 0;
    public static int p2 = 1;
    private static int isBattle =1;

    private Color upLightTileColor = Color.decode("#B2BABB");
    private Color upDarkTileColor = Color.decode("#CCD1D1");
    private Color downLightTileColor = Color.decode("#BFCDCA");
    private Color downDarkTileColor = Color.decode("#AAB8B8");
    
    //private gameObject sourchObjectName;
	
    private final JFrame gameFrame;

    public Table(Board game) {
    	this.battleBoard=game;
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

    private JFrame getGameFrame() {
        return this.gameFrame;
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
    
    UpperStoragePanel getUpperStoragePanel(){
    	return this.upperStoragePanel;
    }
    
    LowerStoragePanel getLowerStoragePanel() {
    	return this.lowerStoragePanel;
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
        final List<UpperStoragePanel> upperStorageTiles;
        final List<LowerStoragePanel> lowerStorageTiles;


        BoardPanel() {
            super(new GridLayout(12,10));
            this.upperStorageTiles = new ArrayList<>();
            for(int i=0; i<10; i++) {
            	upperStoragePanel = new UpperStoragePanel(i);
                this.upperStorageTiles.add(upperStoragePanel);
            	add(upperStoragePanel);

            }
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            this.lowerStorageTiles = new ArrayList<>();
            for(int i=0; i<10; i++) {
            	lowerStoragePanel = new LowerStoragePanel(i);
                this.lowerStorageTiles.add(lowerStoragePanel);
            	add(lowerStoragePanel);
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
            for (final UpperStoragePanel upperStorageTile : upperStorageTiles) {
                upperStorageTile.drawTile(board);
                add(upperStorageTile);           	
            }
            for (final TilePanel boardTile : boardTiles) {
                boardTile.drawTile(board);
                add(boardTile);
            }
            for (final LowerStoragePanel lowerStorageTile : lowerStorageTiles) {
                lowerStorageTile.drawTile(board);
                add(lowerStorageTile);           	
            }
            revalidate();
            repaint();
        }
        
        public UpperStoragePanel getUpperStoragePanel(int i) {
        	return upperStorageTiles.get(i);
        }
        
        public LowerStoragePanel getLowerStoragePanel(int i) {
        	return lowerStorageTiles.get(i);
        }

    }
	public class UpperStoragePanel extends JPanel{
		private final int storageId;
		private gameObject EntityObject;
		private JPanel panel;

		UpperStoragePanel(final int storageId){
			super(new GridBagLayout());
			this.storageId = storageId;
			this.panel = this;
			
			setPreferredSize(STORAGE_PANEL_DIMENSION);
            setBorder(BorderFactory.createMatteBorder(0, 0, 10, 0, Color.decode("#273746")));
            
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent event) {
        			EntityObject = getGameBoard().getStorage1().get(storageId);
        			
                    if (isRightMouseButton(event)) {
                        sourceObject = null;
                    } else if (isLeftMouseButton(event)) {
                    	sourceObject = EntityObject;
                    	
                    	//int x, int y;
                    	
                    	
                    	//Take out from storage to board - Setting isInStorage
       					//getGameBoard().getStorage1().takeOut(getGameBoard().getP1choice(), x, y);
    					
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
		
		UpperStoragePanel get() {
			return this;
		}
		
		
        void drawTile(final Board board) {
            assignTileColor();
            validate();
            repaint();
        }
        
        void assignTileColor() {
		    setBackground((this.storageId)%2==0 ? Color.decode("#85929E") : Color.decode("#5D6D7E"));
        }
        
        public void assignChampion(final Board board, final gameObject Entity) {
        	JLabel newLabel = new JLabel(Entity.getName());
        	add(newLabel);
        }
        
        void removeChampion() {
        	removeAll();
        }
        
	}
	
	public class LowerStoragePanel extends JPanel{
		private final int storageId;
		private gameObject EntityObject;
		private JPanel panel;
		
		LowerStoragePanel(final int storageId){
			super(new GridBagLayout());
			this.storageId = storageId;
			this.panel = this;
			setPreferredSize(STORAGE_PANEL_DIMENSION);
            setBorder(BorderFactory.createMatteBorder(10, 0, 0, 0, Color.decode("#273746")));
            
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent event) {
        			EntityObject = getGameBoard().getStorage2().get(storageId);
        			
                    if (isRightMouseButton(event)) {
                        sourceObject = null;
                    } else if (isLeftMouseButton(event)) {
                    	sourceObject = EntityObject;
    					
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
        void drawTile(final Board board) {
            assignTileColor();
            validate();
            repaint();
        }
        
        void assignTileColor() {
		    setBackground((this.storageId)%2!=0 ? Color.decode("#85929E") : Color.decode("#5D6D7E"));
        }
        
        public void assignChampion(final Board board, final gameObject Entity) {
        	JLabel newLabel = new JLabel(Entity.getName());
        	newLabel.setForeground(Color.WHITE);
        	add(newLabel);
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
        					if((Board.round + 1)*2 == p1/2+1) getGameBoard().player1.setReady(true);
        					p1=p1+2;

        				}
        				else { //Player2
        					getGameBoard().storage2.takeOut(cellNum, row, col);
        					BoardManager.ENTITIES_ONBOARD[row][col] = getGameBoard().storage2.get(cellNum);
        					BoardManager.ENTITIES_ONBOARD[row][col].setPlayer(getGameBoard().player2);//temporary code
        					BoardManager.ENTITIES_ONBOARD[row][col].setInstorage(false);
        					getGameBoard().player2.setnumOfobj(getGameBoard().player2.getnumOfobj()+1);
       							        					
        					if((Board.round + 1)*2 == (p2-1)/2+1) getGameBoard().player2.setReady(true);
        					p2=p2+2;
        				}
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

        void drawTile(final Board board) {
            assignTileColor();
            damageColor();
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
        	JLabel newLabel = new JLabel(Entity.getName());
        	if(Entity.getPlayer()==battleBoard.player2) newLabel.setForeground(Color.WHITE);
        	add(newLabel);
        }
        
        public void damageColor() {
        	int damaged=0;
        	if(this.getTileObject()!=null) {
        	if(this.getTileObject().getHealth()<this.getTileObject().getMaxHealth()*0.9 && this.getTileObject().getHealth()>this.getTileObject().getMaxHealth()*0.7) {
              	damaged=1;}
            	else if(this.getTileObject().getHealth()<=this.getTileObject().getMaxHealth()*0.7 && this.getTileObject().getHealth()>this.getTileObject().getMaxHealth()*0.4) {
            	damaged=2;}
            	else if(this.getTileObject().getHealth()<=this.getTileObject().getMaxHealth()*0.4 && this.getTileObject().getHealth()>this.getTileObject().getMaxHealth()*0) {
            	damaged=3;}
            	else if(this.getTileObject().getHealth()==0) {
            	damaged=4;}
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
   
    
}
