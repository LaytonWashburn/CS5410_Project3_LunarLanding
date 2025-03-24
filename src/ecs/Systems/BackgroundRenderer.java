package ecs.Systems;

import ecs.Entities.Entity;
import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;
import edu.usu.graphics.Texture;

public class BackgroundRenderer extends System{

    Graphics2D graphics;
    Texture texBackground;

    public BackgroundRenderer(Graphics2D graphics, Texture texBackground){

        this.graphics = graphics;
        this.texBackground = texBackground;
    }

    @Override
    public void update(double elapsedTime) {

        Rectangle recBackground = new Rectangle(-1.0f, -1.0f, 2.0f, 2.0f, -0.01f); // Set the z to negative to show the terrain

        graphics.draw(this.texBackground, recBackground, Color.WHITE);

    }

}
