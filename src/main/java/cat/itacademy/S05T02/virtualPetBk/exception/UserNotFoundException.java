package cat.itacademy.S05T02.virtualPetBk.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String userName){
        super("User not found with this user name:" + userName);
    }
}
