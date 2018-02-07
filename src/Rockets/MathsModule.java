package Rockets;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MathsModule implements Runnable{
    private final double s, v;
    private final double gamma;
    private final double alpha;
    private final double distanceAway;
    //outputs:
    private double xCollide, yCollide, timeCollide, timeToStart;

    protected double getS() {
        return s;
    }

    protected double getV() {
        return v;
    }

    protected double getGamma() {
        return gamma;
    }

    protected double getAlpha() {
        return alpha;
    }

    protected double getDistanceAway() {
        return distanceAway;
    }

    public MathsModule(Ball aa,  Ball rocket, double distanceAway){
        this.s = rocket.getSpeed(); //Speed of rocket to be stopped.
        this.v = aa.getSpeed();     //Speed of AA rocket
        this.gamma = Math.toRadians(90 - Math.abs(90 - rocket.getAngle()));
        this.alpha = Math.toRadians(aa.getAngle());
        this.distanceAway = distanceAway;
        //init outputs
        this.timeToStart = 0; this.xCollide = 0; this.yCollide = 0; this.timeCollide = 0;
    }

    @Override
    public void run() {
        List<myThread> rootCalculations = Arrays.asList
            (new A(this), new B(this), new C(this));

        threadListRunner(rootCalculations);

        //Checks if missile can be stopped.
        Root rt = new Root(rootCalculations.get(0).getOutput(),
            rootCalculations.get(1).getOutput(), rootCalculations.get(2).getOutput());
        rt.run();

        //If missile can be stopped, work out how, else issue a warning.
        if (rt.hasRoots()) {
            //if there is only one root
            if (rootCalculations.get(0).getOutput() == 0) {
                timeToStart = -1000;
            } else {
                RootFinder rtFinder = new RootFinder(rt);
                rtFinder.run();
                double x = rtFinder.getOutput();

                double dx = distanceAway - x;
                double a1 = 9.81 / 2;

                List<myThread> calculations = Arrays.asList(
                    //y index (0)
                    new Calculator(this) {
                        @Override
                        public void calculator() {
                            output = x * Math.tan(alpha) -
                                (9.91 * x * x) / (2 * v * v * Math.cos(alpha));
                        }
                    },
                    //b1 index (1)
                    new Calculator(this) {
                        @Override
                        public void calculator() {
                            output = -s * Math.sin(gamma);
                        }
                    },
                    //c1 index (2)
                    new Calculator(this) {
                        @Override
                        public void calculator() {
                            output = dx * Math.tan(gamma) - (9.81 * dx * dx) /
                                (2 * s * s * Math.cos(gamma) * Math.cos(gamma));
                        }
                    },
                    //b2 index (3)
                    new Calculator(this) {
                        @Override
                        public void calculator() {
                            output = -v * Math.sin(alpha);
                        }
                    },
                    //c2 index (4)
                    new Calculator(this) {
                        @Override
                        public void calculator() {
                            output = x * Math.tan(alpha) - (9.81 * x * x) /
                                (2 * v * v * Math.cos(alpha) * Math.cos(alpha));
                        }
                    }
                );

                threadListRunner(calculations);

                List<myThread> calculations2 = Arrays.asList(
                    //tA = root(a1, b1, c1) index (0)
                    new RootFinder(a1, calculations.get(1).getOutput()
                        , calculations.get(2).getOutput()),
                    //tB = root(a1, b2, c2) index (1)
                    new RootFinder(a1, calculations.get(3).getOutput()
                        , calculations.get(4).getOutput())
                );

                threadListRunner(calculations2);

                //outputs
                xCollide = x;
                yCollide = calculations.get(0).getOutput();
                timeCollide = calculations2.get(1).getOutput();
                timeToStart = Math.abs(calculations2.get(1).getOutput()
                    - calculations2.get(0).getOutput());
            }
        } else {
            //Automatically issue warning to residents.
            System.err.println("Missile cannot be stopped!");
            timeToStart = -10000;
        }


    }

    public double getxCollide() {
        return xCollide;
    }

    public double getyCollide() {
        return yCollide;
    }

    public double getTimeCollide() {
        return timeCollide;
    }

    public double getTimeToStart() {
        return timeToStart;
    }

    private void threadListRunner(List<myThread> threads){
        List<Thread> st = threads.stream().map(Thread::new).collect(Collectors.toList());
        st.stream().forEach(Thread::start);
        for (Thread th : st){
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
abstract class myThread implements Runnable{
    protected double output;

    public double getOutput(){
        return output;
    }
}
abstract class Calculator extends myThread{
    protected final MathsModule mm;

    public Calculator(MathsModule mm){
        this.mm = mm;
        this.output = 0;
    }

    abstract public void calculator();

    public void run(){
        calculator();
    }
}
class A extends Calculator{
    public A(MathsModule mm){
        super(mm);
    }
    public void calculator(){
        double aTop = Math.pow(mm.getS() * Math.cos(mm.getGamma()),2)
            - Math.pow(mm.getV() * Math.cos(mm.getAlpha()),2);
        double aBottom = Math.pow(mm.getV() * Math.cos(mm.getGamma()) * Math.cos(mm.getAlpha()),2);
        output = aTop / aBottom;
    }
}
class B extends Calculator{
    public B(MathsModule mm){
        super(mm);
    }
    public void calculator(){
        double bTop =  (2 * mm.getS() * mm.getS() * Math.cos(mm.getGamma()))
            * (Math.sin(mm.getGamma()) +  Math.cos(mm.getGamma()) * Math.tan(mm.getAlpha()))
            - 2 * mm.getDistanceAway() * 9.81;
        double bBottom = 9.81 * Math.cos(mm.getGamma()) * Math.cos(mm.getGamma());
        output = - bTop / bBottom;
    }
}
class C extends Calculator{
    public C(MathsModule mm){
        super(mm);
    }
    public void calculator(){
        double cTop = 2 * mm.getDistanceAway() * mm.getS() * mm.getS()
            * Math.cos(mm.getGamma()) * Math.sin(mm.getGamma())
            - 9.81 * mm.getDistanceAway() * mm.getDistanceAway();
        double cBottom = 9.81 * Math.cos(mm.getGamma()) * Math.cos(mm.getGamma());
        output = cTop / cBottom;
    }
}
class Root extends myThread{
    protected final double a;
    protected final double b;
    protected final double c;

    public Root(double a, double b, double c){
        this.a = a; this. b = b; this.c = c;
        this.output = 0;
    }
    public Root(double a, double b, double c, double output){
        this(a, b, c);
        this.output = output;
    }
    public boolean hasRoots(){
        return output >= 0;
    }
    public void run(){
        output = b * b - 4 * a * c;
    }
}
class RootFinder extends Root{
    public RootFinder(double a, double b, double c){
        super(a, b, c);
        super.run();
    }
    public RootFinder(Root rt){
        super(rt.a, rt.b, rt.c, rt.output);
    }

    @Override
    public void run(){
        output = (- b - Math.sqrt(output)) / ( 2 * a);
    }
}