package Simulation;

import Areas.Map;
import Areas.PrecinctForMap;
import Users.UserAccount;

public class RegionGrowingSimulation extends Simulation{
    
    /*
        Decisions are based on similarity constraints (we choose)
            Can be grown randomly as well
    1.) Start with seed, compare to neighbors
    2.) Stop after certain conditions are met
        Shouldn't go past population/# of districts
    3.) Get another seed, repeat step 2
    4.) Simulation ends after all precincts have been assigned
    
    Or pick all seeds at once and grow all seeds together
    
    Problems can occur if seed is on an edge
    
    How to choose seeds:
        Random one in collection of unused seeds
    
    */
    
    /*
        Sequence Diagram Steps:
            //create map
            //n=# of districts that will be made=# of seeds that we'll get
            //get seeds and create districts and add one seed to each district
            //In loop:
                create move object, moving a precinct from unused district to an actual district
                apply the move
    */
        
    public RegionGrowingSimulation(UserAccount u, SimulationParams s){
       //initializes Algorithm? or calls initializeAlgotithm?
       super(u,s);
    }
    
    public void getSeedPrecincts(){
        
        
    }
    
    @Override
    public boolean isAcceptable(){
        return false;
    }
    
    @Override
    public void initializeAlgorithm(){
        startingMap=new Map(params.getState());
        
    }  
    
    @Override
    public void doStep(){
        
    }

    @Override
    public void pickMove(){
        
    }
    
    @Override
    public void updateDistricts(PrecinctForMap a, PrecinctForMap b){
        
    }
}
