package de.ait.javalessons.service;

import de.ait.javalessons.model.BankAccount;
import de.ait.javalessons.repositories.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BankAccountServiceIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private static final String BASE_URL = "/accounts";

    private BankAccount accountOne;
    private BankAccount accountTwo;
    private BankAccount accountThree;
    private BankAccount accountFour;
    private BankAccount accountFive;

    @BeforeEach
    void setUp() {
        accountOne = new BankAccount("DE11111111", "Friedrich Müller", 0.0);
        accountTwo = new BankAccount("DE22222222", "Johann Schmidt", 1000);
        accountThree = new BankAccount("DE33333333", "Heinrich Weber", 5500.55);
        accountFour = new BankAccount("DE44444444", "Klaus Schneider", 10000.00);
        accountFive = new BankAccount("DE55555555", "Wolfgang Fischer", 7500);

        bankAccountRepository.deleteAll();
        bankAccountRepository.saveAll(
                List.of(
                        accountOne,
                        accountTwo,
                        accountThree,
                        accountFour,
                        accountFive
                )
        );
    }

    @Test
    @DisplayName("Get all accounts test")
    void testGetAccountsReturnAllAccounts() {
        ResponseEntity<BankAccount[]> response =
                testRestTemplate.getForEntity(BASE_URL, BankAccount[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().length);
        assertEquals("DE44444444", response.getBody()[3].getAccountNumber());
    }

    @Test
    @DisplayName("Get bank account by id")
    void testGetAccountByIdReturnBankAccount() {
        String url = BASE_URL + "/" + accountThree.getId();
        ResponseEntity<BankAccount> response =
                testRestTemplate.getForEntity(url, BankAccount.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("DE33333333", response.getBody().getAccountNumber());
    }

    @Test
    @DisplayName("Get bank account by id negative - account does not exist")
    void testGetAccountByIdNegative() {
        // Предположим, заведомо несуществующий id = 100 (при тестовых данных)
        String url = BASE_URL + "/100";
        ResponseEntity<BankAccount> response =
                testRestTemplate.getForEntity(url, BankAccount.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Create bank account test")
    void testCreateBankAccount() {
        // Создадим новый аккаунт
        String url = BASE_URL + "?accountNumber=DE66666666&ownerName=Alex Lewis";
        ResponseEntity<BankAccount> response =
                testRestTemplate.postForEntity(url, null, BankAccount.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Alex Lewis", response.getBody().getOwnerName());

        // Проверим, что в базе стало 6 аккаунтов
        BankAccount[] accounts = testRestTemplate.getForEntity(BASE_URL, BankAccount[].class).getBody();
        assertNotNull(accounts);
        assertEquals(6, accounts.length);

        // Убедимся, что среди них есть только что созданный
        assertTrue(
                Arrays.stream(accounts)
                        .anyMatch(account -> account.getAccountNumber().equals("DE66666666"))
        );
    }

    @Test
    @DisplayName("Deposit positive test")
    void testDepositPositive() {
        // Пополним на 500.55
        String url = BASE_URL + "/" + accountFour.getId() + "/deposit?amount=500.55";
        ResponseEntity<Double> response =
                testRestTemplate.postForEntity(url, null, Double.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10500.55, response.getBody());

        // Проверим, что в базе баланс тоже обновился
        BankAccount updatedAcc =
                testRestTemplate.getForEntity(BASE_URL + "/" + accountFour.getId(), BankAccount.class)
                        .getBody();
        assertNotNull(updatedAcc);
        assertEquals(10500.55, updatedAcc.getBalance());
    }

    @Test
    @DisplayName("Deposit negative test - amount exceeds max limit")
    void testDepositNegative() {
        // Предположим, что свыше 5000 нельзя пополнять (логика в сервисе)
        String url = BASE_URL + "/" + accountFour.getId() + "/deposit?amount=5001";
        ResponseEntity<String> response =
                testRestTemplate.postForEntity(url, null, String.class);

        // Ожидаем, что сервис выбросит исключение -> HTTP 500
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("Withdraw positive test")
    void testWithdrawPositive() {
        // Снимаем 2000 с аккаунта, у которого 7500
        String url = BASE_URL + "/" + accountFive.getId() + "/withdraw?amount=2000";
        ResponseEntity<Double> response =
                testRestTemplate.postForEntity(url, null, Double.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5500.00, response.getBody());

        // Сверим с базой
        BankAccount updatedAcc =
                testRestTemplate.getForEntity(BASE_URL + "/" + accountFive.getId(), BankAccount.class)
                        .getBody();
        assertNotNull(updatedAcc);
        assertEquals(5500.00, updatedAcc.getBalance());
    }

    @Test
    @DisplayName("Withdraw negative test - amount exceeds max limit")
    void testWithdrawNegative() {
        // Предположим, нельзя снимать свыше 2000 за раз (логика в сервисе)
        String url = BASE_URL + "/" + accountFour.getId() + "/withdraw?amount=2003";
        ResponseEntity<String> response =
                testRestTemplate.postForEntity(url, null, String.class);

        // Ожидаем ошибку
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("Withdraw negative test - amount on account less than min limit")
    void testWithdrawNegativeLowAmount() {
        // Предположим, если после снятия на счёте останется < 100 (логика в сервисе),
        // то тоже ошибка
        String url = BASE_URL + "/" + accountTwo.getId() + "/withdraw?amount=990";
        ResponseEntity<String> response =
                testRestTemplate.postForEntity(url, null, String.class);

        // Ожидаем ошибку
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
