package com.devsu.hackerearth.backend.account.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.account.client.ClientHttpAsync;
import com.devsu.hackerearth.backend.account.client.dto.ClientDto;
import com.devsu.hackerearth.backend.account.exception.AccountNotFoundException;
import com.devsu.hackerearth.backend.account.exception.InactiveAccountException;
import com.devsu.hackerearth.backend.account.exception.InsufficientFundsException;
import com.devsu.hackerearth.backend.account.exception.TransactionNotFoundException;
import com.devsu.hackerearth.backend.account.exception.ValidationException;
import com.devsu.hackerearth.backend.account.mapper.TransactionMapper;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final ClientHttpAsync clientHttpAsync;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            TransactionMapper transactionMapper,
            ClientHttpAsync clientHttpAsync,
            AccountRepository accountRepository

    ) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.clientHttpAsync = clientHttpAsync;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<TransactionDto> getAll() {
        // Get all transactions
        return transactionRepository.findAll().stream().map(transactionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public TransactionDto getById(Long id) {
        // Get transactions by id
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction doest not exists " + id));
        return transactionMapper.toDto(transaction);

    }

    @Override
    public TransactionDto createInitalTransaction(TransactionDto transactionDto) {
        // Create transaction
        Transaction transaction = transactionMapper.toEntity(transactionDto);
        transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    @Override
    @Transactional
    public TransactionDto create(TransactionDto transactionDto) {
        // Obtener la cuenta relacionada
        Account account = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account does not exist with ID: " + transactionDto.getAccountId()));
        // Validar que la cuenta esté activa
        if (!account.isActive()) {
            throw new InactiveAccountException(
                    "Cannot create transaction for inactive account: " + account.getNumber());
        }
        double amount = transactionDto.getAmount();
        double currentBalance = getCurrentAccountBalance(account);
        String type = transactionDto.getType();

        validateTransactionType(type, amount);
        // Validar fondos suficientes para retiros/débitos
        validateSufficientFunds(currentBalance, amount, account.getNumber());
        // Calcular nuevo saldo
        double newBalance = currentBalance + amount;
        // Crear y configurar transacción
        Transaction transaction = buildTransaction(transactionDto, account, newBalance);
        // Guardar transacción
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transaction created successfully. ID: {}, Type: {}, Amount: {}, New Balance: {}",
                savedTransaction.getId(), savedTransaction.getType(), amount, newBalance);
        return transactionMapper.toDto(savedTransaction);
    }

    private double getCurrentAccountBalance(Account account) {
        // Obtener el saldo más reciente de las transacciones
        Optional<Transaction> lastTransaction = transactionRepository.findTopByAccountOrderByDateDesc(account);
        if (lastTransaction.isPresent()) {
            return lastTransaction.get().getBalance();
        } else {
            // Si no hay transacciones previas, usar el saldo inicial
            return account.getInitialAmount();
        }
    }

    private void validateSufficientFunds(double currentBalance, double amount, String accountNumber) {
        // Solo validar para retiros (montos negativos)
        if (amount < 0) {
            double withdrawalAmount = Math.abs(amount);
            if (currentBalance < withdrawalAmount) {
                throw new InsufficientFundsException(
                        String.format(
                                "Transaction cannot be completed. Account %s does not have sufficient balance ($%.2f) for the requested withdrawal of $%.2f.",
                                accountNumber, currentBalance, withdrawalAmount));
            }
        }
    }

    private void validateTransactionType(String type, double amount) {
        if (type == null) {
            throw new ValidationException("Transaction Type is required");
        }
        switch (type.toUpperCase()) {
            case "DEPOSITO":
                if (amount <= 0) {
                    throw new ValidationException("Deposit amount must be positive");
                }
                break;
            case "DEPOSITO_INICIAL":
                if (amount <= 0) {
                    throw new ValidationException("Deposit amount must be positive");
                }
                break;
            case "RETIRO":
                if (amount >= 0) {
                    throw new ValidationException("Withdrawal amount must be negative");
                }
                break;
            case "TRANSFERENCIA":
                if (amount >= 0) {
                    throw new ValidationException("Transfer amount must be negative (outgoing transfer)");
                }
                break;
            default:
                throw new ValidationException("Invalid transaction type: " + type);
        }
    }

    private Transaction buildTransaction(TransactionDto transactionDto, Account account, double newBalance) {
        Transaction transaction = new Transaction();
        transaction.setDate(new Date());
        transaction.setType(transactionDto.getType());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setBalance(newBalance);
        transaction.setAccount(account);
        return transaction;
    }

    @Override
    public List<BankStatementDto> getAllByAccountClientIdAndDateBetween(
            Long clientId,
            Date dateTransactionStart,
            Date dateTransactionEnd) {

        List<BankStatementDto> bankStatements = new ArrayList<>();

        CompletableFuture<ClientDto> clientFuture = clientHttpAsync.getClientById(clientId);
        ClientDto clientDto = clientFuture.join();

        if (clientDto == null) {
            return bankStatements;
        }

        List<Transaction> transactions = transactionRepository.findTransactionsByClientIdAndDateRange(
                clientId, dateTransactionStart, dateTransactionEnd);

        log.info("transactions size: {}", transactions.size());

        for (Transaction transaction : transactions) {
            Account account = transaction.getAccount();

            BankStatementDto bankStatement = BankStatementDto.builder()
                    .date(transaction.getDate())
                    .client(clientDto.getName())
                    .accountNumber(account.getNumber())
                    .accountType(account.getType())
                    .initialAmount(account.getInitialAmount())
                    .isActive(account.isActive())
                    .transactionType(transaction.getType())
                    .amount(transaction.getAmount())
                    .balance(transaction.getBalance())
                    .build();

            bankStatements.add(bankStatement);
        }

        return bankStatements;
    }

    @Override
    public TransactionDto getLastByAccountId(Long accountId) {
        // If you need it
        return null;
    }

}
