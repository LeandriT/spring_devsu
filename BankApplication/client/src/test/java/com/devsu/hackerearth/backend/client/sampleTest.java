package com.devsu.hackerearth.backend.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.devsu.hackerearth.backend.client.controller.ClientController;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.service.ClientService;

@SpringBootTest
public class sampleTest {

    private ClientService clientService = mock(ClientService.class);
    private ClientController clientController = new ClientController(clientService);

    @Test
    void createClientTest() {
        // Arrange
        ClientDto newClient = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999", true);
        ClientDto createdClient = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999",
                true);
        when(clientService.create(newClient)).thenReturn(createdClient);

        // Act
        ResponseEntity<ClientDto> response = clientController.create(newClient);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdClient, response.getBody());
    }

    @Test
    void getAllClientsTest() {
        List<ClientDto> clients = Arrays.asList(
                new ClientDto(1L, "Dni1", "Alice", "Pass", "Female", 25, "Addr1", "1234567890", true),
                new ClientDto(2L, "Dni2", "Bob", "Pass", "Male", 30, "Addr2", "0987654321", true));
        when(clientService.getAll()).thenReturn(clients);

        ResponseEntity<List<ClientDto>> response = clientController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clients, response.getBody());
    }

    @Test
    void getClientByIdTest() {
        Long id = 1L;
        ClientDto client = new ClientDto(id, "Dni", "Name", "Password", "Gender", 25, "Address", "1111111111", true);
        when(clientService.getById(id)).thenReturn(client);

        ResponseEntity<ClientDto> response = clientController.get(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(client, response.getBody());
    }

    @Test
    void updateClientTest() {
        Long id = 1L;
        ClientDto input = new ClientDto(null, "Dni", "Updated Name", "Password", "Gender", 30, "Address", "9999999999",
                true);
        ClientDto updated = new ClientDto(id, "Dni", "Updated Name", "Password", "Gender", 30, "Address", "9999999999",
                true);
        when(clientService.update(any(ClientDto.class))).thenReturn(updated);

        ResponseEntity<ClientDto> response = clientController.update(id, input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
    }

    @Test
    void partialUpdateClientTest() {
        Long id = 1L;
        PartialClientDto partial = new PartialClientDto();
        partial.setActive(false);

        ClientDto updated = new ClientDto(id, "Dni", "Name", "Password", "Gender", 25, "New Address", "9999999999",
                false);
        when(clientService.partialUpdate(id, partial)).thenReturn(updated);

        ResponseEntity<ClientDto> response = clientController.partialUpdate(id, partial);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updated, response.getBody());
    }

    @Test
    void deleteClientTest() {
        Long id = 1L;
        doNothing().when(clientService).deleteById(id);

        ResponseEntity<Void> response = clientController.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clientService).deleteById(id);
    }
}
