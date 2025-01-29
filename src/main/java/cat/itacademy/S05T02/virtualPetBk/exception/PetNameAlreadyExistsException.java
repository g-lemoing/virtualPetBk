package cat.itacademy.S05T02.virtualPetBk.exception;

public class PetNameAlreadyExistsException extends RuntimeException {
    public PetNameAlreadyExistsException(String petName, int userId) {
        super(petName + " already exists as pet name for user " + userId );
    }
}
