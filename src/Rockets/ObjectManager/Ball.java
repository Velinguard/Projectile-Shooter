package Rockets.ObjectManager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

//Define other objects.
public class Ball extends Ellipse2D.Float {
    Color colour;

    public Ball(float x, float y, float r){
        super (x, y, r, r);
        colour = Color.GRAY;
    }

    public boolean isInside(int height, int width){
        return this.x >= 0 && this.x <= width
            && this.y >= 0 && this.y <= height;
    }

    public void paint(Graphics2D g) {
        g.setColor(colour);
        g.fill(this);
    }

}

