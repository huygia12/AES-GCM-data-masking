package encryption.algorithm.demos.aesgcmmasking.controller;

import encryption.algorithm.demos.aesgcmmasking.dto.AccountDTO;
import encryption.algorithm.demos.aesgcmmasking.dto.request.AccountLogin;
import encryption.algorithm.demos.aesgcmmasking.dto.request.AccountSignup;
import encryption.algorithm.demos.aesgcmmasking.dto.request.AccountUpdate;
import encryption.algorithm.demos.aesgcmmasking.dto.response.LoginResponse;
import encryption.algorithm.demos.aesgcmmasking.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAccounts() {
        var accounts = accountService.getAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long userId, @RequestBody @Valid AccountUpdate req) throws AccountNotFoundException {
        var account = accountService.updateAccount(userId, req);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginAccount(@RequestBody @Valid AccountLogin req) {
        var res = accountService.login(req);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> signupAccount(@RequestBody @Valid AccountSignup req) {
        accountService.insertAccount(req);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
