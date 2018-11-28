package Simulation;

import Areas.DistrictForMap;
import Areas.Map;
import Areas.PrecinctForMap;
import Users.UserAccount;
import java.util.ArrayList;

public class RegionGrowingSimulation extends Simulation{ 
    /*
        Sequence Diagram Steps:
            //In loop:
                create move object, moving a precinct from unused district to an actual district
                apply the move
                **Problems can occur if seed is on an edge**
    */
        
    public RegionGrowingSimulation(UserAccount u, SimulationParams s){
       super(u,s);
       startingMap=new Map(params.getState());
       currentMap=startingMap;       
    }
    
    public void getSeedPrecincts(){
        Object[] precincts=startingMap.getAllPrecincts().values().toArray();
        Object[] districts=startingMap.getAllDistricts().values().toArray();
        int numOfDistricts=districts.length;
        for(int j=0;j<numOfDistricts;j++){
            PrecinctForMap p=(PrecinctForMap)precincts[Integer.parseInt(""+precincts.length*Math.random())];
            p.isAssigned=true;
            Move move=new Move(p, startingMap.getNullDisrict(), (DistrictForMap)districts[j]);
            ((DistrictForMap)districts[j]).getPrecincts().add(p);
            moves.add(move);
            currentMap.apply(move);
        }
    }
        
    @Override
    public void doStep() throws CloneNotSupportedException{
        ArrayList<Float> goodnesses=new ArrayList<>();
        while (currentMap.getNullDisrict().getPrecincts().size()>0){
            for(DistrictForMap d: currentMap.getAllDistricts().values()){
                for(PrecinctForMap p: d.getBorderPrecincts()){
                    for(PrecinctForMap pm: p.getNeighborPrecincts()){
                        if(!p.isAssigned){
                            Map m=currentMap.cloneApply(new Move(p, currentMap.getNullDisrict(), d));
                            if(m.isAcceptable()){
                                goodnesses.add(m.getGoodness());
                            }
                        }
                    }
                }
            }
            //for each district
                //getBorderPrecincts()
                //getNeighborPrecincts()
                //cloneApply() isAcceptable() getGoodness()
                //choose the acceptable neighbor with the best resulting goodness (pickMove())
        }
    }

    @Override
    public void pickMove(){
        //choose the neighbor with the best resulting goodness
    }
    
    @Override
    public void updateDistricts(PrecinctForMap a, PrecinctForMap b){
        
    }
}
