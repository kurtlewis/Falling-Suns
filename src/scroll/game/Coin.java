/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scroll.game;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Graphics2D;
/**
 *
 * @author Kurt
 */
public class Coin {
    private Image image;
    private int x, y, width, height;
    private boolean visible;
    
    public Coin(int nx, int ny) {
        x = nx;
        y = ny;
        visible = true;
        ImageIcon ii = new ImageIcon(this.getClass().getResource("coin.png"));
        image = ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
    }
    
    public void paint(Graphics2D g2d) {
        if (visible) {
            g2d.drawImage(image, x, y, null);
        }
    }
    
    public void update() {
        if (x < - 500) {
            visible = false;
        }
    }
    
    public void scroll(int cx, int cy) {
        x += cx;
        y += cy;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
