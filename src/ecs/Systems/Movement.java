package ecs.Systems;

import ecs.Entities.Entity;
import ecs.States;
import org.joml.Vector2f;

import java.util.Objects;

/**
 * This system is responsible for handling the movement of any
 * entity with movable & position components.
 */
public class Movement extends System {

    public Movement() {

        super(ecs.Components.Movable.class, ecs.Components.Position.class);
        this.containsPause = false;
    }

    private boolean containsPause;


    /**
     * Customize to look for only the terrain and lander entities
     */
    @Override
    protected boolean isInterested(Entity entity) {

        if (entity.contains(ecs.Components.LunarLander.class)) {
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
                moveEntity(entity, elapsedTime);
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

    // Move the lunar lander
    public Vector2f thrust(Vector2f momentum, double angle, double elapsedTime){
        Vector2f thrust = new Vector2f((float) -Math.sin(angle), (float) Math.cos(angle)); // Direction
        thrust = thrust.mul((float) elapsedTime).mul(0.5f); // Scalar to adjust thrust
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
        var keyboard = entity.get(ecs.Components.KeyboardControlled.class);

        if(keyboard.enabled){
            lunarLander.momentum.y += gravity.gravity.y * (float)elapsedTime; // Apply the gravity

            if (Objects.requireNonNull(moveable.moving) == States.Up) {

                lunarLander.fuel -= 0.01; // Adjust the amount of fuel on the lunar lander
                lunarLander.momentum = lunarLander.momentum.sub(thrust(lunarLander.momentum, rotatable.getRotation(), elapsedTime).mul((float)elapsedTime));
            }

            position.vector = position.vector.add(lunarLander.momentum);
        }
    }


}
