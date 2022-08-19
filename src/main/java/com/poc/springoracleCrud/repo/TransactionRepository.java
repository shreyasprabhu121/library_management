package com.poc.springoracleCrud.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poc.springoracleCrud.model.BookModel;
import com.poc.springoracleCrud.model.BookTransaction;

public interface TransactionRepository extends JpaRepository<BookTransaction, String>{
	@Query("SELECT COUNT(t) from BookTransaction t where t.userid=?1 and t.retrunStatus='PENDING'")
	int getPendingBookCount(String userid);
	
	@Query("select b from BookTransaction b where b.bookid=?1 and b.retrunStatus='PENDING'")
	BookTransaction getTransactionByBookID(String bookid);
}
