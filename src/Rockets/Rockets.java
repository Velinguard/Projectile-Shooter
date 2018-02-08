/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rockets;

import Rockets.ImageManager.ImageManager;
import Rockets.MathsManager.MathsModule;
import Rockets.ObjectManager.AA;
import Rockets.ObjectManager.Ball;
import Rockets.ObjectManager.Debris;
import Rockets.ObjectManager.Missile;
import Rockets.ObjectManager.MovingBall;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 *
 * @author Sam
 */
public class Rockets extends JPanel {
    private static int WIDTH = 1920;
    public static int HEIGHT = 1080;
    private static int SCALER = 1;
    private static double milliSecondTimer;
    private static int count;
    private static double delta;
    private static ArrayList<Integer> keysDown;
    private static ArrayList<Ball> stationary;
    private static ArrayList<Debris> debris;
    private static AA aa;
    private static Missile missile;
    private static double  timeToStart;
    private static double loopsGone;
    private static double yCollide, xCollide, timeCollide;
    private static boolean hit;
    private static boolean running;
    private static double distanceAway;
    private static double vM, vAA, aM, aAA;
    private static ImageManager bg1;

    public Rockets(){
        //Init
        keysDown = new ArrayList<>();
        debris = new ArrayList<>();
        stationary = new ArrayList<>();
        hit  = false;

        bg1 = new ImageManager("BackgroundMineLowRes.jpg");

        //init rockets
        vM = 200;
        aM = 40;
        vAA = 300;
        aAA = 120; // 90 + (90 - angle)
        distanceAway = 1600;
        milliSecondTimer = 0;

        reset();

        //Other
        KeyListener listener = new MyKeyListener();
        addKeyListener(listener);
        setFocusable(true);
    }
    public void reset(){
        debris = new ArrayList<>();
        stationary = new ArrayList<>();
        missile = new Missile(0,0,10,vM,aM, true, "missile.png");
        aa = new AA(1600,0,10,vAA,aAA, false, "AAMissileCopy.png");

        milliSecondTimer = 0;
        running = true;
        hit = false;
        count = 1;

        MathsModule mm = new MathsModule(missile , aa, distanceAway);
        Thread th = new Thread(mm);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timeToStart = mm.getTimeToStart();
        xCollide = mm.getxCollide();
        yCollide = mm.getyCollide();
        timeCollide = mm.getTimeCollide();

    }


