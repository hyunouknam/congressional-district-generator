package Simulation;

import Areas.PrecinctForMap;
import Users.UserAccount;

public class SimulatedAnnealingSimulation extends Simulation{
    private float temperature;
    private float nextGoodness;
    
    public SimulatedAnnealingSimulation(UserAccount u, SimulationParams s){
       //initializes Algorithm? or calls initializeAlgotithm?
       super(u,s);
    }
    
    private void getStartingDistricting(){
        //start is current districting? From 2016 election?
    }
    
    @Override
    public boolean isAcceptable(){
        return false;
    }
    
    @Override
    public void initializeAlgorithm(){
        //startingMap is current districting, pull from DB
        
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
