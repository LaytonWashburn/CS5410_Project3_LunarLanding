package ecs.Systems;

import ecs.Entities.Entity;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import utils.IReturn;
import utils.KeyboardInput;

import static org.lwjgl.glfw.GLFW.*;

public class Pause extends System{


    public interface IpauseGame {
        void invoke(Entity entity);
    }



    private final Pause.IpauseGame IpauseGame;
    private final utils.IReturn IReturn;
    private Graphics2D graphics;
    long window;

    KeyboardInput keyboardInput;

    public Pause(Graphics2D graphics, long window, IpauseGame ipauseGame, IReturn game){
        super(ecs.Components.Pause.class); // Look for the pause component
        this.IpauseGame = ipauseGame;
        this.graphics = graphics;
        this.window = window;
        this.keyboardInput = new KeyboardInput(this.window);
        this.IReturn = game;

        this.keyboardInput.registerCommand(GLFW_KEY_UP, true, (double elapsedTime, ecs.Components.Pause.PauseState state) -> {
            java.lang.System.out.println("In the arrow up");
            state = state.previous();
            return state;
        });

        this.keyboardInput.registerCommand(GLFW_KEY_DOWN, true, (double elapsedTime, ecs.Components.Pause.PauseState state) -> {
            state = state.next();
            return state;
        });
    }



    @Override
    public void update(double elapsedTime) {

        boolean invoke = false;
        Entity temp = null;
        for (var entity : entities.values()) {

            // entity.get(ecs.Components.Pause.class).currState = this.keyboardInput.update(elapsedTime, entity.get(ecs.Components.Pause.class).currState);
            // entity.get(ecs.Components.Pause.class).currState = ecs.Components.Pause.PauseState.QUIT;
            if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS && entity.get(ecs.Components.Pause.class).currState == ecs.Components.Pause.PauseState.QUIT) {
                entity.get(ecs.Components.Pause.class).currState = entity.get(ecs.Components.Pause.class).currState.previous();
            }

            if (glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS && entity.get(ecs.Components.Pause.class).currState == ecs.Components.Pause.PauseState.RESUME) {
                entity.get(ecs.Components.Pause.class).currState = entity.get(ecs.Components.Pause.class).currState.next();
            }

            if (glfwGetKey(window, GLFW_KEY_ENTER) == GLFW_PRESS) {
                if(entity.get(ecs.Components.Pause.class).currState == ecs.Components.Pause.PauseState.QUIT){
                    //glfwSetWindowShouldClose(graphics.getWindow(), true);
                    this.IReturn.invoke();
                }
                invoke = true;
                temp = entity;
            }

            render(entity);

        }

        if(invoke){
            this.IpauseGame.invoke(temp);
        }

    }

    public void render(Entity entity){
        var pause = entity.get(ecs.Components.Pause.class);

        graphics.drawTextByHeight(pause.fontMenu, "Pause Menu", -0.5f, -0.75f, 0.2f, Color.RED);
        graphics.drawTextByHeight(pause.fontMenu, "Resume", 0.0f - 0.5f / 2, -0.3f, 0.2f, pause.currState == ecs.Components.Pause.PauseState.RESUME ? Color.YELLOW : Color.BLUE);
        graphics.drawTextByHeight(pause.fontMenu, "Quit", 0.0f - 0.5f / 2, -0.1f, 0.2f, pause.currState == ecs.Components.Pause.PauseState.QUIT ? Color.YELLOW : Color.BLUE);
    }
}
