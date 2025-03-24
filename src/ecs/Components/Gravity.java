package ecs.Components;

import org.joml.Vector2f;

public class Gravity extends Component{

    public Vector2f gravity =  new Vector2f(0, 0.001f);

    public Gravity(){}
}
