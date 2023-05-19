package pl.edu.pwste.transaction;

import lombok.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class TransactionResponse {
    private String message;
    private Integer id;
    private double amount;
    private String fromUserEmail;
    private String toUserEmail;
    private LocalDateTime createdAt;

}