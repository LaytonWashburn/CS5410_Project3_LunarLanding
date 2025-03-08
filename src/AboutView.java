import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;

import static org.lwjgl.glfw.GLFW.*;

public class AboutView extends GameStateView {

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.About;
    private Font font;

    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);

        inputKeyboard = new KeyboardInput(graphics.getWindow());
        // When ESC is pressed, set the appropriate new game state
        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> {
            nextGameState = GameStateEnum.MainMenu;
        });
    }

    @Override
    public void initializeSession() {
        nextGameState = GameStateEnum.About;
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
        final String message1 = "Welcome to the Lunar Lander!.";
        final String message2 = "The goal of this game is to safely land the Lunar Lander";
        final String message3 = "Get the Heads Up Display to show all green to land safely";
        final String message4 = "Use the left and right arrow keys to rotate the lander";
        final String message5 = "Use up arrow for thrust";
        final String message6 = "Good Luck!";
        final float height = 0.075f;
        final float width = font.measureTextWidth(message1, height);

        graphics.drawTextByHeight(font, message1, -0.75f, -0.75f, height, Color.YELLOW);

        float top = -0.5f;
        top = drawMessage(message2, -0.75f, top, height);
        top = drawMessage(message3, -0.75f, top, height);
        top = drawMessage(message4, -0.75f, top, height);
        top = drawMessage(message5, -0.75f, top, height);
        drawMessage(message6, -0.75f, 0.25f, height);
    }

    public float drawMessage(String message, float left, float top, float height){
        graphics.drawTextByHeight(font, message, left, top, height, Color.YELLOW);
        return top  + height;
    }
}
