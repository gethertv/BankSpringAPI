package pl.edu.pwste.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @JsonProperty("amount")
    private String amount;
    @JsonProperty("fromAccountNumber")
    private String fromAccountNumber;
    @JsonProperty("toAccountNumber")
    private String toAccountNumber;

    @JsonProperty("title")
    private String title;

    @JsonProperty("address")
    private String address;
}