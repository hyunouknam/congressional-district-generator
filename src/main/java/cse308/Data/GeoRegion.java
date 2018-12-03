package cse308.Data;

import java.util.ArrayList;

public class GeoRegion {
    private ArrayList<Coords> boundaries;
    private Coords center;
    
    public GeoRegion(){
        boundaries=new ArrayList<>();
    }
    
    public void addBoundary(Coords c){
        boundaries.add(c);
    }
    
    public ArrayList<Coords> getBoundaries(){
        return boundaries;
    }
    
    public void setCenter(Coords c){
        center=c;
    }
    
    public Coords getCenter(){
        return center;
    }
}