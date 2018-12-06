package cse308.Simulation;

import cse308.Areas.Map;
import java.util.ArrayList;

public class SimulationWorker extends Thread{
    private static ArrayList<Simulation> queue=new ArrayList<>();
    
    public static void addToRunQueue(Simulation sim){
        queue.add(sim);
    }
    
    public static void removeFromRunQueue(Simulation sim){
        queue.remove(sim);
    }
    
    public static void runNextSimulation(){
        Simulation sim=queue.get(0);
        Map result;
        if(sim.progress==0 && sim.getClass().equals(RegionGrowingSimulation.class)){
            ((RegionGrowingSimulation)sim).getSeedPrecincts();
        }
        while (!sim.isDone() && !sim.isPaused){
            try{
                sim.doStep();                
            }
            catch(CloneNotSupportedException exception){
                System.err.println("Clone of simulation's map couldn't be made");
            }            
        }
        if(sim.isDone() && sim.getClass().equals(SimulatedAnnealingSimulation.class)){
            Map bestMap=((SimulatedAnnealingSimulation)sim).bestMap;
            if(bestMap.getGoodness()>sim.getGoodness()){
                result=bestMap;
            }
        }
        result=sim.currentMap;
        //add result to database--entyManager.addSimulation(result)
        if (sim.isDone()){
            queue.remove(0);
        }
    }
    
    public void pause(){
        queue.get(0).isPaused=true;
    }
    
    
    public void proceed(){
        if(queue.get(0).isPaused!=false){
            queue.get(0).isPaused=false;
            runNextSimulation();
        }
    }
    
    public void terminate(){
        queue.get(0).isPaused=true;
        queue.remove(0);        
    }
}
