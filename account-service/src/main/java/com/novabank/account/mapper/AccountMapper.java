package com.novabank.account.mapper;

import com.novabank.account.dto.AccountDTO;
import com.novabank.account.model.Account;

public class AccountMapper {

    public static AccountDTO toDTO(Account account) {
        return new AccountDTO(
                account.getIban(),
                account.getBalance(),
                account.getCreatedAt(),
                account.getClientId()
        );
    }
}
