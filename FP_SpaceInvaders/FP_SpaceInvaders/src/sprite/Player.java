package sprite;

import javax.swing.ImageIcon;

import main.Commons;

import java.awt.event.KeyEvent;

public class Player extends Sprite {
    private int width;
    private int lives = Commons.PLAYER_LIVES;
    private int score = 0;

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        var playerImg = "images/player.png";
        var ii = new ImageIcon(playerImg);

        width = ii.getImage().getWidth(null);
        setImage(ii.getImage());

        int START_X = 275;
        setX(START_X);

        int START_Y = 470;
        setY(START_Y);
    }

    public void act() {
        x += dx;
        
        if (x <= 0) {
            x = 0;
        }
        if (x >= Commons.BOARD_WIDTH - 2 * width) {
            x = Commons.BOARD_WIDTH - 2 * width;
        }
    }
    
    public void setLives(int lives) {
    	this.lives = lives;
    }
    
    public int getLives() {
    	return this.lives;
    }

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_A) {
            dx = -5;
        }
        if (key == KeyEvent.VK_D) {
            dx = 5;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_A) {
            dx = 0;
        }
        if (key == KeyEvent.VK_D) {
            dx = 0;
        }
    }
}