import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;
import utils.GameScores;
import utils.Serializer;

public class GamePlayView extends GameStateView {

    private KeyboardInput inputKeyboard;
    private GameStateEnum nextGameState = GameStateEnum.GamePlay;
    private Font font;

    private GameModel gameModel;
    public Serializer serializer;
    public GameScores gameScores;

    public GamePlayView(Serializer serializer, GameScores gameScores){
        this.serializer = serializer;
        this.gameScores = gameScores;
    }


    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);

        inputKeyboard = new KeyboardInput(graphics.getWindow());

//        // When ESC is pressed, set the appropriate new game state
//        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> {
//            nextGameState = GameStateEnum.MainMenu;
//        });

    }

    @Override
    public void initializeSession() {
        gameModel = new GameModel(serializer, gameScores);
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
    public void render(double elapsedTime) {}
}
