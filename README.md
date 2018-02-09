# Projectile-Shooter

A java package that calculates when a rocket must be launched in order to collide with another rocket in a 2D plane.
Includes a java application to give a visual representation of the collision.

### Example Implementation

```
MathsModule mm = new MathsModule(missile , aa, distanceAway);
Thread th = new Thread(mm);
th.start();

//Complete other operations..

try {
  th.join();
} catch (InterruptedException e) {
  e.printStackTrace();
}

//To get outputs:

timeToStart = mm.getTimeToStart();
xCollide = mm.getxCollide();
yCollide = mm.getyCollide();
timeCollide = mm.getTimeCollide();
```

Where missile and aa are Java objects that satisfy the Projectiles interface defined by:

```
public interface Projectile {
    public double getSpeed();
    public double getAngle();
}
```

Where `getSpeed()` returns the initial velocity of a Projectile in ms^-1, and `getAngle()` returns the angle of the rocket in radians.

Furthermore `distanceAway` is a double that represents the distance between the two launch sites in meters. 

### Use of Threads

It was important to implement MathsModule as a thread, as in a real life situation you may have multiple missiles being launched at the same time, and you would need to calculate the launch time for these missiles simultaneously.
