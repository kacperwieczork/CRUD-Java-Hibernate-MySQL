import dto.UserOperation;
import dto.UserOperationReponse;
import model.Book;
import model.BorrowedBook;
import model.User;
import respository.BookRepository;
import respository.BoorrowedBookRepository;
import respository.UserRepository;

import java.util.*;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OperationProcessor {

    private UserRepository userRepository = new UserRepository();
    private BookRepository bookRepository = new BookRepository();
    private BoorrowedBookRepository boorrowedBookRepository = new BoorrowedBookRepository();

    public UserOperationReponse process(UserOperation userOperation) {
        switch (userOperation.getOperation()) {
            case "login":
                return processLogin(userOperation);
            case "register":
                return processRegister(userOperation);
            case "listAvailableBooks":
                return processListAvailableBooks(userOperation);
            case "listMyBorrows":
                return processListMyBorrows(userOperation);
            case "borrowBook":
                return processBorrowBook(userOperation);
            case "returnBook":
                return processReturnBook(userOperation);
            default:
                return new UserOperationReponse("ERROR");
        }
    }

    private UserOperationReponse processRegister(UserOperation userOperation) {
        User user = new User();
        user.setUsername(userOperation.getLogin());
        user.setPassword(userOperation.getPassword());
        try {
            this.userRepository.save(user);
            User user1 = this.userRepository.checkIfExists(userOperation.getLogin(), userOperation.getPassword());
            return new UserOperationReponse(user1.getId().toString());
        } catch (Exception e) {
            return new UserOperationReponse("Bḽäd tworzenia uzytkownika, juz taki istnieje!");
        }
    }

    private UserOperationReponse processListAvailableBooks(UserOperation userOperation) {
        Stream<Book> stream = this.bookRepository.getAll().stream();
        Stream<String> stringStream = stream
                .filter(book -> !Objects.nonNull(book.getBorrowedBook()))
                .map(book -> {
                    return "ID: " + book.getId() + " AUTOR: " + book.getAuthor() + " TYTUḼ: " + book.getTitle() + "\n";
                });
        String collect = stringStream.collect(Collectors.joining(""));
        return new UserOperationReponse(collect);
    }

    private UserOperationReponse processReturnBook(UserOperation userOperation) {
        List<BorrowedBook> userBorrowings = boorrowedBookRepository.getUserBorrowings(userOperation.getBorrowedBookUserId());

        Stream<BorrowedBook> borrowedBookStream = userBorrowings.stream().filter(borrow -> borrow.getBook().getId().equals(userOperation.getBorrowBookId()));
        Optional<BorrowedBook> any = borrowedBookStream.findAny();
        if (any.isPresent()) {
            boorrowedBookRepository.delete(any.get().getId());
            return new UserOperationReponse("Pomyšlnie zwrocono ksiazke!");
        } else {
            return new UserOperationReponse("Bḽëdne dane!");
        }
    }

    private UserOperationReponse processBorrowBook(UserOperation userOperation) {
        Book book = this.bookRepository.get(userOperation.getBorrowBookId());
        if (!Objects.isNull(book.getBorrowedBook())) {
            return new UserOperationReponse("Nie mo«na wypo«yczyc wypo«yczonej ju« ksiä«ki");
        } else {
            BorrowedBook borrowedBook = new BorrowedBook();
            borrowedBook.setBook(book);
            User byId = userRepository.get(userOperation.getBorrowedBookUserId());
            borrowedBook.setUser(byId);
            borrowedBook.setBorrowDate(new Date());
            borrowedBook.setDueDate(addOneMonth());
            boorrowedBookRepository.add(borrowedBook);
            return new UserOperationReponse("Pomyšlnie wypo«yczono ksiazke: " + borrowedBook);
        }
    }


    public Date addOneMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }

    private UserOperationReponse processListMyBorrows(UserOperation userOperation) {
        Long borrowedBookUserId = userOperation.getBorrowedBookUserId();
        String collect = boorrowedBookRepository.getUserBorrowings(borrowedBookUserId).stream().map(borrowing -> {
            return borrowing.toString() + "\n";
        }).collect(Collectors.joining(""));
        return new UserOperationReponse(collect);
    }

    private UserOperationReponse processLogin(UserOperation userOperation) {
        User user1 = null;
        try {
            user1 = this.userRepository.checkIfExists(userOperation.getLogin(), userOperation.getPassword());
        } catch (Exception e) {
            return new UserOperationReponse(null);
        }
        return new UserOperationReponse(user1.getId().toString());
    }
}
