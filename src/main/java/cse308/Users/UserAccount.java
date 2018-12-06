package cse308.Users;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import cse308.Areas.Map;

//@Entity
public class UserAccount {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
    private UserRole role;
    private String[] preferences;
    private Map[] maps;
    private String username;
    private String password;
    private boolean signedIn=false;
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
    
    public UserAccount(){
        role=UserRole.TEMP;
    }
    
    public UserAccount(String user, String pass){
        role=UserRole.REGISTERED;
        username=user;
        password=pass;
    }
    
    public void register(String user, String pass){
        role=UserRole.REGISTERED;
        username=user;
        password=pass;
        //EntityManager.addUser(this);
    }
    
    public boolean login(){
        if(UserManager.checkUserPassword(username, password)){
            signedIn=true;
        }
        return signedIn;        
    }
    
    public void logout(){
        signedIn=false;
    }
    
    public void getMaps(){
        //maps=EntityManger.getMapsForUser(username);        
    }
    
    public void setPreferences(String[] pref){
        preferences=pref;
    }
    public void setMaps(Map[] maps){
        this.maps=maps;
    }

	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}
	
	public String toString() {
		return "username: " + username + " password: " + password;
	}
}
