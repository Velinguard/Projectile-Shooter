package Rockets.ObjectManager;

public class AA extends MovingBall {

    public AA(float x, float y, float r, double speed, double angle, boolean fire,
        String path) {
        super(x, y, r, speed, 90 + Math.abs(90 - angle), fire, path);
    }

    @Override
    public double getAngleForImage(){
        return Math.PI - ( super.getAngle() - Math.PI / 2);
    }
}
