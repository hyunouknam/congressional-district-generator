package Simulation;

import Areas.Map;
import Areas.PrecinctForMap;
import java.util.Stack;

public class Simulation {
   private UserAccount user;
   private SimulationParams simParams;
   private Map startingMap;
   private Stack<Move> moves;
   private Map currentMap; //startMap+all moves so far
   
   public float getProgress(){
       //return value between 0 and 1
       return 0;
   }
   
   public boolean isAlgorithmInitialized(){
       return false;
   }
   
   public void initializeAlgorithm(){
       
   }
   
   public void pause(){
       
   }
   
   public void terminate(){
       
   }
   
   public boolean isDone(){
       return getProgress()==1;
   }
   
   public void doStep(){
       //
   }
   
   public void pickMove(){
       
   }
   
   public void updateDistricts(PrecinctForMap a, PrecinctForMap b){
       
   }
   
   public void postUpdate(){
       
   }
   
   public void queueForWork(Simulation s){
       
   }
}
