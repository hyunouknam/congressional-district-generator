package Simulation;

import Areas.MasterState;
import Areas.PrecinctForMap;
import Areas.DistrictForMap;

public class Move {
    PrecinctForMap precinct;
    DistrictForMap dOld;
    DistrictForMap dNew;
    
    Move subMove;
    
    public MasterState getState(){
        return precinct.getMap().getState();
    }
    
}
