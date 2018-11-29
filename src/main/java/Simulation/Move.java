package Simulation;

import Areas.MasterState;
import Areas.PrecinctForMap;
import Areas.DistrictForMap;

public class Move {
    PrecinctForMap precinct;
    DistrictForMap dOld;
    DistrictForMap dNew;    
    Move subMove;
    
    public Move(PrecinctForMap p, DistrictForMap oldD, DistrictForMap newD){
        precinct=p;
        dOld=oldD;
        dNew=newD;
    }
    
    public MasterState getState(){
        return precinct.getMap().getState();
    }
    
}
