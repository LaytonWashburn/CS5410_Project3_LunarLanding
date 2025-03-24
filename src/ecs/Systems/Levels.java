package ecs.Systems;

import ecs.Components.Level;
import ecs.Entities.Entity;
import edu.usu.graphics.Color;
import edu.usu.graphics.Font;
import edu.usu.graphics.Graphics2D;
import utils.GameScores;
import utils.Serializer;

import java.util.ArrayList;
import java.util.Map;

public class Levels extends System{

    Graphics2D graphics;
    Font font;
    Serializer serializer;
    GameScores gameScores;
    boolean saved;
    boolean containsPause;

    public Levels(Graphics2D graphic, Font font, Serializer serializer, GameScores gameScores){
        // Look for the Lunar Lander and the Terrain
        super(ecs.Components.Level.class);
        this.graphics = graphic;
        this.font = font;
        this.serializer = serializer;
        this.gameScores = gameScores;
        this.saved = false;
        this.containsPause = false;
    }

    /**
     * Customize to look for only the terrain and lander entities
     */
    @Override
    protected boolean isInterested(Entity entity) {

        if (entity.contains(ecs.Components.Level.class)) {
            return true;
        }

        if(entity.contains(ecs.Components.Pause.class)){
            containsPause = true;
            return true;
        }

        return false;
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

    public void update(double elapsedTime) {

       if(!containsPause){
           Entity lunarLanderEntity = find(entities, ecs.Components.LunarLander.class);
           Entity terrainEntity = find(entities, ecs.Components.Terrain.class);
           var lunarLanderLevel = lunarLanderEntity.get(ecs.Components.Level.class);
           var keyboard = lunarLanderEntity.get(ecs.Components.KeyboardControlled.class);
           var lunarLander = lunarLanderEntity.get(ecs.Components.LunarLander.class);
           var terrainLevel = terrainEntity.get(ecs.Components.Level.class);
           var terrain = terrainEntity.get(ecs.Components.Terrain.class);
           var positionLunLander = lunarLanderEntity.get(ecs.Components.Position.class);
           var rotatable = lunarLanderEntity.get(ecs.Components.Rotatable.class);
           var score = lunarLanderEntity.get(ecs.Components.Score.class);
           var sounds = lunarLanderEntity.get(ecs.Components.Sounds.class);

           // Update the level
           if(!lunarLander.alive){
               String explosionMessage = "Missed the landing. Better luck next time";
               graphics.drawTextByWidth(this.font, explosionMessage, -0.5f, -0.25f, 1.0f, Color.YELLOW);
           }

           if(lunarLanderLevel.level == Level.LEVEL.TRANSITION){

               String message = Integer.toString(lunarLanderLevel.transitionCount);
               graphics.drawTextByWidth(this.font, message, -0.5f, -0.9f, 1.0f, Color.YELLOW);

               if(lunarLanderLevel.accumulatedTime >= lunarLanderLevel.countDownTime){
                   lunarLanderLevel.accumulatedTime = Math.max(lunarLanderLevel.accumulatedTime - lunarLanderLevel.countDownTime, 0);
                   lunarLanderLevel.transitionCount -= 1;
               }

               else if (lunarLanderLevel.transitionCount == 0) {
                   terrain.terrainGenerated = false;
                   keyboard.enabled = true;
                   rotatable.isRotating = true;
                   lunarLanderLevel.level = Level.LEVEL.TWO;
                   terrainLevel.level = Level.LEVEL.TWO;
                   positionLunLander.vector.x = 0.0f;
                   positionLunLander.vector.y = -.5f;

               }
               else {
                   lunarLanderLevel.accumulatedTime += elapsedTime;
               }
           }

           // Landed safely in level 2
           if(!keyboard.enabled && lunarLander.alive && lunarLanderLevel.level == Level.LEVEL.TWO){
               lunarLanderLevel.level = Level.LEVEL.END;
               terrainLevel.level = Level.LEVEL.END;
               serializer.saveGameState(gameScores);

           }

           // Finished the Game Message
           if(lunarLanderLevel.level == Level.LEVEL.END){
               String message =  "Congrats!!! You Finished the Game!!";
               if(!saved){
                   this.gameScores.scores.add(score.score);
                   serializer.saveGameState(this.gameScores);
                   saved = true;
               }
               graphics.drawTextByWidth(this.font, message, -0.5f, -0.50f, 1.0f, Color.YELLOW);
//            GameState state = new GameState(10, 2);
//            serializer.saveGameState(state);
           }

           // Transition from level one to transition state
           if(!keyboard.enabled && lunarLander.alive && lunarLanderLevel.level == Level.LEVEL.ONE){
               sounds.completion.play();
               lunarLanderLevel.level = Level.LEVEL.TRANSITION;
               terrainLevel.level = Level.LEVEL.TRANSITION;
           }
       }

    }

    /**
     * Returns a collection of all the movable entities.
     */
    private Entity find(Map<Long, Entity> entities, Class component) {
        var movable = new ArrayList<Entity>();

        for (var entity : entities.values()) {
            if (entity.contains(component) ) {
                movable.add(entity);
            }
        }

        return movable.getFirst();
    }
}
