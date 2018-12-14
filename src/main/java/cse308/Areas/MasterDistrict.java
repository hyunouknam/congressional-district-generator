package cse308.Areas;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

//import javax.persistence.Entity;
//import javax.persistence.Id;

@Entity
@Table(name="district")
@Immutable
public class MasterDistrict{

    @Id
    private String id;
    private String name;
    
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
    
    public String jsonFormat() {
    	String s = "{";
    	s = s + "\"id\":";
    	s = s + "\"" + id + "\"";
    	s = s + ", \"name\":";
    	s = s + "\"" + name + "\"";
    	s = s + "}";
    	return s;
    }
    
    public String toString() {
    	String s = "id: " + id;
    	s = s + "name: " + name;
    	return s;
    }
}