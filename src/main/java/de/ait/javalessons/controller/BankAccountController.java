package de.ait.javalessons.controller;

import de.ait.javalessons.model.BankAccount;
import de.ait.javalessons.service.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/accounts")
public class BankAccountController { //3 распределяет внешние запросы на выполнение методам

    //поле, указывающее, куда обращаться за методами
    private BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping
    public ResponseEntity<List<BankAccount>> getAllBankAccounts() {
        //вызываем метод getAllBankAccount из bankAccountService
        List<BankAccount> bankAccounts = bankAccountService.getAllBankAccounts();
        log.info("Found {} bank accounts", bankAccounts.size()); //логируем
        return ResponseEntity.ok(bankAccounts); // возвращаем лист и статус 200
    }

    @PostMapping
    // получаем из запроса данные номера и имени держателя
    public ResponseEntity<BankAccount> createBankAccount(@RequestParam String accountNumber,
                                                         @RequestParam String ownerName) {
        // создаем новый аккаунт с полями номера, имени держателя и балансом
        BankAccount bankAccount = new BankAccount(accountNumber,ownerName,0.0);
        // сохраняем данные аккаунта в новой переменной
        BankAccount savedBankAccount = bankAccountService.saveNewBankAccount(bankAccount);
        if(savedBankAccount == null){ // если аккаунта нет
            log.error("Error saving bank account"); // логгируем
            // возвращаем пустой ответ с статусом ошибки сервера
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        log.info("Saved bank account with id {}", savedBankAccount.getId());
        // возвращаем новый аккаунт с статусом CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBankAccount);
    }

    @GetMapping({"/{id}"})
    // получаем из запроса данные id
    public ResponseEntity<BankAccount> getBankAccountById(@PathVariable Long id){
        log.info("Getting bank account with id {}", id);// логируем id
        // возвращаем Optional через метод findBankAccountById, который принимает id
        return bankAccountService.findBankAccountById(id)
                // если счет найден, оборачиваем его в ResponseEntity с кодом 200 OK
                .map(ResponseEntity::ok)
                // если счет не найден, возвращаем ResponseEntity с кодом 404 NOT_FOUND и пустым телом
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/{id}/deposit")
    // получаем из запроса данные id и сумму пополнения
    public ResponseEntity<Double> deposit(@PathVariable Long id, @RequestParam double amount){
        log.info("Depositing {} to bank account with id {}", amount, id);
        // вызываем метод deposit сервиса для пополнения счета и возвращаем результат в ResponseEntity с кодом 200 OK
        return ResponseEntity.ok(bankAccountService.deposit(amount, id));
    }

    @PostMapping("/{id}/withdraw")
    // извлекаем id из пути запроса и amount из параметров запроса
    public ResponseEntity<Double> withdraw(@PathVariable Long id, @RequestParam double amount){
        // логируем операцию снятия: выводим сумму снятия и идентификатор счета
        log.info("Withdrawing {} from bank account with id {}", amount, id);
        // вызываем метод withdraw сервиса для списания средств со счета и возвращаем результат в ResponseEntity с кодом 200 OK
        return ResponseEntity.ok(bankAccountService.withdraw(amount, id));
    }
}
