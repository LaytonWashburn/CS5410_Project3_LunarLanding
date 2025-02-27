package ecs.Components;

import java.util.HashMap;
import java.util.Map;

public class KeyboardControlled extends Component {
    public Map<Integer, Rotatable.Direction> keys;
    public Map<Rotatable.Direction, Integer> lookup;

    public KeyboardControlled(Map<Integer, Rotatable.Direction> keys) {
        this.keys = keys;

        // Build the action to key lookup based on the key to action inf
        lookup = new HashMap<>();
        for (var mapping : keys.entrySet()) {
            lookup.put(mapping.getValue(), mapping.getKey());
        }
    }
}
