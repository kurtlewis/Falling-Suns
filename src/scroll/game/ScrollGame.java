/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scroll.game;

import javax.swing.JFrame;
import javax.swing.ImageIcon;

/**
 *
 * @author 14lewisku
 */
public class ScrollGame extends JFrame{

    /**
     * @param args the command line arguments
     */
    public ScrollGame() {
        add(new Board());
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setTitle("Falling Suns");
        setResizable(false);
        setVisible(true);
        setIconImage(new ImageIcon(this.getClass().getResource("icon.png")).getImage());
        
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        new ScrollGame();
    }
    
}
