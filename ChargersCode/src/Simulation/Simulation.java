package Simulation;

import Areas.Map;
import Areas.PrecinctForMap;
import Users.UserAccount;
import java.util.Stack;

public abstract class Simulation {
   protected UserAccount user;
   protected SimulationParams params;
   protected Map startingMap;
   protected Stack<Move> moves;
   protected Map currentMap; //startMap+all moves so far
   protected float currentGoodness; //goodness of the current map
   
   public Simulation(UserAccount u, SimulationParams s){
       params=s;
       user=u;
   }
   
   public float getProgress(){
       //return value between 0 and 1
       return 0;
   }
   
   public void pause(){
       
   }
   
   public void terminate(){
       //setisdone to yes
       //remove from simworker's queue
   }
   
   public boolean isDone(){
       return getProgress()==1;
   }
   
   public abstract void doStep()throws CloneNotSupportedException;
   
   public abstract void pickMove();
   
   public abstract void updateDistricts(PrecinctForMap a, PrecinctForMap b);
   
   public void postUpdate(){
       
   }
   
   public void queueForWork(){
       SimulationWorker.addToRunQueue(this);
   }
   
   public float getGoodness(){
       return currentGoodness;
   }
}
