package de.ait.javalessons.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity //1 Сущьность связанная с таблицей BankAccount в БД, при анотации @Table можно называть табл. по своему
public class BankAccount {

    @Id // Генерация ID автоматически из свободных в базе данных
    @GeneratedValue(strategy = GenerationType.AUTO)// Указывает на генерацию ID по увеличкнию на 1 значение
    // Поля
    private Long id;

    private String accountNumber; // String в связи сожидаемо большим количеством знаков(16)

    private String ownerName;

    private double balance;


    public BankAccount() {// Конструктор по умолчанию, необходим для JPA
        // Default constructor, required for JPA
    }

    public BankAccount(String accountNumber, String ownerName, double balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
