package ecs.Systems;

import ecs.Components.Particles;
import ecs.States;
import ecs.Entities.Entity;
import utils.MyRandom;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class ParticleRenderer extends Renderer {

    private final Graphics2D graphics;
    private final Texture texParticle;
    private final MyRandom random;
    private boolean containsPause;

    public ParticleRenderer(Graphics2D graphics, Texture texParticle) {

        // Looks for LunarLander Class
        super(graphics, ecs.Components.LunarLander.class);
        this.graphics = graphics;
        this.texParticle = texParticle;
        this.random = new MyRandom();
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
                renderParticles(entity, elapsedTime);
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


    // Render each particle
    public void renderParticles(Entity entity, double elapsedTime) {

        var particles = entity.get(Particles.class);
        var moving = entity.get(ecs.Components.Movable.class);
        var lunarLander = entity.get(ecs.Components.LunarLander.class);

        if(!lunarLander.alive && !particles.exploded){
            particles.particlesThrust.clear();
            shipCrash(entity);
            particles.exploded = true;
            //return;
        }

        if(lunarLander.alive && moving.moving == States.Up && lunarLander.fuel > 0.0f){
            shipThrust(entity);
        }


        // Update existing particles
        List<Long> removeMeThrust = new ArrayList<>();
        List<Long> removeMeExplosion = new ArrayList<>();

        for (Particles.Particle p : particles.getParticlesThrust()) {
            if (!p.update(elapsedTime)) {
                removeMeThrust.add(p.name);
            }
        }

        for (Particles.Particle p : particles.getParticlesExplosion()) {
            if (!p.update(elapsedTime)) {
                removeMeExplosion.add(p.name);
            }
        }

        // Remove dead particles from thrust
        for (Long key : removeMeThrust) {
            particles.particlesThrust.remove(key);
        }

        // Remove dead particles from explosion
        for (Long key : removeMeExplosion) {
            particles.particlesExplosion.remove(key);
        }


        // Render any particles in the thrust map
         for(Particles.Particle particle : particles.getParticlesThrust()){
             graphics.draw(texParticle, particle.area, particle.rotation, particle.center, Color.WHITE);
         }

         // Render any explosion particles
        for(Particles.Particle particle : particles.getParticlesExplosion()){
            graphics.draw(texParticle, particle.area, particle.rotation, particle.center, Color.WHITE);
        }


    }

    @Override
    public void renderEntity(Entity entity) {}


    //
    private Particles.Particle createThrust(Entity entity) {

        var position = entity.get(ecs.Components.Position.class);
        var lunarLander = entity.get(ecs.Components.LunarLander.class);
        var rotatable = entity.get(ecs.Components.Rotatable.class);

        // new Vector2f(entity.get(LunarLander.class).momentum).mul(-0.1f)
        float size = (float) this.random.nextGaussian(0.015f, 0.004f);
        /*
            Think about this
         */
        var p = new Particles.Particle(
                new Vector2f(position.getPosX() + 0.1f, position.getPosY() + 0.1f),
                new Vector2f((float) -Math.sin(rotatable.getRotation()) + (float) random.nextGaussian(0, 0.25f),
                        (float) Math.cos(rotatable.getRotation()) + (float) random.nextGaussian(0, 0.25f)),
                (float) Math.abs(this.random.nextGaussian(0.4f, 0.05f)),
                new Vector2f(size, size),
                this.random.nextGaussian(1.5, 0.5));

        return p;
    }

    // Render the particles for when the ship thrust
    // Create a number of thrust particles
    // Render the particles in the overall direction opposite of the current angle, behind the ship
    public void shipThrust(Entity entity){

        var lunarLander = entity.get(ecs.Components.LunarLander.class);
        var particles = entity.get(ecs.Components.Particles.class);

        // Generate some new particles
        for (int i = 0; i < 8; i++) {
            var particle = createThrust(entity);
             particles.particlesThrust.put(particle.name, particle);
        }
    }

    private Particles.Particle createExplosion(Entity entity) {

        var position = entity.get(ecs.Components.Position.class);

        // new Vector2f(entity.get(LunarLander.class).momentum).mul(-0.1f)
        float size = (float) this.random.nextGaussian(0.015f, 0.004f);
        var p = new Particles.Particle(
                new Vector2f(position.getPosX() + 0.1f, position.getPosY() + 0.1f),
                this.random.nextCircleVector(),
                (float) this.random.nextGaussian(0.07f, 0.05f),
                new Vector2f(size, size),
                this.random.nextGaussian(3, 1));

        return p;
    }

    // Render the ship explosion for when the ship crashes
    public void shipCrash(Entity entity){
        var lunarLander = entity.get(ecs.Components.LunarLander.class);
        var particles = entity.get(ecs.Components.Particles.class);

        // Generate some new particles
        for (int i = 0; i < 100; i++) {
            var particle = createExplosion(entity);
            particles.particlesExplosion.put(particle.name, particle);
        }
    }
}
