package ecs.Systems;

import ecs.Components.Component;
import ecs.Components.Level;
import ecs.Components.Segments;
import ecs.Components.Terrain;
import ecs.Entities.Entity;
import utils.MyRandom;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Triangle;
import org.joml.Vector3f;
import  ecs.Components.Segments.Segment;

import java.lang.System;
import java.util.ArrayList;
import java.util.List;

public class TerrainRenderer extends Renderer { // Renderer extends systems

    private final Graphics2D graphics;
    private int RUN_MIDPOINT_ALGORITHM_TIMES = 7;
    private boolean containsPause;

    public TerrainRenderer(Graphics2D graphics) {
        super(graphics,
              ecs.Components.Terrain.class,
              ecs.Components.Segments.class,
              ecs.Components.Collision.class);

        this.graphics = graphics;
        this.containsPause = false;

    }


    /**
     * Customize to look for only the terrain and lander entities
     */
    @Override
    protected boolean isInterested(Entity entity) {

        if (entity.contains(ecs.Components.Terrain.class)) {
            return true;
        }

        if(entity.contains(ecs.Components.Pause.class)){
            containsPause = true;
            return true;
        }

        return false;
    }

    @Override
    public void update(double elapsedTime) {
        // Draw each of the game entities!
        for (var entity : entities.values()) {
            if(!containsPause){
                renderEntity(entity);
            }
        }
    }

    /**
     * Removes the entity from the tracking collection.  If the entity was actually in
     * the system true is returned, false otherwise.
     */
    public boolean remove(long id) {

        Entity entity = entities.remove(id);
        if(entity == null){
            return false;
        }

        if (entity.contains(ecs.Components.Pause.class)){
            this.containsPause = false;
        }

        return true;
    }

//    @Override
//    public void update(double elapsedTime) {
//
//        for(Entity entity : entities.values()){
//            // Draw each of the game entities!
//            renderEntity(entity);
//        }
//
//    }

    // Render the terrain
    @Override
    public void renderEntity(Entity entity) {

        if(entity == null){
            return;
        }

        if(!entity.get(Terrain.class).terrainGenerated){
            generateTerrain(entity);
            entity.get(Terrain.class).terrainGenerated = true;
        }

        var segments = entity.get(Segments.class);

        Triangle triangle;
        Vector3f pt1;
        Vector3f pt2;
        Vector3f pt3;
        Vector3f pt4;

        // Iterate through the segments
        for(Segment segment: segments.getSegments()){
            // if(segment.safeZone){
                graphics.draw(segment.startPt, segment.endPt, Color.BLUE); // Draw the terrain outline
                pt1 = segment.startPt; // Get the starting point

                pt2 = new Vector3f(pt1);
                pt2.y = pt1.y + 10;

                pt3 = segment.endPt;

                pt4 = new Vector3f(pt3);
                pt4.y = pt4.y + 10;

                // Draw the first triangle
                triangle = new Triangle(pt1, pt2, pt3);
                graphics.draw(triangle, Color.BLACK);

                // Draw the second triangle
                triangle = new Triangle(pt3, pt2, pt4);
                graphics.draw(triangle, Color.BLACK);
            }
        // }

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
        List<Segment> safeZones = new ArrayList<>(); // Make list of safe zones

        var segments =  terrain.get(ecs.Components.Segments.class); // Get the segments from the terrain. initially empty
        segments.reset(); // Reset the terrain segments so no residual segments affect the new terrain
        
        // Number of safe zones depends on the level
        Segment safeZone1;
        Segment safeZone2;

        // Generate Safe zone x value
        // Make the range on the left
        float safeZone1X = rnd.nextRange(-0.75f, -0.2f); // Make sure the safe zone if 15% away from the edge of the screen
        float safeZone1Y = rnd.nextRange(0, 0.5f);

        Vector3f safeZone1Vec1 = new Vector3f(safeZone1X, safeZone1Y, 0);
        Vector3f safeZone1Vec2 = new Vector3f(safeZone1X + 0.2f, safeZone1Y, 0);

        safeZone1 = new Segment(safeZone1Vec1,
                safeZone1Vec2,
                true);

        safeZones.add(safeZone1);

        float safeZone2X = rnd.nextRange(0.2f, 0.75f);
        float safeZone2Y = rnd.nextRange(0, 0.5f);

        if(terrain.get(Level.class).level == Level.LEVEL.ONE){

            Vector3f safeZone2Vec1 = new Vector3f(safeZone2X, safeZone2Y, 0);
            Vector3f safeZone2Vec2 = new Vector3f(safeZone2X + 0.2f, safeZone2Y, 0);

            safeZone2 = new Segment(safeZone2Vec1,
                    safeZone2Vec2,
                    true);

            safeZones.add(safeZone2);
        }

        // Choose two endpoints
        // Change this to random elevations
        float elevation1 = rnd.nextRange(0, 0.75f);
        float elevation2 = rnd.nextRange(0, 0.75f);

        Vector3f startPt = new Vector3f(-1, elevation1, 0);
        Vector3f endPt = new Vector3f(1, elevation2, 0);

        Vector3f pt = new Vector3f(startPt);

        // Make the initial segments
        for(Segment segment: safeZones){
            Segment seg1 = new Segment(pt, segment.startPt, false);
            segments.add(seg1);
            segments.add(segment);
            pt = new Vector3f(segment.endPt);
        }

        segments.add(new Segment(pt, endPt, false));

        ArrayList<Segment> temp; // Temp array to help copy the new segments into
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

}
