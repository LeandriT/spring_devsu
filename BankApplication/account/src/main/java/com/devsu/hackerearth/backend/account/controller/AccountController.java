package com.devsu.hackerearth.backend.account.controller;

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

import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.service.AccountService;

@Validated
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping
	public ResponseEntity<List<AccountDto>> getAll() {
		// api/accounts
		// Get all accounts

		return new ResponseEntity<>(accountService.getAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AccountDto> get(@PathVariable @Min(1) Long id) {
		// api/accounts/{id}
		// Get accounts by id
		return new ResponseEntity<>(accountService.getById(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<AccountDto> create(@Valid @RequestBody AccountDto accountDto) {
		// api/accounts
		// Create accounts
		return new ResponseEntity<>(accountService.create(accountDto), HttpStatus.CREATED);
	}

	@PutMapping("{id}")
	public ResponseEntity<AccountDto> update(@PathVariable @Min(1) Long id, @Valid @RequestBody AccountDto accountDto) {
		// api/accounts/{id}
		// Update accounts
		accountDto.setId(id);
		return new ResponseEntity<>(accountService.update( accountDto), HttpStatus.OK);
	}

	@PatchMapping("{id}")
	public ResponseEntity<AccountDto> partialUpdate(@PathVariable @Min(1) Long id,
			@Valid @RequestBody PartialAccountDto partialAccountDto) {
		// api/accounts/{id}
		// Partial update accounts
		return ResponseEntity.ok(accountService.partialUpdate(id, partialAccountDto));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable @Min(1) Long id) {
		// api/accounts/{id}
		// Delete accounts
		accountService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
