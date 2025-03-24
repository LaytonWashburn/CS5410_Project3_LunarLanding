package ecs.Entities;

import ecs.Components.Segments;
import org.joml.Vector3f;

public class Terrain {
    public static Entity create() { // Texture square,

        var terrain = new Entity();

        terrain.add(new ecs.Components.Collision());
        terrain.add(new Segments());
        terrain.add(new ecs.Components.Terrain());
        terrain.add(new ecs.Components.Level());

        return terrain;
    }
}