    public static void main(String[] args) throws InterruptedException{
        JFrame frame = new JFrame("Missile Launcher");
        Rockets app = new Rockets();
        frame.setSize((int)(WIDTH * SCALER),(int)(HEIGHT * SCALER));
        frame.add(app);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.requestFocus();
        long lastLoopTime = System.nanoTime();
        int fps = 0, lastFpsTime = 0, lastMilliSecondTimer = 0, count = 1;
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        //Game Loop
        while(true){
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;
            delta = updateLength / ((double)OPTIMAL_TIME);
            lastFpsTime += updateLength;
            lastMilliSecondTimer += updateLength;
            fps++;
            if (lastFpsTime > 100000000 / 6 * count){
                if (running){
                    milliSecondTimer += (0.1 / 6);
                    app.gravity();
                }
                count++;
            }
            if (lastFpsTime >= 1000000000){
                System.out.println("(FPS: "+fps+")");
                //milliSecondTimer += 1;
                lastFpsTime = 0;
                fps = 0;
                count = 1;
            }
            loopsGone++;
            app.repaint();

            Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000 );
        }
    }

    public void gravity(){
        if (timeToStart <= milliSecondTimer) {
            aa.setFired();
            //Trail of other rocket
            if (milliSecondTimer > 0.1 * count && aa.isInside(HEIGHT, WIDTH)) {
                stationary.add(new Ball(aa.x, aa.y, 5));
                debris.add(new Debris(aa));
            }
        }

        for (MovingBall debris: debris) {
            rocketMover(debris);
        }

        rocketMover(aa);
        rocketMover(missile);

        double dx = Math.abs(aa.getCenterX() - missile.getCenterX());
        double dy = Math.abs(aa.getCenterY() - missile.getCenterY());
        double r = Math.sqrt(dx * dx + dy * dy);

        if (r <= aa.getHeight() / 2 + missile.getHeight() / 2 && !hit){
            //They have collided
            double speed = aa.getSpeed() + missile.getSpeed();
            for (int z = 0; z < speed / 10; z++){
                synchronized (this) {
                    debris.add(new Debris(aa, missile));
                }
            }
            hit = true;
            missile.remove();
            aa.remove();
        }

        //For the trail
        if (milliSecondTimer > 0.1 * count ){
            if (missile.isInside(HEIGHT, WIDTH)) {
                stationary.add(new Ball(missile.x, missile.y, 5));
                count++;
            }
        }
    }

    private void rocketMover(MovingBall rocket){
        if (rocket.getFired()) {
            rocket.decrementVSpeed(9.81 / 60.0);
            rocket.move(0, (rocket.getVSpeed() / 60.0));
            rocket.move(1, (rocket.getHSpeed() / 60.0));
        }
    }


    //Window Painter
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.setFocusable(true);
        this.requestFocusInWindow();

        g2d.drawImage(bg1.getImage(), 0,0, null);
        try {
            debris.stream().forEach(t -> t.paint(g2d));
            stationary.stream().forEach(t -> t.paint(g2d));
        } catch (Exception e){

        }

        if (missile.getFired()){
            missile.paint(g2d);
            g2d.drawImage(missile.transform(),
                (int) missile.getCenterX() - missile.getIm().getWidth() / 2
                , (int) missile.getCenterY() - missile.getIm().getHeight() / 2, null);
        }
        if (aa.getFired()){
            aa.paint(g2d);
            g2d.drawImage(aa.transform(),
                (int) aa.getCenterX() - aa.getIm().getWidth() / 2
                , (int) aa.getCenterY() - aa.getIm().getHeight() / 2, null);
        }

        g2d.setColor(Color.WHITE);

        g2d.drawString("Missile:", 10, 20);
        g2d.drawString("Angle: " + aM + "°", 10, 35);
        g2d.drawString("Velocity: " + vM + "m/s", 10, 50);

        g2d.drawString("Anti-Air Missile:", WIDTH - 150, 20);
        g2d.drawString("Angle: " + aAA + "°", WIDTH - 150, 35);
        g2d.drawString("Velocity: " + vAA + "m/s", WIDTH - 150, 50);
        g2d.drawString("Situated " + distanceAway + "m away", WIDTH - 150, 65);
        DecimalFormat df = new DecimalFormat("#.00");
        g2d.drawString("Collision at:", 10, HEIGHT - 80);
        g2d.drawString("( x = " + df.format(xCollide) + "m, y = " + df.format(yCollide) + "m)" , 10, HEIGHT - 65);
        g2d.drawString("After " + df.format(timeCollide) + "s", 10, HEIGHT - 50);

        g2d.drawString("Controls:", WIDTH - 420,  HEIGHT - 82);
        g2d.drawString("Speed: A and D for Missile; Left and Right arrow for AA.", WIDTH - 350,  HEIGHT - 80);
        g2d.drawString("Angle: W and S for Missile; Up and Down arrow for AA.", WIDTH - 350,  HEIGHT - 65);
        g2d.drawString("Pause Simulation: Space, Reset Simulation: Escape ", WIDTH - 350,  HEIGHT - 50);
    }

    //Listens for button presses
    public class MyKeyListener implements KeyListener{

        public void action(){
            if (keysDown.contains(KeyEvent.VK_SPACE)){
                running = !running;
            }
            if (keysDown.contains(KeyEvent.VK_ESCAPE)){
                reset();
            }
            if (keysDown.contains(KeyEvent.VK_W)){
                aM += 3;
            }
            if (keysDown.contains(KeyEvent.VK_S)){
                aM -= 3;
            }
            if (keysDown.contains(KeyEvent.VK_D)){
                vM += 5;
            }
            if (keysDown.contains(KeyEvent.VK_A)){
                vM -= 3;
            }
            if (keysDown.contains(KeyEvent.VK_UP)){
                aAA += 3;
            }
            if (keysDown.contains(KeyEvent.VK_DOWN)){
                aAA -= 3;
            }
            if (keysDown.contains(KeyEvent.VK_RIGHT)){
                vAA -= 5;
            }
            if (keysDown.contains(KeyEvent.VK_LEFT)){
                vAA += 3;
            }
        }
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!keysDown.contains(e.getKeyCode())){
                keysDown.add(e.getKeyCode());
            }
            action();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keysDown.remove(new Integer(e.getKeyCode()));
        }
    }
}