import ecs.Entities.*;
import ecs.Entities.LunarLander;
import ecs.GameState;
import ecs.Systems.*;
import ecs.Systems.KeyboardInput;
import edu.usu.audio.Sound;
import edu.usu.audio.SoundManager;
import edu.usu.graphics.*;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.List;
import ecs.Components.Segments.Segment;
public class GameModel {

    // Enum for what level the game is currently on
    public enum LEVEL{
        ONE, // Level 1
        TWO, // Level 2
        TRANSITION, // Transitioning between levels
        END // Game has ended
    }

    // Globals

    private LEVEL gameLevel;
    private GameState gameState;
    private final List<Entity> removeThese = new ArrayList<>();
    private final List<Entity> addThese = new ArrayList<>();

    // Systems
    private ecs.Systems.Collision sysCollision;
    private ecs.Systems.Movement sysMovement;
    private ecs.Systems.KeyboardInput sysKeyboardInput;
    private LunarLanderRenderer sysLunarLanderRenderer;
    private ecs.Systems.TerrainRenderer sysTerrainRenderer;
    private ecs.Systems.BackgroundRenderer sysBackGroundRenderer;
    private ecs.Systems.HeadsUpDisplayRenderer sysHeadsUpDisplayRenderer;
    private ecs.Systems.Rotation sysRotation;
    private ecs.Systems.ParticleRenderer sysParticleSystem;
    private ecs.Systems.Levels sysLevels;

    // Textures
    Texture texSpaceShip;
    Texture texBackground;
    Texture texParticle;

    // Sound
    private SoundManager audio;
    private Sound crash;
    private Sound thrust;

    public void initialize(Graphics2D graphics) {

        texSpaceShip = new Texture("resources/characters/lander.png");
        texBackground = new Texture("resources/images/background.png");
        texParticle = new Texture("resources/images/smoke.png");
        Font fontHeadsUpDisplay = new Font("resources/fonts/Roboto-bold.ttf", 64, true);

        audio = new SoundManager();
        thrust = audio.load("thrust", "resources/audio/thrust.ogg", false);
        crash = audio.load("crash", "resources/audio/crash.ogg", false);

        gameLevel = LEVEL.TWO; // Set the game level, by default make it level ONE
        gameState = new GameState();

        sysCollision = new Collision((Entity entity) -> {
            // removeEntity(entity); // Save callback state for when the spaceship needs to get removed
        });
        sysMovement = new Movement(); // Movement System
        sysKeyboardInput = new KeyboardInput(graphics.getWindow()); // KeyboardInput System
        sysBackGroundRenderer = new BackgroundRenderer(graphics, texBackground);
        sysTerrainRenderer = new TerrainRenderer(graphics); // TerrainRenderer System
        sysLunarLanderRenderer = new LunarLanderRenderer(graphics); //
        sysHeadsUpDisplayRenderer = new HeadsUpDisplayRenderer(graphics, fontHeadsUpDisplay);
        sysRotation = new Rotation();
        sysParticleSystem = new ParticleRenderer(graphics, texParticle); // Particle system
        sysLevels = new Levels(graphics, fontHeadsUpDisplay);
        initializeTerrain();
        initializeSpaceShip(texSpaceShip);

    }

    public void update(double elapsedTime) {
        // Because ECS framework, input processing is now part of the update
        sysKeyboardInput.update(elapsedTime);

        // Now do the normal update
        sysMovement.update(elapsedTime);
        sysCollision.update(elapsedTime);

        for (var entity : removeThese) {
            removeEntity(entity);
        }
        removeThese.clear();

        for (var entity : addThese) {
            addEntity(entity);
        }
        addThese.clear();

        // Because ECS framework, rendering is now part of the update
        sysTerrainRenderer.update(elapsedTime); // Render the terrain
        sysBackGroundRenderer.update(elapsedTime);    // Update the background
        sysLunarLanderRenderer.update(elapsedTime); // Render the lunar lander
        sysHeadsUpDisplayRenderer.update(elapsedTime);
        sysRotation.update(elapsedTime); // Update the rotation
        sysParticleSystem.update(elapsedTime);
        sysLevels.update(elapsedTime);
    }

    private void addEntity(Entity entity) {
        sysKeyboardInput.add(entity);
        sysMovement.add(entity);
        sysCollision.add(entity);
        sysTerrainRenderer.add(entity);
        sysLunarLanderRenderer.add(entity);
        sysRotation.add(entity);
        sysBackGroundRenderer.add(entity);
        sysHeadsUpDisplayRenderer.add(entity);
        sysParticleSystem.add(entity);
        sysLevels.add(entity);
    }

    private void removeEntity(Entity entity) {
        sysKeyboardInput.remove(entity.getId());
        sysMovement.remove(entity.getId());
        sysCollision.remove(entity.getId());
        sysTerrainRenderer.remove(entity.getId());
        sysLunarLanderRenderer.remove(entity.getId());
        sysRotation.remove(entity.getId());
        sysBackGroundRenderer.remove(entity.getId());
        sysHeadsUpDisplayRenderer.remove(entity.getId());
        sysParticleSystem.remove(entity.getId());
        sysLevels.remove(entity.getId());
    }




    private void initializeTerrain(){ // Texture triangle
        addEntity(Terrain.create());
    }

    // Initialize the Lunar Lander
    private void initializeSpaceShip(Texture texSpaceship){
        // Initialize the spaceship and rotate it on it's side
        addEntity(LunarLander.create(texSpaceship, 0.0f, -0.5f, (0.5f * (float)Math.PI), thrust, crash));
    }

}
