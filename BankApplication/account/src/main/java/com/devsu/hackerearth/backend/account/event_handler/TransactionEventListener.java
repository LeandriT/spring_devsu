package com.devsu.hackerearth.backend.account.event_handler;

import java.util.Date;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.service.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventListener implements ApplicationListener<TransactionEventDto> {
    private final TransactionService transactionService;

    @EventListener
    public void handleTransactionEvent(TransactionEventDto transactionEventDto) {
        TransactionDto transactionDto = TransactionDto.builder()
                .accountId(transactionEventDto.getAccountId())
                .date(new Date())
                .type("DEPOSITO_INICIAL")
                .amount(transactionEventDto.getAmount())
                .balance(transactionEventDto.getBalance())
                .build();
        transactionService.createInitalTransaction(transactionDto);
    }

    @Override
    public void onApplicationEvent(@SuppressWarnings("null") TransactionEventDto event) {
        log.info("Received transaction onApplicationEvent");
    }

}
