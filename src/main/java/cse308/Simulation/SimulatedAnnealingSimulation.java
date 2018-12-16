package cse308.Simulation;

import cse308.Areas.DistrictForMap;
import cse308.Areas.Map;
import cse308.Areas.PrecinctForMap;
import cse308.Users.UserAccount;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class SimulatedAnnealingSimulation extends Simulation{
    private double temperature;
    private final double alpha;
    private final int rounds;
    protected Map bestMap;
    private boolean repeat=false;
    
    public SimulatedAnnealingSimulation(UserAccount u, SimulatedAnnealingParams s){
        super(u,s);
        startingMap=getStartingMap();
        currentMap=startingMap;
        bestMap=currentMap;
        File properties=new File(".."+File.separator+".."+File.separator+".."+File.separator+"resources"+File.separator+"constants.properties");
        JsonReader reader;
        try{
            reader=Json.createReader(new FileReader(properties));
        }catch (FileNotFoundException error){
            System.err.println("Properties file could not be found, using default values.");
            temperature=1.0;
            alpha=0.9;
            rounds=100;
            return;
        }       
        JsonObject json=reader.readObject();
        temperature=json.getJsonNumber("temperature").doubleValue();
        alpha=json.getJsonNumber("alpha").doubleValue();
        rounds=json.getJsonNumber("rounds").intValue();
    }
    
    /*
    Description:
        Gets the current districting from the database (latest election)
    */
    private Map getStartingMap(){
        //start is current districting? From 2016 election?
        //currentMap=EntityManager.findCurrentMap(State s);      
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
        updateGUI();
        temperature*=alpha;
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
        DistrictForMap district = var ? variantOne():variantTwo();
     
        Object[] precincts=district.getBorderPrecincts().toArray();
        PrecinctForMap randomPrecinct=(PrecinctForMap)precincts[precincts.length*(int)Math.random()]; //gets random border precinct
        
        Set<DistrictForMap> neighborDistricts = randomPrecinct.getNeighborDistricts();
        
        Object[] newDistricts=neighborDistricts.toArray();
        DistrictForMap newDistrict=(DistrictForMap)newDistricts[newDistricts.length*(int)Math.random()]; //chooses random border district for move
        
        Move m=new Move(randomPrecinct, newDistrict);
        Map nextMap=currentMap.cloneApply(m);
        
        double nextGoodness=ObjectiveFuncEvaluator.evaluateObjective(params.functionWeights, nextMap);
        if(nextGoodness>currentGoodness){
            currentMap=nextMap;
            currentGoodness=nextGoodness;
        }
        else{
            if(currentGoodness==nextGoodness){
                repeat=true;
                return;
            }
            if(calcAcceptanceProb(currentGoodness, nextGoodness, temperature)>Math.random()){
                currentMap=nextMap;
                currentGoodness=nextGoodness;
            }
        }
        if(currentGoodness>ObjectiveFuncEvaluator.evaluateObjective(params.functionWeights,bestMap)){
            bestMap=currentMap;
        }
        moves.add(m);
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
        //progress would be 1 if temp reaches min or goodness has stayed constant
        if(repeat){
            progress=1;
        }
        else{
            progress+=alpha;
        }            
        //number of runs=Temp/alpha*[100-1000]
        //progress will be updated after each temp change: so divided into temp/alpha's
    }
    
    public double calcAcceptanceProb(double current, double next, double temp){
        return Math.E*(next-current)/temp;
    }
}