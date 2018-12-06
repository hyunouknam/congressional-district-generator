package cse308.Simulation;

import cse308.Areas.DistrictForMap;
import cse308.Areas.Map;
import cse308.Areas.PrecinctForMap;
import cse308.Users.UserAccount;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RegionGrowingSimulation extends Simulation{
    int numOfPrecincts;
    
    public RegionGrowingSimulation(UserAccount u, SimulationParams s){
       super(u,s);
       startingMap=new Map(params.forState); //create new blank map
       currentMap=startingMap;    //for regiongrowing, blankmap=startingmap=currentmap
//       numOfPrecincts=startingMap.getAllPrecincts().values().size();
       numOfPrecincts = 6339;
    }
    
    /*
    Description:
        Gets the list of precincts for the state for which the algorithm is running.
        Picks random precincts to be chosen as the seeds, one for each district.
    */
    public void getSeedPrecincts(){
        Object[] precincts=startingMap.getAllPrecincts().toArray(); //needed to make it an array in order to get random ones
        Collection <DistrictForMap> districts=startingMap.getAllDistricts();
        for(DistrictForMap d: districts){
            PrecinctForMap p=(PrecinctForMap)precincts[Integer.parseInt(""+precincts.length*Math.random())];
            p.isAssigned=true;
            Move move=new Move(p, startingMap.getNullDisrict(), d);
            d.getPrecincts().add(p);
            moves.add(move);
            currentMap.apply(this.params.functionWeights, move);
        }
    }
        
    /*
    Description:
        Checks if there are still precincts to be assigned.
        Runs pickMove(), a call to updateGUI() and updates the progress of the simulation
    */
    @Override
    public void doStep() {
        if(currentMap.getNullDisrict().getPrecincts().size()>0){
            pickMove();            
            updateProgress();
            postUpdate();
        }
    }
                 
    /*
    Description:
        Chooses the neighboring precinct that results in the best goodness once added, for each district
    */
    @Override
    public void pickMove() {
        Set<MoveTriple> goodnesses=new HashSet<>();
        for(DistrictForMap d: currentMap.getAllDistricts()) {
            for(PrecinctForMap p: d.getBorderPrecincts()){ //updates each time
                for(PrecinctForMap pm: p.getNeighborPrecincts()){ //neighbors of precincts on the border of the district
                    if(!p.isAssigned){
                        Move move=new Move(p, currentMap.getNullDisrict(), d);
                        Map m=currentMap.cloneApply(this.params.functionWeights, move);
                        goodnesses.add(new MoveTriple(m.calculateGoodness(params.functionWeights), m, move));
                    }
                }
            }
        }
        //sort map by goddnesses and add precinct that results in the best goodness
        MoveTriple bestTriple=null;
        for (MoveTriple t: goodnesses){
            bestTriple= t.compareTo(bestTriple)>0 ? t:bestTriple;
        }
        currentMap=bestTriple.map;
        currentGoodness=currentMap.getGoodness();
        moves.add(bestTriple.move);
    }
    
    /*
    Description:
        Updates the simulations progress, which is based on the # of moves 
        # of moves/ # of precincts in state -> one move per precinct
    */
    @Override
    public void updateProgress(){
        progress=moves.size()/numOfPrecincts;
    }
}