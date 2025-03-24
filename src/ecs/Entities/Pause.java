package ecs.Entities;

import ecs.States;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class Pause {

    public static Entity create(){
        var pause = new Entity();

        pause.add(new ecs.Components.Pause(
                Map.of(
                        GLFW_KEY_ESCAPE, ecs.Components.Pause.PauseState.RESUME
                )
        ));

        return pause;
    }
}
