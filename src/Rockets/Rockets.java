/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Rockets;

import static Rockets.Rockets.HEIGHT;
import static Rockets.Rockets.SCALER;
import static Rockets.Rockets.WIDTH;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 *
 * @author Sam
 */
public class Rockets extends JPanel {
    public static int WIDTH = 1920;
    public static int HEIGHT = 1280;
    public static int SCALER = 1;
    public static double milliSecondTimer;
    public static double delta;
    public static ArrayList<Integer> keysDown;
    public static ArrayList<Ball> rockets;
    public static double  timeToStart;
    public static double count;
    public static double loopsGone;
    public static double yCollide;
    
    
    public Rockets(){
        //Init
        keysDown = new ArrayList<Integer>();
        rockets = new ArrayList<Ball>();    
        
        //init rockets
        rockets.add(new Ball(0,0,10,200,50, true));
        rockets.add(new Ball(1600,0,10,200,120, false));
        timeToStart = 1110; // any high number
        milliSecondTimer = 0;
        count = 1;
        
        //Other
        KeyListener listener = new MyKeyListener();
        addKeyListener(listener);
        setFocusable(true);   
    }
    public static void main(String[] args) throws InterruptedException{
        JFrame frame = new JFrame("App Name");
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
               milliSecondTimer += (0.1 / 6);
               gravity();
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
    public static void gravity(){
        for (int i = 0; i < rockets.size(); i++){
            
            if(timeToStart <= milliSecondTimer){
                rockets.get(1).fired = true;
                //Trail of other rocket
                if (milliSecondTimer > 0.1 * count){
                    rockets.add(new Ball(rockets.get(1).x - 20 ,rockets.get(1).y - HEIGHT + 100,5,0,0,false));
                }
            }
            
            if(rockets.get(i).fired){
                if (rockets.get(i).y <= HEIGHT - yCollide - 100  ){
                    //System.out.println("Hit at " + yCollide);
                }
                rockets.get(i).vSpeed -= 9.81 / 60.0;
                rockets.get(i).move(0, (double) (rockets.get(i).vSpeed / 60.0));
                rockets.get(i).move(1, (double) (rockets.get(i).hSpeed / 60.0));
            } else if (timeToStart==1110){
                //function to work out the time displacment
                derr(1);
            }
            
            //For the trail
            if (milliSecondTimer > 0.1 * count){
                rockets.add(new Ball(rockets.get(0).x - 20 ,rockets.get(0).y - HEIGHT + 100,5,0,0,false));
                count++;
            }
            
        }
    }     
    public static void derr(int index){
        double X = 1600;
        double angle = 90 - Math.abs(90 - rockets.get(index).angle); 
        if (angle < rockets.get(0).angle){
            
            System.err.println("AA cannot hit target");
            
        } else {
            
            double s = rockets.get(index).speed, gamma = Math.toRadians(angle);
            double v = rockets.get(0).speed, alpha = Math.toRadians(rockets.get(0).angle);
            
            double aTop = s * s * Math.cos(gamma) * Math.cos(gamma) - v * v * Math.cos(alpha) * Math.cos(alpha);
            double aBottom = v * v * Math.cos(gamma) * Math.cos(gamma) * Math.cos(alpha) * Math.cos(alpha);
            double a = aTop / aBottom;
            
            double bTop = Math.cos(gamma) * Math.sin(gamma) * 2 * s * s + 2 * s * s * Math.cos(gamma) * Math.cos(gamma) * Math.tan(alpha) - 2 * X * 9.81;
            double bBottom = 9.81 * Math.cos(gamma) * Math.cos(gamma);          
            double b = - bTop / bBottom;
            
            double cTop = 2 * X * s * s * Math.cos(gamma) * Math.sin(gamma) - 9.81 * 1600 * 1600;
            double cBottom = 9.81 * Math.cos(gamma) * Math.cos(gamma);
            double c = cTop / cBottom;
            
            double x = (- b - Math.sqrt(b * b - 4 * a * c)) / ( 2 * a);
            
            double dx = X - x;
            
            double a1 = 9.81 / 2;
            double b1 = - s * Math.sin(gamma);
            double c1 = dx * Math.tan(gamma) - (9.81 * dx * dx) / (2 * s * s * Math.cos(gamma) * Math.cos(gamma));
            
            double tA = (- b1 - Math.sqrt(b1 * b1 - 4 * a1 * c1)) / ( 2 * a1);
            
            double b2 = - s * Math.sin(alpha);
            double c2 = x * Math.tan(alpha) - (9.81 * x * x) / (2 * v * v * Math.cos(alpha) * Math.cos(alpha));
            
            double tB = (- b2 - Math.sqrt(b2 * b2 - 4 * a1 * c2)) / ( 2 * a1);
            
            if (a == 0){
                timeToStart = -1000;
            } else{
                timeToStart = Math.abs(tB - tA);
            }
        }
        
    }
    
    //Window Painter
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.setFocusable(true);
        this.requestFocusInWindow();
        
        int k;
        g2d.setColor(Color.CYAN);
        int w = 10 * SCALER;
        int rows = HEIGHT / w;
        int columns = WIDTH / w;
        for (k = 0; k < rows; k++) {
            g2d.drawLine(0, k * w, WIDTH, k * w);
        }
        for (k = 0; k < columns; k++) {
            g2d.drawLine(k * w, 0, k * w, HEIGHT);
        }
        g2d.setColor(Color.BLUE);
        w = 100 * SCALER;
        rows = HEIGHT / w;
        columns = WIDTH / w;
        for (k = 0; k < rows; k++) {
            g2d.drawLine(0, k * w, WIDTH, k * w);
        }
        for (k = 0; k < columns; k++) {
            g2d.drawLine(k * w, 0, k * w, HEIGHT);
        }
        for (int i = 0; i < rockets.size(); i++){
            rockets.get(i).paint(g2d);
        }
                                    
    }
    //Listens for button presses
    public class MyKeyListener implements KeyListener{

