package main;

import sound.Sound;
import sprite.Alien;
import sprite.Player;
import sprite.Shot;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import main.MenuResource;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Board extends JPanel {

    private Dimension d;
    private List<Alien> aliens;
    private Player player;
    private Shot shot;
    private int bombChance = Commons.CHANCE;

    private int direction = -1;
    private int deaths = 0;
    private boolean invasion = false;

    private boolean inGame = true;
    private String explImg = "images/explosion.gif";
    private String message = "Game Over";

    private Timer timer;
    
    private int level = 1;
    private int highScore;
    File f = new File("highscore.txt");
    
    private GameState state;
    private BufferedImage img; 
    
    //sounds
    private String alienBeam = "sounds/alienBeam.wav";
    private String bulletSound = "sounds/bulletSound.wav";
    private String damageSound = "sounds/damageSound.wav";
    private String deathSound = "sounds/deathSound.wav";
    private String hitmarkerSound = "sounds/hitmarkerSound.wav";
    private String levelUpSound = "sounds/levelUpSound.wav";
    private String gameStart = "sounds/gameStart.wav";

    public Board() {
        initBoard();
        gameInit();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        addMouseListener(new MAdapter());
        addMouseMotionListener(new MAdapter());
        setFocusable(true);
        
        d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setBackground(Color.black);

        state = GameState.MAIN_MENU;
        
        timer = new Timer(1000/Commons.FPS, new GameCycle());
        timer.start();
    }


    private void gameInit() {
        aliens = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                var alien = new Alien(Commons.ALIEN_INIT_X + 45 * j,
                        Commons.ALIEN_INIT_Y + 45 * i);
                aliens.add(alien);
            }
        }

        player = new Player();
        shot = new Shot();
    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private void doGameCycle() {
    	if(state == GameState.RUNNING) {
    		update();
    		repaint();
    	}
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.BLACK);
		g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
		
        
		if(state == GameState.RUNNING) {
			doDrawing(g);
		}
		else if(state == GameState.MAIN_MENU) {
			graphicMainMenu(g);
		}
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.white);

        if (inGame) {
//            g.drawLine(0, Commons.GROUND,
//                    Commons.BOARD_WIDTH, Commons.GROUND);
        	drawBg(g);
        	drawBombing(g);
            drawAliens(g);
           
            drawShot(g);
            drawPlayer(g);
            drawLives(g);
            drawScore(g);
            drawHighScore(g);
            drawLevel(g);
        } else {
//            if (timer.isRunning()) {
//                timer.stop();
//            }
            gameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }
    
    private void drawBg(Graphics g) {
    	g.drawImage(MenuResource.bg, 0, 0, MenuResource.bg.getWidth(), MenuResource.bg.getHeight(), null);
    }

    private void drawAliens(Graphics g) {
        for (Alien alien : aliens) {
            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {
                alien.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {
            player.die();
//            inGame = false;
        }
    }

    private void drawShot(Graphics g) {
        if (shot.isVisible()) {
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    private void drawBombing(Graphics g) {
        for (Alien a : aliens) {
            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }
    
    private void drawLives(Graphics g) {
    	int live_img_pos = 1;
//    	g.drawString("Lives:", 0, 10);
    	g.drawImage(MenuResource.bar, 18, 7, MenuResource.bar.getWidth(), MenuResource.bar.getHeight(), null);
    	for(int i = 1; i <= player.getLives(); i++) {
//    		g.drawImage(player.getImage(), live_img_pos + 18 * i, 2, this);
    		g.drawImage(MenuResource.health, live_img_pos + 28*i, 20, MenuResource.health.getWidth(), MenuResource.health.getHeight(), null);
    	}
    }
    
    private void drawScore(Graphics g) {
    	g.drawImage(MenuResource.score, 475, 7, MenuResource.score.getWidth(), MenuResource.score.getHeight(), null);
		g.setColor(new Color(0, 147, 201));
		g.setFont(new Font("Arial", Font.PLAIN, 12));
    	g.drawString("SCORE: " + player.getScore(), 475, 30);
    }
    
    private void drawHighScore(Graphics g) {
    	g.drawImage(MenuResource.hg, 250, 6, MenuResource.hg.getWidth(), MenuResource.hg.getHeight(), null);
    	g.setColor(new Color(0, 147, 201));
		g.setFont(new Font("Arial", Font.BOLD, 15));
    	g.drawString(" " + highScore, 267, 37);
    }
    
    private void drawLevel(Graphics g) {
    	g.drawImage(MenuResource.lv, 265, 530, MenuResource.lv.getWidth()/17, MenuResource.lv.getHeight()/18, null);
    	g.setColor(new Color(0, 0, 0));
		g.setFont(new Font("Arial", Font.BOLD, 12));
    	g.drawString("LEVEL: " + level, 280, 547);
    }

    private void gameOver(Graphics g) {
//        g.setColor(Color.black);
//        g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
//
//        g.setColor(new Color(0, 32, 48));
//        g.fillRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
//        g.setColor(Color.white);
//        g.drawRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
//
//        var small = new Font("Helvetica", Font.BOLD, 14);
//        var fontMetrics = this.getFontMetrics(small);
//
//        g.setColor(Color.white);
//        g.setFont(small);
//        g.drawString(message, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
//                Commons.BOARD_WIDTH / 2);
    	state = GameState.MAIN_MENU;
    	graphicMainMenu(g);
    }

    private void update() {
    	//naik level jika semua alien sudah dibunuh
        if (deaths == Commons.NUMBER_OF_ALIENS_TO_DESTROY) {
//          inGame = false;
//          timer.stop();
//          message = "Game won!";
        	
			deaths = 0;
			level++;
			direction = Math.abs(direction) + 1;
			bombChance = bombChance + 1;
			int oldScore = player.getScore();
			gameInit();
			player.setScore(oldScore);
            new Sound(levelUpSound).play();
        }
        
      //Ends game if player runs out of lives
        if (player.getLives() == 0 || invasion == true) {
			// Gives the player an option to play again or exit
			int answer = JOptionPane.showConfirmDialog(null, "Would you like to play again?",
			  "You lost the game with " + player.getScore() + " points", 0);
			// If they choose to play again, this resets every element in the game
			if (answer == 0) {
				restart();
			    new Sound(gameStart).play();
			}
			// If they choose not to play again, it closes the game
			if (answer == 1) {
			    inGame = false;
			}
        }
        
        //highscore
        try {
            Scanner fileScan = new Scanner(f);
            while (fileScan.hasNextInt()) {
                String nextLine = fileScan.nextLine();
                Scanner lineScan = new Scanner(nextLine);
                highScore = lineScan.nextInt();
            }
        } catch (FileNotFoundException e) {
        }
        
        //update highscore
        try {
            if (player.getScore() > highScore) {
                String scoreString = Integer.toString(player.getScore());
                PrintWriter pw = new PrintWriter(new FileOutputStream(f, false));
                pw.write(scoreString);
                pw.close();
            }
        } catch (FileNotFoundException e) {
        }

        // player
        player.act();

        // shot
        if (shot.isVisible()) {
            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Alien alien : aliens) {
                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + Commons.ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + Commons.ALIEN_HEIGHT)) {
                        var ii = new ImageIcon(explImg);
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        deaths++;
                        shot.die();
                        player.setScore(player.getScore() + alien.getPoint());
                        new Sound(hitmarkerSound).play();
                    }
                }
            }

            int y = shot.getY();
            y -= Commons.PLAYER_SHOOT_SPEED;

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        // aliens
        for (Alien alien : aliens) {
            int x = alien.getX();

            if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT && direction > 0) {
                direction = -direction;
                Iterator<Alien> i1 = aliens.iterator();
                while (i1.hasNext()) {
                    Alien a2 = i1.next();
                    a2.setY(a2.getY() + Commons.GO_DOWN);
                }
            }

            if (x <= Commons.BORDER_LEFT && direction < 0) {
                direction = -direction;
                Iterator<Alien> i2 = aliens.iterator();
                while (i2.hasNext()) {
                    Alien a = i2.next();
                    a.setY(a.getY() + Commons.GO_DOWN);
                }
            }
        }

        Iterator<Alien> it = aliens.iterator();
        while (it.hasNext()) {
            Alien alien = it.next();
            if (alien.isVisible()) {
                int y = alien.getY();
                if (y > Commons.GROUND - Commons.ALIEN_HEIGHT) {
//                	inGame = false;
//                	message = "Invasion!";
                	invasion = true;
                	new Sound(deathSound).play();
                }
            	alien.act(direction);
            }
        }

        // bombs ke player
        var generator = new Random();
        for (Alien alien : aliens) {
            int shot = generator.nextInt(3000);
            Alien.Bomb bomb = alien.getBomb();
            if (shot <= bombChance && alien.isVisible() && bomb.isDestroyed()) {
                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
                new Sound(alienBeam).play();
            }

            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = player.getX();
            int playerY = player.getY();
            
            //jika terkena tembakan
            if (player.isVisible() && !bomb.isDestroyed()) {
                if (bombX >= (playerX)
                        && bombX <= (playerX + Commons.PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + Commons.PLAYER_HEIGHT)) {
                    player.setLives(player.getLives() - 1);
                    if(player.getLives() == 0) {
                    	var ii = new ImageIcon(explImg);
                        player.setImage(ii.getImage());
                    	player.setDying(true);
                    	new Sound(deathSound).play();
                    }
                    bomb.setDestroyed(true);
                    new Sound(damageSound).play();
                }
            }

            if (!bomb.isDestroyed()) {
                bomb.setY(bomb.getY() + Commons.ALIEN_BOMB_SPEED);
                if (bomb.getY() >= Commons.GROUND - Commons.BOMB_HEIGHT) {
                    bomb.setDestroyed(true);
                }
            }
        }
    }
    
    public void restart() {
    	if (!timer.isRunning())
    		timer.start();
		inGame = true;
	    aliens.clear();
		invasion = false;
	    bombChance = Commons.CHANCE;
	    direction = -1;
	    level = 1;
	    deaths = 0;
	    gameInit();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
            int x = player.getX();
            int y = player.getY();
            int key = e.getKeyCode();
    		if(state == GameState.RUNNING) {

    			if (key == KeyEvent.VK_SPACE) {
    				if (inGame) {
    					if (!shot.isVisible()) {
    						shot = new Shot(x, y);
                            new Sound(bulletSound).play();
    					}
    				}
    			}
    		}
    		else if(state == GameState.MAIN_MENU) {
    			switch(e.getKeyCode()) {
    			case KeyEvent.VK_ENTER:
    				state = GameState.RUNNING;
    				break;
    			case KeyEvent.VK_P:
    				state = GameState.RUNNING;
    				if(!inGame) {
    					restart();
    				}
    			    new Sound(gameStart).play();
    				break;	
    			case KeyEvent.VK_E:
    				System.exit(0);
    				break;
    			}
    		}
        }
    }
    
    public static boolean playHover = false;
    public static boolean exitHover = false;
    private String buttonHover = "sounds/buttonHover.wav";
    
    private class MAdapter extends MouseAdapter {
    	public void mouseClicked(MouseEvent e) {
    		int eX = e.getX();
    		int eY = e.getY();
    		if(eX >= 200 && eX <= MenuResource.play_img.getWidth() + 200 &&
    		   eY >= 280 && eY <= MenuResource.play_img.getHeight() + 280 &&
    		   state == GameState.MAIN_MENU) {
    			state = GameState.RUNNING;
				if(!inGame) {
					restart();
				}
			    new Sound(gameStart).play();
    		} else if (eX >= 200 && eX <= MenuResource.exit_img.getWidth() + 200 &&
 	    		   eY >= 370 && eY <= MenuResource.exit_img.getHeight() + 370 &&
 	    		   state == GameState.MAIN_MENU) {
	 			System.exit(0);
    		}
    	}
    	
    	public void mouseMoved(MouseEvent e) {
    		int eX = e.getX();
    		int eY = e.getY();
    		if(eX >= 200 && eX <= MenuResource.play_img.getWidth() + 200 &&
    		   eY >= 280 && eY <= MenuResource.play_img.getHeight() + 280 &&
    		   state == GameState.MAIN_MENU) {
    			if(!playHover)
    				new Sound(buttonHover).play();
    			playHover = true;
    			exitHover = false;
    			setCursor(new Cursor(Cursor.HAND_CURSOR));
//    			System.out.println("Mouse Masuk Play");
    			repaint();
    		} else if (eX >= 200 && eX <= MenuResource.exit_img.getWidth() + 200 &&
    	    		   eY >= 370 && eY <= MenuResource.exit_img.getHeight() + 370 &&
    	    		   state == GameState.MAIN_MENU) {
    			if(!exitHover)
    				new Sound(buttonHover).play();
    			exitHover = true;
    			playHover = false;
    			setCursor(new Cursor(Cursor.HAND_CURSOR));
//    			System.out.println("Mouse Masuk Exit");
    			repaint();
    		} else {
    			playHover = false;
    			exitHover = false;
    			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//    			System.out.println("Mouse Keluar Play & Exit: " + eX + ", " + eY);
    			repaint();
    		}
    	}
    }
    
	private static void graphicMainMenu(Graphics g) {
		//Title
		g.drawImage(MenuResource.bg, 0, 0, MenuResource.bg.getWidth(), MenuResource.bg.getHeight(), null);
		g.drawImage(MenuResource.logo_img, 160, 70, MenuResource.logo_img.getWidth(), MenuResource.logo_img.getHeight(), null);
		if(playHover) {
			g.drawImage(MenuResource.play_imgHvr, 200, 280, MenuResource.play_imgHvr.getWidth(), MenuResource.play_imgHvr.getHeight(), null);
		} else {
			g.drawImage(MenuResource.play_img, 200, 280, MenuResource.play_img.getWidth(), MenuResource.play_img.getHeight(), null);
		}
		if(exitHover) {
			g.drawImage(MenuResource.exit_imgHvr, 200, 370, MenuResource.exit_imgHvr.getWidth(), MenuResource.exit_imgHvr.getHeight(), null);
		} else {
			g.drawImage(MenuResource.exit_img, 200, 370, MenuResource.exit_img.getWidth(), MenuResource.exit_img.getHeight(), null);
		}
		g.drawImage(MenuResource.controls_img, 20, 490, MenuResource.controls_img.getWidth(), MenuResource.controls_img.getHeight(), null);
	}
}