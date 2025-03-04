package ecs.Systems;

import ecs.Components.Movable;
import ecs.Components.Segments;
import ecs.Direction;
import ecs.Entities.Entity;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Collision extends System {

    public interface IspaceShipRemoved {
        void invoke(Entity entity);
    }

    private final IspaceShipRemoved spaceShipRemoved;

    public Collision(IspaceShipRemoved spaceShipRemoved) {
        super(ecs.Components.Collision.class);

        this.spaceShipRemoved = spaceShipRemoved;
    }

    /**
     * Check to see if any movable components collide with any other
     * collision components.
     * <p>
     * Step 1: find all movable components first
     * Step 2: Test the movable components for collision with other (but not self) collision components
     */
    @Override
    public void update(double elapsedTime) {

        var movable = findMovable(entities);

        ecs.Entities.Entity spaceship = null;
        // Iterate through all the entities
        for (var entity : entities.values()) {
            // java.lang.System.out.println(entity);
            // Iterate through the movable entities
            for (var entityMovable : movable) {
                // If the object collided, stop the lander
                // Need some value to show that the ship exploded
                if (entity != entityMovable && collides(entity, entityMovable)) { // entity is the terrain, entityMovable is the Lunar Lander

                    // This seems like it's going to overwrite items
                    // But we only have one lunar lander
                    java.lang.System.out.println("In the update for the collision");
                    spaceship = entityMovable;
                }
            }

        }
        // Remove the space
        if(spaceship != null){
            spaceShipRemoved.invoke(spaceship); // Remove the spaceship that's moveable
            var lunarLander = spaceship.get(ecs.Components.LunarLander.class);
            lunarLander.shipCrash = true;
        }

    }

    /**
     * Public method that allows an entity with a single cell position
     * to be tested for collision with anything else in the game.
     */
    public boolean collidesWithAny(Entity proposed) {
//        var aPosition = proposed.get(ecs.Components.Position.class);
//
//        for (var entity : entities.values()) {
//            if (entity.contains(ecs.Components.Collision.class) && entity.contains(ecs.Components.Position.class)) {
//                var ePosition = entity.get(ecs.Components.Position.class);
//
//                for (var segment : ePosition.segments) {
//                    if (aPosition.getX() == segment.x && aPosition.getY() == segment.y) {
//                        return true;
//                    }
//                }
//            }
//        }

        return false;
    }

    /**
     * Returns a collection of all the movable entities.
     */
    private List<Entity> findMovable(Map<Long, Entity> entities) {
        var movable = new ArrayList<Entity>();

        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Movable.class) && entity.contains(ecs.Components.Position.class)) {
                movable.add(entity);
            }
        }

        return movable;
    }

    /**
     * We know that only the snake is moving and that we only need
     * to check its head for collision with other entities.  Therefore,
     * don't need to look at all the segments in the position, with the
     * exception of the movable itself...a movable can collide with itself.
     */
    private boolean collides(Entity a, Entity b) { // a is terrain, b is the lunar lander

        var spaceShipCoordinates = b.get(ecs.Components.Position.class); // Get the position of the lunar lander
        var segments = a.get(Segments.class); // Get the segments from the terrain
        // var movable = b.get(ecs.Components.Movable.class);

        for(Segments.Segment segment: segments.getSegments()){ // Iterate through the segments
            // Test if it collides
            boolean collision = lineCircleIntersection(new Vector2f(segment.startPt.x, segment.startPt.y),
                    new Vector2f(segment.endPt.x, segment.endPt.y),
                    new Vector2f(spaceShipCoordinates.posX +0.1f, spaceShipCoordinates.posY+0.1f), // I don't like having hard coded values in here
                    0.09f);
            if( collision && !segment.safeZone){
                return true; // Return true for a collision
            }
            // This is where the check for the speed and angle need to be
            // if (collision) {}
        }
        return false; // Return false if no collision
    }

    // I (Dean) wrote this for use in my Java implementation of the game.
    // I (Layton) am using this code for my game.
    // Vector2f is defined in the JOML library.
    private boolean lineCircleIntersection(Vector2f pt1, Vector2f pt2, Vector2f circleCenter, float circleRadius) {

        // Translate points to circle's coordinate system
        Vector2f d = pt2.sub(pt1); // ecs.Direction vector of the line
        Vector2f f = pt1.sub(circleCenter); // Vector from circle center to the start of the line

        float a = d.dot(d);
        float b = 2 * f.dot(d);
        float c = f.dot(f) - circleRadius * circleRadius;

        float discriminant = b * b - 4 * a * c;

        // If the discriminant is negative, no real roots and thus no intersection
        if (discriminant < 0) {
            return false;
        }

        // Check if the intersection points are within the segment
        discriminant = (float) Math.sqrt(discriminant);
        float t1 = (-b - discriminant) / (2 * a);
        float t2 = (-b + discriminant) / (2 * a);

        if (t1 >= 0 && t1 <= 1) {
            return true;
        }
        if (t2 >= 0 && t2 <= 1) {
            return true;
        }

        return false;
    }

}
