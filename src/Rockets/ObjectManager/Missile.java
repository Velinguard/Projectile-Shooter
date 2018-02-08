package Rockets.ObjectManager;

public class Missile extends MovingBall {

    public Missile(float x, float y, float r, double speed, double angle, boolean fire,
        String path) {
        super(x, y, r, speed, angle, fire, path);
    }

    @Override
    public double getAngleForImage(){
        return angle = 4 * Math.PI / 2 - super.getAngle();
    }
}
