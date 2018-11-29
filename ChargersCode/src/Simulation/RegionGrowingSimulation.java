package Simulation;

import Areas.DistrictForMap;
import Areas.Map;
import Areas.PrecinctForMap;
import Users.UserAccount;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;
import javafx.util.Pair;

public class RegionGrowingSimulation extends Simulation{ 
    /*
        **Problems can occur if seed is on an edge**
    */
    int numOfPrecincts;
    public RegionGrowingSimulation(UserAccount u, SimulationParams s){
       super(u,s);
       startingMap=new Map(params.getState()); //create new blank map
       currentMap=startingMap;    //for regiongrowing, blankmap=startingmap=currentmap
       numOfPrecincts=startingMap.getAllPrecincts().values().size();
    }
    
    public void getSeedPrecincts(){
        Object[] precincts=startingMap.getAllPrecincts().values().toArray(); //needed to make it an array in order to get random ones
        Collection <DistrictForMap> districts=startingMap.getAllDistricts().values();
        for(DistrictForMap d: districts){
            PrecinctForMap p=(PrecinctForMap)precincts[Integer.parseInt(""+precincts.length*Math.random())];
            p.isAssigned=true;
            Move move=new Move(p, startingMap.getNullDisrict(), d);
            d.getPrecincts().add(p);
            moves.add(move);
            currentMap.apply(move);
        }
    }
        
    @Override
    public void doStep() throws CloneNotSupportedException{
        //dostep will go through a new round adding precincts to districts
        if(currentMap.getNullDisrict().getPrecincts().size()>0){
            pickMove();
            //updateGUI();
            //set progress--based on number of moves? number of remaining districts unassigned? ...?
            
        }
    }
                 

    @Override
    public void pickMove() throws CloneNotSupportedException{
        //chooses the neighbor with the best resulting goodness
            //for each district
                //getBorderPrecincts()
                //getNeighborPrecincts()
                //cloneApply() isAcceptable() getGoodness()
                //choose an acceptable neighbor with the best resulting goodness (pickMove())
        HashMap<Float, Pair<Map, Move>> goodnesses=new HashMap<>();
        for(DistrictForMap d: currentMap.getAllDistricts().values()){
            for(PrecinctForMap p: d.getBorderPrecincts()){
                for(PrecinctForMap pm: p.getNeighborPrecincts()){
                    if(!p.isAssigned){
                        Move move=new Move(p, currentMap.getNullDisrict(), d);
                        Map m=currentMap.cloneApply(move);
                        if(m.isAcceptable()){
                            goodnesses.put(m.getGoodness(), new Pair<>(m, move));
                        }
                    }
                }
            }
        }
        //sort map by goddnesses and add precinct that results in the best goodness
        TreeMap<Float, Pair<Map, Move>> sortedGoodness=new TreeMap<>(goodnesses);
        for (java.util.Map.Entry<Float, Pair<Map, Move>> pair : sortedGoodness.entrySet()){
            currentMap=pair.getValue().getKey();
            moves.add(pair.getValue().getValue());
            break;
        }       
    }
    
    @Override
    public void updateProgress(float p){
        //progress=# of moves # of precincts in state -> one move per precinct
        progress=moves.size()/numOfPrecincts;
    }
    
    @Override
    public void updateDistricts(PrecinctForMap a, PrecinctForMap b){
        
    }
}
