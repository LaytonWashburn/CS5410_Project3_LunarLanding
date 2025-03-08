import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;

import java.awt.*;
import java.util.HashMap;
import static org.lwjgl.glfw.GLFW.*;

public class ControlsView extends GameStateView {

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.HighScores;
    private Font font;
    private Font fontSelected;
    private int access;

    // Define states for the controls
    private enum ControlState {
        ROTATE_LEFT,
        ROTATE_RIGHT,
        THRUST;

        public ControlState next() {
            int nextOrdinal = (this.ordinal() + 1) % ControlState.values().length;
            return ControlState.values()[nextOrdinal];
        }

        public ControlState previous() {
            int previousOrdinal = (this.ordinal() - 1) % ControlState.values().length;
            if (previousOrdinal < 0) {
                previousOrdinal = THRUST.ordinal();
            }
            return ControlState.values()[previousOrdinal];
        }
    }

    private HashMap<ControlState, Integer> controlMap;

    private int ROTATE_LEFT;
    private int ROTATE_RIGHT;
    private int THRUST;
    private ControlState currentState;
    private boolean setNewConfigurationKey;


    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        // Initialize the controls
        ROTATE_LEFT = GLFW_KEY_LEFT;
        ROTATE_RIGHT = GLFW_KEY_RIGHT;
        THRUST = GLFW_KEY_UP;
        currentState = ControlState.ROTATE_LEFT;

        controlMap = new HashMap<>();
        controlMap.put(ControlState.ROTATE_LEFT, this.ROTATE_LEFT);
        controlMap.put(ControlState.ROTATE_RIGHT, this.ROTATE_RIGHT);
        controlMap.put(ControlState.THRUST, this.THRUST);

        font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);
        fontSelected = new Font("resources/fonts/Roboto-Bold.ttf", 48, false);
        inputKeyboard = new KeyboardInput(graphics.getWindow());

        // Arrow keys to navigate the menu
        inputKeyboard.registerCommand(GLFW_KEY_UP, true, (double elapsedTime) -> {
            currentState = currentState.previous();
        });

        inputKeyboard.registerCommand(GLFW_KEY_DOWN, true, (double elapsedTime) -> {
            currentState = currentState.next();
        });

        // When ESC is pressed, set the appropriate new game state
        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> {
            nextGameState = GameStateEnum.MainMenu;
        });

         access = 0;
    }

    @Override
    public void initializeSession() {
        nextGameState = GameStateEnum.Controls;
    }

    @Override
    public GameStateEnum processInput(double elapsedTime) {

        // Updating the keyboard can change the nextGameState
        inputKeyboard.update(elapsedTime);

        return nextGameState;
    }

    @Override
    public void update(double elapsedTime) {
    }

    @Override
    public void render(double elapsedTime) {
        final String message = "Customize the Controls";
        final float height = 0.075f;
        final float width = font.measureTextWidth(message, height);

        graphics.drawTextByHeight(font, message, 0.0f - width / 2, -0.85f, height, Color.BLACK);

        final float HEIGHT_MENU_ITEM = 0.075f;
        float top = -0.25f;

        top = renderMenuItem(currentState == ControlState.ROTATE_LEFT ? fontSelected : font, "Rotate Left : Left Arrow ", top, HEIGHT_MENU_ITEM, currentState == ControlState.ROTATE_LEFT ? Color.YELLOW : Color.BLUE);
        top = renderMenuItem(currentState == ControlState.ROTATE_RIGHT ? fontSelected : font, "Rotate Right : Right Arrow", top, HEIGHT_MENU_ITEM, currentState == ControlState.ROTATE_RIGHT ? Color.YELLOW : Color.BLUE);
        renderMenuItem(currentState == ControlState.THRUST ? fontSelected : font, "Thrust : Up Arrow", top, HEIGHT_MENU_ITEM, currentState == ControlState.THRUST ? Color.YELLOW : Color.BLUE);

    }

    /**
     * Centers the text horizontally, at the specified top position.
     * It also returns the vertical position to draw the next menu item
     */
    private float renderMenuItem(Font font, String text, float top, float height, Color color) {
        float width = font.measureTextWidth(text, height);
        graphics.drawTextByHeight(font, text, 0.0f - width / 2, top, height, color);

        return top + height;
    }
}
