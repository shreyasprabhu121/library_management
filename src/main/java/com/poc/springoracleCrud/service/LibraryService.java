package com.poc.springoracleCrud.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.springoracleCrud.model.BookModel;
import com.poc.springoracleCrud.model.BookTransaction;
import com.poc.springoracleCrud.model.UserModel;
import com.poc.springoracleCrud.pojo.BookBorrowResponse;
import com.poc.springoracleCrud.pojo.BookReturnResponse;
import com.poc.springoracleCrud.repo.BookRepository;
import com.poc.springoracleCrud.repo.TransactionRepository;
import com.poc.springoracleCrud.repo.UserRepository;

@Service
public class LibraryService {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LibraryService.class);

	@Autowired
	BookRepository bookRepo;

	@Autowired
	UserRepository userrepo;

	@Autowired
	TransactionRepository tranRepo;

	SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

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
		} else
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

	public String updateborrowstatus(String bookid) {
		String returnText = "";
		Optional<BookModel> bookByID = bookRepo.findById(bookid);
		if (bookByID.isPresent()) {
			BookModel bookfromdb = bookByID.get();
			if (bookfromdb.isIsborrowed())
				returnText = "Book returned successfully";
			else
				returnText = "Book borrowed successfully";
			bookfromdb.setIsborrowed(!bookfromdb.isIsborrowed());
			bookRepo.save(bookfromdb);

		}
		return returnText;

	}

	public String addUser(UserModel user) {
		LOGGER.info("User addition request received");
		int emailcount = userrepo.checkDuplicateEmail(user.getEmailid());

		try {
			if (emailcount > 0) {
				throw new Exception("EmailID already exixts:- " + user.getEmailid());
			}
			userrepo.save(user);
		} catch (Exception e) {

			LOGGER.error("Error While adding the user with exception :- {}", e.getMessage());
			return e.getMessage();
		}
		LOGGER.info("User added successfully with userid:- {}", user.getUserid());
		return "User Added " + user.toString();
	}

	public BookBorrowResponse borrowBook(String bookid, String userid) {
		BookBorrowResponse borrowResponse = new BookBorrowResponse();
		LOGGER.info("Borrow book request received with bookid: {} & useriid:{}", bookid, userid);
		String response = "";
		LOGGER.info("Checking book availibilty!!");
		Optional<BookModel> bookByID = bookRepo.findById(bookid);
		BookModel bookfromdb = new BookModel();
		if (bookByID.isPresent()) {

			bookfromdb = bookByID.get();
			if (bookfromdb.isIsborrowed()) {
				borrowResponse.setErrMsg("Book is already borrowed by different user");
				response = "Book is already borrowed by different user";
				LOGGER.info("Book is borrowed by different user");
			} else {
				LOGGER.info("Book available to borrow, checking user details");
				Optional<UserModel> userbyid = userrepo.findById(userid);
				UserModel userFromDB = new UserModel();
				if (userbyid.isPresent()) {
					userFromDB = userbyid.get();
					LOGGER.info("Checking pending books from user");
					int userBookCount = tranRepo.getPendingBookCount(userid);
					if (userBookCount < 2) {
						LOGGER.info("Borrowing the book: {}", bookfromdb.getBookName());
						BookTransaction bookTrans = new BookTransaction();
						bookTrans.setBookid(bookid);
						bookTrans.setUserid(userid);
						bookTrans.setRetrunStatus("PENDING");

						// SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy",
						// Locale.getDefault());
						String borrowdate = sfd.format(new Date());
						bookTrans.setBorrowDate(borrowdate);
						Calendar cal = Calendar.getInstance();
						try {
							cal.setTime(sfd.parse(borrowdate));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						cal.add(Calendar.DAY_OF_MONTH, 7);
						String dateAfter = sfd.format(cal.getTime());
						updateborrowstatus(bookid);
						tranRepo.save(bookTrans);
						
						buildBorrowResponse(borrowResponse, bookfromdb, userFromDB, borrowdate, dateAfter);
						
						response = "Book Borrowed!! retrun date is: " + dateAfter;
						LOGGER.info("Book Borrow successful with expected return date: {}", dateAfter);
					} else {
						borrowResponse.setErrMsg("User exceeded book borrow limit");
						response = "User exceeded book borrow limit";
						LOGGER.info("User exceeded the borrow limit");
					}

				} else {
					borrowResponse.setErrMsg("User not found");
					response = "User not found";
					LOGGER.info("User not found");
				}
			}
		} else {
			borrowResponse.setErrMsg("Book not found");
			response = "Book not found";
		}

		return borrowResponse;
	}

	

	public BookReturnResponse returnBook(String bookid) {
		BookReturnResponse returnResponse = new BookReturnResponse();
		Optional<BookModel> bookByID = bookRepo.findById(bookid);
		BookModel bookfromdb = new BookModel();
		if (bookByID.isPresent()) {
			bookfromdb = bookByID.get();
		}

		BookTransaction bookTransDB = new BookTransaction();
		bookTransDB = tranRepo.getTransactionByBookID(bookid);
		if (bookTransDB != null) {
			UserModel userDetails = userrepo.findById(bookTransDB.getUserid()).get();
			String borrowDate = bookTransDB.getBorrowDate();

			String returnDate = sfd.format(new Date());

			int lateFee = calculateLateFee(borrowDate, returnDate);

			updateborrowstatus(bookid);

			bookTransDB.setRetrunDate(returnDate);
			bookTransDB.setRetrunStatus("RETURNED");
			tranRepo.save(bookTransDB);
			
			
			buildReturnResponse(returnResponse, bookfromdb, bookTransDB, userDetails, returnDate, lateFee);

			String response = "Book Returned Successfully";
			if (lateFee > 0) {
				returnResponse.setLateFee(lateFee);
				response += "!! Collect late fee of " + lateFee + ".rs";
			}
		}

		return returnResponse;
	}

	private void buildReturnResponse(BookReturnResponse returnResponse, BookModel bookfromdb,
			BookTransaction bookTransDB, UserModel userDetails, String returnDate, int lateFee) {
		returnResponse.setBookDetails(bookfromdb);
		returnResponse.setBorrowedDate(bookTransDB.getBorrowDate());
		returnResponse.setReturnDate(returnDate);
		returnResponse.setLateFeeApplicable(lateFee > 0 ? true : false);
		returnResponse.setReturnStatus(true);
		returnResponse.setUserDetails(userDetails);
	}
	
	private void buildBorrowResponse(BookBorrowResponse borrowResponse, BookModel bookfromdb, UserModel userFromDB,
			String borrowdate, String dateAfter) {
		borrowResponse.setBookDetails(bookfromdb);
		borrowResponse.setUserDetails(userFromDB);
		borrowResponse.setBorrowStatus(true);
		borrowResponse.setBorrowedOn(borrowdate);
		borrowResponse.setReturnBy(dateAfter);
	}

	public int calculateLateFee(String startdate, String endDate) {

		long dateDiff = 0;
		try {
			Date date1 = sfd.parse(startdate);
			Date date2 = sfd.parse(endDate);
			long diff = date2.getTime() - date1.getTime();
			dateDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return dateDiff > 7 ? (int) (dateDiff * 5) : 0;
	}
}
