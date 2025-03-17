package de.ait.javalessons.repositories;

import de.ait.javalessons.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {
}
