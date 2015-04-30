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
public class Wave {
    private double x, speed;
    private Image image;
    public Wave(double speed, int dx) {
        ImageIcon ii = new ImageIcon(this.getClass().getResource("explosion test.gif"));
        image = ii.getImage();
        x = 0 - dx - image.getWidth(null);
        if (speed > 0) {
            this.speed = speed;
        } else {
            this.speed  = .01;
        }
    }
    
    public void paint(Graphics2D g2d) {
        g2d.drawImage(image, (int)x, 0, null);
        //g2d.drawRect((int)x, 0, 2, 1000);
    }
    
    public void increaseSpeed() {
        speed += .0002;
    }
    
    public int getX() {
        return (int)x + image.getWidth(null) - 50;
    }
    
    public void move() {
        x += speed;
    }
    
    public void scroll(int cx) {
        x += cx;
    }
}
