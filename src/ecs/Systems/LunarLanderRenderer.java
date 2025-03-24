package ecs.Systems;

import ecs.Components.LunarLander;
import ecs.Entities.Entity;
import edu.usu.graphics.*;
import org.joml.Vector2f;

public class LunarLanderRenderer extends Renderer {
    private final Graphics2D graphics;

    private boolean containsPause;

    public LunarLanderRenderer(Graphics2D graphics) {
        // Look for the Lunar Lander class
        super(graphics, LunarLander.class);

        this.graphics = graphics;
        containsPause = false;
    }


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



    // Render the spaceship
    @Override
    public void renderEntity(Entity entity) {

        var rotatable = entity.get(ecs.Components.Rotatable.class);
        var appearance = entity.get(ecs.Components.Appearance.class);
        var spaceship = entity.get(LunarLander.class);
        var position = entity.get(ecs.Components.Position.class);
        var gravity = entity.get(ecs.Components.Gravity.class);
        var sounds = entity.get(ecs.Components.Sounds.class);
        Rectangle area = new Rectangle(position.getPosX(), position.getPosY(), 0.2f, 0.2f);


        // Render the spaceship
        // Maybe check the Collision entity if it collided or not
       if(spaceship.alive)
       {
           graphics.draw(appearance.image,
                   area,
                   rotatable.getRotation(),
                   new Vector2f(position.getPosX() + 0.1f, position.getPosY() +.1f),
                   Color.WHITE);
       }
    }
}
