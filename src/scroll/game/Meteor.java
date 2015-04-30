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
public class Meteor {
    private double x, y, angle, imagerotate, speed;
    private int width, height;
    private boolean visible, active;
    private Random generator;
    private Image image, warningimage;
    
    public Meteor() {
        generator = new Random();
        visible = true;
        active = true;
        y = -600;
        x = generator.nextInt(1200);
        double[] tempg = new double[2];
        for (int i = 0; i < 2; i++) {
            do {
                tempg[i] = generator.nextDouble();
            } while (tempg[i] < .5);
        }
        imagerotate = generator.nextDouble() + generator.nextDouble() + generator.nextDouble();
        angle = 3.14 + tempg[0] + tempg[1];
        speed = 5;
        ImageIcon ii = new ImageIcon(this.getClass().getResource("asteroid-" + generator.nextInt(4) + ".png"));
        image = ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
        ii = new ImageIcon(this.getClass().getResource("warning.png"));
        warningimage = ii.getImage();
    }
    
    public void paint(Graphics2D g2d) {
        if (visible) {
            if (y < 0 && active) {
                g2d.drawImage(warningimage, (int)x, 15, null);
            } else {
                if (visible) {
                    g2d.rotate(-imagerotate, x + (width/2), y+(height/2));
                    g2d.drawImage(image, (int)x, (int)y, null);
                    g2d.rotate(imagerotate, x + (width/2), y + (height/2));
                }
            }
        }
        
    }
    
    public void scroll(double cx, double cy) {
        x += cx;
        y += cy;
    }
    
    public void move(ArrayList blocks) {
        x += speed/6 * Math.cos(angle);
        y -= speed * Math.sin(angle);
        for (int i = 0; i < blocks.size(); i++) {
            Block b = (Block)blocks.get(i);
            if (getBounds().intersects(b.getBounds())) {
                speed = 0;
                active = false;
                //visible = false;
            }
        }
        if (y > 1500 || x < -500) {
            visible = false;
        }
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}
