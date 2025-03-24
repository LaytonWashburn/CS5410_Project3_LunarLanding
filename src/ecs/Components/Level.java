package ecs.Components;

public class Level extends Component{
    public enum LEVEL {
        ONE, TWO, TRANSITION, END
    }

    public double countDownTime;
    public double accumulatedTime;
    public int transitionCount;

    public LEVEL level;

    public  Level(){
        this.level = LEVEL.ONE;
        this.countDownTime = 1;
        this.accumulatedTime = 0;
        this.transitionCount = 3;
    }
}
