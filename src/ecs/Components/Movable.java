package ecs.Components;

import ecs.Direction;

public class Movable extends Component {


    public Direction moving;
    public double moveInterval; // seconds
    public double elapsedInterval;

    public Movable(Direction moving, double moveInterval) {
        this.moving = moving;
        this.moveInterval =moveInterval;
    }
}
