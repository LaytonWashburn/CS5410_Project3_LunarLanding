import ecs.Entities.*;
import ecs.Entities.LunarLander;
import ecs.Systems.*;
import ecs.Systems.KeyboardInput;
import ecs.Systems.Pause;
import edu.usu.audio.Sound;
import edu.usu.audio.SoundManager;
import edu.usu.graphics.*;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;


public class GameModel {


    // Only the GameModel should know what level we're on
    // The Game Model coordinates systems
    /*
        Game Model requests the terrain to be rendered
        None of the systems should know what the level is
        Start with the terrain generation
        Count down system
            lambda

     */

    // Enum for what level the game is currently on
    public enum LEVEL{
        ONE, // Level 1
        TWO, // Level 2
        TRANSITION, // Transitioning between levels
        END // Game has ended
    }

    // Globals

    private LEVEL gameLevel;
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
    private ecs.Systems.Pause sysPause;

    // Textures
    Texture texSpaceShip;
    Texture texBackground;
    Texture texParticle;

    // Sound
    private SoundManager audio;
    private Sound crash;
    private Sound thrust;

    private KeyboardInput inputKeyboard;
    private boolean keyBoardPause;

    public void initialize(Graphics2D graphics) {

        keyBoardPause = false;
        texSpaceShip = new Texture("resources/characters/lander.png");
        texBackground = new Texture("resources/images/background.png");
        texParticle = new Texture("resources/images/smoke.png");
        Font fontHeadsUpDisplay = new Font("resources/fonts/Roboto-bold.ttf", 64, true);

        audio = new SoundManager();
        thrust = audio.load("thrust", "resources/audio/thrust.ogg", false);
        crash = audio.load("crash", "resources/audio/crash.ogg", false);

        gameLevel = LEVEL.TWO; // Set the game level, by default make it level ONE

        sysCollision = new Collision();

        sysMovement = new Movement(); // Movement System

        sysKeyboardInput = new KeyboardInput(graphics.getWindow(), (Entity entity) -> {
            if(!keyBoardPause){
                addEntity(entity);
                keyBoardPause = true;
            }
        }); // KeyboardInput System

        sysBackGroundRenderer = new BackgroundRenderer(graphics, texBackground);
        sysTerrainRenderer = new TerrainRenderer(graphics); // TerrainRenderer System
        sysLunarLanderRenderer = new LunarLanderRenderer(graphics); //
        sysHeadsUpDisplayRenderer = new HeadsUpDisplayRenderer(graphics, fontHeadsUpDisplay);
        sysRotation = new Rotation();
        sysParticleSystem = new ParticleRenderer(graphics, texParticle); // Particle system
        sysLevels = new Levels(graphics, fontHeadsUpDisplay);
        sysPause = new Pause(graphics, graphics.getWindow(), (Entity entity) -> {
            // DO SOMETHING
            removeEntity(entity);
            keyBoardPause = false;
        });

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
        sysPause.update((elapsedTime));
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
        sysPause.add(entity);
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
        sysPause.remove(entity.getId());
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
