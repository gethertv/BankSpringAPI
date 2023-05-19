package pl.edu.pwste.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  boolean existsByBankAccountNumber(String numberAccount);

  User findByBankAccountNumber(String bankAccountNumber);
  @Modifying
  @Query("UPDATE User SET email = :newEmail WHERE id = :userId")
  int updateEmail(@Param("userId") Integer userId, @Param("newEmail") String newEmail);


}
