package cse308.Areas;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.operation.union.CascadedPolygonUnion;
import org.locationtech.jts.geom.Coordinate;


public class DistrictForMap{
    private int id;
    private final MasterDistrict master;
    private Map map;
    
    public DistrictForMap(){
        master=null; //null district, for unassigned precincts      
    }
    
    public DistrictForMap(MasterDistrict md){
        master=md;
    }
    
    public Set<PrecinctForMap> getBorderPrecincts(){
        //get the precincts that are on te border of the district
        return null;
    }
    
    /*
    Description:
        //Returns the districts holding the precincts that neighbor a precinct on the border of this districtformap
    */
    public Set<DistrictForMap> getNeighborDistricts(){        
        Set<DistrictForMap> neighbors=new HashSet<>();
        for(PrecinctForMap border: getBorderPrecincts()){
            for(PrecinctForMap neighbor: border.getNeighborPrecincts()){
                neighbors.add(neighbor.getParentDistrict());
            }
        }
        return neighbors;
    }
    
    public Map getMap(){
        return map;
    }
    public void setMap(Map m){
        map=m;
    }
    
    public int getID(){
        return id;
    }
    
    public void setID(int i){
        id=i;
    }
    
    public MasterDistrict getMaster(){
        return master;
    }
    
    public Set<PrecinctForMap> getPrecincts(){
        Set <PrecinctForMap> precincts=new HashSet<>();
        for(PrecinctForMap p: map.getPrecinctDistrictMapping().keySet()){
            if (map.getPrecinctDistrictMapping().get(p)==this){
                precincts.add(p);
            }
        }
        return precincts;
    }
    
    public Geometry getDistrictBoundary() {
    	GeometryFactory geometryFactory = new GeometryFactory();
    	Coordinate a = new Coordinate(-83.86962890625,40.94671366508002);
    	Coordinate b = new Coordinate(-84.24316406249999,40.12849105685408);
    	Coordinate c = new Coordinate(-83.75976562499999, 39.825413103424786);
    	Coordinate d = new Coordinate(-83.07861328125,40.06125658140474);
    	Coordinate e = new Coordinate(-83.86962890625,40.94671366508002);
    	Coordinate[] coord = new Coordinate[5];
    	coord[0]=a;
    	coord[1]=b;
    	coord[2]=c;
    	coord[3]=d;
    	coord[4]=e;
    	Polygon pol1 = geometryFactory.createPolygon(coord);
    	
    	Coordinate f = new Coordinate(-83.86962890625,40.94671366508002);
    	Coordinate g = new Coordinate(-83.07861328125,40.094882122321145);
        Coordinate h = new Coordinate(-82.705078125,40.329795743702064);
    	Coordinate i = new Coordinate(-82.55126953124999,40.730608477796636);
    	Coordinate j = new Coordinate(-82.5732421875,41.11246878918088);
    	Coordinate k = new Coordinate(-83.86962890625,40.94671366508002);
    	Coordinate[] coord1 = new Coordinate[5];
    	coord1[0]=f;
    	coord1[1]=g;
    	coord1[2]=h;
    	coord1[3]=i;
    	coord1[4]=j;
    	coord1[5]=k;
    	Polygon pol2 = geometryFactory.createPolygon(coord1);
    	
//    	Polygon[] polArray = new Polygon[2];
//    	polArray[0]=pol1;
//    	polArray[1]=pol2;
    	
    	ArrayList<Polygon> polArray = new ArrayList<>();
    	polArray.add(pol1);
    	polArray.add(pol2);
    	
    	CascadedPolygonUnion polUnion = new CascadedPolygonUnion(polArray);
    	return null;
    }
}