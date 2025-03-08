import ecs.Entities.Entity;
import ecs.Systems.Pause;
import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class HighScoresView extends GameStateView {

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.HighScores;
    private Font font;
    private GameScores gameScores;
    private Serializer serializer;



    public HighScoresView( Serializer serializer, GameScores scores){
        this.serializer = serializer;
        this.gameScores = scores;
    }

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
        nextGameState = GameStateEnum.HighScores;
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
        final String message = "These are the high scores";
        final float height = 0.075f;
        final float width = font.measureTextWidth(message, height);
        float top = -1.0F + 2 * height;

        graphics.drawTextByHeight(font, message, 0.0f - width / 2, top, height, Color.YELLOW);
        top += 0.3F;
        this.serializer.loadGameState(this.gameScores);
        for(Integer score: this.gameScores.scores){
            graphics.drawTextByHeight(font, "Here is a score: " + score, 0.0f - width / 2, top, height, Color.YELLOW);
            System.out.println();
            top += 0.1F;
        }

    }
}
