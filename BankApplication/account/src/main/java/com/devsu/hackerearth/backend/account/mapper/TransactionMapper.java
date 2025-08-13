package com.devsu.hackerearth.backend.account.mapper;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMapper {

    /**
     * Convierte una entidad Transaction a TransactionDto
     */
    public TransactionDto toDto(Transaction entity) {
        if (entity == null) {
            return null;
        }
        return new TransactionDto(
                entity.getId(),
                entity.getDate(),
                entity.getType(),
                entity.getAmount(),
                entity.getBalance(),
                entity.getAccount().getId()
        // entity.getAccountId()
        );
    }

    /**
     * Convierte un TransactionDto a entidad Transaction
     */
    public Transaction toEntity(TransactionDto dto) {
        if (dto == null) {
            return null;
        }
        Transaction entity = new Transaction();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setType(dto.getType());
        entity.setAmount(dto.getAmount());
        entity.setBalance(dto.getBalance());
        Account account = new Account();
        account.setId(dto.getAccountId());
        entity.setAccount(account);
        // entity.setAccountId(dto.getAccountId());
        return entity;
    }

    /**
     * Actualiza una entidad Transaction existente con datos de TransactionDto
     * (Útil para operaciones UPDATE)
     */
    public void updateEntityFromDto(TransactionDto dto, Transaction entity) {
        if (dto == null || entity == null) {
            return;
        }
        // No se actualiza el ID
        entity.setDate(dto.getDate());
        entity.setType(dto.getType());
        entity.setAmount(dto.getAmount());
        entity.setBalance(dto.getBalance());
        Account account = new Account();
        account.setId(dto.getAccountId());
         entity.setAccount(account);
        //entity.setAccountId(dto.getAccountId());
    }

    /**
     * Convierte lista de entidades a lista de DTOs
     */
    public List<TransactionDto> toDtoList(List<Transaction> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Convierte lista de DTOs a lista de entidades
     */
    public List<Transaction> toEntityList(List<TransactionDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}