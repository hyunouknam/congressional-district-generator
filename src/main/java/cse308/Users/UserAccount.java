package cse308.Users;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import cse308.Simulation.SavedSimulation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import cse308.Simulation.Simulation;

@Entity
@Table(name="users")
@EntityListeners(AuditingEntityListener.class)
public class UserAccount {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
    private UserRole role;
    
//    private String preferencesJSON;

    //@OneToMany(mappedBy = "user")
    @Transient
    private List<SavedSimulation> sims;

    private String email;
    private String password;
//    private boolean signedIn=false;

    public UserAccount(){
        role=UserRole.TEMP;
        //preferences = new ArrayList<>();
		sims = new ArrayList<>();
    }
    
    public UserAccount(String user, String pass){
        role=UserRole.REGISTERED;
        email=user;
        password=pass;
//        preferences = new ArrayList<>();
        sims = new ArrayList<>();
    }
    
    
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public UserRole getRole() {
		return role;
	}
	public void setRole(UserRole role) {
		this.role = role;
	}

//	public List<String> getPreferences() {
//		return preferences;
//	}
//
//	public void setPreferences(List<String> preferences) {
//		this.preferences = preferences;
//	}

	public List<SavedSimulation> getMaps() { return sims; }
	public void setMaps(List<SavedSimulation> sims) { this.sims = sims; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public void register(String user, String pass){
//        role=UserRole.REGISTERED;
        email=user;
        password=pass;
//        EntityManager.addUser(this);
    }

	public String toString() {
		return "username: " + email + " password: " + password;
	}

	public JSONObject toJSON() {
    	System.out.println(String.format("Serializing User Account %s, sims has len %d", email, sims.size()));
    	JSONObject json = new JSONObject();;
    	json.put("username", email);
		json.put("id", String.valueOf(id));

		JSONArray simJsons = new JSONArray();
		for(SavedSimulation sim: this.sims) {
			simJsons.put(sim.toJSON());
		}
		json.put("simulations", simJsons);

		return json;
	}
}
