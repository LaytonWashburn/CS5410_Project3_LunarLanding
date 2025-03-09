package ecs.Systems;

import ecs.Components.LunarLander;
import ecs.Components.Particles;
import ecs.Entities.Entity;
import ecs.Entities.Pause;
import ecs.States;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class KeyboardInput extends System {

    private final long window;

    public interface IpauseGame {
        void invoke(Entity entity);
    }

    private final KeyboardInput.IpauseGame IpauseGame;

    public KeyboardInput(long window, IpauseGame callback) {
        super(ecs.Components.KeyboardControlled.class,
                ecs.Components.LunarLander.class);

        this.window = window;
        this.IpauseGame = callback;
    }

    @Override
    public void update(double gameTime) {

        boolean fireLambda = false;
        for (var entity : entities.values()) {
            var rotatable = entity.get(ecs.Components.Rotatable.class);
            var input = entity.get(ecs.Components.KeyboardControlled.class);
            var moving = entity.get(ecs.Components.Movable.class);
            var sounds = entity.get(ecs.Components.Sounds.class);

            if(input.enabled){
                // Register the key presses
                if (glfwGetKey(window, input.lookup.get(States.Left)) == GLFW_PRESS) {
                    rotatable.rotating = input.keys.get(GLFW_KEY_LEFT);
                }
                else if (glfwGetKey(window, input.lookup.get(States.Right)) == GLFW_PRESS) {
                    rotatable.rotating = input.keys.get(GLFW_KEY_RIGHT);
                }
                else if (glfwGetKey(window, input.lookup.get(States.Up)) == GLFW_PRESS) {
                    moving.moving = input.keys.get(GLFW_KEY_UP);
                    sounds.playThrust();
                }

                else {
                    rotatable.rotating = States.None;
                    moving.moving = States.None;
                }
            }
            if (glfwGetKey(window, input.lookup.get(States.PAUSE)) == GLFW_PRESS) {
                fireLambda = true;
            }
        }

        if(fireLambda){
            Entity pause = Pause.create();
            this.IpauseGame.invoke(pause); // Create a pause entity
        }
    }
}
