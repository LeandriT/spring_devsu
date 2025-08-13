package com.devsu.hackerearth.backend.client.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.client.exception.ClientNotFoundException;
import com.devsu.hackerearth.backend.client.mapper.ClientMapper;
import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;
	private final ClientMapper clientMapper;

	public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
		this.clientRepository = clientRepository;
		this.clientMapper = clientMapper;
	}

	@Override
	public List<ClientDto> getAll() {
		// Get all clients
		return clientRepository.findAll().stream()
				.map(clientMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public ClientDto getById(Long id) {
		// Get clients by id
		Client client = clientRepository.findById(id)
				.orElseThrow(() -> new ClientNotFoundException("Client doest not exists " + id));
		return clientMapper.toDto(client);
	}

	@Override
	public ClientDto create(ClientDto clientDto) {
		Client client = clientMapper.toEntity(clientDto);
		client.setPassword(this.hashPassword(clientDto.getPassword()));
		clientRepository.save(client);
		return clientMapper.toDto(client);
	}

	@Override
	public ClientDto update( ClientDto clientDto) {
		Client client = clientRepository.findById(clientDto.getId())
				.orElseThrow(() -> new ClientNotFoundException("Client doest not exists " + clientDto.getId()));
		clientMapper.updateEntityFromDto(clientDto, client);
		client.setPassword(this.hashPassword(clientDto.getPassword()));
		clientRepository.save(client);
		return clientMapper.toDto(client);
	}

	@Override
	public ClientDto partialUpdate(Long id, PartialClientDto partialClientDto) {
		// Partial update client
		Client client = clientRepository.findById(id)
				.orElseThrow(() -> new ClientNotFoundException("Client doest not exists " + id));
		client.setActive(partialClientDto.isActive());
		clientRepository.save(client);
		return clientMapper.toDto(client);
	}

	@Override
	public void deleteById(Long id) {
		try {
			clientRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ClientNotFoundException("Client doest not exists " + id);
		}
	}

	private String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = md.digest(password.getBytes());
			return Base64.getEncoder().encodeToString(hashBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error al hashear la contraseña", e);
		}
	}
}
