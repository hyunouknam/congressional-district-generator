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
    
    public RegionGrowingSimulation(UserAccount u, RegionGrowingParams s){
        super(u,s);
        startingMap=new Map(params.forState, params.numDistricts); //create new blank map
        currentMap=startingMap.clone();    //for regiongrowing, blankmap=startingmap=currentmap
            numOfPrecincts=startingMap.getAllPrecincts().size();
        getSeedPrecincts();
    }
    
    public void getSeedPrecincts(){
        boolean var=params.algorithm.contains("Random")? false: true;
        if(var){
            getSeedPrecinctsOne();
        }
        else{
            getSeedPrecinctsTwo();
        }
    }
    
    /*
    Description:
        Gets the list of precincts for the state for which the algorithm is running.
        Picks random precincts to be chosen as the seeds, one for each district.
    */
    private void getSeedPrecinctsOne(){
        Object[] precincts=startingMap.getAllPrecincts().toArray();
        Collection <DistrictForMap> districts=startingMap.getAllDistricts();
        for(DistrictForMap d: districts){
            PrecinctForMap p=(PrecinctForMap)precincts[precincts.length*(int)Math.random()];
            p.isAssigned=true;
            Move move=new Move(p, d);
            moves.add(move);
            currentMap.apply(move);
        }
    }
    
    private void getSeedPrecinctsTwo(){
        //seeds are the n precincts with the smallest opulations
    }
        
    /*
    Description:
        Checks if there are still precincts to be assigned.
        Runs pickMove(), updates the progress of the simulation, and a call to updateGUI()
    */
    @Override
    public void doStep(){
        if(currentMap.getNullDisrict().getPrecincts().size()>0){
            pickMove();            
            updateProgress();
            updateGUI();
        }
    }
                 
    /*
    Description:
        Chooses the neighboring precinct that results in the best goodness once added, for each district
    */
    @Override
    public void pickMove(){        
        Set<MoveTriple> goodnesses=new HashSet<>();
        for(DistrictForMap d: currentMap.getAllDistricts()){
            for(PrecinctForMap p: d.getBorderPrecincts()){ //updates each time
                for(PrecinctForMap pm: p.getNeighborPrecincts()){ //neighbors of precincts on the border of the district
                    if(!p.isAssigned){
                        Move move=new Move(p, d);
                        Map m=currentMap.cloneApply(move);
                        double goodness = ObjectiveFuncEvaluator.evaluateObjective(params.functionWeights,m);
                        goodnesses.add(new MoveTriple(goodness, m, move));
                    }
                }
            }
        }
        //sort map by goodnesses and add precinct that results in the best goodness
        MoveTriple bestTriple=null;
        for (MoveTriple t: goodnesses){
            bestTriple= t.compareTo(bestTriple)>0 ? t:bestTriple;
        }
        currentMap=bestTriple.map;
        currentGoodness=bestTriple.goodness;
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