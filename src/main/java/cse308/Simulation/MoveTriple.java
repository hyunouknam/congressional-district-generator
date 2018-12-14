package cse308.Simulation;

import cse308.Areas.Map;

public class MoveTriple implements Comparable<MoveTriple>{
    double goodness;
    Map map;
    Move move;
    
    public MoveTriple(double g, Map map, Move move){
        goodness=g;
        this.map=map;
        this.move=move;
    }
    
    @Override
    public int compareTo(MoveTriple t){
        if(t.goodness>goodness){
            return -1;
        }
        else if (t.goodness<goodness){
            return 1;
        }
        return 0;
    }
    
    @Override
    public boolean equals(Object t){
        MoveTriple triple=(MoveTriple)t;
        return triple.goodness==goodness;
    }
    
}
