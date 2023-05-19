package pl.edu.pwste.user;

import pl.edu.pwste.config.JwtService;
import pl.edu.pwste.token.Token;
import pl.edu.pwste.token.TokenRepository;
import pl.edu.pwste.token.TokenType;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;


    public User findByBankAccountNumber(String bankAccountNumber) {
        return userRepository.findByBankAccountNumber(bankAccountNumber);
    }

    @Transactional
    public Map<String, String> updateEmail(int id, String email)
    {


        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik o podanym identyfikatorze nie istnieje."));

        user.setEmail(email);

        Token previousToken = tokenRepository.findActiveTokenByUser(user)
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono aktywnego tokenu dla użytkownika."));
        previousToken.setRevoked(true);

        Token newToken = Token.builder()
                .token(jwtService.generateToken(user))
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();

        userRepository.save(user);
        tokenRepository.save(newToken);

        Map<String, String> response = new HashMap<>();
        response.put("token", newToken.token);
        response.put("email", user.getEmail());
        response.put("firstname", user.getFirstname());
        response.put("lastname", user.getLastname());
        response.put("id", String.valueOf(user.getId()));

        return response;


    }



}
