package ecs.Components;

import edu.usu.graphics.Color;
import edu.usu.graphics.Rectangle;
import org.joml.Vector2f;
import utils.MyRandom;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Particles extends Component{

    // Particle Container
    public static class Particle {

        public Particle(Vector2f center, Vector2f direction, float speed, Vector2f size, double lifetime) {
            this.name = nextName++;
            this.center = center;
            this.direction = direction;
            this.speed = speed;
            this.size = size;
            this.area = new Rectangle(center.x - size.x / 2, center.y - size.y / 2, size.x, size.y);
            this.lifetime = lifetime;

            this.rotation = 0;
        }

        public boolean update(double elapsedTime) {
            // Update how long it has been alive
            alive += elapsedTime;

            // Update its center
            center.x += (float) (elapsedTime * speed * direction.x);
            center.y += (float) (elapsedTime * speed * direction.y);
            area.left += (float) (elapsedTime * speed * direction.x);
            area.top += (float) (elapsedTime * speed * direction.y);

            // Rotate proportional to its speed
            rotation += (speed / 0.5f);

            // Return true if this particle is still alive
            return alive < lifetime;
        }

        public long name;
        public Vector2f size;
        public Vector2f center;
        public Rectangle area;
        public float rotation;
        private Vector2f direction;
        private float speed;
        private double lifetime;
        private double alive = 0;
        private static long nextName = 0;
    }

    public final Map<Long, Particle> particlesExplosion = new HashMap<>();
    public final Map<Long, Particle> particlesThrust = new HashMap<>();
    public final MyRandom random = new MyRandom();

    public boolean exploded;

    private final Vector2f center;
    private final float sizeMean;
    private final float sizeStdDev;
    private final float speedMean;
    private final float speedStdDev;
    private final float lifetimeMean;
    private final float lifetimeStdDev;


    public Particles(Vector2f center, float sizeMean, float sizeStdDev, float speedMean, float speedStdDev, float lifetimeMean, float lifetimeStdDev) {
        this.center = center;
        this.sizeMean = sizeMean;
        this.sizeStdDev = sizeStdDev;
        this.speedMean = speedMean;
        this.speedStdDev = speedStdDev;
        this.lifetimeMean = lifetimeMean;
        this.lifetimeStdDev = lifetimeStdDev;
        this.exploded = false;
    }

    public Collection<Particle> getParticlesExplosion(){
        return this.particlesExplosion.values();
    }

    public Collection<Particle> getParticlesThrust(){
        return this.particlesThrust.values();
    }

}
