package cse308.Simulation;

import cse308.Areas.DistrictForMap;
import cse308.Areas.Map;
import cse308.Areas.PrecinctForMap;
import cse308.Users.UserAccount;

public class SimulatedAnnealingSimulation extends Simulation{
    private float temperature;
    private double alpha;
    private int rounds;
    private boolean repeat=false;
    public Map bestMap;
    
    public SimulatedAnnealingSimulation(UserAccount u, SimulatedAnnealingParams s){
        super(u,s);
        startingMap=getStartingMap();
        currentMap=startingMap;
        bestMap=currentMap;
        temperature=1.0f;
        alpha=0.9;
        rounds=250;      
    }
    
    /*
    Description:
        Gets the current districting from the database (latest election)
    */
    private Map getStartingMap(){  
        currentMap=params.getState().getCurrentMap();
        if(((SimulatedAnnealingParams)params).startingMap!=null){
            currentMap=((SimulatedAnnealingParams)params).startingMap;
        }        
        currentGoodness=ObjectiveFuncEvaluator.evaluateObjective(params.functionWeights, currentMap);
        return this.params.getState().getCurrentMap();
    }
    
    /*
    Description:
        Tries to increase goodness <rounds> times at the given temperature
    */
    @Override
    public void doStep(){
        for (int i=0;i<rounds;i++){
            pickMove();
            //TODO: keep track of total steps taken
        }
        updateProgress();
        //updateGUI();        
    }

    /*
    Description:
        Chooses a random district (A) and gets a random precinct on its border.
        Chooses a random district (B) that borders the previously chosen district.
        Moves the precinct from precinct A to precinct B if it's likely to improve the goodness.
    */
    @Override
    public void pickMove() {
        boolean var=params.algorithm.contains("Random")? false: true;
        DistrictForMap district = var ? variantTwo():variantOne();
        
        Object[] precincts=district.getBorderPrecincts().toArray();
        PrecinctForMap randomPrecinct=(PrecinctForMap)precincts[precincts.length*(int)Math.random()]; //gets random border precinct
        
        Object[] newDistricts = randomPrecinct.getNeighborDistricts().toArray(); 
        DistrictForMap newDistrict=(DistrictForMap)newDistricts[newDistricts.length*(int)Math.random()]; //chooses random border district for move
        while (newDistrict.getMaster().getID().contains("NULL")){
            newDistrict=(DistrictForMap)newDistricts[newDistricts.length*(int)Math.random()];
        }
        Move m=new Move(randomPrecinct, newDistrict);
        Map nextMap=currentMap.cloneApply(m);        
        double nextGoodness=ObjectiveFuncEvaluator.evaluateObjective(params.functionWeights, nextMap);
        if(nextGoodness>currentGoodness){
            currentMap=nextMap;
            currentGoodness=nextGoodness;
            moves.add(m);
        }
        else{
            if(currentGoodness==nextGoodness){
                repeat=true;
                return;
            }
            if(calcAcceptanceProb(currentGoodness, nextGoodness, temperature)>Math.random()){
                currentMap=nextMap;
                currentGoodness=nextGoodness;
                moves.add(m);
            }
        }
        if(currentGoodness>ObjectiveFuncEvaluator.evaluateObjective(params.functionWeights,bestMap)){
            bestMap=currentMap;
        }        
    }
    
    /*
    Description:
        Chooses a random district (A) and gets a random precinct on its border.
        Chooses a random district (B) that borders the previously chosen district.
    */
    public DistrictForMap variantOne(){
        Object[] districts=currentMap.getAllDistricts().toArray();       
        DistrictForMap randomDistrict=(DistrictForMap)districts[districts.length*(int)Math.random()]; //gets random district
        return randomDistrict;
    }
    
    /*
    Description:
        Chooses the district (A) with the highest population.
        Chooses a random district (B) that borders the previously chosen district.
    */
    public DistrictForMap variantTwo(){
        Object[] districts=currentMap.getAllDistricts().toArray();
        DistrictForMap mostPopulated=(DistrictForMap)districts[0];
        int population=mostPopulated.getPopulation();
        for (int i=1;i<districts.length;i++){
            DistrictForMap d=(DistrictForMap)districts[i];
            if(d.getPopulation()>population){
                mostPopulated=d;
                population=d.getPopulation();
            }
        }
        return mostPopulated;
    }
    
    /*
    Description:
        Updates the simulations progress, which is based on alpha 
        alpha*run/ 1 -> 1/alpha runs
    */
    @Override
    public void updateProgress(){
        temperature*=alpha;
        if(repeat){
            progress=0;
        }
        else{
            progress=temperature;
        }            
        //number of runs=Temp/alpha*[100-1000]
        //progress will be updated after each temp change: so divided into temp/alpha's
    }
    
    public double calcAcceptanceProb(double current, double next, double temp){
        return Math.E*(next-current)/temp;
    }
    
    @Override
    public boolean isDone() {        
    	return temperature<0.0001;
    }
}