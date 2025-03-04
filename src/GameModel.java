import ecs.Entities.*;
import ecs.Entities.LunarLander;
import ecs.Systems.*;
import ecs.Systems.KeyboardInput;
import edu.usu.graphics.*;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.List;
import ecs.Components.Segments.Segment;
public class GameModel {

    // Enum for what level the game is currently on
    public enum LEVEL{
        ONE,
        TWO
    }

    private int RUN_MIDPOINT_ALGORITHM_TIMES = 7;
    private LEVEL gameLevel;
    private final List<Entity> removeThese = new ArrayList<>();
    private final List<Entity> addThese = new ArrayList<>();


    private ecs.Systems.Collision sysCollision;
    private ecs.Systems.Movement sysMovement;
    private ecs.Systems.KeyboardInput sysKeyboardInput;
    private LunarLanderRenderer sysLunarLanderRenderer;
    private ecs.Systems.TerrainRenderer sysTerrainRenderer;
    private ecs.Systems.BackgroundRenderer sysBackGroundRenderer;
    private ecs.Systems.HeadsUpDisplayRenderer sysHeadsUpDisplayRenderer;
    private ecs.Systems.Rotation sysRotation;
    private ecs.Systems.Gravity sysGravity;

    public void initialize(Graphics2D graphics) {

        var texSpaceShip = new Texture("resources/characters/spaceship.png");
        var texBackground = new Texture("resources/images/background.png");
        Font fontHeadsUpDisplay = new Font("resources/fonts/Roboto-bold.ttf", 64, true);

        gameLevel = LEVEL.TWO; // Set the game level, by default make it level ONE
        sysCollision = new Collision((Entity entity) -> {
            // removeEntity(entity); // Save callback state for when the spaceship needs to get removed
        });
        sysMovement = new Movement(); // Movement System
        sysKeyboardInput = new KeyboardInput(graphics.getWindow()); // KeyboardInput System
        sysBackGroundRenderer = new BackgroundRenderer(graphics, texBackground);
        sysTerrainRenderer = new TerrainRenderer(graphics); // TerrainRenderer System
        sysLunarLanderRenderer = new LunarLanderRenderer(graphics); //
        sysHeadsUpDisplayRenderer = new HeadsUpDisplayRenderer(graphics, fontHeadsUpDisplay);
        sysRotation = new Rotation();
        sysGravity = new Gravity();

        initializeTerrain();
        initializaSpaceShip(texSpaceShip);

    }

    public void update(double elapsedTime) {
        // Because ECS framework, input processing is now part of the update
        sysKeyboardInput.update(elapsedTime);

        // Now do the normal update
        sysMovement.update(elapsedTime);
        sysCollision.update(elapsedTime);

        for (var entity : removeThese) {
            removeEntity(entity);
        }
        removeThese.clear();

        for (var entity : addThese) {
            addEntity(entity);
        }
        addThese.clear();

        // Because ECS framework, rendering is now part of the update
        sysTerrainRenderer.update(elapsedTime); // Render the terrain
        // Update the background
        sysBackGroundRenderer.update(elapsedTime);
        sysLunarLanderRenderer.update(elapsedTime); // Render the lunar lander
        sysHeadsUpDisplayRenderer.update(elapsedTime);
        sysRotation.update(elapsedTime); // Update the rotation
        sysGravity.update(elapsedTime); // Update the gravity
    }



    private void addEntity(Entity entity) {
        sysKeyboardInput.add(entity);
        sysMovement.add(entity);
        sysCollision.add(entity);
        sysTerrainRenderer.add(entity);
        sysLunarLanderRenderer.add(entity);
        sysRotation.add(entity);
        sysGravity.add(entity);
        sysBackGroundRenderer.add(entity);
        sysHeadsUpDisplayRenderer.add(entity);
    }

    private void removeEntity(Entity entity) {
        sysKeyboardInput.remove(entity.getId());
        sysMovement.remove(entity.getId());
        sysCollision.remove(entity.getId());
        sysTerrainRenderer.remove(entity.getId());
        sysLunarLanderRenderer.remove(entity.getId());
        sysRotation.remove(entity.getId());
        sysGravity.remove(entity.getId());
        sysBackGroundRenderer.remove(entity.getId());
        sysHeadsUpDisplayRenderer.remove(entity.getId());
    }


