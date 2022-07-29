package com.poc.springoracleCrud.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@Data
public class BookModel {

	@Id
	@GenericGenerator(name = "book_id", strategy = "com.poc.springoracleCrud.config.BookIDGenerator")
	@GeneratedValue(generator = "book_id")
	@Column(name = "book_id")
	private String bookid;
	
	@Column(name = "book_name")
	private String bookName;
	
	@Column(name = "author")
	private String author;
	
	@Column(name = "isborrowed")
	private boolean isborrowed;

	@Override
	public String toString() {
		return "[bookid=" + bookid + ", bookName=" + bookName + ", author=" + author + ", isborrowed="
				+ isborrowed + "]";
	}
	
    
}
