package com.devsu.hackerearth.backend.account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test") // Usa application-test.properties para configuración de pruebas
@Transactional // Rollback automático después de cada test
public class AccountIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    private Long testAccountId;

    @BeforeEach
    void setUp() {
        // Crear datos de prueba
        Account testAccount = new Account();
        testAccount.setNumber("ACC-10002");
        testAccount.setType("SAVINGS");
        testAccount.setInitialAmount(100.0);
        testAccount.setClientId(2L);
        testAccount.setActive(true);

        Account savedAccount = accountRepository.save(testAccount);
        testAccountId = savedAccount.getId();
    }

    @Test
    void getAccountById_ShouldReturnAccount_WhenAccountExists() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/accounts/{id}", testAccountId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testAccountId))
                .andExpect(jsonPath("$.number").value("ACC-10002"))
                .andExpect(jsonPath("$.type").value("SAVINGS"))
                .andExpect(jsonPath("$.initialAmount").value(100.0))
                .andExpect(jsonPath("$.clientId").value(2))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void getAccountById_ShouldReturnNotFound_WhenAccountDoesNotExist() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/accounts/{id}", 99999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAccountById_ShouldReturnBadRequest_WhenIdIsNotNumeric() throws Exception {
        // Act & Assert - ID no numérico
        mockMvc.perform(get("/api/accounts/{id}", "abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}