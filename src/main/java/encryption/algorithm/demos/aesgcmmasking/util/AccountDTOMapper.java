package encryption.algorithm.demos.aesgcmmasking.util;

import encryption.algorithm.demos.aesgcmmasking.dto.AccountDTO;
import encryption.algorithm.demos.aesgcmmasking.entity.Account;

import java.util.function.Function;

public class AccountDTOMapper implements Function<Account, AccountDTO> {
    @Override
    public AccountDTO apply(Account account) {
        return (account == null) ? null : new AccountDTO(
                account.getId(), account.getEmail(), account.getName(), account.getAddress(),
                account.getPhone(), account.getDateOfBirth(), account.getCitizenID(), account.getCreatedAt());
    }
}
