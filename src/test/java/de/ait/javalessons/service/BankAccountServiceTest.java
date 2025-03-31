package de.ait.javalessons.service;

import de.ait.javalessons.model.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BankAccountServiceTest {

    @Value("${bank.min-balance:0.0}")
    private double minBalance;


    @Autowired // Внедрение зависимости сервиса банковских счетов
    private BankAccountService bankAccountService;


    private BankAccount testAccount; // Переменная для хранения тестового банковского счета

    private List<BankAccount> testBankAccounts; // Переменная для хранения списка всех тестовых банковских счетов

    @BeforeEach
    void setUp() {
        testBankAccounts = bankAccountService.getAllBankAccounts();  // Инициализируем список банковских счетов
        testAccount = bankAccountService.getAllBankAccounts().get(0); // Берем первый счет из списка в качестве тестового
    }

    /* 1.***********************************get a list of all accounts****************************************** */
    @Test
    @DisplayName("Получение всех банковских аакаунтов") // анотация для описания, что делает тест
    void getAllBankAccountsTest() {
        // Получаем размер списка банковских счетов, полученного в методе setUp()
        int sizeOfList = testBankAccounts.size();
        // Проверяем, что размер списка, возвращаемого сервисом, соответствует ожидаемому размеру
        assertEquals(sizeOfList, bankAccountService.getAllBankAccounts().size());
        // Получаем первый банковский счет из списка, возвращаемого сервисом
        BankAccount account = bankAccountService.getAllBankAccounts().get(0);

        assertNotNull(account); // Проверяем, что полученный счет не равен null
        // сравниваем id, номер счета, имя владельца баланс тестового аккаунта с полученным из сервиса
        assertEquals(testAccount.getId(), account.getId());
        assertEquals(testAccount.getAccountNumber(), account.getAccountNumber());
        assertEquals(testAccount.getOwnerName(), account.getOwnerName());
        assertEquals(testAccount.getBalance(), account.getBalance());

    }

    /* 2.***********************************find an account by its id****************************************** */
    @Test
    void findBankAccountByIdTestSuccessfully() {
        // получаем объект  Optional<BankAccount> из переменной testAccount с помощью метода findBankAccountById
        Optional<BankAccount> bankAccount = bankAccountService.findBankAccountById(testAccount.getId());
        // проверяем на True наличие объекта
        assertTrue(bankAccount.isPresent());
        // извлекаем номер счета из найденного банковского счета в переменную
        String accountNumber = bankAccount.get().getAccountNumber();
        // сравниваем номер счета найденного счета с номером тестового счета, ожидаем совпадение
        assertEquals(testAccount.getAccountNumber(), accountNumber);
    }

    @Test
    void findBankAccountByIdTestFailed() {
        Long testId = 1000L;
        Optional<BankAccount> bankAccount = bankAccountService.findBankAccountById(testId);
        assertTrue(bankAccount.isEmpty());
    }

    /* 3.***********************************open an account****************************************** */
    @Test
    void saveNewBankAccount() {
        int oldSize = bankAccountService.getAllBankAccounts().size();// получаем количество всех аккаунтов
        // создаеме тестовый аккаунт
        BankAccount testAccountToSave = new BankAccount("1011", "Bob Neumann", 11500.0);


        BankAccount newBankAccount = bankAccountService.saveNewBankAccount(testAccountToSave); // сохраняем аккаунт в базе данных
        int newSize = bankAccountService.getAllBankAccounts().size();// получаем новое количество всех аккаунтов
        assertTrue(newSize == oldSize + 1);// проверяем на True новое  количество с старым + 1
        // сравниваем созданный и сохраненный аккаунты
        assertEquals(testAccountToSave.getAccountNumber(), newBankAccount.getAccountNumber());
    }
    /* 4.***********************************deposit amount into account****************************************** */


    @Test
    void deposit_shouldIncreaseBalance() {
        double initialBalance = testAccount.getBalance();
        double newBalance = bankAccountService.deposit(1000.0, testAccount.getId());

        assertEquals(initialBalance + 1000.0, newBalance);
    }

    @Test
    void deposit_shouldThrowIllegalArgumentException_whenAmountNotGreaterThan0() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bankAccountService.deposit(-200.0, testAccount.getId()));
        assertEquals("Amount must be greater than 0", exception.getMessage());

    }
    /* 5.***********************************withdrawal from account****************************************** */


    @Test
    void withdraw_shouldDecreaseBalance() {
        double initialBalance = testAccount.getBalance();
        double newBalance = bankAccountService.withdraw(500.0, testAccount.getId());

        assertEquals(initialBalance - 500.0, newBalance);
    }

    @Test
    void withdtaw_shouldThrowIllegalArgumentException_WhenBankAccountNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bankAccountService.withdraw(300.0, 1000L));
        assertEquals("Bank account with id 1000 not found", exception.getMessage());

    }

    //Нехватка средств
    @Test
    void withdraw_shouldThrowIllegalArgumentException_whenInsufficientFunds() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bankAccountService.withdraw(testAccount.getBalance() + 10.0, testAccount.getId()));
        assertEquals("Amount is greater than the current balance", exception.getMessage());
    }

    @Test
    void withdraw_shouldThrowIllegalArgumentException_whenAmountNotGreaterThan0() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bankAccountService.withdraw(-200.0, testAccount.getId()));
        assertEquals("Amount must be greater than 0", exception.getMessage());

    }

    @Test
    void withdraw_shouldThrowIllegalArgumentException_whenAmountGreaterThanMaxWithdrawalAmount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bankAccountService.withdraw(5200.0, testAccount.getId()));
        assertEquals("Amount is greater than the current balance", exception.getMessage());
    }

    @Test
    void withdraw_shouldThrowIllegalArgumentException_WhenTheAmountLeftOnTheAccountWillBeLessThanTheAllowedAmount() {
        Double tooBigAmount = testAccount.getBalance() - minBalance + 10.0;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bankAccountService.withdraw(tooBigAmount, testAccount.getId()));
        assertEquals("The current balance is less than the minimum balance", exception.getMessage());
    }

}
