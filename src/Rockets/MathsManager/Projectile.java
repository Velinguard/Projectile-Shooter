package Rockets.MathsManager;

//If two objects implement this the time needed for them to collide can be computed.
public interface Projectile {
    public double getSpeed();
    public double getAngle();
}
