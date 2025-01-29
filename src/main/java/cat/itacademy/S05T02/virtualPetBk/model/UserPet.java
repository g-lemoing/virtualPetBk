package cat.itacademy.S05T02.virtualPetBk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users_has_pets")
public class UserPet {
    @Id
    @Column(name = "users_has_pets_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int petUserId;
    @Column(name = "users_id")
    private int userId;
    @Column(name = "pets_animal")
    @Enumerated(EnumType.STRING)
    private Animal animal;
    @Column(name = "pet_name")
    private String petName;
    @Column(name = "pet_color")
    @Enumerated(EnumType.STRING)
    private PetColor petColor;
    @Column(name = "pet_energylevel")
    private double petEnergyLevel;
    @Column(name = "pet_mood")
    private double petMood;
    @Column(name = "pet_hungrylevel")
    private double petHungryLevel;

    public UserPet() {
    }

    public UserPet(int userId, Animal animal, String petName, PetColor petColor,
                   double petEnergyLevel, double petMood, double petHungryLevel) {
        this.userId = userId;
        this.animal = animal;
        this.petName = petName;
        this.petColor = petColor;
        this.petEnergyLevel = petEnergyLevel;
        this.petMood = petMood;
        this.petHungryLevel = petHungryLevel;
    }

    public int getPetUserId() {
        return petUserId;
    }

    public void setPetUserId(int petUserId) {
        this.petUserId = petUserId;
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

    public void setAnimal(Animal animal) {
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

    public double getPetEnergyLevel() {
        return petEnergyLevel;
    }

    public void setPetEnergyLevel(double petEnergyLevel) {
        this.petEnergyLevel = petEnergyLevel;
    }

    public double getPetMood() {
        return petMood;
    }

    public void setPetMood(double petMood) {
        this.petMood = petMood;
    }

    public double getPetHungryLevel() {
        return petHungryLevel;
    }

    public void setPetHungryLevel(double petHungryLevel) {
        this.petHungryLevel = petHungryLevel;
    }
}
