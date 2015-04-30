/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scroll.game;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Rectangle;
import java.awt.Graphics2D;

/**
 *
 * @author 14lewisku
 */
public class Block {
    private int x, y, width, height;
    
    public Block(int nx, int ny, int nwidth, int nheight) {
        x = nx;
        y = ny;
        width = nwidth;
        height = nheight;
    }
    
    public void paint(Graphics2D g2d) {
        g2d.fillRect(x, y, width, height);
    }
    
    public int getRightSide() {
        return x + width;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    public void scroll(int cx, int cy) {
        x += cx;
        y += cy;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
