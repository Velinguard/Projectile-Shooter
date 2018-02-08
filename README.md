# Projectile-Shooter

A java package that calculates when a rocket must be launched in order to collide with another rocket in a 2D plane.
Includes a java application to give a visual representation of the collision.

###Example Implementation

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

It was important to implement MathsModule as a thread, as in a real life situation you may have multiple missiles being launched at the same time, and you would need to calculate the launch time for these missiles simultaneously.
