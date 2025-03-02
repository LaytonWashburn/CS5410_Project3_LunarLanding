package ecs.Systems;

import ecs.Components.Movable;
import ecs.Components.Rotatable;
import org.lwjgl.glfw.GLFW;
import ecs.Direction;
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
            var moving = entity.get(ecs.Components.Movable.class);


            if (glfwGetKey(window, input.lookup.get(Direction.Left)) == GLFW_PRESS) {
                rotatable.rotating = input.keys.get(GLFW_KEY_LEFT);
            }
            else if (glfwGetKey(window, input.lookup.get(Direction.Right)) == GLFW_PRESS) {
                rotatable.rotating = input.keys.get(GLFW_KEY_RIGHT);
            }
            else if (glfwGetKey(window, input.lookup.get(Direction.Up)) == GLFW_PRESS) {
                moving.moving = input.keys.get(GLFW_KEY_UP);
            }
            else {
                rotatable.rotating = Direction.None;
                moving.moving = Direction.None;

            }
        }
    }
}
