package cse308.Simulation;

import cse308.Areas.DistrictForMap;
import cse308.Areas.Map;
import cse308.Areas.MasterPrecinct;
import cse308.Areas.PrecinctForMap;
import cse308.Users.UserAccount;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;

public class RegionGrowingSimulation extends Simulation{
    int numOfPrecincts;
    private Random rand = new Random();

    public RegionGrowingSimulation(RegionGrowingParams s){
        super(s);
        startingMap=new Map(params.getState()); //create new blank map
        currentMap=startingMap.clone();    //for regiongrowing, blankmap=startingmap=currentmap
        getSeedPrecinctsOne();
    }

    public void getSeedPrecincts(){
        boolean var=params.algorithm.contains("Random");
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
        /*Object[] precincts=startingMap.getAllPrecincts().toArray();
        Collection <DistrictForMap> districts=startingMap.getAllDistricts();
        for(DistrictForMap d: districts){
            PrecinctForMap p=(PrecinctForMap)precincts[precincts.length*(int)Math.random()];
            
            p.isAssigned=true;
            Move move=new Move(p, d);
            moves.add(move);
            currentMap.apply(move);
        }
        System.out.println("");*/
    	
    	PrecinctForMap[] pm;
    	Collection <DistrictForMap> districts = new ArrayList<>();
    	for(DistrictForMap d : startingMap.getAllDistricts()) {
    		if(!d.getMaster().getID().equals("CT_NULL") &&
    				!d.getMaster().getID().equals("NJ_NULL") &&
    				!d.getMaster().getID().equals("NE_NULL")) {
    			
    			districts.add(d);
    			
    		}
    	}
    	
    	for(DistrictForMap d : districts) {
    		pm = startingMap.getNullDistrict().getPrecincts().toArray(new PrecinctForMap[startingMap.getAllPrecincts().size()]);
			PrecinctForMap p = pm[rand.nextInt(pm.length)];
			
			Move move = new Move(p, d);
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
        if(!isDone()){
        	
            pickMove();
            updateProgress();
            //updateGUI();
        }
    }
                 
    /*
    Description:
        Chooses the neighboring precinct that results in the best goodness once added, for each district
    */
    @Override
    public void pickMove(){        

    	Set<PrecinctForMap> borderPrecincts = new HashSet<>();
    	Set <PrecinctForMap> nullP = currentMap.getNullDistrict().getPrecincts();
    	for(PrecinctForMap p : currentMap.getNullDistrict().getPrecincts()) {	// returning empty
    		if(p.isDistrictBorder()) {
    			borderPrecincts.add(p);
    		}
    	}
    	
    	Collection <DistrictForMap> districts = new ArrayList<>();
    	for(DistrictForMap d : currentMap.getAllDistricts()) {
    		if(!d.getMaster().getID().equals("CT_NULL") &&
    				!d.getMaster().getID().equals("NJ_NULL") &&
    				!d.getMaster().getID().equals("NE_NULL")) {
    			
    			districts.add(d);
    			
    		}
    	}
    	
    	List <Map> maps = new ArrayList<>();
    	List <Double> goodnesses = new ArrayList<>();
    	List <Move> theMoves = new ArrayList<>();
    	for(DistrictForMap d : districts) {
    		PrecinctForMap[] precincts = (PrecinctForMap[])borderPrecincts.toArray(new PrecinctForMap[borderPrecincts.size()]);
    		PrecinctForMap randomPrecinct=precincts[rand.nextInt(precincts.length)];
    		
    		System.out.println("population: " + randomPrecinct.getPopulation());
    		
    		Move m = new Move(randomPrecinct, d);
    		theMoves.add(m);
    		Map newM = currentMap.cloneApply(m);
    		maps.add(newM);
    		double good=ObjectiveFuncEvaluator.evaluateObjective(params.functionWeights, newM);
    		goodnesses.add(good);
    	}
    	
    	double highestGoodness = Collections.max(goodnesses);
    	
    	int index = 0;
    	for(int i = 0; i < goodnesses.size(); i++) {
    		if(goodnesses.get(i) == highestGoodness) {
    			index = i;
    			break;
    		}
    	}
    	
    	Map nm = currentMap.cloneApply(theMoves.get(index));
    	currentMap = nm;
    	moves.add(theMoves.get(index));

        
        
        System.out.println("goodness: " + highestGoodness);
        System.out.println("number in null district left: " + currentMap.getNullDistrict().getPrecincts().size());
    	//}
    	
    }
    
    /*
    Description:
        Updates the simulations progress, which is based on the # of moves 
        # of moves/ # of precincts in state -> one move per precinct
    */
    @Override
    public void updateProgress(){
        progress=moves.size()/1;
        System.out.println("UpdatingMap");
        this.savedSim.setCurrentMap(currentMap);
    }
    
    @Override
    public boolean isDone() {
    	return (currentMap.getNullDistrict().getPrecincts().isEmpty());
    }
}