package com.devsu.hackerearth.backend.client.mapper;

import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientMapper {

    /**
     * Convierte una entidad Client a ClientDto
     * 
     * @param client la entidad Client
     * @return ClientDto o null si client es null
     */
    public ClientDto toDto(Client client) {
        if (client == null) {
            return null;
        }

        return ClientDto.builder()
                .id(client.getId())
                .dni(client.getDni())
                .name(client.getName())
                .password(client.getPassword())
                .gender(client.getGender())
                .age(client.getAge())
                .address(client.getAddress())
                .phone(client.getPhone())
                .isActive(client.isActive())
                .build();
    }

    /**
     * Convierte un ClientDto a entidad Client
     * 
     * @param clientDto el DTO
     * @return Client entity o null si clientDto es null
     */
    public Client toEntity(ClientDto clientDto) {
        if (clientDto == null) {
            return null;
        }

        Client client = new Client();

        // Campos de Base
        client.setId(clientDto.getId());

        // Campos de Person
        client.setDni(clientDto.getDni());
        client.setName(clientDto.getName());
        client.setGender(clientDto.getGender());
        client.setAge(clientDto.getAge());
        client.setAddress(clientDto.getAddress());
        client.setPhone(clientDto.getPhone());

        // Campos de Client
        client.setPassword(clientDto.getPassword());
        client.setActive(clientDto.isActive());

        return client;
    }

    /**
     * Actualiza una entidad Client existente con datos del DTO
     * Útil para operaciones UPDATE donde no quieres crear nueva instancia
     */
    public void updateEntityFromDto(ClientDto clientDto, Client client) {
        if (clientDto == null || client == null) {
            return;
        }

        // No actualizar ID (generalmente inmutable)

        // Actualizar campos de Person
        client.setDni(clientDto.getDni());
        client.setName(clientDto.getName());
        client.setGender(clientDto.getGender());
        client.setAge(clientDto.getAge());
        client.setAddress(clientDto.getAddress());
        client.setPhone(clientDto.getPhone());

        // Actualizar campos de Client
        client.setPassword(clientDto.getPassword());
        client.setActive(clientDto.isActive());
    }

    /**
     * Convierte una lista de Client entities a lista de DTOs
     */
    public List<ClientDto> toDtoList(List<Client> clients) {
        if (clients == null) {
            return null;
        }

        return clients.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una lista de DTOs a lista de Client entities
     */
    public List<Client> toEntityList(List<ClientDto> clientDtos) {
        if (clientDtos == null) {
            return null;
        }

        return clientDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
