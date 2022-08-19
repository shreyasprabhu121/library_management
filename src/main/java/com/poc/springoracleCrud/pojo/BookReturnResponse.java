package com.poc.springoracleCrud.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.poc.springoracleCrud.model.BookModel;
import com.poc.springoracleCrud.model.UserModel;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookReturnResponse {
	
	private boolean returnStatus;
	private String borrowedDate;
	private String returnDate;
	private BookModel bookDetails;
	private UserModel userDetails;
	private boolean isLateFeeApplicable;
	private Integer lateFee;

}
