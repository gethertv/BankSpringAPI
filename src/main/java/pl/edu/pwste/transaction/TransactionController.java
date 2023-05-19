package pl.edu.pwste.transaction;

import pl.edu.pwste.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    @GetMapping("")
    public ResponseEntity<List<TransactionDTO>> getUserTransactions(
            Authentication authentication,
              @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String email = authentication.getName();
        List<Transaction> transactions = transactionService.getTransactions(email, page, size);

        List<TransactionDTO> transactionDTOs = new ArrayList<>();

        for (Transaction transaction : transactions) {
            UserDTO fromUserDTO = new UserDTO(
                    transaction.getFromUser().getId(),
                    transaction.getFromUser().getFirstname(),
                    transaction.getFromUser().getLastname(),
                    transaction.getFromUser().getEmail(),
                    transaction.getFromUser().getBalance(),
                    transaction.getFromUser().getBankAccountNumber(),
                    transaction.getFromUser().getRole(),
                    null
            );

            UserDTO toUserDTO = new UserDTO(
                    transaction.getToUser().getId(),
                    transaction.getToUser().getFirstname(),
                    transaction.getToUser().getLastname(),
                    transaction.getToUser().getEmail(),
                    transaction.getToUser().getBalance(),
                    transaction.getToUser().getBankAccountNumber(),
                    transaction.getToUser().getRole(),
                    null
            );

            TransactionDTO transactionDTO = new TransactionDTO(
                    transaction.getTitle(),
                    transaction.getAddress(),
                    transaction.getId(),
                    transaction.getAmount(),
                    fromUserDTO,
                    toUserDTO,
                    transaction.getCreatedAt()
            );

            transactionDTOs.add(transactionDTO);
        }

        return ResponseEntity.ok(transactionDTOs);
    }





    @PostMapping("/create")
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestBody TransactionRequest transactionRequest,
            Authentication authentication
    ) {
        double amount = Double.parseDouble(transactionRequest.getAmount());
        String fromAccountNumber = transactionRequest.getFromAccountNumber();
        String toAccountNumber = transactionRequest.getToAccountNumber();

        System.out.println("# fromAccountNumber "+fromAccountNumber);
        System.out.println("# toAccountNumber "+toAccountNumber);
        if(fromAccountNumber==null || toAccountNumber==null)
        {
            TransactionResponse transactionResponse = new TransactionResponse();
            transactionResponse.setMessage("Błędne dane!");
            return ResponseEntity.badRequest().body(transactionResponse);
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            TransactionResponse transactionResponse = new TransactionResponse();
            transactionResponse.setMessage("Nie możesz użyć swojego numeru konta.");
            return ResponseEntity.badRequest().body(transactionResponse);
        }

        if (!transactionService.isAccountNumberExists(toAccountNumber)) {
            TransactionResponse transactionResponse = new TransactionResponse();
            transactionResponse.setMessage("Podany numer konta nie istnieje.");
            return ResponseEntity.badRequest().body(transactionResponse);
        }

        System.out.println("AM: "+amount);
        System.out.println("fromAccountNumber: "+fromAccountNumber);
        System.out.println("toAccountNumber: "+toAccountNumber);
        double fromAccountBalance = transactionService.getAccountBalance(fromAccountNumber);
        if (fromAccountBalance < amount) {
            TransactionResponse transactionResponse = new TransactionResponse();
            transactionResponse.setMessage("Konto bankowe nie ma wystarczających środków.");
            return ResponseEntity.badRequest().body(transactionResponse);
        }

        TransactionResponse transactionByAccountNumbers = transactionService.createTransactionByAccountNumbers(
                transactionRequest.getTitle(),
                transactionRequest.getAddress(),
                amount,
                fromAccountNumber,
                toAccountNumber
        );
        if (transactionByAccountNumbers != null) {
            return ResponseEntity.ok(transactionByAccountNumbers);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}