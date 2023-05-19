package pl.edu.pwste.transaction;

import pl.edu.pwste.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String title;
    private String address;
    private Integer id;
    private double amount;
    private UserDTO fromUser;
    private UserDTO toUser;
    private LocalDateTime createdAt;
}