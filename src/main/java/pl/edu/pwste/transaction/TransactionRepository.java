package pl.edu.pwste.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("SELECT t FROM Transaction t WHERE t.fromUser.email = :email OR t.toUser.email = :email")
    List<Transaction> findByEmail(String email);

    Page<Transaction> findByFromUserEmail(String email, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.bankAccountNumber = :accountNumber OR u.bankAccountNumber = :accountNumber")
    boolean existsByAccountNumber(String accountNumber);
}
