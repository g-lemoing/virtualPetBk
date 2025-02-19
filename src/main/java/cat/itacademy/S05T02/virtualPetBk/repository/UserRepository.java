package cat.itacademy.S05T02.virtualPetBk.repository;

import cat.itacademy.S05T02.virtualPetBk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByUserName(String userName);
}
