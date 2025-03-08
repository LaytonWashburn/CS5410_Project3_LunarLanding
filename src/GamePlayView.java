import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class GamePlayView extends GameStateView {

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.GamePlay;
    private Font font;

    private GameModel gameModel;


    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);

        inputKeyboard = new KeyboardInput(graphics.getWindow());

//        // When ESC is pressed, set the appropriate new game state
//        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> {
//            System.out.println("ESCAPE KEY PRESSED");
//            nextGameState = GameStateEnum.MainMenu;
//        });

    }

    @Override
    public void initializeSession() {
        gameModel = new GameModel();
        gameModel.initialize(graphics);
        nextGameState = GameStateEnum.GamePlay;
    }

    @Override
    public GameStateEnum processInput(double elapsedTime) {
        // Updating the keyboard can change the nextGameState
        inputKeyboard.update(elapsedTime);
        return nextGameState;
    }

    @Override
    public void update(double elapsedTime) {
        gameModel.update(elapsedTime);
    }




    @Override
    public void render(double elapsedTime) {
//        final String message = "Isn't this game fun!";
//        final float height = 0.075f;
//        final float width = font.measureTextWidth(message, height);
//
//
//        for(int position = 0; position < this.points.size()-1; position++){
//            graphics.draw(this.points.get(position), this.points.get(position+1), Color.RED);
//        }
//
//        graphics.drawTextByHeight(font, message, 0.0f - width / 2, 0 - height / 2, height, Color.YELLOW);
    }
}
