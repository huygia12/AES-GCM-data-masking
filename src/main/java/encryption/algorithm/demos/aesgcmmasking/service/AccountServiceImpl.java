package encryption.algorithm.demos.aesgcmmasking.service;

import encryption.algorithm.demos.aesgcmmasking.config.PBKDF2Config;
import encryption.algorithm.demos.aesgcmmasking.constant.StatusMessage;
import encryption.algorithm.demos.aesgcmmasking.dto.AccountDTO;
import encryption.algorithm.demos.aesgcmmasking.dto.request.AccountLogin;
import encryption.algorithm.demos.aesgcmmasking.dto.request.AccountSignup;
import encryption.algorithm.demos.aesgcmmasking.dto.request.AccountUpdate;
import encryption.algorithm.demos.aesgcmmasking.dto.response.LoginResponse;
import encryption.algorithm.demos.aesgcmmasking.encryption.*;
import encryption.algorithm.demos.aesgcmmasking.entity.Account;
import encryption.algorithm.demos.aesgcmmasking.exception.encryption.WrongCipherModeException;
import encryption.algorithm.demos.aesgcmmasking.exception.user.UserExistedException;
import encryption.algorithm.demos.aesgcmmasking.exception.user.UserInvalidException;
import encryption.algorithm.demos.aesgcmmasking.exception.user.UserNotFoundException;
import encryption.algorithm.demos.aesgcmmasking.repository.AccountRepository;
import encryption.algorithm.demos.aesgcmmasking.util.AccountDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    //16 bytes to hex string
    @Value("${encryption.master_key}")
    private String masterKey;

    private final AccountRepository accountRepository;

    private final AccountDTOMapper accountDTOMapper;

    private final AESGCM cipher;

    private final PasswordEncoder passwordEncoder;

    private final PBKDF2Config.PBKDF2Util pbkdf2Util;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<AccountDTO> getAccounts() {
        return accountRepository
                .findAll()
                .stream()
                .map(accountDTOMapper).toList();
    }

    @Override
    public AccountDTO updateAccount(Long id, AccountUpdate req) throws AccountNotFoundException {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(StatusMessage.USER_NOT_FOUND));

        try{
            // decrypt user key
            String[] encryptedKeyWithIW = account.getKey().split("\\.");

            cipher.init(new AEADParams(HexUtils.hexToBytes(masterKey), HexUtils.hexToBytes(encryptedKeyWithIW[1]), CipherMode.DECRYPT));
            byte[] userKeyBytes = HexUtils.hexToBytes(cipher.decrypt(encryptedKeyWithIW[0]));
            log.info("Decrypted user key: {}", HexUtils.bytesToHex(userKeyBytes));

            String iv;
            if(req.getName() != null){
                iv = HexUtils.randomHex(12);
                cipher.init(new AEADParams(userKeyBytes, HexUtils.hexToBytes(iv), CipherMode.ENCRYPT));

                String encryptedNameWithIV = cipher.encrypt(req.getName());
                account.setName(encryptedNameWithIV.concat(String.format(".%s", iv)));
            }
            if(req.getAddress() != null){
                iv = HexUtils.randomHex(12);
                cipher.init(new AEADParams(userKeyBytes, HexUtils.hexToBytes(iv), CipherMode.ENCRYPT));

                String encryptedAddressWithIV = cipher.encrypt(req.getAddress());
                account.setAddress(encryptedAddressWithIV.concat(String.format(".%s", iv)));
            }
            if(req.getPhone() != null){
                iv = HexUtils.randomHex(12);
                cipher.init(new AEADParams(userKeyBytes, HexUtils.hexToBytes(iv), CipherMode.ENCRYPT));

                String encryptedPhoneWithIV = cipher.encrypt(req.getPhone());
                account.setPhone(encryptedPhoneWithIV.concat(String.format(".%s", iv)));
            }
            if(req.getDateOfBirth() != null){
                iv = HexUtils.randomHex(12);
                cipher.init(new AEADParams(userKeyBytes, HexUtils.hexToBytes(iv), CipherMode.ENCRYPT));

                String encryptedBirthDateWithIV = cipher.encrypt(req.getDateOfBirth().format(formatter));
                account.setDateOfBirth(encryptedBirthDateWithIV.concat(String.format(".%s", iv)));
            }
            if(req.getCitizenID() != null){
                iv = HexUtils.randomHex(12);
                cipher.init(new AEADParams(userKeyBytes, HexUtils.hexToBytes(iv), CipherMode.ENCRYPT));

                String encryptedCitizenIDWithIV = cipher.encrypt(req.getCitizenID());
                account.setCitizenID(encryptedCitizenIDWithIV.concat(String.format(".%s", iv)));
            }
            if(req.getEmail() != null){
                account.setEmail(req.getEmail());
            }

            accountRepository.save(account);

            return accountDTOMapper.apply(account);
        } catch (DataIntegrityViolationException e) {
            throw new UserExistedException(StatusMessage.EMAIL_EXISTED);
        } catch (WrongModeException wme){
            throw new WrongCipherModeException(StatusMessage.WRONG_CIPHER_MODE);
        }
    }

    @Override
    public void insertAccount(AccountSignup req) {
        try{
            String key = HexUtils.randomHex(16);
            String iv;
            log.info("New generated key: {}", key);

            //encrypt name
            if(req.getName() != null){
                iv = HexUtils.randomHex(12);
                cipher.init(new AEADParams(HexUtils.hexToBytes(key), HexUtils.hexToBytes(iv), CipherMode.ENCRYPT));

                String encryptedNameWithIV = cipher.encrypt(req.getName());
                req.setName(encryptedNameWithIV.concat(String.format(".%s", iv)));
            }

            //encrypt key
            iv = HexUtils.randomHex(12);
            cipher.init(new AEADParams(HexUtils.hexToBytes(masterKey), HexUtils.hexToBytes(iv), CipherMode.ENCRYPT));
            String encryptedKeyWithIV = cipher.encrypt(key).concat(String.format(".%s", iv));

            //finalise
            Account account = Account.builder()
                    .email(req.getEmail())
                    .password(passwordEncoder.encode(req.getPassword()))
                    .name(req.getName())
                    .key(encryptedKeyWithIV)
                    .build();

            accountRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            throw new UserExistedException(StatusMessage.EMAIL_EXISTED);
        } catch (WrongModeException wme){
            throw new WrongCipherModeException(StatusMessage.WRONG_CIPHER_MODE);
        }
    }

    @Override
    public LoginResponse login(AccountLogin req) {
        Account account = getValidAccount(req.getEmail(), req.getPassword());

        try{
            //down here login request must be valid
            String[] encryptedKeyWithIW = account.getKey().split("\\.");
            cipher.init(new AEADParams(HexUtils.hexToBytes(masterKey), HexUtils.hexToBytes(encryptedKeyWithIW[1]), CipherMode.DECRYPT));
            String userKey = cipher.decrypt(encryptedKeyWithIW[0]);

            //generate key to encrypt user key
            String iv = HexUtils.randomHex(12);
            byte[] salt = pbkdf2Util.generateSalt();
            byte[] key = pbkdf2Util.generateKeyFromPassword(req.getPassword(), salt);

            cipher.init(new AEADParams(key, HexUtils.hexToBytes(iv), CipherMode.ENCRYPT));
            String encryptedKey = cipher.encrypt(userKey);

            byte[] tagBytes = new byte[16];
            cipher.computeMAC(tagBytes);

            var res = LoginResponse.builder()
                    .iv(iv)
                    .salt(HexUtils.bytesToHex(salt))
                    .key(encryptedKey)
                    .tag(HexUtils.bytesToHex(tagBytes))
                    .build();
            log.info("res: " + res.toString());
            return res;
        } catch (WrongModeException wme){
            throw new WrongCipherModeException(StatusMessage.WRONG_CIPHER_MODE);
        }
    }

    public Account getValidAccount(String email, String password) throws UserNotFoundException, UserInvalidException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(StatusMessage.USER_NOT_FOUND));

        if(!passwordEncoder.matches(password, account.getPassword())){
            throw new UserInvalidException("Password or email does not match");
        }

        return account;
    }
}
