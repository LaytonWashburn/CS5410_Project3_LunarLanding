package ecs.Components;

import ecs.States;

public class Movable extends Component {


    public States moving;
    public double moveInterval; // seconds
    public double elapsedInterval;

    public Movable(States moving, double moveInterval) {
        this.moving = moving;
        this.moveInterval =moveInterval;
    }
}
