package Simulation;

import Areas.DistrictForMap;
import Areas.Map;
import Areas.PrecinctForMap;
import Users.UserAccount;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
    
    /*
    Description:
        Gets the list of precincts for the state for which the algorithm is running.
        Picks random precincts to be chosen as the seeds, one for each district.
    */
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
        
    /*
    Description:
        Checks if there are still precincts to be assigned.
        Runs pickMove(), a call to updateGUI() and updates the progress of the simulation
    */
    @Override
    public void doStep() throws CloneNotSupportedException{
        //dostep will go through a new round adding precincts to districts
        if(currentMap.getNullDisrict().getPrecincts().size()>0){
            pickMove();
            //postUpdate(JSON);
            //set progress--based on number of moves? number of remaining districts unassigned? ...?
            
        }
    }
                 
    /*
    Description:
        Chooses the neighboring precinct that results in the best goodness once added, for each district
    */
    @Override
    public void pickMove() throws CloneNotSupportedException{        
        Set<MoveTriple> goodnesses=new HashSet<>();
        for(DistrictForMap d: currentMap.getAllDistricts().values()){
            for(PrecinctForMap p: d.getBorderPrecincts()){
                for(PrecinctForMap pm: p.getNeighborPrecincts()){ //neighbors of precincts on the border of the district
                    if(!p.isAssigned){
                        Move move=new Move(p, currentMap.getNullDisrict(), d);
                        Map m=currentMap.cloneApply(move);
                        if(m.isAcceptable()){
                            goodnesses.add(new MoveTriple(m.getGoodness(), m, move));
                        }
                    }
                }
            }
        }
        //sort map by goddnesses and add precinct that results in the best goodness
        MoveTriple bestTriple=null;
        for (MoveTriple t: goodnesses){
            bestTriple= t.compareTo(bestTriple)>0 ? t:bestTriple;
            currentMap=bestTriple.map;
            moves.add(bestTriple.move);
        }       
    }
    
    /*
    Description:
        Updates the simulations progress, which is based on the #
    */
    @Override
    public void updateProgress(float p){
        //progress=# of moves # of precincts in state -> one move per precinct
        progress=moves.size()/numOfPrecincts;
    }
    
    @Override
    public void updateDistricts(PrecinctForMap a, PrecinctForMap b){
        
    }
}