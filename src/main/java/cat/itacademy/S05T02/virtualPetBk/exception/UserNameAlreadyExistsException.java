package cat.itacademy.S05T02.virtualPetBk.exception;

public class UserNameAlreadyExistsException extends RuntimeException{
    public UserNameAlreadyExistsException(String userName){
        super("User name " + userName + " already exists in database.");
    }
}
