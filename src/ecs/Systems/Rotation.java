package ecs.Systems;

import ecs.Components.LunarLander;
import ecs.Entities.Entity;
import ecs.States;

public class Rotation extends System{


    public Rotation() {

        super(ecs.Components.Rotatable.class, LunarLander.class);
        containsPause = false;
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
                rotateEntity(entity, elapsedTime);
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

    //@Override
//    public void update(double elapsedTime) {
//        for (var entity : entities.values()) {
//            rotateEntity(entity, elapsedTime);
//        }
//    }

    // Rotation is in radians
    private void rotateEntity(ecs.Entities.Entity entity, double elapsedTime) {
        var rotatable = entity.get(ecs.Components.Rotatable.class);

        if(rotatable.isRotating){
            switch (rotatable.rotating) {
                case States.Left:
                    rotate(entity, (-(float)Math.PI) / 180); // Rotate -1.0 degrees
                    break;
                case States.Right:
                    rotate(entity, ((float)Math.PI) / 180); // Rotate 1.0 degrees
                    break;

            }
        }

    }

    private void rotate(ecs.Entities.Entity entity, float rotation ) {
        var rotatable = entity.get(ecs.Components.Rotatable.class);
        rotatable.setRotation(rotatable.getRotation() + rotation); // Set the rotation
    }

}
