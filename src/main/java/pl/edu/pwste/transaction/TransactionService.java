package pl.edu.pwste.transaction;

import pl.edu.pwste.user.User;
import pl.edu.pwste.user.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public List<Transaction> getTransactions(String email, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findByFromUserEmail(email, pageable).getContent();
    }

    public double getAccountBalance(String accountNumber) {
        User user = userRepository.findByBankAccountNumber(accountNumber);
        if (user != null) {
            return user.getBalance();
        } else {
            return 0.0;
        }
    }

    /*

        public List<TransactionResponse> getTransactions(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactionPage = transactionRepository.findByFromUserEmail(email, pageable);
        List<Transaction> transactions = transactionPage.getContent();

        List<TransactionResponse> transactionResponses = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionResponse response = new TransactionResponse();
            response.setId(transaction.getId());
            response.setAmount(transaction.getAmount());
            response.setFromUserEmail(transaction.getFromUser().getEmail());
            response.setToUserEmail(transaction.getToUser().getEmail());
            response.setCreatedAt(transaction.getCreatedAt());
            transactionResponses.add(response);
        }

        return transactionResponses;
    }
     */
    public boolean isAccountNumberExists(String accountNumber) {
        return transactionRepository.existsByAccountNumber(accountNumber);
    }
    public TransactionResponse createTransactionByAccountNumbers(String title, String address, double amount, String fromAccountNumber, String toAccountNumber) {
        User fromUser = userRepository.findByBankAccountNumber(fromAccountNumber);
        User toUser = userRepository.findByBankAccountNumber(toAccountNumber);



        if(fromUser ==null || toUser==null)
            return null;

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setFromUser(fromUser);
        transaction.setToUser(toUser);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setTitle(title);
        transaction.setAddress(address);

        transactionRepository.save(transaction);

        fromUser.setBalance(fromUser.getBalance()-amount);

        toUser.setBalance(toUser.getBalance()+amount);

        userRepository.save(fromUser);


        return convertToTransactionResponse(transaction);
    }

    private TransactionResponse convertToTransactionResponse(Transaction transaction) {
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setId(transaction.getId());
        transactionResponse.setAmount(transaction.getAmount());
        transactionResponse.setFromUserEmail(transaction.getFromUser().getEmail());
        transactionResponse.setToUserEmail(transaction.getToUser().getEmail());
        transactionResponse.setCreatedAt(transaction.getCreatedAt());
        transactionResponse.setMessage("Pomyślnie zrealizowano przelew!");


        return transactionResponse;
    }
}
