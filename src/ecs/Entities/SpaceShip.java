package ecs.Entities;
import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

public class SpaceShip {

    public static Entity create(Texture texSpaceShip, float x, float y) {
        var spaceship = new Entity();

        spaceship.add(new ecs.Components.Appearance(texSpaceShip, Color.WHITE));
//        spaceship.add(new ecs.Components.Position(x,y));
//        spaceship.add(new ecs.Components.Collision());
        spaceship.add(new ecs.Components.SpaceShip(x, y));

        return spaceship;
    }

}
