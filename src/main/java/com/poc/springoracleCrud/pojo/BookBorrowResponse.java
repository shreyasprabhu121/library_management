package com.poc.springoracleCrud.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.poc.springoracleCrud.model.BookModel;
import com.poc.springoracleCrud.model.UserModel;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookBorrowResponse {
	
	private boolean borrowStatus;
	
	private BookModel bookDetails;
	private UserModel userDetails;
	private String borrowedOn;
	private String returnBy;
	
	private String errMsg;


}
