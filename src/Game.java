import edu.usu.graphics.*;
import utils.GameScores;
import utils.Serializer;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class Game {

    private final Graphics2D graphics;
    private HashMap<GameStateEnum, IGameState> states;
    private IGameState currentState;
    GameStateEnum nextStateEnum = GameStateEnum.MainMenu;
    GameStateEnum prevStateEnum = GameStateEnum.MainMenu;
    public Serializer serializer;
    public GameScores gameScores;

    public Game(Graphics2D graphics) {

        this.graphics = graphics;
        this.gameScores = new GameScores();
    }


    public void initialize() {

        serializer = new Serializer();

        // Map the states to the views
        states = new HashMap<>() {
            {
                put(GameStateEnum.MainMenu, new MainMenuView());
                put(GameStateEnum.GamePlay, new GamePlayView(serializer, gameScores));

                put(GameStateEnum.HighScores, new HighScoresView(serializer, gameScores));

                put(GameStateEnum.Controls, new ControlsView());
                put(GameStateEnum.Help, new HelpView());
                put(GameStateEnum.About, new AboutView());
            }
        };

        // Give all game states a chance to initialize, other than the constructor
        for (var state : states.values()) {
            state.initialize(graphics);
        }


        currentState = states.get(GameStateEnum.MainMenu);
        currentState.initializeSession();


    }

    public void shutdown() {
        this.serializer.shutdown();
    }

    public void run() {
        // Grab the first time
        double previousTime = glfwGetTime();

        while (!graphics.shouldClose()) {
            double currentTime = glfwGetTime();
            double elapsedTime = currentTime - previousTime;    // elapsed time is in seconds
            previousTime = currentTime;

            processInput(elapsedTime);
            update(elapsedTime);
            render(elapsedTime);
        }
    }

    private void processInput(double elapsedTime) {
        // Poll for window events: required in order for window, keyboard, etc events are captured.
        glfwPollEvents();

        // Process the input got the current state
        // Calls the views' internal processInput method
        nextStateEnum = currentState.processInput(elapsedTime);
    }

    private void update(double elapsedTime) {
        // Special case for exiting the game
        if (nextStateEnum == GameStateEnum.Quit) {
            glfwSetWindowShouldClose(graphics.getWindow(), true);
        } else {
            if (nextStateEnum == prevStateEnum) {
                currentState.update(elapsedTime);
            } else {
                currentState = states.get(nextStateEnum);
                currentState.initializeSession();
                prevStateEnum = nextStateEnum;
            }
        }
    }

    // Render the current state
    // Calls the current state's internal render method
    private void render(double elapsedTime) {
        graphics.begin();
        currentState.render(elapsedTime);
        graphics.end();
    }
}
