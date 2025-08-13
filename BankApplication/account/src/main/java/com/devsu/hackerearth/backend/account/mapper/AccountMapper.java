package com.devsu.hackerearth.backend.account.mapper;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountMapper {

    /**
     * Convierte una entidad Account a AccountDto
     */
    public AccountDto toDto(Account entity) {
        if (entity == null)
            return null;

        return new AccountDto(
                entity.getId(),
                entity.getNumber(),
                entity.getType(),
                entity.getInitialAmount(),
                entity.isActive(),
                entity.getClientId());
    }

    /**
     * Convierte un AccountDto a entidad Account
     */
    public Account toEntity(AccountDto dto) {
        if (dto == null)
            return null;

        Account entity = new Account();
        // Base
        entity.setId(dto.getId());
        // Campos propios
        entity.setNumber(dto.getNumber());
        entity.setType(dto.getType());
        entity.setInitialAmount(dto.getInitialAmount());
        entity.setActive(dto.isActive());
        entity.setClientId(dto.getClientId());
        return entity;
    }

    /**
     * Actualiza una entidad Account existente con datos del DTO (UPDATE full)
     * No modifica el ID.
     */
    public void updateEntityFromDto(AccountDto dto, Account entity) {
        if (dto == null || entity == null)
            return;

        // No tocar entity.setId(...)
        entity.setNumber(dto.getNumber());
        entity.setType(dto.getType());
        entity.setInitialAmount(dto.getInitialAmount());
        entity.setActive(dto.isActive());
        entity.setClientId(dto.getClientId());
    }

    /**
     * Convierte lista de entidades a lista de DTOs
     */
    public List<AccountDto> toDtoList(List<Account> entities) {
        if (entities == null)
            return null;
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Convierte lista de DTOs a lista de entidades
     */
    public List<Account> toEntityList(List<AccountDto> dtos) {
        if (dtos == null)
            return null;
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

}
