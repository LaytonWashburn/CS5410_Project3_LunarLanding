package ecs.Systems;

import ecs.Components.Component;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;

public abstract class Renderer extends System {


    private final Graphics2D graphics;

    public Renderer(Graphics2D graphics, Class<? extends Component>... types) {
        super(types);
        this.graphics = graphics;
    }


    public abstract void update(double elapsedTime);

    public abstract void renderEntity(ecs.Entities.Entity entity);
}
