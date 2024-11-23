package com.paye.whitelabel.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.paye.whitelabel.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	// Fetch transactions by user ID
	List<Transaction> findByUserId(Long id);

	List<Transaction> findByUsername(String username);

}
