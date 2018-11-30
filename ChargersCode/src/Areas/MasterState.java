package Areas;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;

import Data.PrecinctRepository;
import Data.StateRepository;

@Entity
public class MasterState{

    @Id
    private int id;
    private final String name;
    private final String consText;
    private final boolean popIsEst;
    private final int numOfDistricts;
    private final Map currentMap;
    private Set<MasterDistrict> districts;
    private Set<MasterPrecinct> precincts;

    @Autowired
    private PrecinctRepository precinctRepository;
    
    public MasterState(String name, String consText, boolean popIsEst, int numOfDistricts){
        this.name=name;
        this.consText=consText;
        this.popIsEst=popIsEst;
        this.numOfDistricts=numOfDistricts;

        precinctRepository.findAll().forEach(precincts::add);
        currentMap = new Map(this);
    }
    
    public String getName(){
        return name;
    }
    
    public String getConsText(){
        return consText;
    }
    
    public boolean isPopEst(){
        return popIsEst;
    }
    
    public int getNumDistricts(){
        return numOfDistricts;
    }
    
    public Map getCurrentMap(){
        return currentMap;
    }
    
    public Set<MasterDistrict> getDistricts(){
        return districts;
    }
    
    public Set<MasterPrecinct> getPrecincts(){
        return precincts;
    }
    
    public int getID(){
        return id;
    }
    
    public void setID(int i){
        id=i;
    }
}