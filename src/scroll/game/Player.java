/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scroll.game;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
/**
 *
 * @author 14lewisku
 */
public class Player {
    private double x, y, dx, movement_speed, jump_speed;
    private int health, width, height, upwardmomentum, jumpcount;
    private Image image, heart;
    private ImageIcon ii;
    private boolean visible, jumppressed;
    private final int GRAVITY = 2;
    
    public Player() {
        x = 300;
        y = 300;
        width = 60;
        height = 130;
        movement_speed = 1.5;
        jump_speed = 3.5;
        health = 3;
        ii = new ImageIcon(this.getClass().getResource("Heart.png"));
        heart = ii.getImage();
    }


    public void paint(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.fillRect((int)x, (int)y, width, height);
        //g2d.drawImage(image, (int)x, (int)y, null);
        //draw any items that belong to him here.
        for (int i = 0; i < health; i++) {
            g2d.drawImage(heart, 850 - (35 * i), 540, null);
        }
    }
    
    public void move(ArrayList blocks) {
        if (checkMovement(blocks, new Rectangle((int)(x + dx), (int)(y - 2), width, height - 2))) {
            x += dx;
            if (x < 2) {
                x = 2;
            }
        }
        if (upwardmomentum > 0) {
            if (checkMovement(blocks, new Rectangle((int)x, (int)(y), width, 5))) {
                y -= jump_speed;
            } else {
                upwardmomentum = 0;
            }
        }
        applyGravity(blocks);
    }
    
    public int getHealth() {
        return health;
    }
    
    public void updateHealth(int change) {
        health += change;
    }
    
    private boolean checkMovement(ArrayList blocks, Rectangle player) {
        for (int i = 0; i < blocks.size(); i++) {
            Block block = (Block)blocks.get(i);
            Rectangle r = block.getBounds();
            if (r.intersects(player)) {
                return false;
            }
        }
        
        return true;
    }
    
    private void applyGravity(ArrayList blocks) {
        boolean onsurface = false;
        Rectangle player = new Rectangle((int)x, (int)(y + height) - 1, width, 2);
        for (int i = 0; i < blocks.size(); i++) {
            Block block = (Block)blocks.get(i);
            Rectangle brect = block.getBounds();
            if (player.intersects(brect)) {
                onsurface = true;
                jumpcount = 0;
            }
        }
        if (!onsurface) {
            if (upwardmomentum > 0) {
                upwardmomentum -= 1;
            } else {
                y += 2;
            }
        }
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
    
    public void scroll(double cx, double cy) {
        x += cx;
        y += cy;
    }
    
    public int getMomentum() {
        return upwardmomentum;
    }
    
    public int getX() {
        return (int)x;
    }
    
    public int getY() {
        return (int)y;
    }
    
    public void setVisisble(boolean visible) {
        this.visible = visible;
    }
    
    public void reset(double newspeed) {
        x = 300;
        y = 300;
        movement_speed = newspeed;
    }
    
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            if (jumpcount < 2 && !jumppressed) {
                upwardmomentum = 50;
                jumpcount++;
            }
            jumppressed = true;
        }
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            dx = movement_speed;
        }
        
        /*if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            dy = movement_speed;
        }*/
        
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            dx = - movement_speed;
        }
    }
    
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            jumppressed = false;
        }
        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
        
        /*if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            dy = 0;
        }*/
        
        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            dx = 0;
        }
    }
    
}
