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
    public static int HEIGHT = 1080;
    public static int SCALER = 1;
    public static double milliSecondTimer;
    public static double delta;
    public static ArrayList<Integer> keysDown;
    public ArrayList<Ball> rockets;
    double timeToStart;
    double count;
    public static double loopsGone;
    
    
    public Rockets(){
        //Init
        keysDown = new ArrayList<Integer>();
        rockets = new ArrayList<Ball>();    
        
        //init rockets
        rockets.add(new Ball(0,0,10,200,50, true));
        rockets.add(new Ball(1600,0,10,200,110, false));
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
            if (lastFpsTime > 100000000 * count){
               milliSecondTimer += 0.1;
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
        for (int i = 0; i < rockets.size(); i++){
            
            if(timeToStart <= milliSecondTimer){
                rockets.get(1).fired = true;
                //Trail of other rocket
                if (milliSecondTimer > 0.1 * count){
                    rockets.add(new Ball(rockets.get(1).x - 20 ,rockets.get(1).y - HEIGHT + 100,5,0,0,false));
                }
                
            }
            
            if(rockets.get(i).fired){
                rockets.get(i).vSpeed -= 9.81 / 60.0;
                rockets.get(i).move(0, (float) (rockets.get(i).vSpeed / 60.0));
                rockets.get(i).move(1, (float) (rockets.get(i).hSpeed / 60.0));
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
    public void derr(int index){
        double a = 0;
        double X = 1600;
        double angle = 90 - Math.abs(90 - rockets.get(index).angle); 
        a = (1 / Math.pow(rockets.get(index).speed * Math.cos(Math.toRadians(angle)) , 2) - 1 / Math.pow(rockets.get(0).speed * Math.cos(Math.toRadians(rockets.get(0).angle)), 2)) * (9.81 / 2);
        double b = 0;
        b = (Math.tan(Math.toRadians(angle)) + Math.tan(Math.toRadians(rockets.get(0).angle)) - (X / Math.pow(rockets.get(index).speed * Math.cos(Math.toRadians(angle)), 2)));
        double c = 0;
        c = (9.81 * X * X) / (2 * Math.pow(rockets.get(index).speed * Math.cos(Math.toRadians(angle)), 2)) - X * Math.tan(Math.toRadians(angle));
        
        double x = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        //double y = (X - x) * Math.tan(Math.toRadians(rockets.get(index).angle)) - (9.81 * ( X * X - 2 * X * x + x * x)) / ( 2 *  Math.pow(rockets.get(index).speed * Math.cos(Math.toRadians(rockets.get(index).angle)), 2)); 
        
        double timeB = (- x) / (Math.cos(Math.toRadians(rockets.get(index).angle)) * rockets.get(index).speed);
        double timeA = (X - x) / (Math.cos(Math.toRadians(rockets.get(0).angle)) * rockets.get(0).speed);
                
        
        timeToStart = timeA - timeB;
        System.out.println(timeToStart);
    }
    
    
    
    
    //Window Painter
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.setFocusable(true);
        this.requestFocusInWindow();

        //Game loop, but everything time related * delta to get seconds.
        gravity();
        
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
        System.out.println(vSpeed);
        this.mass = 3;
    }
    public Ball(float x, float y, float r){
        super (x, y, r, r);
        this.fired = false;
    }

    public void move(int d, float s) {
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