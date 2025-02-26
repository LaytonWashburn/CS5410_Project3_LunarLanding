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
    private float ELEVATION;
    private float RUN_MIDPOINT_ALGORITHM_TIMES;
    private ArrayList<Vector3f> points;

    // Current Game Level
    private enum GameLevel {
        LEVEL_1,
        LEVEL_2;
    }

    GameLevel currentLevel;

    @Override
    public void initialize(Graphics2D graphics) {
        super.initialize(graphics);

        font = new Font("resources/fonts/Roboto-Regular.ttf", 48, false);

        inputKeyboard = new KeyboardInput(graphics.getWindow());
        // When ESC is pressed, set the appropriate new game state
        inputKeyboard.registerCommand(GLFW_KEY_ESCAPE, true, (double elapsedTime) -> {
            nextGameState = GameStateEnum.MainMenu;
        });



        currentLevel = GameLevel.LEVEL_1; // Set the current game level
        ELEVATION = 0.1f;
        RUN_MIDPOINT_ALGORITHM_TIMES = 5;
        points = new ArrayList<>(); // Make array
        generateTerrain();

    }

    @Override
    public void initializeSession() {
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
    }

    /**
     *
     * @param vec1
     * @param vec2
     * @return
     */
    private Vector3f midpointAlgorithm(Vector3f vec1, Vector3f vec2){

        float tempX = (vec1.x + vec2.x) / 2;
        float tempY = (vec1.y + vec2.y) / 2;
        tempY += ELEVATION;
        ELEVATION *= -1;

        return new Vector3f(tempX, tempY, 0);
    }

    /**
     * Add the two endpoints; randomly choose their elevations
     * Add the safe zone(s)
     * – Randomly choose the starting x, y position
     * – End x = Start x + some distance; End y = Start y
     * Apply the midpoint algorithm some number of times
     * Other notes
     * – Don’t apply on the lines for the safe zone(s)
     * – Don’t let y (elevation) go below 0
     * – Ensure safe zone is at least 15% away from the borders
     */
    private void generateTerrain(){

        Vector3f vec1 = new Vector3f(-1.0f, 0.0f, 0);;
        Vector3f vec2 = new Vector3f(1.0f, 0.0f, 0);

        // Add two endpoints
        this.points.add(vec1);
        this.points.add(vec2);

        ArrayList<Vector3f> temp;
        for(int i = 0; i < this.RUN_MIDPOINT_ALGORITHM_TIMES; i++){

            temp = new ArrayList<>(); // Make temporary array list to replace points array list at end of each iteration

            // Loop through all points except the last and get the midpoint
            for(int item = 0; item < this.points.size() - 1; item++){

                temp.add(this.points.get(item));
                Vector3f tempPoint = midpointAlgorithm(this.points.get(item), this.points.get(item+1));
                temp.add(tempPoint);

            }
            temp.add(this.points.getLast()); // Add last point into temp
            this.points = temp; // Copy temp into points array list
        }
    }



    @Override
    public void render(double elapsedTime) {
        final String message = "Isn't this game fun!";
        final float height = 0.075f;
        final float width = font.measureTextWidth(message, height);


        for(int position = 0; position < this.points.size()-1; position++){
            graphics.draw(this.points.get(position), this.points.get(position+1), Color.RED);
        }

        graphics.drawTextByHeight(font, message, 0.0f - width / 2, 0 - height / 2, height, Color.YELLOW);
    }
}
