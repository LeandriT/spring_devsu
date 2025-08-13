package com.devsu.hackerearth.backend.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.devsu.hackerearth.backend.account.controller.AccountController;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.service.AccountService;

@SpringBootTest
public class sampleTest {

	private AccountService accountService = mock(AccountService.class);
	private AccountController accountController = new AccountController(accountService);

	@Test
	void createAccountTest() {
		// Arrange
		AccountDto newAccount = new AccountDto(1L, "number", "savings", 0.0, true, 1L);
		AccountDto createdAccount = new AccountDto(1L, "number", "savings", 0.0, true, 1L);
		when(accountService.create(newAccount)).thenReturn(createdAccount);

		// Act
		ResponseEntity<AccountDto> response = accountController.create(newAccount);

		// Assert
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(createdAccount, response.getBody());
	}

	@Test
	void getAllAccountsTest() {
		List<AccountDto> mockList = Arrays.asList(
				new AccountDto(1L, "001", "AHORROS", 100.0, true, 1L),
				new AccountDto(2L, "002", "CORRIENTE", 200.0, true, 1L));
		when(accountService.getAll()).thenReturn(mockList);

		ResponseEntity<List<AccountDto>> response = accountController.getAll();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(mockList, response.getBody());
	}

	@Test
	void getAccountByIdTest() {
		Long id = 1L;
		AccountDto account = new AccountDto(id, "001", "AHORROS", 150.0, true, 1L);
		when(accountService.getById(id)).thenReturn(account);

		ResponseEntity<AccountDto> response = accountController.get(id);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(account, response.getBody());
	}

	@Test
	void updateAccountTest() {
		Long id = 1L;
		AccountDto updatedDto = new AccountDto(null, "001", "CORRIENTE", 300.0, true, 1L);
		AccountDto updatedAccount = new AccountDto(id, "001", "CORRIENTE", 300.0, true, 1L);
		when(accountService.update(any(AccountDto.class))).thenReturn(updatedAccount);

		ResponseEntity<AccountDto> response = accountController.update(id, updatedDto);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedAccount, response.getBody());
	}

	@Test
	void partialUpdateAccountTest() {
		Long id = 1L;
		PartialAccountDto partialDto = new PartialAccountDto();
		partialDto.setActive(false);

		AccountDto partiallyUpdated = new AccountDto(id, "001", "AHORROS", 150.0, false, 1L);
		when(accountService.partialUpdate(id, partialDto)).thenReturn(partiallyUpdated);

		ResponseEntity<AccountDto> response = accountController.partialUpdate(id, partialDto);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(partiallyUpdated, response.getBody());
	}

	@Test
	void deleteAccountTest() {
		Long id = 1L;
		doNothing().when(accountService).deleteById(id);

		ResponseEntity<Void> response = accountController.delete(id);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(accountService).deleteById(id);
	}
}
