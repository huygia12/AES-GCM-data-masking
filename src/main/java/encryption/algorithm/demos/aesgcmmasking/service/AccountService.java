package encryption.algorithm.demos.aesgcmmasking.service;


import encryption.algorithm.demos.aesgcmmasking.dto.AccountDTO;
import encryption.algorithm.demos.aesgcmmasking.dto.request.AccountLogin;
import encryption.algorithm.demos.aesgcmmasking.dto.request.AccountSignup;
import encryption.algorithm.demos.aesgcmmasking.dto.request.AccountUpdate;
import encryption.algorithm.demos.aesgcmmasking.dto.response.LoginResponse;
import encryption.algorithm.demos.aesgcmmasking.exception.encryption.GenerateKeyFromPWFailException;
import encryption.algorithm.demos.aesgcmmasking.exception.encryption.WrongCipherModeException;
import encryption.algorithm.demos.aesgcmmasking.exception.user.UserExistedException;
import encryption.algorithm.demos.aesgcmmasking.exception.user.UserInvalidException;
import encryption.algorithm.demos.aesgcmmasking.exception.user.UserNotFoundException;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface AccountService {
    List<AccountDTO> getAccounts();

    AccountDTO updateAccount(Long id, AccountUpdate req) throws UserExistedException, WrongCipherModeException,
            AccountNotFoundException;

    void insertAccount(AccountSignup req) throws UserExistedException, WrongCipherModeException;

    LoginResponse login(AccountLogin req) throws UserNotFoundException, UserInvalidException,
            WrongCipherModeException, GenerateKeyFromPWFailException;
}
