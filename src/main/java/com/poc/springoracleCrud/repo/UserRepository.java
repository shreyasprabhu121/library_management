package com.poc.springoracleCrud.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.poc.springoracleCrud.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, String>{
	
	@Query(nativeQuery =true, value="select count(*) from users where emailid=:emailid")
	int checkDuplicateEmail(@Param("emailid") String emailid);
}
