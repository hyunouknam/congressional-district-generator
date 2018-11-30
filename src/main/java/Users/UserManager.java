package Users;

public class UserManager {
    public UserAccount getTemporaryUser(){
        UserAccount temp=new UserAccount();
        return temp;
    }
    
    public static UserAccount getRegisteredUser(String uname){
        //EntityManager.findUser(uname);
        return null;
    }
    
    public static boolean checkUserPassword(String uname, String password){
        //compare uname and passwrod to entries in database
        //EntityManager.userExists(uname, password);
        return false;
    }
}
