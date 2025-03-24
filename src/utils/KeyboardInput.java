package utils;

import ecs.Components.Pause;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardInput {

    /**
     * The type of method to invoke when a keyboard event is invoked
     */
    public interface ICommand {
        Pause.PauseState invoke(double elapsedTime, Pause.PauseState state);
    }

    public KeyboardInput(long window) {
        this.window = window;
    }

    public void registerCommand(int key, boolean keyPressOnly, ICommand callback) {
        commandEntries.put(key, new CommandEntry(key, keyPressOnly, callback));
        // Start out by assuming the key isn't currently pressed
        keysPressed.put(key, false);
    }

    /**
     * Go through all the registered command and invoke the callbacks as appropriate
     */
    public Pause.PauseState update(double elapsedTime, Pause.PauseState state) {
        System.out.println(state);
        for (var entry : commandEntries.entrySet()) {
            if (entry.getValue().keyPressOnly && isKeyNewlyPressed(entry.getValue().key)) {
                state = entry.getValue().callback.invoke(elapsedTime, state);
            } else if (!entry.getValue().keyPressOnly && glfwGetKey(window, entry.getKey()) == GLFW_PRESS) {
                state = entry.getValue().callback.invoke(elapsedTime, state);
            }

            // For the next time around, remember the current state of the key (pressed or not)
            keysPressed.put(entry.getKey(), glfwGetKey(window, entry.getKey()) == GLFW_PRESS);
        }
        return state;
    }

    /**
     * Returns true if the key is newly pressed.  If it was already pressed, then
     * it returns false
     */
    private boolean isKeyNewlyPressed(int key) {
        return (glfwGetKey(window, key) == GLFW_PRESS) && !keysPressed.get(key);
    }

    private final long window;
    // Table of registered callbacks
    private final HashMap<Integer, CommandEntry> commandEntries = new HashMap<>();
    // Table of registered callback keys previous pressed state
    private final HashMap<Integer, Boolean> keysPressed = new HashMap<>();

    /**
     * Used to keep track of the details associated with a registered command
     */
    private record CommandEntry(int key, boolean keyPressOnly, ICommand callback) {
    }


    /**
     * getCommand
     * @param key
     * @return CommandEntry or null
     * Returns the item in the hashmap or null if it doesn't exist
     */
    public CommandEntry getCommand(int key){
        if(this.commandEntries.containsKey(key)){
            return this.commandEntries.get(key);
        }
        return null;
    }

    public void clear(){
        this.commandEntries.clear();
    }

}

