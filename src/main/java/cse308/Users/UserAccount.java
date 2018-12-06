package cse308.Users;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import cse308.Areas.Map;
import cse308.Simulation.Simulation;

@Entity
@Table(name="users")
@EntityListeners(AuditingEntityListener.class)
public class UserAccount {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
    private UserRole role;
    
    @ElementCollection
    private List<String> preferences;
    
    @OneToMany
    @JoinColumn(name="map_id")
    private List<Simulation> sims;
    private String username;
    private String password;
//    private boolean signedIn=false;
    
    public UserAccount(){
        role=UserRole.TEMP;
    }
    
    public UserAccount(String user, String pass){
        role=UserRole.REGISTERED;
        username=user;
        password=pass;
        preferences = new ArrayList<>();
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

	public List<String> getPreferences() {
		return preferences;
	}

	public void setPreferences(List<String> preferences) {
		this.preferences = preferences;
	}

	public List<Simulation> getMaps() {
		return sims;
	}

	public void setMaps(List<Simulation> sims) {
		this.sims = sims;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

//	public boolean isSignedIn() {
//		return signedIn;
//	}
//
//	public void setSignedIn(boolean signedIn) {
//		this.signedIn = signedIn;
//	}

	public void register(String user, String pass){
//        role=UserRole.REGISTERED;
        username=user;
        password=pass;
//        EntityManager.addUser(this);
    }
//    
//    public boolean login(){
//        if(UserManager.checkUserPassword(username, password)){
//            signedIn=true;
//        }
//        return signedIn;        
//    }
//    
//    public void logout(){
//        signedIn=false;
//    }
	
	public String toString() {
		return "username: " + username + " password: " + password;
	}
}
