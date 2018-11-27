package Simulation;

import java.util.ArrayList;

public class SimulationWorker extends Thread{
    private static ArrayList<Simulation> queue;
    
    public static void addToRunQueue(Simulation sim){
        queue.add(sim);
    }
    
    public static void runNextSimulation(){
        Simulation sim=queue.get(0);
        while (!sim.isDone()){
            sim.doStep();
        }
        queue.remove(sim);
    }
}
