/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scroll.game;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author Kurt
 */
public class Enemy {
    private double x, y;
    private int width, height;
    private final int GRAVITY = 2;
    
    public Enemy() {
        
    }
    
    
    
    private void applyGravity(ArrayList blocks) {
        boolean onsurface = false;
        Rectangle player = new Rectangle((int)x, (int)(y + height) - 1, width, 2);
        for (int i = 0; i < blocks.size(); i++) {
            Block block = (Block)blocks.get(i);
            Rectangle brect = block.getBounds();
            if (player.intersects(brect)) {
                onsurface = true;
                //jumpcount = 0;
            }
        }
        /*if (!onsurface) {
            if (upwardmomentum > 0) {
                upwardmomentum -= 1;
            } else {
                y += 2;
            }
        }*/
    }
}
