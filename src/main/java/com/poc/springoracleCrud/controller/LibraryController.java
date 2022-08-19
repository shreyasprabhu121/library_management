package com.poc.springoracleCrud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poc.springoracleCrud.model.BookModel;
import com.poc.springoracleCrud.model.UserModel;
import com.poc.springoracleCrud.pojo.BookBorrowResponse;
import com.poc.springoracleCrud.pojo.BookReturnResponse;
import com.poc.springoracleCrud.service.LibraryService;


@RestController
@RequestMapping("/library")
public class LibraryController {

	@Autowired
	LibraryService libraryService;

	@PostMapping("/addbook")
	public String addBook(@RequestBody BookModel book) {
		return libraryService.addBook(book);
	}
	
	@GetMapping("/searchbook/{name}")
	public List<BookModel> searchBook(@PathVariable("name") String bookname){
		return libraryService.searchBook(bookname);
	}
	
	@DeleteMapping("/deletebook/{bookid}")
	public String deleteBook(@PathVariable("bookid") String bookid) {
		return libraryService.deleteBook(bookid);
	}
	
	@GetMapping("/updateborrowstatus/{id}")
	public String updatestatus(@PathVariable("id") String bookid) {
		return libraryService.updateborrowstatus(bookid);
	}
	
	@PostMapping("/adduser")
	public String addUser(@RequestBody UserModel user) {
		return libraryService.addUser(user);
	}
	
	@GetMapping("/borrowbook/{bookid}/{userid}")
	public BookBorrowResponse borrowBook(@PathVariable("bookid") String bookid, @PathVariable("userid") String userid) {
		return libraryService.borrowBook(bookid, userid);
	}
	
	@GetMapping("/returnbook/{bookid}")
	public BookReturnResponse addUser(@PathVariable("bookid") String bookid) {
		return libraryService.returnBook(bookid);
	}
}
