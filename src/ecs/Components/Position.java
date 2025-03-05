package ecs.Components;

import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class Position extends Component {

//    public float posX;
//    public float posY;
    public Vector2f vector;
    public Position(float x, float y) {
//        this.posX = x;
//        this.posY = y;
        this.vector = new Vector2f(x, y);
    }

    public float getPosX() {
        return this.vector.x;
        // return this.posX;
    }

    public float getPosY() {
        return  this.vector.y;
        // return this.posY;
    }
}
