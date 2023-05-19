package pl.edu.pwste.token;

import java.util.List;
import java.util.Optional;

import pl.edu.pwste.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, Integer> {

  @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
  List<Token> findAllValidTokenByUser(Integer id);

  Optional<Token> findByToken(String token);

  @Query("SELECT t FROM Token t WHERE t.user = :user AND t.revoked = false")
  Optional<Token> findActiveTokenByUser(@Param("user") User user);

}
