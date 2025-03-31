package de.ait.javalessons.service;

import de.ait.javalessons.model.BankAccount;
import de.ait.javalessons.repositories.BankAccountRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j // аннотация позволяющая логировать
public class BankAccountService {//4 методы вызывают сервисы, тоесть посредники между контроллером и базой данных

    private BankAccountRepository bankAccountRepository;

    private String message = "Amount must be greater than 0";

    @Value("${bank.min-balance:0.0}")// значение берется из application.properties, значение после(:) по умолч.
    private double minBalance; // переменная для значения мин.баланса

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    //Получение списка всех счетов
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();// Используя метод репозитория находит все аккаунты и отправляет их в List
    }

    // Плоучение счета по id
    public Optional<BankAccount> findBankAccountById(Long id) { // Optional (Сущьность или empty)
        log.info("findBankAccountById: {}", id);// логирование
        return bankAccountRepository.findById(id);// передает id в метод наследуется из CRUDRepository возвращает конкр. репо.
    }

    // Открытие(сздание) нового счета
    public BankAccount saveNewBankAccount(BankAccount bankAccount) { // принимает и возвращает сущьность BankAccount
        log.info("saveNewBankAccount: {}", bankAccount);
        return bankAccountRepository.save(bankAccount);// Получает  объект целиком(в том числе и сгенерированный id ) и сохраняет в базе данных
    }

    // Пополнить(принимает переменные amount и bankAccountId)
    @Transactional // аннотация подстраховывает транзакцию(rollBack оставляет счет прежним с логированием), при непредвид. обст.(выкл. свет)
    public double deposit(double amount, Long bankAccountId){
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)// проверка наличия id
                // Исключение об отсутствии id (без аккаунта невозможно начислить средства)
                .orElseThrow(() -> new IllegalArgumentException("Bank account with id " + bankAccountId + " not found"));
        if(amount <= 0){ // исключаем 0 или отрицательное пополнение(скрытое снятие) баланса
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        bankAccount.setBalance(bankAccount.getBalance() + amount); // добавляем к балансу аккаунта сумму пополнения
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount); // сохраняем изменения в БД
        return savedBankAccount.getBalance(); // Возвращаем сохраненные данные баланса аккаунта для контроля
    }

    // Снять
    @Transactional
    public double withdraw(double amount, Long bankAccountId){
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Bank account with id " + bankAccountId + " not found"));
        if(amount <= 0){
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        if(amount > bankAccount.getBalance()){ // исключаем снятие суммы превышающей значение баланса
            log.error("Amount is greater than the current balance");
            throw new IllegalArgumentException("Amount is greater than the current balance");
        }
        if(bankAccount.getBalance() - amount < minBalance){ // исключаем снятие суммы превышающей значение  мин. баланса
            log.error("The current balance is less than the minimum balance");
            throw new IllegalArgumentException("The current balance is less than the minimum balance");
        }
        else { // иначе вычитаем значение суммы от баланса и устанавливаем новое значение
            bankAccount.setBalance(bankAccount.getBalance() - amount);
            // логируем процедуру снятия указывая значения суммы, id, баланса аккаунта
            log.info("Withdrawal of {} from account with id {} resulted in balance {}", amount, bankAccountId, bankAccount.getBalance());
            BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
            return savedBankAccount.getBalance();
        }
    }

}
