package ecs.Components;

import org.joml.Vector2f;

public class LunarLander extends Component{


//    // This should be a vector
//    public double speed; // m /s

    public double fuel; // gallons
    public double angle; // In radians
    public boolean alive;
    public boolean shipInSafeZone;
    public Vector2f momentum;

    // Ship starts out with
    // 20.0 gal fuel
    // 0 speed
    // 90 degree / pi/2 angle
    // Not exploded
    public LunarLander(){

        fuel = 20.0f;
        angle = (float)Math.PI / 2.0f; // Starts out on it's side
        momentum = new Vector2f(0.0f, 0.0f);
        shipInSafeZone = false;
        alive = true;

    }

}
