package ecs.Systems;

import ecs.Entities.Entity;
import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;

public class HeadsUpDisplayRenderer extends Renderer {

    Graphics2D graphics;
    Font font;
    boolean containsPause;

    public HeadsUpDisplayRenderer(Graphics2D graphics, Font font){
        super(graphics, ecs.Components.LunarLander.class, ecs.Components.Pause.class);

        this.graphics = graphics;
        this.font = font;
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

    @Override
    public void renderEntity(Entity entity) {

        var lunarLander = entity.get(ecs.Components.LunarLander.class);
        var rotatable = entity.get(ecs.Components.Rotatable.class);

        // Draw the info for the space ship
        // Fuel : Green for is any left, white when empty
        // Vertical speed : White above 2 m / s, green otherwise
        // Angle : White between 5 and 355, green otherwise
        double angle = rotatable.getRotation() * 180 / Math.PI;; // (lunarLander.angle % 2 * Math.PI) * 180 / Math.PI;
        double speed = Math.sqrt((lunarLander.momentum.x * lunarLander.momentum.x) + (lunarLander.momentum.y * lunarLander.momentum.y)) * 2000;
        Color fuelColor = lunarLander.fuel > 0.0 ? Color.GREEN : Color.WHITE;
        Color verticalSpeedColor =  speed > 2.0f ? Color.WHITE : Color.GREEN;
        Color angleColor = angle < 355.0f && angle > 5.0f ? Color.WHITE : Color.GREEN;

        graphics.drawTextByWidth(this.font, "Fuel: " + String.format("%.2f", Math.max(lunarLander.fuel, 0)),  0.75f, -0.8f, 0.15f ,  fuelColor);
        graphics.drawTextByWidth(this.font, "Speed: " + String.format("%.2f", speed) + " m/s",  0.75f, -0.75f, 0.2f ,  verticalSpeedColor);
        graphics.drawTextByHeight(this.font, "Angle: " + String.format("%.2f", angle) + " °",  0.75f, -0.7f, 0.038f ,  angleColor);

    }
}
