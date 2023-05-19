package pl.edu.pwste.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService service;
    private final UserRepository userRepository;

    @PostMapping("/email/update")
    public ResponseEntity<?> updateEmail(
            @RequestBody Map<String, Object> requestBody,
            Authentication authorization)
    {
        return ResponseEntity.ok(service.updateEmail((Integer) requestBody.get("id"), (String) requestBody.get("email")));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getEmail(@RequestParam("id") String id,
                                             Authentication authorization)
    {
        return ResponseEntity.ok(service.getUserRepository().findById(Integer.parseInt(id)));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<Double> getUserBalance(@PathVariable("id") Integer id, Authentication authorization) {
        User user = userRepository.findById(id).get();
        if (user != null) {

            if(!authorization.getName().equalsIgnoreCase(user.getEmail()))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

            double balance = user.getBalance();
            return ResponseEntity.ok(balance);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
