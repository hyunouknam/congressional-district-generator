package cse308.Data;

public class Coords {
    private final float lat;
    private final float lon;
    
    public Coords(float x, float y){
        lat=x;
        lon=y;
    }
    
    public float getLatitude(){
        return lat;
    }
    
    public float geltLongitude(){
        return lon;
    }
}
