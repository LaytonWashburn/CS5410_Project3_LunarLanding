package ecs.Entities;
import ecs.Direction;
import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;
import org.joml.Vector2f;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class LunarLander {

    public static Entity create(Texture texSpaceShip, float x, float y, float rotation) {
        final double MOVE_INTERVAL = .150; // seconds

        var lunarLander = new Entity();

        lunarLander.add(new ecs.Components.LunarLander());
        lunarLander.add(new ecs.Components.Appearance(texSpaceShip, Color.WHITE));
        lunarLander.add(new ecs.Components.Gravity());
        lunarLander.add(new ecs.Components.Collision());
        lunarLander.add(new ecs.Components.Position(x, y));
        lunarLander.add(new ecs.Components.Rotatable(rotation));
        lunarLander.add(new ecs.Components.Particles(new Vector2f(0, 0),
                                                0.015f, 0.004f,
                                                0.07f, 0.05f,
                                                3, 1));
        lunarLander.add(new ecs.Components.Movable(Direction.None, MOVE_INTERVAL));
        lunarLander.add(new ecs.Components.KeyboardControlled(
                Map.of(
                        GLFW_KEY_LEFT, Direction.Left,
                        GLFW_KEY_RIGHT, Direction.Right,
                        GLFW_KEY_UP, Direction.Up
                )));

        return lunarLander;
    }

}
