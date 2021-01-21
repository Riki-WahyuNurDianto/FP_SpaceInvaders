package sprite;
import javax.swing.ImageIcon;

public class Alien extends Sprite {
    private Bomb bomb;
    private final int POINT = 100;
    
    //Constructor
    public Alien(int x, int y) {
        initAlien(x, y);
    }

    private void initAlien(int x, int y) {
        this.x = x;
        this.y = y;
        
        bomb = new Bomb(x, y);
        
        var alienImg = "images/alien.png";
        var ii = new ImageIcon(alienImg);
        
        setImage(ii.getImage());
    }
    
    //Method
    public void act(int direction) {
        this.x += direction;
    }
    
    //Getter Setter
    public Bomb getBomb() {
        return bomb;
    }
    
    public int getPoint() {
    	return POINT;
    }
    
    //Inner Class
    public class Bomb extends Sprite {
        private boolean destroyed;
        public Bomb(int x, int y) {
            initBomb(x, y);
        }

        private void initBomb(int x, int y) {
            setDestroyed(true);
            
            this.x = x;
            this.y = y;
            
            var bombImg = "images/bomb.png";
            var ii = new ImageIcon(bombImg);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
            return destroyed;
        }
    }
}