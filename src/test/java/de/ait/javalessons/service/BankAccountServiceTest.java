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


    @Autowired
    private BankAccountService bankAccountService;


    private BankAccount testAccount;

    private List<BankAccount> testBankAccounts;

    @BeforeEach
    void setUp() {
        testBankAccounts = bankAccountService.getAllBankAccounts();
        testAccount = bankAccountService.getAllBankAccounts().get(0);
    }

    /* 1.***********************************get a list of all accounts****************************************** */
    @Test
    @DisplayName("Получение всех банковских аакаунтов")
    void getAllBankAccountsTest() {

        int sizeOfList = testBankAccounts.size();
        assertEquals(sizeOfList, bankAccountService.getAllBankAccounts().size());

        BankAccount account = bankAccountService.getAllBankAccounts().get(0);

        assertNotNull(account);
        assertEquals(testAccount.getId(), account.getId());
        assertEquals(testAccount.getAccountNumber(), account.getAccountNumber());
        assertEquals(testAccount.getOwnerName(), account.getOwnerName());
        assertEquals(testAccount.getBalance(), account.getBalance());

    }

    /* 2.***********************************find an account by its id****************************************** */
    @Test
    void findBankAccountByIdTestSuccessfully() {
        Optional<BankAccount> bankAccount = bankAccountService.findBankAccountById(testAccount.getId());
        assertTrue(bankAccount.isPresent());
        String accountNumber = bankAccount.get().getAccountNumber();
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
        int oldSize = bankAccountService.getAllBankAccounts().size();
        BankAccount testAccountToSave = new BankAccount("1011", "Bob Neumann", 11500.0);


        BankAccount newBankAccount = bankAccountService.saveNewBankAccount(testAccountToSave);
        int newSize = bankAccountService.getAllBankAccounts().size();
        assertTrue(newSize == oldSize + 1);
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
