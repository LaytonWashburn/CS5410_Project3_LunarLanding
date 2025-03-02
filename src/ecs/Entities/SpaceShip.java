package ecs.Entities;
import ecs.Components.Rotatable;
import ecs.Direction;
import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class SpaceShip {

    public static Entity create(Texture texSpaceShip, float x, float y, float rotation) {
        final double MOVE_INTERVAL = .150; // seconds

        var spaceship = new Entity();

        spaceship.add(new ecs.Components.SpaceShip());
        spaceship.add(new ecs.Components.Appearance(texSpaceShip, Color.WHITE));
        spaceship.add(new ecs.Components.Gravity());
        spaceship.add(new ecs.Components.Collision());
        spaceship.add(new ecs.Components.Position(x, y));
        spaceship.add(new ecs.Components.Rotatable(rotation));
        spaceship.add(new ecs.Components.Movable(Direction.None, MOVE_INTERVAL));
        spaceship.add(new ecs.Components.KeyboardControlled(
                Map.of(
                        GLFW_KEY_LEFT, Direction.Left,
                        GLFW_KEY_RIGHT, Direction.Right,
                        GLFW_KEY_UP, Direction.Up
                )));

        return spaceship;
    }

}
