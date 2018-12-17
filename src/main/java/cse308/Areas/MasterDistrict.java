package cse308.Areas;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cse308.Data.Util;
import org.hibernate.annotations.Immutable;
import org.json.JSONObject;

//import javax.persistence.Entity;
//import javax.persistence.Id;

@Entity
@Table(name="district")
@Immutable
public class MasterDistrict{

    @Id
    private String id;
    private String name;
    
    @ManyToOne
	@JoinColumn(name = "state_id")
    private MasterState state;
    
    public MasterDistrict() {
    	
    }
    
    public MasterDistrict(String n){
        name=n;
    }
    public String getID(){
        return id;
    }
    
    public void setID(String i){
        id=i;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name) {
    	this.name=name;
    }
    
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("id", id);
        j.put("name", name);


        //dont need to add any extra data, as district data is now computed on client
        return j;
    }

    public String toString() {
    	String s = "(id: " + id;
    	s = s + "name: " + name + "java: " + System.identityHashCode(this) + ")";
    	return s;
    }
}