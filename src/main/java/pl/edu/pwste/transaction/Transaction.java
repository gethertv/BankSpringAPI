package pl.edu.pwste.transaction;

import pl.edu.pwste.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue
    public Integer id;

    private String title;
    private String address;
    public double amount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user")
    public User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user")
    public User toUser;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

}
