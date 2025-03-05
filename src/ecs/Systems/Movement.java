package ecs.Systems;

import ecs.Components.Movable;
import ecs.Direction;
import org.joml.Vector2f;
import org.joml.Vector2i;

/**
 * This system is responsible for handling the movement of any
 * entity with movable & position components.
 */
public class Movement extends System {

    public Movement() {
        super(ecs.Components.Movable.class, ecs.Components.Position.class);
    }


    // Momentum
    // Applied thrust changes the momentum,
    // Thrust doens't change the ship
    // Momentum and elasped time moves the ship
    public static Vector2f translatePoint(Vector2f point, float angleRadians, float movementDistance) {
        // Calculate the movement in the x direction using cosine of the angle
        float dy = movementDistance * (float) Math.cos(angleRadians);

        // Calculate the movement in the y direction using sine of the angle
        // For top-left origin, positive sin means going down (increasing y), negative sin means going up (decreasing y)
        float dx = movementDistance * (float) Math.sin(angleRadians);

        // Add the calculated movement to the original point
        float newX = point.x - dx;
        float newY = point.y + dy;  // Subtracting for top-left coordinate system (moving "up" is decreasing y)

        // Return the translated point
        return new Vector2f(newX, newY);
    }

    @Override
    public void update(double elapsedTime) {
        for (var entity : entities.values()) {
            moveEntity(entity, elapsedTime);
        }
    }

    // Move the lunar lander
    // Takes the current position in x and y and the rotation, and then translates the points
    private void move(ecs.Entities.Entity entity, float move ) {
        var position = entity.get(ecs.Components.Position.class);
        var rotatable = entity.get(ecs.Components.Rotatable.class);

        // Get the translation vector
        var vec = translatePoint(new Vector2f(position.posX, position.posY),
                                 rotatable.getRotation(),
                                 -0.005f);
        position.posX = vec.x;
        position.posY = vec.y;
    }

    private void moveEntity(ecs.Entities.Entity entity, double elapsedTime) {
        var moveable = entity.get(ecs.Components.Movable.class);
        var lunarLander = entity.get(ecs.Components.LunarLander.class);
        switch (moveable.moving) {
            case Direction.Up:
                move(entity, (0.01f * (float)Math.PI));
                lunarLander.fuel -= 0.01; // Adjust the amount of fuel on the lunar lander
                break;

        }
    }


}
