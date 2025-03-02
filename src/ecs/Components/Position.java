package ecs.Components;

import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class Position extends Component {

    public float posX;
    public float posY;
    public Vector2f vector;
    public Position(float x, float y) {
        this.posX = x;
        this.posY = y;
//        this.vector.x = x;
//        this.vector.y = y;
    }
//    public void setPosition(Vector2f vec){
//
//    }

    public float getPosX() {

        return this.posX;
    }

    public float getPosY() {

        return this.posY;
    }
}
