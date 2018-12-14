package cse308.Simulation;

import cse308.Areas.DistrictForMap;
import cse308.Areas.MasterState;
import cse308.Areas.PrecinctForMap;

public class Move {
    PrecinctForMap precinct;
    DistrictForMap dOld;
    DistrictForMap dNew;    

    public Move(PrecinctForMap p, DistrictForMap oldD, DistrictForMap newD){
        assert p.getMap() == oldD.getMap() && p.getMap() == newD.getMap(): "Move must be defined on prec and dists of the same map";

        precinct=p;
        dOld=oldD;
        dNew=newD;
    }
    
    public MasterState getState(){ return precinct.getMap().getState(); }
    
    public PrecinctForMap getPrecinct(){ return precinct; }
    public DistrictForMap getOldDistrict(){ return dOld; }
    public DistrictForMap getNewDistrict(){ return dNew; }
}