package ecs.Systems;

import ecs.Direction;
import org.joml.Vector2f;

import java.util.Objects;

/**
 * This system is responsible for handling the movement of any
 * entity with movable & position components.
 */
public class Movement extends System {

    public Movement() {
        super(ecs.Components.Movable.class, ecs.Components.Position.class);
    }

    @Override
    public void update(double elapsedTime) {
        for (var entity : entities.values()) {
            moveEntity(entity, elapsedTime);
        }
    }

    // Move the lunar lander
    public Vector2f thrust(Vector2f momentum, double angle, double elapsedTime){
        Vector2f thrust = new Vector2f((float) -Math.sin(angle), (float) Math.cos(angle));
        thrust = thrust.mul((float) elapsedTime);
        thrust = thrust.add(momentum);
        return thrust;
    }

    // Momentum
    // Applied thrust changes the momentum,
    // Thrust doesn't change the ship
    // Momentum and elapsed time moves the ship

    private void moveEntity(ecs.Entities.Entity entity, double elapsedTime) {
        var moveable = entity.get(ecs.Components.Movable.class);
        var lunarLander = entity.get(ecs.Components.LunarLander.class);
        var position = entity.get(ecs.Components.Position.class);
        var gravity = entity.get(ecs.Components.Gravity.class);
        var rotatable = entity.get(ecs.Components.Rotatable.class);

        lunarLander.momentum.y += gravity.gravity.y * (float)elapsedTime;

        if (Objects.requireNonNull(moveable.moving) == Direction.Up) {

            lunarLander.fuel -= 0.01; // Adjust the amount of fuel on the lunar lander
            lunarLander.momentum = lunarLander.momentum.sub(thrust(lunarLander.momentum, rotatable.getRotation(), elapsedTime).mul((float)elapsedTime));
        }

        position.vector = position.vector.add(lunarLander.momentum);
    }


}
