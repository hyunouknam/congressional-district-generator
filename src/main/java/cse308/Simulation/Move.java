package cse308.Simulation;

import cse308.Areas.DistrictForMap;
import cse308.Areas.MasterDistrict;
import cse308.Areas.MasterPrecinct;
import cse308.Areas.MasterState;
import cse308.Areas.PrecinctForMap;

public class Move {
    MasterPrecinct precinct;
    MasterDistrict dNew;    

    public Move(PrecinctForMap p, DistrictForMap newD){
        assert p.getMap() == newD.getMap(): "Move must be defined on prec and dists of the same map";
        init(p.getMaster(), newD.getMaster());
    }
    
    public Move(MasterPrecinct p, MasterDistrict newD){
    	init(p, newD);
    }
    
    private void init(MasterPrecinct p, MasterDistrict d) {
    	this.precinct = p;
    	this.dNew = d;
    }
    
    public MasterPrecinct getPrecinct(){ return precinct; }
    public MasterDistrict getNewDistrict(){ return dNew; }
}