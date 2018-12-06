package cse308.Simulation;

import cse308.Areas.DistrictForMap;
import cse308.Areas.Map;
import cse308.Areas.PrecinctForMap;
import cse308.Users.UserAccount;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
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
    
    public SimulatedAnnealingSimulation(UserAccount u, SimulationParams s){
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
            temperature=1.0f;
            alpha=0.9f;
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
        //EntityManager.findCurrentMap(State s);`         
        currentGoodness=getGoodness();
        return null;
    }
    
    /*
    Description:
        Tries to increase goodness <rounds> times at the given temperature
    */
    @Override
    public void doStep()throws CloneNotSupportedException{
        for (int i=0;i<rounds;i++){
            pickMove();
        }
        if(currentGoodness>bestMap.getGoodness()){
            bestMap=currentMap;
        }
        temperature*=alpha;
    }

    /*
    Description:
        Chooses a random district (A) and gets a random precinct on its border.
        Chooses a random district (B) that borders the previously chosen district.
        Moves the precinct from precinct A to precinct B if it's likely to improve the goodness.
    */
    @Override
    public void pickMove() throws CloneNotSupportedException{
        Object[] districts=currentMap.getAllDistricts().values().toArray();
        DistrictForMap randomDistrict=(DistrictForMap)districts[districts.length*(int)Math.random()]; //gets random district
        Object[] precincts=randomDistrict.getBorderPrecincts().toArray();
        PrecinctForMap randomPrecinct=(PrecinctForMap)precincts[precincts.length*(int)Math.random()]; //gets random border precinct
        Set<DistrictForMap> neighborDistricts=new HashSet<>();
        HashMap<PrecinctForMap, DistrictForMap> precinctDistrictMapping=currentMap.getPrecinctDistrictMapping();
        for(PrecinctForMap p: randomPrecinct.getNeighborPrecincts()){
            DistrictForMap district=precinctDistrictMapping.get(p);
            if(!district.equals(randomDistrict)){
                neighborDistricts.add(district); //adds each district bordering the previosuly chosen district
            }
        }
        Object[] newDistricts=neighborDistricts.toArray();
        DistrictForMap newDistrict=(DistrictForMap)newDistricts[newDistricts.length*(int)Math.random()]; //chooses random border district for move
        Move m=new Move(randomPrecinct, randomDistrict, newDistrict);
        Map nextMap=currentMap.cloneApply(this.params.functionWeights,m);
        float nextGoodness=nextMap.getGoodness();
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
        moves.add(m);
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
    
    public double calcAcceptanceProb(float current, float next, double temp){
        return Math.E*(next-current)/temp;
    }
}