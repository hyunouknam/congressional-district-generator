package cse308.Areas;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MasterDistrict{

    @Id
    private int id;
    private final String name;
    
    
    public MasterDistrict(String n){
        name=n;
    }
    public int getID(){
        return id;
    }
    
    public void setID(int i){
        id=i;
    }
    
    public String getName(){
        return name;
    }
    
    public String jsonFormat() {
    	String s = "{";
    	s = s + "\"id\":";
    	s = s + "\"" + id + "\"";
    	s = s + ", \"name\":";
    	s = s + "\"" + name + "\"";
    	s = s + "}";
    	return s;
    }
}