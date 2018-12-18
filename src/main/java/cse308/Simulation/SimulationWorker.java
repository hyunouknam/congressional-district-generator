package cse308.Simulation;

import cse308.Areas.Map;
import java.util.ArrayList;

public class SimulationWorker extends Thread{
    private ArrayList<Simulation> queue=new ArrayList<>();
    
    public void addToRunQueue(Simulation sim){
        queue.add(sim);
    }
    
    public void removeFromRunQueue(Simulation sim){
        queue.remove(sim);
    }
    
    public void runNextSimulation(){
        if(!queue.isEmpty()){
            Simulation sim=queue.get(0);
            Map result;
            System.out.println("In runNextSim");
            while (!sim.isDone() && !sim.isPaused){
                System.out.println("Doing step");
                sim.doStep();
            }
        //        if(sim.isDone() && sim.getClass().equals(SimulatedAnnealingSimulation.class)){
        //            Map bestMap=((SimulatedAnnealingSimulation)sim).bestMap;
        //            if(ObjectiveFuncEvaluator.evaluateObjective(sim.get, bestMap)>sim.getGoodness()){
        //                result=bestMap;
        //            }
        //        }
            result=sim.currentMap;
            if (sim.isDone()){ 
                queue.remove(0);
            }
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
    
    @Override
    public void run(){
        if(!queue.isEmpty()){
            runNextSimulation();
        }
    }
}