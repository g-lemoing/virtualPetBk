package cat.itacademy.S05T02.virtualPetBk.exception;

public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException(int petUserId) {
        super("Pet not found with petUserId " + petUserId);
    }
}
