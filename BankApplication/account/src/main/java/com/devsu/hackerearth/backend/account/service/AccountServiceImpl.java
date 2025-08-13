package com.devsu.hackerearth.backend.account.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.event_handler.TransactionEventDto;
import com.devsu.hackerearth.backend.account.event_handler.TransactionEventListener;
import com.devsu.hackerearth.backend.account.exception.AccountNotFoundException;
import com.devsu.hackerearth.backend.account.exception.DuplicateAccountException;
import com.devsu.hackerearth.backend.account.exception.ValidationException;
import com.devsu.hackerearth.backend.account.mapper.AccountMapper;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final TransactionEventListener transactionEventListener;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper,
            TransactionEventListener transactionEventListener) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.transactionEventListener = transactionEventListener;
    }

    @Override
    public List<AccountDto> getAll() {
        // Get all accounts
        return accountRepository.findAll().stream().map(accountMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public AccountDto getById(Long id) {
        // Get accounts by id
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account doest not exists " + id));
        return accountMapper.toDto(account);
    }

    @Override
    public AccountDto create(AccountDto accountDto) {
        // validation account type
        validateAccountType(accountDto.getType());
        List<Account> duplicates = accountRepository.findByClientIdAndTypeAndNumber(
                accountDto.getClientId(), accountDto.getType(), accountDto.getNumber());
        // validation duplicated account
        validateDuplicateOnCreate(duplicates);
        // Create account
        Account account = accountMapper.toEntity(accountDto);
        accountRepository.save(account);
        if (accountDto.getInitialAmount() > Double.valueOf(0.0)) {
            TransactionEventDto event = new TransactionEventDto(
                    this,
                    new Date(),
                    "DEPOSITO_INICIAL",
                    accountDto.getInitialAmount(),
                    account.getId(),
                    accountDto.getInitialAmount());
            transactionEventListener.handleTransactionEvent(event);
        }
        return accountMapper.toDto(account);
    }

    @Override
    public AccountDto update(AccountDto accountDto) {
        // validate account type
        validateAccountType(accountDto.getType());
        // validate account duplicated
        List<Account> duplicates = accountRepository.findByClientIdAndTypeAndNumber(
                accountDto.getClientId(), accountDto.getType(), accountDto.getNumber());

        validateDuplicateOnUpdate(duplicates, accountDto.getId());
        // update account
        Account account = accountRepository.findById(accountDto.getId())
                .orElseThrow(() -> new AccountNotFoundException("Account doest not exists " + accountDto.getId()));
        accountMapper.updateEntityFromDto(accountDto, account);
        accountRepository.save(account);
        return accountMapper.toDto(account);
    }

    @Override
    public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) {
        // Partial update account
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account doest not exists " + id));
        account.setActive(partialAccountDto.isActive());
        accountRepository.save(account);
        return accountMapper.toDto(account);
    }

    @Override
    public void deleteById(Long id) {
        // Delete account
        try {
            accountRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new AccountNotFoundException("Account doest not exists " + id);
        }
    }

    private void validateAccountType(String type) {
        if (!"AHORROS".equalsIgnoreCase(type) && !"CORRIENTE".equalsIgnoreCase(type)) {
            throw new ValidationException("Account type must be either 'AHORROS' or 'CORRIENTE'.");
        }
    }

    private void validateDuplicateOnCreate(List<Account> existingAccounts) {
        if (!existingAccounts.isEmpty()) {
            throw new DuplicateAccountException("An account with this number and type already exists for the client.");
        }
    }

    private void validateDuplicateOnUpdate(List<Account> existingAccounts, Long currentAccountId) {
        boolean duplicateExists = existingAccounts.stream()
                .anyMatch(a -> !a.getId().equals(currentAccountId));
        if (duplicateExists) {
            throw new DuplicateAccountException(
                    "Another account with the same number and type already exists for this client.");
        }
    }
}
