package main;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class SpaceInvaders extends JFrame  {
	
	//Constructor
    public SpaceInvaders() {
        initUI();
    }
    
    //Method
    private void initUI() {
        add(new Board());
        
        setTitle("Space Invaders");
        setSize(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    //Main
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
           @Override
           public void run() {
               new SpaceInvaders().setVisible(true);
           }
       });
   }
}