package Rockets;

import static Rockets.Rockets.HEIGHT;
import static Rockets.Rockets.rockets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

//Define other objects.
public class Ball extends Ellipse2D.Float {

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
        colour = Color.GRAY;
        this.va = (int) 9.81;
        if (rockets.size()>0){
            angle = 90 + Math.abs(90 - angle);
        }
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
    public Ball (float x, float y){
        super(x + 20, y + HEIGHT - 100, 3,3);
        double random = Math.random() * 5;
        super.height = (float) (random / 2) + 1;
        super.width = (float) (random / 2) + 1;
        double speed = rockets.get(1).speed + rockets.get(0).speed;
        speed /= 10;
        double random2 = Math.random() * speed - speed / 2;
        double random3 = Math.random() * speed - speed;
        this.hSpeed = random2;this.vSpeed = random3;
        this.mass = 1;
        this.fired = true;
        colour= Color.BLACK;

    }

    public double getSpeed(){return this.speed;}

    public double getAngle() {
        return angle;
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