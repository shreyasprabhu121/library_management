package com.poc.springoracleCrud.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.springoracleCrud.model.BookModel;
import com.poc.springoracleCrud.model.UserModel;
import com.poc.springoracleCrud.repo.BookRepository;
import com.poc.springoracleCrud.repo.UserRepository;

@Service
public class LibraryService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LibraryService.class);

	@Autowired
	BookRepository bookRepo;
	
	@Autowired
	UserRepository userrepo;

	public String addBook(BookModel book) {
		LOGGER.info("Book addition request received");
		try {
			bookRepo.save(book);
		} catch (Exception e) {
			if (e.getMessage().contains("ConstraintViolationException")) {
				LOGGER.error("Book with name {} already exists", book.getBookName());
				return "Book already exists with name: " + book.getBookName();
			}
			LOGGER.error("Error While adding the book {} with exception {}", book.getBookName(), e.getMessage());
			return e.getMessage();
		}
		LOGGER.info("Book added successfully with bookid:- {}", book.getBookid());
		return "Book Added " + book.toString();
	}

	public List<BookModel> searchBook(String bookname) {
		LOGGER.info("Search request received with search text '{}'", bookname);
		List<BookModel> searchedBooks = new ArrayList<>();
		try {
			searchedBooks = bookRepo.findByBookName(bookname.toLowerCase());
		} catch (Exception e) {
			LOGGER.error("Error while searching books in DB");
		}
		if (searchedBooks.isEmpty()) {
			LOGGER.info("No books found");
		}else
			LOGGER.info("{} books found with search text '{}'", searchedBooks.size(), bookname);
		return searchedBooks;
	}

	public String deleteBook(String book_id) {
		LOGGER.info("Book deletion request received for BookId:- {}", book_id);
		try {
			bookRepo.deleteById(book_id);
		} catch (Exception e) {
			LOGGER.error("Deleting book failed with BookId:- {}", book_id);
			return "Deletion of book " + book_id + " failed";
		}
		LOGGER.info("Book deleted successfully with BookId:- {}", book_id);
		return "Book Deleted Successfully";
	}
	
	public void updateborrowstatus(String bookid) {
		Optional<BookModel> bookByID=bookRepo.findById(bookid);
		if(bookByID.isPresent()) {
		BookModel bookfromdb=bookByID.get();
		bookfromdb.setIsborrowed(!bookfromdb.isIsborrowed());
		bookRepo.save(bookfromdb);
		}
		
		
	}
	
	public String addUser(UserModel user) {
		LOGGER.info("User addition request received");
		int emailcount=userrepo.checkDuplicateEmail(user.getEmailid());
		
		try {
			if(emailcount>0) {
				throw new Exception("EmailID already exixts:- "+user.getEmailid());
			}
			userrepo.save(user);
		} catch (Exception e) {
			
			LOGGER.error("Error While adding the user with exception :- {}", e.getMessage());
			return e.getMessage();
		}
		LOGGER.info("User added successfully with userid:- {}", user.getUserid());
		return "User Added " + user.toString();
	}
}
