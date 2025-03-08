package ecs.Systems;
import ecs.Components.Level;
import ecs.Components.Segments;
import ecs.Entities.Entity;
import org.joml.Vector2f;
import java.util.ArrayList;
import java.util.Map;

public class Collision extends System {


    public Collision() {
        super(ecs.Components.Collision.class);
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

        Entity lunarLander = findMovable(entities);
        // Entity spaceship = null;

        // Iterate through all the entities
        for (var entity : entities.values()) {


            if (entity != lunarLander) { // entity is the terrain, lunarLander is the Lunar Lander
                collides(entity, lunarLander);
                // This seems like it's going to overwrite items
                // But we only have one lunar lander

                // spaceship = lunarLander;
            }

        }

//        // Remove the space
//        if(spaceship != null){
//            spaceShipRemoved.invoke(spaceship); // Remove the spaceship that's moveable
//            lunarLander.get(ecs.Components.LunarLander.class).alive = false;
//        }

    }

    /**
     * Returns a collection of all the movable entities.
     */
    private Entity findMovable(Map<Long, Entity> entities) {
        var movable = new ArrayList<Entity>();

        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Movable.class) && entity.contains(ecs.Components.LunarLander.class) ) {
                movable.add(entity);
            }
        }

        return movable.getFirst();
    }

    /**
     * We know that only the snake is moving and that we only need
     * to check its head for collision with other entities.  Therefore,
     * don't need to look at all the segments in the position, with the
     * exception of the movable itself...a movable can collide with itself.
     */
    private void collides(Entity a, Entity b) { // a is terrain, b is the lunar lander


        var spaceShipCoordinates = b.get(ecs.Components.Position.class); // Get the position of the lunar lander
        var segments = a.get(Segments.class); // Get the segments from the terrain
        var lunarLanderKeyBoardControlled = b.get(ecs.Components.KeyboardControlled.class);
        var lunarLander = b.get(ecs.Components.LunarLander.class);
        var rotatable = b.get(ecs.Components.Rotatable.class);

        for(Segments.Segment segment: segments.getSegments()){ // Iterate through the segments

            // Test if it collides
            boolean collision = lineCircleIntersection(new Vector2f(segment.startPt.x, segment.startPt.y),
                    new Vector2f(segment.endPt.x, segment.endPt.y),
                    new Vector2f(spaceShipCoordinates.getPosX() + 0.1f, spaceShipCoordinates.getPosY() + 0.1f), // I don't like having hard coded values in here
                    0.07f);

            if( collision && !segment.safeZone){
                lunarLanderKeyBoardControlled.enabled = false;
                lunarLander.alive = false;
                rotatable.isRotating = false;
            }

             // This is where the check for the speed and angle need to be
              if (collision && segment.safeZone ) {
                  double angle = (Math.abs(rotatable.getRotation() * 180 / Math.PI));
                  if(angle <= 5 || angle >= 355){
                      lunarLander.alive = true;
                  } else {
                      lunarLander.alive = false;
                  }
                  lunarLanderKeyBoardControlled.enabled = false;
                  rotatable.isRotating = false;

              }
        }

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
