package ecs.Components;
import ecs.States;

public class Rotatable extends Component{

    private float rotation;
    public States rotating;
    public boolean isRotating;

    public Rotatable(float rotation) {
        this.rotation = rotation;
        this.isRotating = true;
    }

    public float getRotation(){
        return this.rotation;
    }

    public void setRotation(float rotation){
        this.rotation = rotation;
    }
}
