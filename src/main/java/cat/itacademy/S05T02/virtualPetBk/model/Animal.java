package cat.itacademy.S05T02.virtualPetBk.model;

public enum Animal {DOG("Dog"), LION("Lion"), KANGAROO("Kangaroo"), KOALA("Koala");
    private String animalDesc;

    Animal(String animalDesc) {
        this.animalDesc = animalDesc;
    }
}
