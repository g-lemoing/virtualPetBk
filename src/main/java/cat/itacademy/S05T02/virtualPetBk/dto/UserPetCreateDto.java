package cat.itacademy.S05T02.virtualPetBk.dto;

import cat.itacademy.S05T02.virtualPetBk.model.Animal;
import cat.itacademy.S05T02.virtualPetBk.model.PetColor;

public class UserPetCreateDto {
    private int userId;
    private Animal animal;
    private String petName;
    private PetColor petColor;

    public UserPetCreateDto() {
    }

    public UserPetCreateDto(int userId, Animal animal, String petName, PetColor petColor) {
        this.userId = userId;
        this.animal = animal;
        this.petName = petName;
        this.petColor = petColor;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setPetId(Animal animal) {
        this.animal = animal;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public PetColor getPetColor() {
        return petColor;
    }

    public void setPetColor(PetColor petColor) {
        this.petColor = petColor;
    }
}
