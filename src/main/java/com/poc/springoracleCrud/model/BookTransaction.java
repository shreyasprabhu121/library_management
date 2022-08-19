package com.poc.springoracleCrud.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;


@Entity
@Table(name = "transactions")
@Data
public class BookTransaction {

	@Id
	@GenericGenerator(name = "transaction_id", strategy = "com.poc.springoracleCrud.config.TransactionIDGenerator")
	@GeneratedValue(generator = "transaction_id")
	@Column(name = "transaction_id")
	private String tranid;
	
	@Column(name = "book_id")
	private String bookid;
	
	@Column(name = "user_id")
	private String userid;
	
	@Column(name = "return_status")
	private String retrunStatus;
	
	@Column(name = "borrowdate")
	private String borrowDate;
	
	@Column(name = "returndate")
	private String retrunDate;
}