    /**
     *
     * @param vec1
     * @param vec2
     * @return
     */
    private double getRandomElevation(Vector3f vec1, Vector3f vec2){
        MyRandom rnd = new MyRandom(); // Random number generator kind of

        // Computer the elevation
        double rg = rnd.nextGaussian(0, 1); // Gaussian random number, mean of 0 and variance of 1
        double s = 0.25; // 0.175 * iteration; // Change the value of s at each level of refinement
        double r = s * rg * Math.abs((vec2.x - vec1.x));
        double y = ((vec1.y + vec2.y) * 0.5) + r; // New elevation
        return Math.max(0, y); // Don't let the elevation go below 0
    }

    /**
     *
     * @param vec1
     * @param vec2
     * @return
     */
    private Vector3f midpointAlgorithm(Vector3f vec1, Vector3f vec2, int iteration){
        double y = getRandomElevation(vec1, vec2);
        double newX =  0.5 * (vec1.x + vec2.x);  // Find the new x coordinate
        return new Vector3f((float)newX, (float)y, 0);
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
    private void generateTerrain(Entity terrain){

        MyRandom rnd = new MyRandom(); // Make a random number generate object

        var segments =  terrain.get(ecs.Components.Segments.class); // Get the segments from the terrain
        ArrayList<Segment> temp; // Temp array to help copy the new segments into

        // Number of safe zones depends on the level
        Segment safeZone1;

        float safeZone1X = rnd.nextRange(-0.75f, 0.75f); // Make sure the safe zone if 15% away from the edge of the screen
        float safeZone1Y = rnd.nextRange(0, 0.5f);

        Vector3f safeZone1Vec1 = new Vector3f(safeZone1X, safeZone1Y, 0);
        Vector3f safeZone1Vec2 = new Vector3f(safeZone1X + 0.2f, safeZone1Y, 0);

        safeZone1 = new Segment(safeZone1Vec1,
                                safeZone1Vec2,
                                true);

        segments.add(safeZone1);


        // Choose two endpoints
        // Change this to random elevations
        float elevation1 = 0.01f;// rnd.nextRange(0, 1);
        float elevation2 = 0.02f;// rnd.nextRange(0, 1);

        Vector3f startPt = new Vector3f(-1, elevation1, 0);
        Vector3f endPt = new Vector3f(1, elevation2, 0);

        if(gameLevel == LEVEL.TWO){
            Segment seg1 = new Segment(startPt, safeZone1Vec1, false);
            Segment seg2 = new Segment(safeZone1Vec2, endPt, false);
            segments.add(seg1);
            segments.add(seg2);
        }


        for(int i = 0; i < RUN_MIDPOINT_ALGORITHM_TIMES; i++){ // Iterate over the terrain

            temp = new ArrayList<>(); // Make temporary array list to replace points array list at end of each iteration

            for(Segment segment: segments.getSegments()){ // Iterate over the segments
                if(!segment.safeZone){ // If not the safe zone, apply the midpoint algorithm
                    Vector3f tempPoint = midpointAlgorithm(segment.startPt, segment.endPt, i); // Call midpoint algorithm
                    temp.add(new Segment(segment.startPt, tempPoint, false)); // Add new segment
                    temp.add(new Segment(tempPoint, segment.endPt, false)); // Add new segment
                } else {
                    temp.add(new Segment(segment.startPt, segment.endPt, true));
                }
            }
            segments.setSegments(temp); // Copy new list of segments into the actual terrain's segments
        }

    }


    private void initializeTerrain(){ // Texture triangle

        var terrain = Terrain.create();
        addEntity(terrain);
        generateTerrain(terrain);

    }

    // Initialize the Lunar Lander
    private void initializaSpaceShip(Texture texSpaceship){
        // Initialize the spaceship and rotate it on it's side
        var spaceship = LunarLander.create(texSpaceship, 0.0f, -0.5f, (0.5f * (float)Math.PI));
        addEntity(spaceship);
    }

}
