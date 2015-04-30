/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scroll.game;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
/**
 *
 * @author 14lewisku
 */
public class Board extends JPanel{
    private Timer maintimer;
    private TimerTask maintimertask;
    private int B_WIDTH, B_HEIGHT, num_coins, num_meteors, days_remain, distance, speed_level, time_level, freeze_level;
    private final int WIDTH = 875, HEIGHT = 560, FINISH_DISTANCE = 30000;
    private boolean ingame, paused, startup, victory, debug, menuactive;
    private Player player;
    private ArrayList blocks, meteors, coins;
    private Random generator;
    private int groundheight;
    private Wave wave;
    private Font small, menu;
    private Image freezemenu, timemenu, healthmenu, speedmenu, startmenu, startupimage, backgroundimage;
    private Rectangle freezemenuRect, timemenuRect, healthmenuRect,speedmenuRect, startmenuRect;
    
    public Board() {
        addKeyListener(new TAdapter());
        addMouseListener(new MAdapter());
        setFocusable(true);
        setBackground(Color.WHITE);
        setDoubleBuffered(true);
        
        small = new Font("Helvetica", Font.BOLD, 14);
        menu = new Font("Helvetica", Font.BOLD, 20);
        
        setSize(900, 600);
        resetGameState();
        resetTimer();
        //20, 240, 460, 680
        generator = new Random();
        ImageIcon ii = new ImageIcon(this.getClass().getResource("Freeze Wave Menu.png"));
        freezemenu = ii.getImage();
        freezemenuRect = new Rectangle(20, 125, freezemenu.getWidth(null), freezemenu.getHeight(null));
        ii = new ImageIcon(this.getClass().getResource("Slow Time Menu.png"));
        timemenu = ii.getImage();
        timemenuRect = new Rectangle(240, 125, timemenu.getWidth(null), timemenu.getHeight(null));
        ii = new ImageIcon(this.getClass().getResource("Speed Menu.png"));
        speedmenu = ii.getImage();
        speedmenuRect = new Rectangle(460, 125, speedmenu.getWidth(null), speedmenu.getHeight(null));
        ii = new ImageIcon(this.getClass().getResource("Health Menu.png"));
        healthmenu = ii.getImage();
        healthmenuRect = new Rectangle(680, 125, healthmenu.getWidth(null), healthmenu.getHeight(null));
        ii = new ImageIcon(this.getClass().getResource("Start Menu.png"));
        startmenu = ii.getImage();
        startmenuRect = new Rectangle(680, 450, startmenu.getWidth(null), startmenu.getHeight(null));
        ii = new ImageIcon(this.getClass().getResource("startup.png"));
        startupimage = ii.getImage();
        ii = new ImageIcon(this.getClass().getResource("background.png"));
        backgroundimage = ii.getImage();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        B_WIDTH = getWidth();
        B_HEIGHT = getHeight();
    }
    
    private void resetTimer() {
        maintimer = new Timer();
        maintimer.scheduleAtFixedRate(maintimertask = new MainTask(), 100, 10);
    }
    
    private void resetGameState() {
        ingame = true;
        victory = false;
        player = new Player();
        blocks = new ArrayList();
        meteors = new ArrayList();
        coins = new ArrayList();
        distance = 0;
        groundheight = 500;
        speed_level = 0;
        time_level = 0;
        freeze_level = 0;
        wave = new Wave(.75, 200);
        days_remain = 20;
        //initial blocks for start of game.8
        addSection(-1);
        meteors.add(new Meteor());
        startup = true;
        menuactive = true;
        paused = true;
    }
    
    private void createNewDay() {
        player.reset((1.5 + (double)(speed_level * .1)));
        player.updateHealth(1);
        days_remain--;
        distance = 0;
        wave = new Wave((.75 - (time_level * .05)), 200 + (25 * freeze_level));
        if (days_remain <= 0) {
            ingame = false;
        }
        blocks = new ArrayList();  
        meteors = new ArrayList();
        coins = new ArrayList();
        groundheight = 500;
        addSection(-1);
    }
    
