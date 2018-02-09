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

To convert from  degrees to radians simply call `Math.toRadians(angle)`.

Furthermore `distanceAway` is a double that represents the distance between the two launch sites in meters. 

### Use of Threads

It was important to implement MathsModule as a thread, as in a real life situation you may have multiple missiles being launched at the same time, and you would need to calculate the launch time for these missiles simultaneously.

### Project Influence

The idea to develop this software was first considered during my A-Level studies when I was studying Mechanics 3 at A-Level as well as preparing for a STEP 2 exam. I was compelled by one of the questions in the mechanics section of a STEP exam, which required you to work out the time that you should launch a rocket to collide with another. My initial prototype was completed around April 2017 where I was able to complete the task but without the use of threads, and a very rudimentary object structure. Almost a year later I revisited the project, re-writing the code utilising threads, and other techniques such as abstract classes and interfaces, which I had learnt during my degree studies.

### Future of the Project

Looking forward to seeing how this project could develop with time brings me to consider a number of features that could be implemented:

* Implementing the Maths Module to work in 3 Dimensions rather than just on the x/y plane.
* Utilising the thread system of the Maths Module such that the simulation will launch multiple missiles simultaneously, to test how the system will react.
* Implementing the Maths Module to take into account air resistance to calculate when a collision will take place.
