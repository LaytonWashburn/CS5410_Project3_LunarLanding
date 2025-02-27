import ecs.Components.Points;
import ecs.Entities.*;
import ecs.Systems.*;
import ecs.Systems.KeyboardInput;
import edu.usu.graphics.*;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private final int GRID_SIZE = 50;
    private final int OBSTACLE_COUNT = 15;

    private int FACTOR = 1;
    private int RUN_MIDPOINT_ALGORITHM_TIMES = 6;

    private final List<Entity> removeThese = new ArrayList<>();
    private final List<Entity> addThese = new ArrayList<>();

    private ecs.Systems.Renderer sysRenderer;
    private ecs.Systems.TerrainRenderer terrainRenderer;

    private ecs.Systems.Collision sysCollision;
    private ecs.Systems.Movement sysMovement;
    private ecs.Systems.KeyboardInput sysKeyboardInput;

    public void initialize(Graphics2D graphics) {

        // sysRenderer = new Renderer(graphics, GRID_SIZE);
        sysCollision = new Collision((Entity entity) -> {});
        sysMovement = new Movement();
        sysKeyboardInput = new KeyboardInput(graphics.getWindow());
        terrainRenderer = new TerrainRenderer(graphics);

        initializeTerrain();

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
        terrainRenderer.update(elapsedTime); // Render the terrain
        // sysRenderer.update(elapsedTime); // Render the system
    }

//    private void addTerrainEntity(Entity entity){
//        this.terrainRenderer.add(entity);
//    }

    private void addEntity(Entity entity) {
        sysKeyboardInput.add(entity);
        sysMovement.add(entity);
        sysCollision.add(entity);
        // sysRenderer.add(entity);
        terrainRenderer.add(entity);
    }

    private void removeEntity(Entity entity) {
        sysKeyboardInput.remove(entity.getId());
        sysMovement.remove(entity.getId());
        sysCollision.remove(entity.getId());
        // sysRenderer.remove(entity.getId());
    }


    /**
     *
     * @param vec1
     * @param vec2
     * @return
     */
    private Vector3f midpointAlgorithm(Vector3f vec1, Vector3f vec2){

        MyRandom rnd = new MyRandom(); // Random number generator kind of

        // Get the midpoint
        float tempX = (vec1.x + vec2.x) / 2;

        // Computer Elevation
        double  rg = rnd.nextGaussian(0, 1); // Random Gaussian number
        double s = 0.3f; // Surface level factor
        double r = s * rg * Math.abs((vec2.x - vec1.x));

        double y = ((vec2.y + vec1.y) / 2) + r;
        y = y < 0 || y > 0.8 ? y * -1 : y; // Don't let elevation go beneath 0

        return new Vector3f(tempX, (float)y, 0); // Return midpoint
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

        var t =  terrain.get(Points.class);

        ArrayList<Vector3f> temp;

        for(int i = 0; i < RUN_MIDPOINT_ALGORITHM_TIMES; i++){

            temp = new ArrayList<>(); // Make temporary array list to replace points array list at end of each iteration

            // Loop through all points except the last and get the midpoint
            for(int item = 0; item < t.getPoints().size() - 1; item++){

                temp.add(t.getPoints().get(item));
                Vector3f tempPoint = midpointAlgorithm(t.getPoints().get(item), t.getPoints().get(item+1));
                temp.add(tempPoint);

            }
            temp.add(t.getPoints().getLast()); // Add last point into temp
            t.setPoints(temp); // Copy temp into points array list
        }
    }


    private void initializeTerrain(){ // Texture triangle

        MyRandom rnd = new MyRandom();

        float y1 = 0.0f;
        float y2 = 0.0f;

        Vector3f vec1 = new Vector3f(-1.0f, y1, 0);;
        Vector3f vec2 = new Vector3f(1.0f, y2, 0);

        var terrain = Terrain.create(vec1, vec2);

        addEntity(terrain);
        generateTerrain(terrain);

    }



}
