package ecs.Components;

import org.joml.Vector3f;

import java.util.ArrayList;



public class Segments extends Component {

    // Wrapper or Container to hold segment information
    public static class Segment {
        public Vector3f startPt;
        public Vector3f endPt;
        public boolean safeZone;

        public Segment(Vector3f startPt, Vector3f endPt, boolean safeZone){
            this.startPt = startPt;
            this.endPt = endPt;
            this.safeZone = safeZone;
        }
    }

    public ArrayList<Segment> segments;

    public Segments() {
        segments = new ArrayList<>();
    }

    public ArrayList<Segment> getSegments() {
        return this.segments;
    }

    public void add(Segment segment){
        this.segments.add(segment);
    }

    public void setSegments(ArrayList<Segment> points){
        this.segments = points;
    }
    public float getX() {
        return this.segments.getFirst().startPt.x;
    }

    public float getY() { return this.segments.getFirst().startPt.y; }
}




