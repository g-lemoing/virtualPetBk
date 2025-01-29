package cat.itacademy.S05T02.virtualPetBk.repository;

import cat.itacademy.S05T02.virtualPetBk.model.User;
import cat.itacademy.S05T02.virtualPetBk.model.UserPet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPetRepository extends JpaRepository<UserPet, Integer> {
    List<UserPet> findAllUserPetsByUserId(int userId);
    List<UserPet> findByUserIdAndPetName(int userId, String petName);
}
