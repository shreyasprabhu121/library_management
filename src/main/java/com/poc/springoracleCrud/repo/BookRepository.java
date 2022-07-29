package com.poc.springoracleCrud.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poc.springoracleCrud.model.BookModel;

public interface BookRepository extends JpaRepository<BookModel, String>{

	@Query("FROM BookModel d WHERE lower(d.bookName) LIKE %:name%")
	List<BookModel> findByBookName(@Param("name") String name);
}
