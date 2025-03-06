package ecs.Components;
import ecs.Direction;
import java.util.HashMap;
import java.util.Map;

public class KeyboardControlled extends Component {
    public Map<Integer, Direction> keys;
    public Map<Direction, Integer> lookup;

    public boolean enabled;

    public KeyboardControlled(Map<Integer, Direction> keys) {
        this.keys = keys;

        // Build the action to key lookup based on the key to action inf
        lookup = new HashMap<>();
        for (var mapping : keys.entrySet()) {
            lookup.put(mapping.getValue(), mapping.getKey());
        }

        enabled = true;
    }
}
