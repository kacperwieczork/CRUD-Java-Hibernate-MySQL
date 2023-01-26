import dto.UserOperation;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Client {
    public static String userID;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            //nie jest zarejestrowany
            if (userID == null || userID.isEmpty()) {
                System.out.println("""
                        Dostepne operacje:\s
                        logowanie->1\s
                        rejestracja->2""");
                String command = scanner.nextLine();
                switch (command) {
                    case "1" -> login();
                    case "2" -> register();
                    default -> System.out.println("Invalid command.");
                }
            }
            if (userID != null && !userID.isEmpty()) {
                System.out.println("""
                        Wyšwietl wypozyczone ksiazki->1:\s
                        Wyšwietl ksiazki dostepne do wypozyczenia->2\s
                        Zwroc ksiazke->3\s
                        Wypozycz ksiazke->4\s
                        Wyloguj->5""");
                String command = scanner.nextLine();
                switch (command) {
                    case "1" -> viewBorrowed();
                    case "2" -> viewAvailable();
                    case "3" -> returnBook();
                    case "4" -> borrow();
                    case "5" -> logout();
                    default -> System.out.println("Invalid command.");
                }
            }
        }
    }


    private static void logout() {
        System.out.println("Wylogowano pomyslnie");
        userID = null;
    }

    private static void returnBook() {
        UserOperation userOperation = new UserOperation();
        userOperation.setOperation("returnBook");
        userOperation.setBorrowedBookUserId(Long.valueOf(userID));
        System.out.println("Podaj ID ksiazki ktora chcesz zwrocic");
        userOperation.setBorrowBookId(Long.valueOf(new Scanner(System.in).nextLine()));
        tcpRequest(userOperation);
    }

    private static void borrow() {
        UserOperation userOperation = new UserOperation();
        userOperation.setOperation("borrowBook");
        userOperation.setBorrowedBookUserId(Long.valueOf(userID));
        System.out.println("Podaj ID ksiazki ktora chcesz wypozyczyc");
        userOperation.setBorrowBookId(Long.valueOf(new Scanner(System.in).nextLine()));
        tcpRequest(userOperation);
    }

    private static void viewAvailable() {
        UserOperation userOperation = new UserOperation();
        userOperation.setOperation("listAvailableBooks");
        tcpRequest(userOperation);
    }

    private static void viewBorrowed() {
        UserOperation userOperation = new UserOperation();
        userOperation.setOperation("listMyBorrows");
        userOperation.setBorrowedBookUserId(Long.valueOf(userID));
        tcpRequest(userOperation);
    }

    private static void register() {
        UserOperation userOperation = new UserOperation();
        System.out.println("Podaj login");
        userOperation.setLogin(new Scanner(System.in).nextLine());
        System.out.println("Podaj haslo");
        userOperation.setPassword(new Scanner(System.in).nextLine());
        userOperation.setOperation("register");
        tcpRequest(userOperation);
    }

    private static void login() {
        UserOperation userOperation = new UserOperation();
        userOperation.setOperation("login");
        System.out.println("Podaj login");
        userOperation.setLogin(new Scanner(System.in).nextLine());
        System.out.println("Podaj haslo");
        userOperation.setPassword(new Scanner(System.in).nextLine());
        tcpRequest(userOperation);
    }

    private static void tcpRequest(UserOperation userOperation) {
        // parameter storage
        String gateway = "localhost";
        int port = 8080;
        Socket netSocket;
        BufferedReader in;
        try {
            System.out.println("Connecting with: " + gateway + " at port " + port);
            netSocket = new Socket(gateway, port);
            // get the output stream from the socket.
            OutputStream outputStream = netSocket.getOutputStream();
            // create an object output stream from the output stream so we can send an object through it
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            //wysḽanie obiektu do socketu
            objectOutputStream.writeObject(userOperation);

            //tu przyjdzie response z serwera
            in = new BufferedReader(new InputStreamReader(netSocket.getInputStream()));
            System.out.println("Connected");

            // Read and print out the response
            String response;
            while ((response = in.readLine()) != null) {
                processResponse(response, userOperation);
            }
            // Terminate - close all the streams and the socket
            in.close();
            netSocket.close();
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + gateway + ".");
            exit(1);
        } catch (IOException e) {
            System.err.println("No connection with " + gateway + ".");
            exit(1);
        }
    }

    private static void processResponse(String response, UserOperation userOperation) {
        switch (userOperation.getOperation()) {
            case "login":
                if (response != null) {
                    System.out.println("Zalogowano pomyšlnie");
                    System.out.println("Twoje ID: " + response);
                    Client.userID = response;
                } else {
                    System.out.println("Wskazany u«ytkownik nie istnieje");
                }
                break;
            case "register":
                if (response != null) {
                    System.out.println("Utworzono konto pomyšlnie oraz zalogowano");
                    System.out.println("Twoje ID: " + response);
                    Client.userID = response;
                } else {
                    System.out.println("Bḽäd rejestracji");
                }
                break;
            default:
                System.out.println(response);
        }
    }
//
//    public static void main2(String[] args) {
////        loginTest();
//        registerTest();
//        loginTest();
//        availableBooksTest();
//        borrowBookTest();
//        listMyBorrowsTest();
//        returnBookTest();
//        System.out.println("1");
//        listMyBorrowsTest();
//        System.out.println("2");
//        availableBooksTest();
//    }
//
//    private static void returnBookTest() {
//        UserOperation userOperation = new UserOperation();
//        userOperation.setOperation("returnBook");
//        userOperation.setBorrowedBookUserId(Long.valueOf(userID));
//        userOperation.setBorrowBookId(1L);
//        tcpRequest(userOperation);
//    }
//
//    private static void listMyBorrowsTest() {
//        UserOperation userOperation = new UserOperation();
//        userOperation.setOperation("listMyBorrows");
//        userOperation.setBorrowedBookUserId(Long.valueOf(userID));
//        tcpRequest(userOperation);
//    }
//
//    private static void borrowBookTest() {
//        UserOperation userOperation = new UserOperation();
//        userOperation.setOperation("borrowBook");
//        userOperation.setBorrowedBookUserId(Long.valueOf(userID));
//        userOperation.setBorrowBookId(1L);
//        tcpRequest(userOperation);
//    }
//
//    private static void availableBooksTest() {
//        UserOperation userOperation = new UserOperation();
//        userOperation.setOperation("listAvailableBooks");
//        tcpRequest(userOperation);
//    }
//
//    private static void registerTest() {
//        UserOperation userOperation = new UserOperation();
//        userOperation.setLogin("login");
//        userOperation.setPassword("haslo");
//        userOperation.setOperation("register");
//        tcpRequest(userOperation);
//    }
//
//    private static void loginTest() {
//        UserOperation userOperation = new UserOperation();
//        userOperation.setLogin("login");
//        userOperation.setPassword("haslo");
//        userOperation.setOperation("login");
//        tcpRequest(userOperation);
//    }



}
