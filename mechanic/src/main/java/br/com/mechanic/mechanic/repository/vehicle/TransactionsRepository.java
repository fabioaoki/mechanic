package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.provider.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Long> {}
