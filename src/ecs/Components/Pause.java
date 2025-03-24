package ecs.Components;

import ecs.States;
import edu.usu.graphics.Font;

import java.util.HashMap;
import java.util.Map;

public class Pause extends Component{

    public enum PauseState {
        RESUME,
        QUIT;

        public ecs.Components.Pause.PauseState next() {
            int nextOrdinal = (this.ordinal() + 1) % ecs.Components.Pause.PauseState.values().length;
            return ecs.Components.Pause.PauseState.values()[nextOrdinal];
        }

        public ecs.Components.Pause.PauseState previous() {
            int previousOrdinal = (this.ordinal() - 1) % ecs.Components.Pause.PauseState.values().length;
            if (previousOrdinal < 0) {
                previousOrdinal = QUIT.ordinal();
            }
            return ecs.Components.Pause.PauseState.values()[previousOrdinal];
        }
    }



    public Map<Integer, PauseState> keys;
    public Map<PauseState, Integer> lookup;
    public boolean enabled;


    public PauseState currState;
    public Font fontMenu;

    public  Pause(Map<Integer, PauseState> keys){
        System.out.println("Pause being created");
        this.currState = PauseState.RESUME;
        fontMenu = new Font("resources/fonts/Roboto-Regular.ttf", 48, true);
        this.keys = keys;

        // Build the action to key lookup based on the key to action inf
        lookup = new HashMap<>();
        for (var mapping : keys.entrySet()) {
            lookup.put(mapping.getValue(), mapping.getKey());
        }

        enabled = true;
    }
}
