package ecs.Entities;
import ecs.Components.Rotatable;
import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class SpaceShip {

    public static Entity create(Texture texSpaceShip, float x, float y, float rotation) {
        var spaceship = new Entity();

        //        spaceship.add(new ecs.Components.Position(x,y));
//        spaceship.add(new ecs.Components.Collision());
        spaceship.add(new ecs.Components.Appearance(texSpaceShip, Color.WHITE));
        spaceship.add(new ecs.Components.SpaceShip(x, y));
        spaceship.add(new ecs.Components.Rotatable(rotation));
        spaceship.add(new ecs.Components.KeyboardControlled(
                Map.of(
                        GLFW_KEY_LEFT, Rotatable.Direction.Left,
                        GLFW_KEY_RIGHT, Rotatable.Direction.Right
                )));

        return spaceship;
    }

}
