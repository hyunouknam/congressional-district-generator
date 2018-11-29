package Users;

public class UserManager {
    public UserAccount getTemporaryUser(){
        UserAccount temp=new UserAccount();
        return temp;
    }
    
    public  UserAccount createRegisteredUser(){
        //get data from GUI and create nw user
        UserAccount newUser=new UserAccount(/*Takes params*/);
        return newUser;
    }
    
    public UserAccount getRegisteredUser(){
        //pull user from DB
        return null;
    }
    
    public UserAccount checkUserPassword(String username, String password){
        //compare uname and passwrod to entries in database
        return null; //or user account
    }
}
