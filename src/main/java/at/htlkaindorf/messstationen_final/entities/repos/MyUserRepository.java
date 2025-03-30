package at.htlkaindorf.messstationen_final.entities.repos;

import at.htlkaindorf.messstationen_final.entities.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Integer> {
    MyUser findMyUserByUsername(String username);
}