        public void action(){
            if (keysDown.contains(KeyEvent.VK_SHIFT)){
                //example
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
//Define other objects.
class Ball extends Ellipse2D.Float {

    Color colour;
    double[] startPos;
    double ha; //horizontal acceleration, + = -->
    double va; //vertical acceleration, + = ^
    double vSpeed; //vertical speed, + = ^
    double hSpeed; //horizontal speed, + = -->
    double initialVSpeed;
    double speed;
    int mass;
    boolean fired;
    double angle;

    public Ball(float x, float y, float r, double speed, double angle, boolean fire) {
        super(x + 20, y + HEIGHT - 100, r, r);
        colour = Color.blue;
        this.va = (int) 9.81;
        this.angle = angle;
        this.speed = speed;
        this.fired = fire;
        this.startPos = new double[2]; this.startPos[0] = x + 20; this.startPos[1] = y + HEIGHT - 100;
        this.ha = (int) 0;
        this.vSpeed = speed * Math.sin(Math.toRadians(angle));
        this.initialVSpeed = this.vSpeed;
        this.hSpeed = speed * Math.cos(Math.toRadians(angle));
        this.mass = 3;
    }
    public Ball(float x, float y, float r){
        super (x, y, r, r);
        this.fired = false;
    }

    public void move(int d, double s) {
        switch (d) {
            //if within the screen then move s up
            case 0:
                if (s < 0) {
                    //if (HEIGHT * SCALER - super.height > super.getCenterY() + s) {
                        super.y -= s;
                    //}
                } else {
                    //if (super.height < super.getCenterY() - s) {
                        super.y -= s;
                    //}
                }
                break;
            case 1:
                //if within the screen then move s to the right
                if (s < 0) {
                    //if (super.width < super.getCenterX() - s) {
                        super.x += s;
                    //}
                } else {
                    //if (WIDTH * SCALER - super.width > super.getCenterX() + s) {
                        super.x += s;
                    //}
                }
                break;
        }
    }

    public void paint(Graphics2D g) {
        g.setColor(colour);
        g.fill(this);
    }
}

/* Useful shortcuts:
grid (tab) = grid layout every 10 pixels, on a 100 pixel = 1 metre scale, that is 10 cm.
grid2 (tab) = grid layout every 100 pixels, on a 100 pixel = 1 metre scale, that is 1m.
Ball (tab) = creates the Ball class.
Rectangle (tab) = creates the Rectangle class.
*/