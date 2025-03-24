package ecs.Components;

import edu.usu.audio.Sound;
import edu.usu.audio.SoundManager;

public class Sounds extends Component{

    public Sound thrust;
    public Sound crash;
    public Sound completion;
    // private Sound music;

    public Sounds(Sound thrust, Sound crash, Sound completion) {
        this.thrust = thrust;
        this.crash = crash;
        this.completion = completion;
    }

    public void playThrust(){
        this.thrust.play();
    }

    public void playCrash(){
        this.crash.play();
    }

    public void playCompletion(){
        this.crash.play();
    }

}
