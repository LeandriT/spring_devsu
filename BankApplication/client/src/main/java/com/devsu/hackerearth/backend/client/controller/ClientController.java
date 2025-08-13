package com.devsu.hackerearth.backend.client.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.service.ClientService;

@Validated
@RestController
@RequestMapping("/api/clients")
public class ClientController {

	private final ClientService clientService;

	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}

	@GetMapping
	public ResponseEntity<List<ClientDto>> getAll() {
		// api/clients
		// Get all clients
		return new ResponseEntity<>(clientService.getAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClientDto> get(@PathVariable @Min(1) Long id) {
		// api/clients/{id}
		// Get clients by id

		return new ResponseEntity<>(clientService.getById(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ClientDto> create(@Valid @RequestBody ClientDto clientDto) {
		// api/clients
		// Create client
		return new ResponseEntity<>(clientService.create(clientDto), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClientDto> update(@PathVariable @Min(1) Long id, @Valid @RequestBody ClientDto clientDto) {
		// api/clients/{id}
		// Update client
		clientDto.setId(id);
		return new ResponseEntity<>(clientService.update(clientDto), HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ClientDto> partialUpdate(@PathVariable @Min(1) Long id,
			@Valid @RequestBody PartialClientDto partialClientDto) {
		// api/accounts/{id}
		// Partial update accounts
		return new ResponseEntity<>(clientService.partialUpdate(id, partialClientDto), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable @Min(1) Long id) {
		// api/clients/{id}
		// Delete client
		clientService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
