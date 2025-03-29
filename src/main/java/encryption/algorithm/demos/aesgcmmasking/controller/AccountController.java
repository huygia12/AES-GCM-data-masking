package encryption.algorithm.demos.aesgcmmasking.controller;

import encryption.algorithm.demos.aesgcmmasking.dto.AccountDTO;
import encryption.algorithm.demos.aesgcmmasking.dto.request.AccountLogin;
import encryption.algorithm.demos.aesgcmmasking.dto.request.AccountSignup;
import encryption.algorithm.demos.aesgcmmasking.dto.request.AccountUpdate;
import encryption.algorithm.demos.aesgcmmasking.dto.response.ApiResponse;
import encryption.algorithm.demos.aesgcmmasking.dto.response.LoginResponse;
import encryption.algorithm.demos.aesgcmmasking.service.AccountService;
import jakarta.validation.Valid;
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
    public ApiResponse<List<AccountDTO>> getAccounts() {
        var accounts = accountService.getAccounts();
        return new ApiResponse<>(HttpStatus.OK.toString(), accounts);
    }

    @PostMapping("/{userId}")
    public ApiResponse<AccountDTO> updateAccount(@PathVariable Long userId, @RequestBody @Valid AccountUpdate req) throws AccountNotFoundException {
        var account = accountService.updateAccount(userId, req);
        return new ApiResponse<>(HttpStatus.OK.toString(), account);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> loginAccount(@RequestBody @Valid AccountLogin req) {
        var res = accountService.login(req);
        return new ApiResponse<>(HttpStatus.OK.toString(), res);
    }

    @PostMapping("/signup")
    public ApiResponse<LoginResponse> signupAccount(@RequestBody @Valid AccountSignup req) {
        accountService.insertAccount(req);
        return new ApiResponse<>(HttpStatus.CREATED.toString(), null);
    }
}
