package ecs.Components;

import org.joml.Vector3f;

import java.util.ArrayList;


public class Points extends Component {
    public ArrayList<Vector3f> points;

    public Points(Vector3f vec1, Vector3f vec2) {
        points = new ArrayList<>();
        this.points.add(vec1); // Not suppose to have logic in it
        this.points.add(vec2);
    }

    public ArrayList<Vector3f> getPoints() {
        return this.points;
    }
    public void add(Vector3f vec){
        this.points.add(vec);
    }

    public void setPoints(ArrayList<Vector3f> points){
        this.points = points;
    }
    public float getX() {
        return this.points.get(0).x;
    }

    public float getY() { return this.points.get(0).y; }
}




