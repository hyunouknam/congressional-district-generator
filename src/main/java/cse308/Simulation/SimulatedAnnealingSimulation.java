package cse308.Simulation;

import cse308.Areas.Map;
import cse308.Areas.PrecinctForMap;
import cse308.Users.UserAccount;

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
    public void updateProgress(){
     
    }
}
