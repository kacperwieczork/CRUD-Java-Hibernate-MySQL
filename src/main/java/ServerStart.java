import model.Book;
import respository.BookRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.UUID;


public class ServerStart {

    public static void main(String[] args) {
        //dodawania ksiazek do bazy
        BookRepository bookRepository = new BookRepository();
        bookRepository.add(new Book("title1", "author1", UUID.randomUUID().toString().replace("-", "")));
        bookRepository.add(new Book("title2", "author2", UUID.randomUUID().toString().replace("-", "")));
        bookRepository.add(new Book("title3", "author3", UUID.randomUUID().toString().replace("-", "")));
        bookRepository.add(new Book("title4", "author4", UUID.randomUUID().toString().replace("-", "")));
        bookRepository.add(new Book("title5", "author5", UUID.randomUUID().toString().replace("-", "")));
        bookRepository.add(new Book("title6", "author6", UUID.randomUUID().toString().replace("-", "")));

        int port = 8080;
        try {
            Server.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Server server = new Server();
        System.out.println("Server started on port: " + port);
        server.run();
    }

}
