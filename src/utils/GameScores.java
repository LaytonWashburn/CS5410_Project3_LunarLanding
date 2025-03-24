package utils;

import java.util.ArrayList;
import java.util.List;

public class GameScores {


    public List<Integer> scores;
    public GameScores() {
        this.scores = new ArrayList<>();
        this.initialized = true;
    }

    public boolean initialized = false;
}
