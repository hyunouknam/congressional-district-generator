package Simulation;

import java.util.ArrayList;

public class SimulationWorker extends Thread{
    private ArrayList<Simulation> queue;
    
    public void addToRunQueue(Simulation sim){
        queue.add(sim);
    }
    
    public void runNextSimulation(){
        Simulation sim=queue.get(0);
        while (!sim.isDone()){
            sim.doStep();
        }
        queue.remove(sim);
    }
}
