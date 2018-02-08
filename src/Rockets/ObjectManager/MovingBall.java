package Rockets.ObjectManager;

import static Rockets.Rockets.HEIGHT;
import Rockets.ImageManager.ImageManager;
import Rockets.MathsManager.Projectile;
import java.awt.image.BufferedImage;

public class MovingBall extends Ball implements Projectile{
    double vSpeed; //vertical speed, + = ^
    double hSpeed; //horizontal speed, + = -->
    double speed;
    boolean fired;
    double angle;
    public ImageManager im;

    public MovingBall(float x, float y, float r, double speed, double angle
        , boolean fire, String path) {

        super(x + 20, y + HEIGHT - 100, r);
        this.angle = angle;
        this.speed = speed;
        this.fired = fire;
        this.vSpeed = speed * Math.sin(Math.toRadians(angle));
        this.hSpeed = speed * Math.cos(Math.toRadians(angle));
        im = new ImageManager(path);
    }

    public ImageManager getIm() {
        return im;
    }

    public double getSpeed(){return this.speed;}

    public MovingBall(double x, double y, float r){
        super((float) x,(float) y, r);
    }

    public void move(int d, double s) {
        switch (d) {
            case 0:
                if (s < 0) {
                    super.y -= s;
                } else {
                    super.y -= s;
                }
                break;
            case 1:
                if (s < 0) {
                    super.x += s;
                } else {
                    super.x += s;
                }
                break;
        }
    }

    public BufferedImage transform(){
        return im.transformer(getAngleForImage());
    }

    public void setFired() {
        fired = true;
    }

    public void remove() {
        fired = false;
    }

    public boolean getFired() {
        return fired;
    }

    public void decrementVSpeed(double v) {
        vSpeed -= v;
    }

    public double getVSpeed() {
        return vSpeed;
    }

    public double getHSpeed() {
        return hSpeed;
    }

    public double getAngle(){
        return Math.atan2(vSpeed, hSpeed);
    }

    public double getAngleForImage(){
        return 0;
    }

}
