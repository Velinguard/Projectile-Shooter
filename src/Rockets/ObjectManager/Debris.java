package Rockets.ObjectManager;

import java.awt.Color;

public class Debris extends MovingBall{

    //Debris constructor for when there is a collision
    public Debris (MovingBall rocketA, MovingBall rocketB){
        super(rocketA.x, rocketA.y, 3);

        int random1 = (int) (Math.random() * 4);
        super.height = random1;
        super.width = random1;

        double speed = (rocketA.speed + rocketB.speed) / 10;
        double random2 = Math.random() * speed - speed / 2;
        double random3 = Math.random() * speed - speed;
        this.hSpeed = random2;this.vSpeed = random3;
        this.fired = true;
        colour= Color.BLACK;
    }

    //Creates a cool smoke effect from rocket
    public Debris (MovingBall rocket){
        super(rocket.x, rocket.y, 2);
        double random2 = Math.random() * rocket.speed / 3.5 - rocket.speed / 4;
        double random3 = Math.random() * rocket.speed / 4 - rocket.speed / 4;
        this.hSpeed = random2 ;this.vSpeed = random3;
        this.fired = true;
        colour= Color.BLACK;
    }
}