    private void addSection(int num) {
        switch (num) {
            case -2:
                blocks.add(new Block(0, groundheight, WIDTH * 5, 15));
                break;
            case -1:
                blocks.add(new Block(-WIDTH, groundheight, WIDTH * 2, 40));
                blocks.add(new Block(0, -200, 550, 440));
                blocks.add(new Block(0, 240, 190, 280));
                blocks.add(new Block(510, 140, 40, 200));
                break;
            case 0:
                //case 0 is always random, with solid floor.
                blocks.add(new Block(WIDTH, groundheight, WIDTH, 40));
                //add random blocks here
                blocks.add(new Block(WIDTH + generator.nextInt(300) + 100, groundheight - generator.nextInt(280) - 40, generator.nextInt(300) + 50, generator.nextInt(200)+ 40));
                break;
            case 1:
                blocks.add(new Block(WIDTH, groundheight, 350, 40));
                blocks.add(new Block(WIDTH + 450, groundheight, WIDTH - 450, 40));
                break;
            case 2:
                blocks.add(new Block(WIDTH, groundheight, WIDTH, 40));
                blocks.add(new Block(WIDTH + 150, groundheight - 200, 200, 200));
                blocks.add(new Block(WIDTH + 450, groundheight - 600, 300, 350));
                blocks.add(new Block(WIDTH + 447, groundheight - 450, 15, 5));
                coins.add(new Coin(WIDTH + 450, groundheight - 650));
                coins.add(new Coin(WIDTH + 450, groundheight - 700));
                coins.add(new Coin(WIDTH + 450, groundheight - 750));
                coins.add(new Coin(WIDTH + 500, groundheight - 650));
                coins.add(new Coin(WIDTH + 500, groundheight - 700));
                coins.add(new Coin(WIDTH + 500, groundheight - 750));
                coins.add(new Coin(WIDTH + 550, groundheight - 650));
                coins.add(new Coin(WIDTH + 550, groundheight - 700));
                coins.add(new Coin(WIDTH + 550, groundheight - 750));
                coins.add(new Coin(WIDTH + 600, groundheight - 650));
                coins.add(new Coin(WIDTH + 600, groundheight - 700));
                coins.add(new Coin(WIDTH + 600, groundheight - 750));
                coins.add(new Coin(WIDTH + 650, groundheight - 650));
                coins.add(new Coin(WIDTH + 650, groundheight - 700));
                coins.add(new Coin(WIDTH + 650, groundheight - 750));
                coins.add(new Coin(WIDTH + 700, groundheight - 650));
                coins.add(new Coin(WIDTH + 700, groundheight - 700));
                coins.add(new Coin(WIDTH + 700, groundheight - 750));
                
                break;
            case 3:
                blocks.add(new Block(WIDTH, groundheight, 350, 40));
                blocks.add(new Block(WIDTH + 450, groundheight, WIDTH - 450, 40));
                blocks.add(new Block(WIDTH + 150, groundheight - 40, 200, 40));
                blocks.add(new Block(WIDTH + 450, groundheight - 40, 200, 40));
                blocks.add(new Block(WIDTH + 200, groundheight - 80, 150, 40));
                blocks.add(new Block(WIDTH + 450, groundheight - 80, 150, 40));
                blocks.add(new Block(WIDTH + 250, groundheight - 120, 100, 40));
                blocks.add(new Block(WIDTH + 450, groundheight - 120, 100, 40));
                blocks.add(new Block(WIDTH + 300, groundheight - 160, 50, 40));
                blocks.add(new Block(WIDTH + 450, groundheight - 160, 50, 40));
                break;
            case 4:
                blocks.add(new Block(WIDTH, groundheight, WIDTH, 40));
                blocks.add(new Block(WIDTH + 50, groundheight - 200, 500, 40));
                coins.add(new Coin(WIDTH + 50, groundheight - 250));
                coins.add(new Coin(WIDTH + 100, groundheight - 300));
                coins.add(new Coin(WIDTH + 150, groundheight - 250));
                coins.add(new Coin(WIDTH + 200, groundheight - 300));
                coins.add(new Coin(WIDTH + 250, groundheight - 250));
                coins.add(new Coin(WIDTH + 300, groundheight - 300));
                coins.add(new Coin(WIDTH + 350, groundheight - 250));
                coins.add(new Coin(WIDTH + 400, groundheight - 300));
                coins.add(new Coin(WIDTH + 450, groundheight - 250));
                coins.add(new Coin(WIDTH + 500, groundheight - 300));
                break;
            case 5:
                blocks.add(new Block(WIDTH, groundheight, 50, 40));
                blocks.add(new Block(WIDTH + 200, groundheight, 50, 40));
                blocks.add(new Block(WIDTH + 430, groundheight, 80, 40));
                coins.add(new Coin(WIDTH + 200, groundheight - 50));
                coins.add(new Coin(WIDTH+ 150, groundheight - 90));
                coins.add(new Coin(WIDTH + 100, groundheight - 130));
                coins.add(new Coin(WIDTH + 50, groundheight - 90));
                coins.add(new Coin(WIDTH, groundheight - 50));
                coins.add(new Coin(WIDTH + 250, groundheight - 90));
                coins.add(new Coin(WIDTH + 300, groundheight - 130));
                coins.add(new Coin(WIDTH + 350, groundheight - 90));
                coins.add(new Coin(WIDTH + 400, groundheight - 50));
                break;
            case 6: 
                blocks.add(new Block(WIDTH, groundheight, WIDTH, 40));
                blocks.add(new Block(WIDTH + 125, groundheight - 250, 500, 40));
                blocks.add(new Block(WIDTH + 625, groundheight - 250, 50, 250));
                coins.add(new Coin(WIDTH + 575, groundheight - 200));
                coins.add(new Coin(WIDTH + 575, groundheight - 150));
                coins.add(new Coin(WIDTH + 575, groundheight - 100));
                coins.add(new Coin(WIDTH + 575, groundheight - 50));
                coins.add(new Coin(WIDTH + 525, groundheight - 200));
                coins.add(new Coin(WIDTH + 525, groundheight - 150));
                coins.add(new Coin(WIDTH + 525, groundheight - 100));
                coins.add(new Coin(WIDTH + 525, groundheight - 50));
                coins.add(new Coin(WIDTH + 475, groundheight - 200));
                coins.add(new Coin(WIDTH + 475, groundheight - 150));
                coins.add(new Coin(WIDTH + 475, groundheight - 100));
                coins.add(new Coin(WIDTH + 475, groundheight - 50));
                coins.add(new Coin(WIDTH + 425, groundheight - 200));
                coins.add(new Coin(WIDTH + 425, groundheight - 150));
                coins.add(new Coin(WIDTH + 425, groundheight - 100));
                coins.add(new Coin(WIDTH + 425, groundheight - 50));
                break;
            case 7:
                blocks.add(new Block(WIDTH, groundheight, 100, 40));
                blocks.add(new Block(WIDTH + 100, groundheight - 200, 50, 240));
                blocks.add(new Block(WIDTH + 275, groundheight - 400, 50, 440));
                blocks.add(new Block(WIDTH + 450, groundheight - 600, 50, 640));
                blocks.add(new Block(WIDTH * 2, groundheight, 50, 40));
                break;
            case 8:
                blocks.add(new Block(WIDTH, groundheight, WIDTH, 40));
                blocks.add(new Block(WIDTH + 75, groundheight - 250, 40, 250));
                blocks.add(new Block(WIDTH + 190, groundheight - 350, 150, 200));
                blocks.add(new Block(WIDTH + 415, groundheight - 250, 40, 250));
                coins.add(new Coin(WIDTH + 215, groundheight - 100));
                coins.add(new Coin(WIDTH + 215, groundheight - 50));
                coins.add(new Coin(WIDTH + 265, groundheight - 100));
                coins.add(new Coin(WIDTH + 265, groundheight - 50));
                break;
            case 9:
                blocks.add(new Block(WIDTH, groundheight, WIDTH + (int)(WIDTH/2), 40));
                blocks.add(new Block((WIDTH * 3) - (int)((WIDTH/2) - 100), groundheight, (int)(WIDTH/2 - 100), 40));
                blocks.add(new Block(WIDTH + 100, groundheight - 200, WIDTH + (int)(WIDTH/2)- 100, 50));
                //blocks.add(new Block(WIDTH + 150, groundheight - 400, WIDTH + (int)(WIDTH/2) - 150, 200));
                blocks.add(new Block(WIDTH + 200, groundheight - 400, WIDTH + (int)(WIDTH/2) - 200, 50));
                blocks.add(new Block(WIDTH + 250, groundheight - 600, WIDTH + (int)(WIDTH/2) - 250, 50));
                blocks.add(new Block(WIDTH + 250, groundheight - 1000, WIDTH + (int)(WIDTH/2) - 100, 400));
                //blocks.add(new Block((WIDTH * 2) + (int)(WIDTH/2) - 40, groundheight - 200, 40, 200));
                blocks.add(new Block((WIDTH * 2) + (int)(WIDTH/2) + 100, groundheight - 1000, 250, 750));
                //blocks.add(new Block((WIDTH * 2)))
                coins.add(new Coin((WIDTH * 2) + (int)(WIDTH/2) + 5, groundheight - 600));
                coins.add(new Coin((WIDTH * 2) + (int)(WIDTH/2) + 55, groundheight - 600));
                
                coins.add(new Coin((WIDTH * 2), groundheight - 150));
                coins.add(new Coin((WIDTH * 2), groundheight - 100));
                coins.add(new Coin((WIDTH * 2), groundheight - 50));
                coins.add(new Coin((WIDTH * 2) + 50, groundheight - 150));
                coins.add(new Coin((WIDTH * 2) + 50, groundheight - 100));
                coins.add(new Coin((WIDTH * 2) + 50, groundheight - 50));
                coins.add(new Coin((WIDTH * 2) + 100, groundheight - 150));
                coins.add(new Coin((WIDTH * 2) + 100, groundheight - 100));
                coins.add(new Coin((WIDTH * 2) + 100, groundheight - 50));
                coins.add(new Coin((WIDTH * 2) + 150, groundheight - 150));
                coins.add(new Coin((WIDTH * 2) + 150, groundheight - 100));
                coins.add(new Coin((WIDTH * 2) + 150, groundheight - 50));
                break;
            case 10:
                blocks.add(new Block(WIDTH, groundheight, WIDTH + (int)(WIDTH/2), 40));
                blocks.add(new Block((WIDTH * 3) - (int)((WIDTH/2) - 100), groundheight, (int)(WIDTH/2 - 100), 40));
                blocks.add(new Block(WIDTH + 100, groundheight - 200, WIDTH + (int)(WIDTH/2)- 100, 50));
                //blocks.add(new Block(WIDTH + 150, groundheight - 400, WIDTH + (int)(WIDTH/2) - 150, 200));
                blocks.add(new Block(WIDTH + 200, groundheight - 400, WIDTH + (int)(WIDTH/2) - 200, 50));
                blocks.add(new Block(WIDTH + 250, groundheight - 600, WIDTH + (int)(WIDTH/2) - 250, 50));
                blocks.add(new Block(WIDTH + 250, groundheight - 1000, WIDTH + (int)(WIDTH/2) - 100, 400));
                //blocks.add(new Block((WIDTH * 2) + (int)(WIDTH/2) - 40, groundheight - 200, 40, 200));
                blocks.add(new Block((WIDTH * 2) + (int)(WIDTH/2) + 100, groundheight - 1000, 250, 750));
                //blocks.add(new Block((WIDTH * 2)))
                coins.add(new Coin((WIDTH * 2) + (int)(WIDTH/2) + 5, groundheight - 600));
                coins.add(new Coin((WIDTH * 2) + (int)(WIDTH/2) + 55, groundheight - 600));
                
                coins.add(new Coin((WIDTH * 2), groundheight - 550));
                coins.add(new Coin((WIDTH * 2), groundheight - 500));
                coins.add(new Coin((WIDTH * 2), groundheight - 450));
                coins.add(new Coin((WIDTH * 2) + 50, groundheight - 550));
                coins.add(new Coin((WIDTH * 2) + 50, groundheight - 500));
                coins.add(new Coin((WIDTH * 2) + 50, groundheight - 450));
                coins.add(new Coin((WIDTH * 2) + 100, groundheight - 550));
                coins.add(new Coin((WIDTH * 2) + 100, groundheight - 500));
                coins.add(new Coin((WIDTH * 2) + 100, groundheight - 450));
                coins.add(new Coin((WIDTH * 2) + 150, groundheight - 550));
                coins.add(new Coin((WIDTH * 2) + 150, groundheight - 500));
                coins.add(new Coin((WIDTH * 2) + 150, groundheight - 450));
                break;
            case 11:
                blocks.add(new Block(WIDTH, groundheight, 125, 40));
                blocks.add(new Block((WIDTH * 2) - 125, groundheight, 125, 40));
                int temp = generator.nextInt(WIDTH - 425) + 100;
                blocks.add(new Block(WIDTH + temp, groundheight, 100, 40));
                
                coins.add(new Coin(WIDTH + temp, groundheight - 50));
                coins.add(new Coin(WIDTH + temp + 50, groundheight - 50));
                //coins.add(new Coin(WIDTH + temp + 100, groundheight - 50));
                break;
            case 12:
                blocks.add(new Block(WIDTH, groundheight, WIDTH, 40));
                blocks.add(new Block(WIDTH + 70, groundheight - 150, 70, 150));
                blocks.add(new Block(WIDTH + 140, groundheight - 100, 70, 100));
                blocks.add(new Block(WIDTH + 210, groundheight - 150, 70, 150));
                blocks.add(new Block(WIDTH + 280, groundheight - 100, 70, 100));
                blocks.add(new Block(WIDTH + 350, groundheight - 150, 70, 150));
                blocks.add(new Block(WIDTH + 420, groundheight - 100, 70, 100));
                blocks.add(new Block(WIDTH + 490, groundheight - 150, 70, 150));
                blocks.add(new Block(WIDTH + 560, groundheight - 100, 70, 100));
                blocks.add(new Block(WIDTH + 630, groundheight - 150, 70, 150));
                
                coins.add(new Coin(WIDTH + 155, groundheight - 145));
                coins.add(new Coin(WIDTH + 295, groundheight - 145));
                coins.add(new Coin(WIDTH + 435, groundheight - 145));
                coins.add(new Coin(WIDTH + 575, groundheight - 145));
                break;
            case 13:
                blocks.add(new Block(WIDTH, groundheight, (int)(WIDTH/2) - 190, 40));
                blocks.add(new Block(WIDTH + (int)(WIDTH/2) + 190, groundheight, (int)(WIDTH/2) - 190, 40));
                blocks.add(new Block(WIDTH + (int)(WIDTH/2) - 40, groundheight - 300, 80, 300));
                blocks.add(new Block(WIDTH + (int)(WIDTH/2) - 190, groundheight - 300, 380, 40));
                
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) - 190, groundheight));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) - 140, groundheight));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) - 90, groundheight));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) - 40, groundheight));
                //coins.add(new Coin(WIDTH + (int)(WIDTH/2), groundheight));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2), groundheight));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) + 45, groundheight));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) + 95, groundheight));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) + 145, groundheight));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) - 190, groundheight + 50));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) - 140, groundheight + 50));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) - 90, groundheight + 50));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) - 40, groundheight + 50));
                //coins.add(new Coin(WIDTH + (int)(WIDTH/2), groundheight));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2), groundheight + 50));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) + 45, groundheight + 50));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) + 95, groundheight + 50));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) + 145, groundheight + 50));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) - 140, groundheight + 100));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) - 90, groundheight + 100));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) - 40, groundheight + 100));
                //coins.add(new Coin(WIDTH + (int)(WIDTH/2), groundheight));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2), groundheight + 100));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) + 45, groundheight + 100));
                coins.add(new Coin(WIDTH + (int)(WIDTH/2) + 95, groundheight + 100));
                break;
            case 14:
                blocks.add(new Block(WIDTH, groundheight, WIDTH, 40));
                blocks.add(new Block(WIDTH, groundheight - 1000, WIDTH, 550));
                for (int j = 200; j < 900; j += 500) {
                    for (int i = 0; i < 10; i++) {
                        blocks.add(new Block(WIDTH + j + (i * 5), groundheight - (i * 30), 100 - (i * 10), 30));
                    }
                }
                for (int i = 0; i < 10; i++) {
                    blocks.add(new Block(WIDTH + 450 + (i * 5), groundheight - 450 + (i * 30), 100 - (i * 10), 30));
                }
                break;
            case 15:
                break;
        }
    }
    
    private void scroll() {
       int scrollx = 0, scrolly = 0;
       //calculate how much it needs to scroll
        if (player.getX() > 500) {
           scrollx = 500 - player.getX();
        }
        if (player.getX() < 200 ) {
            if (doesPreviousSectionExist()) {
                scrollx = 200 - player.getX();
                distance--;
            }
        }
        if (player.getY() < 50) {
            scrolly = 50 - player.getY();
        }
        if (player.getY() > 380) {
            scrolly = 380 - player.getY();
        }
        
        //scroll all items on screen.
        distance -= scrollx;
        groundheight += scrolly;
        player.scroll(scrollx, scrolly);
        wave.scroll(scrollx);
        for (int i = 0; i < blocks.size(); i++) {
            Block b = (Block)blocks.get(i);
            b.scroll(scrollx, scrolly);
        }
        for (int i = 0; i < coins.size(); i++) {
            Coin c = (Coin)coins.get(i);
            c.scroll(scrollx, scrolly);
        }
        for (int i = 0; i < meteors.size(); i++) {
            Meteor m = (Meteor)meteors.get(i);
            m.scroll(scrollx, scrolly);
        }
    }
    
    private void checkCollisions() {
        
        /*boolean onsurface = false;
        Rectangle p_rect = player.getBounds();
        for (int i = 0; i < blocks.size(); i++) {
            Block b = (Block)blocks.get(i);
            Rectangle b_rect = b.getBounds();
            if (p_rect.intersects(b_rect)) {
                onsurface = true;
            }
            
        }
        if (!onsurface) {
            player.scroll(0, 2);
        }*/
        Rectangle p = player.getBounds();
        for (int i = 0; i < meteors.size(); i++) {
            Meteor m = (Meteor)meteors.get(i);
            Rectangle meteor = m.getBounds();
            if (meteor.intersects(p)) {
                if (m.isActive()) {
                    player.updateHealth(-1);
                    m.setActive(false);
                } else {
                    num_meteors++;
                    m.setVisible(false);
                }
            }
        }
        for (int i = 0; i < coins.size(); i++) {
            Coin c = (Coin)coins.get(i);
            Rectangle coin  = c.getBounds();
            if (p.intersects(coin)) {
                num_coins++;
                c.setVisible(false);
            }
        }
        
        
        
        if (player.getBounds().getY() > groundheight + 200) {
            player.updateHealth(-1);
        }
        
        if (wave.getX() > player.getX()) {
            player.updateHealth(-1);
        }
        
        if (player.getHealth() <= 0) {
            //ingame = false;
            menuactive = true;
            createNewDay();
            days_remain--;
        }
    }
    
    private void checkHits() {
        
    }
    
    private boolean doesNextSectionExist() {
        for (int i = blocks.size() - 1; i >= 0; i--) {
            Block b = (Block)blocks.get(i);
            if (b.getRightSide() > WIDTH) {
                return true;
            }
        }
        return false;
    }
    
    private boolean doesPreviousSectionExist() {
        for (int i = 0; i < blocks.size(); i++) {
            Block b = (Block)blocks.get(i);
            if (b.getX() < 0) {
                return true;
            }
        }
        return false;
    }
    
    private void deleteOldBlocks() {
        for (int i = 0; i < blocks.size(); i++) {
            Block b = (Block)blocks.get(i);
            if (b.getRightSide() < -500) {
                blocks.remove(i);
            } else {
                return;
            }
        }
    }
    
    private void spawnMeteors() {
        int actor = generator.nextInt();
        if (actor % 200 == 0 || actor % 300 == 0) {
            meteors.add(new Meteor());
        }
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        if (startup) {
            //g2d.setColor(Color.BLACK);
            g2d.fillRect(-10, -10, 1000, 1000);
            g2d.drawImage(startupimage, 40, 0, this);
            g2d.setColor(Color.WHITE);
            g2d.setFont(menu);
            g2d.drawString("Click anywhere to continue.", 600, 550);
            g2d.drawString("Created, designed, and programmed by Kurt Lewis.", 15, 550);
        } else {
            if (ingame) {
                if (paused) {
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.fillRect(0, 0, 900, 700);
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(menu);
                    FontMetrics metr = this.getFontMetrics(menu);
                    g2d.drawString("Paused", (B_WIDTH - metr.stringWidth("Paused")) / 2, 200);
                    g2d.drawString("Click anywhere, or press \"P\" or \"ESC\" to continue.", (B_WIDTH - metr.stringWidth("Click anywhere, or press \"P\" or \"ESC\" to continue.")) / 2, 225);
                    g2d.drawString("Controls:", (B_WIDTH - metr.stringWidth("Controls:"))/ 2, 325);
                    g2d.drawString("Left/Right arrow keys or \"A\"/\"D\" to move.", (B_WIDTH - metr.stringWidth("Left/Right arrow keys or \"A\"/\"D\" to move.")) / 2, 350);
                    g2d.drawString("Up arrow key or \"W\" to jummp and double jump.", (B_WIDTH - metr.stringWidth("Up arrow key or \"W\" to jummp and double jump."))/2, 375);
                    g2d.drawString("Space bar to end the day at any time.", (B_WIDTH - metr.stringWidth("Space bar to end the day at any time."))/2, 400);
                } else {
                    if (menuactive) {
                        //Menu
                        //maybe make background black?
                        g2d.fillRect(-1, -1, 1000, 900);
                        g2d.setColor(Color.WHITE);
                        g2d.setFont(menu);
                        FontMetrics metr = this.getFontMetrics(menu);
                        g2d.drawString("Days Left: " + days_remain, 15, 30);
                        g2d.drawString("Coins Collected: " + num_coins, 15, 60);
                        g2d.drawString("Meteors Collected: " + num_meteors, 15, 90);
                        //buy more health - meteors?
                        //start with the wave further away - coins
                        //slow wave down - coins
                        //player movement speed - coins

                        g2d.drawString("UPGRADES", (WIDTH - metr.stringWidth("UPGRADES")) / 2, 115);
                        g2d.drawImage(freezemenu, 20, 125, this);
                        g2d.drawImage(timemenu, 240, 125, this);
                        g2d.drawImage(speedmenu, 460, 125, this);
                        g2d.drawImage(healthmenu, 680, 125, this);
                        g2d.drawImage(startmenu, 680, 450, this);
                        String cost;
                        cost = ((freeze_level * 5) + 5) + " Coins";
                        g2d.drawString(cost, 20 + ((200 - metr.stringWidth(cost)) / 2), 350);
                        cost = ((time_level * 5) + 5) + " Coins";
                        g2d.drawString(cost, 240 + ((200 - metr.stringWidth(cost)) / 2), 350);
                        cost = ((speed_level * 5) + 5) + " Coins";
                        g2d.drawString(cost, 460 + ((200 - metr.stringWidth(cost)) / 2), 350);
                        cost = "10 Meteors";
                        g2d.drawString(cost, 680 + ((200 - metr.stringWidth(cost)) / 2), 350);

                        g2d.setFont(small);
                        g2d.drawString("Freeze Wave - Freezes the wave at the beggining of each day.", 20, 500);
                        g2d.drawString("Slow Time - Slows the speed of the wave.", 20, 515);
                        g2d.drawString("Movement Speed - Increases your running speed.", 20, 530);
                        g2d.drawString("Health - Replenishes one heart.", 20, 545);
                    } else {
                        //ingame
                        g2d.drawImage(backgroundimage, 0, 0, this);
                        g2d.setColor(Color.DARK_GRAY);
                        for (int i = 0; i < blocks.size(); i++) {
                            Block b = (Block) blocks.get(i);
                            b.paint(g2d);
                        }
                        for (int i = 0; i < meteors.size(); i++) {
                            Meteor m = (Meteor) meteors.get(i);
                            m.paint(g2d);
                        }
                        for (int i = 0; i < coins.size(); i++) {
                            Coin c = (Coin) coins.get(i);
                            c.paint(g2d);
                        }
                        player.paint(g2d);
                        wave.paint(g2d);
                        //******
                        //test code to draw progress bar
                        //******
                        //general bar
                        g2d.setColor(Color.WHITE);
                        g2d.fillRect(25, 540, 300, 25);
                        g2d.setColor(Color.BLACK);
                        g2d.setFont(small);
                        g2d.drawRect(25, 540, 300, 25);
                        g2d.drawString("Finish", 275, 557);
                        //player
                        double length;
                        length = ((double) distance / (double) FINISH_DISTANCE) * 298;
                        if (length > 299) {
                            length = 299;
                        }
                        g2d.setColor(Color.BLUE);
                        g2d.fillRect(26, 541, (int) length, 24);
                        if (length > 45) {
                            g2d.setColor(Color.BLACK);
                            g2d.drawString("Player", (int) length - 20, 557);
                        }

                        //wave
                        length = (double)((distance + wave.getX()) / (double) FINISH_DISTANCE) * 298;
                        if (length > 299) {
                            length = 299;
                        }
                        g2d.setColor(Color.RED);
                        g2d.fillRect(26, 541, (int) length, 24);
                        if (length > 45) {
                            g2d.setColor(Color.BLACK);
                            g2d.drawString("Wave", (int) length - 15, 557);
                        }
                        //******
                        //end of progress bar
                        //******
                        g2d.setColor(Color.DARK_GRAY);


                        if (debug) {
                            g2d.setColor(Color.WHITE);
                            g2d.drawString("Blocks: " + blocks.size(), 5, 15);
                            g2d.drawString("Meteors: " + meteors.size(), 5, 25);
                            g2d.drawString("Coins: " + coins.size(), 5, 35);
                            g2d.drawString("Distance Remaining: " + distance, 5, 45);
                        }
                    }
                }
            } else {
                //end of game
                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRect(0, 0, 1000, 1000);
                g2d.setColor(Color.WHITE);
                g2d.setFont(menu);
                FontMetrics metr = this.getFontMetrics(menu);
                if (victory) {
                    g2d.drawString("Congratulations! You saved the world!", (B_WIDTH - metr.stringWidth("Congratulations! You saved the world!"))/2, 250);
                } else {
                    g2d.drawString("Game Over. Press ESC or P to continue.", (B_WIDTH - metr.stringWidth("Game Over. Press ESC or P to continue.")) / 2, B_HEIGHT / 2);
                }
            }
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    private class MainTask extends TimerTask {
        @Override
        public void run() {
            if (!paused && !menuactive && !startup) {
                player.move(blocks);
                wave.move();
                wave.increaseSpeed();
                
                
                //meteors
                spawnMeteors();
                for (int i = 0; i < meteors.size(); i++) {
                    Meteor m = (Meteor)meteors.get(i);
                    if (m.isVisible()) {
                        m.move(blocks);
                    } else {
                        meteors.remove(i);
                    }
                }
                for (int i = 0; i < coins.size(); i++) {
                    Coin c = (Coin)coins.get(i);
                    if (c.isVisible()) {
                        c.update();
                    } else {
                        coins.remove(i);
                    }
                }
                scroll();
                checkCollisions();
                deleteOldBlocks();
                if (!doesNextSectionExist()) {
                    //actual random generator
                    if (distance < FINISH_DISTANCE) {
                        //addSection(generator.nextInt(16));
                        addSection(15);
                    } else {
                        addSection(-2);
                        victory = true;
                        ingame = false;
                    }
                    //test segments here
                    //addSection(7);
                }
            } else {
                if (startup) {
                    //move the image up for the startup story
                }
                if (menuactive) {
                
                }
            }
            if (!ingame) {
               //maintimer.cancel();
               //maintimertask.cancel();
               paused = true;
               //resetGameState();
            }
            repaint();
        }
    }
    
    
    
    private class TAdapter extends KeyAdapter {
        
        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
            int key = e.getKeyCode();
            
            if (key == KeyEvent.VK_M) {
                if (debug) {
                    debug = false;
                } else {
                    debug = true;
                }
            }
            if (key == KeyEvent.VK_P || key == KeyEvent.VK_ESCAPE) {
               if (!paused) {
                   //maintimer.cancel();
                   paused = true;
                   repaint();
               } else {
                   //resetTimer();
                   paused = false;
                   if (startup) {
                       startup = false;
                   }
                   if (!ingame) {
                       resetGameState();
                   }
               }   
            }
            
            if (key == KeyEvent.VK_SPACE) {
                if (!menuactive) {
                    createNewDay();
                    menuactive = true;
                }
            }
        } 
    }
    
    private class MAdapter extends MouseAdapter {
        
        @Override
        public void mouseClicked(MouseEvent e) {
            if (startup) {
                //paused = false;
                startup = false;
                return;
            }
            
            if (paused) {
                paused = false;
                return;
            }
            
            if (menuactive) {
                Point mousepoint = e.getPoint();
                
                if (freezemenuRect.contains(mousepoint)) {
                    if (num_coins >= (freeze_level * 5) + 5) {
                        num_coins -= (freeze_level * 5) + 5;
                        freeze_level++;
                    }
                }
                
                if (timemenuRect.contains(mousepoint)) {
                    if (num_coins >= (time_level * 5) + 5) {
                        num_coins -= (time_level * 5) + 5;
                        time_level++;
                    }
                }
                
                if (speedmenuRect.contains(mousepoint)) {
                    if (num_coins >= (speed_level * 5) + 5) {
                        num_coins -= (speed_level * 5) + 5;
                        speed_level++;
                    }
                }
                
                if (healthmenuRect.contains(mousepoint)) {
                    if (num_meteors >= 10) {
                        num_meteors -= 10;
                        player.updateHealth(1);
                    }
                }
                
                if (startmenuRect.contains(mousepoint)) {
                    menuactive = false;
                }
            }
        }
        
    }
}