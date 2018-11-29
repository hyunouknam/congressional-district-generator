package Simulation;

import Areas.Map;
import Areas.PrecinctForMap;
import Users.UserAccount;

public class SimulatedAnnealingSimulation extends Simulation{
    private float temperature;
    private float nextGoodness;
    
    public SimulatedAnnealingSimulation(UserAccount u, SimulationParams s){
       super(u,s);
       //startingMap is current districting, pull from DB
       startingMap=getStartingMap();
       currentMap=startingMap;
    }
    
    private Map getStartingMap(){
        //start is current districting? From 2016 election?
        //EntityManager.findCurrentMap(State s);`
        //set currentgoodness to the goodness of the map
        return null;
    }
    
    @Override
    public void doStep(){
        
    }

    @Override
    public void pickMove(){
        
    }
    @Override
    public void updateProgress(float p){
     
    }
    
    @Override
    public void updateDistricts(PrecinctForMap a, PrecinctForMap b){
        
    }
}
