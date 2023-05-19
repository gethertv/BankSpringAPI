package pl.edu.pwste.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("refresh_token")
  private String refreshToken;


  @JsonProperty("number_account")
  private String numberAccount;
  private String errorMessage;

  private String firstname;
  private String lastname;
  private int id;

  public boolean isSuccessful() {
    return accessToken != null && !accessToken.isEmpty() && refreshToken != null && !refreshToken.isEmpty();
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
