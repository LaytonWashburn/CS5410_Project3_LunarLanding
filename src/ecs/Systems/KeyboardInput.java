package ecs.Systems;

import ecs.Components.Movable;
import ecs.Components.Rotatable;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class KeyboardInput extends System {

    private final long window;

    public KeyboardInput(long window) {
        super(ecs.Components.KeyboardControlled.class);

        this.window = window;
    }

    @Override
    public void update(double gameTime) {

        for (var entity : entities.values()) {
            var rotatable = entity.get(ecs.Components.Rotatable.class);
            var input = entity.get(ecs.Components.KeyboardControlled.class);
            var spaceShip = entity.get(ecs.Components.SpaceShip.class);


            if (glfwGetKey(window, input.lookup.get(Rotatable.Direction.Left)) == GLFW_PRESS) {
                rotatable.rotating = input.keys.get(GLFW_KEY_LEFT);
            }
            else if (glfwGetKey(window, input.lookup.get(Rotatable.Direction.Right)) == GLFW_PRESS) {
                rotatable.rotating = input.keys.get(GLFW_KEY_RIGHT);
            }
            else {
                rotatable.rotating = Rotatable.Direction.None;
            }
        }
    }
}
