package com.example;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        BookInventorySystem bookInventorySystem = new BookInventorySystem();
        UserManagementSystem userManagementSystem = new UserManagementSystem();

        addBooksToInventory(bookInventorySystem);

        addUsersToSystem(userManagementSystem);

        LibraryFacade facade = new LibraryFacadeImpl(bookInventorySystem, userManagementSystem);

        Scanner scanner = new Scanner(System.in);
        String userName = "";
        String action = "";
        String bookTitle = "";

        while (true) {
            System.out.println("\nSelect an action:");
            System.out.println("1. Borrow a book");
            System.out.println("2. Return a book");
            System.out.println("3. Search for books");
            System.out.println("4. Check book availability");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    userName = getUserName(scanner);
                    bookTitle = getBookTitle(scanner);
                    boolean borrowed = facade.borrowBook(userName, bookTitle);
                    if (borrowed) {
                        System.out.println("Book \"" + bookTitle + "\" borrowed successfully by " + userName);
                    } else {
                        System.out.println("Sorry, the book \"" + bookTitle + "\" is not available.");
                    }
                    break;
                case 2:
                    userName = getUserName(scanner);
                    bookTitle = getBookTitle(scanner);
                    boolean returned = facade.returnBook(userName, bookTitle);
                    if (returned) {
                        System.out.println("Book \"" + bookTitle + "\" returned successfully by " + userName);
                    } else {
                        System.out.println("Sorry, either the book \"" + bookTitle + "\" is not borrowed by " + userName + " or it does not exist.");
                    }
                    break;
                case 3:
                    System.out.print("Enter search query: ");
                    bookTitle = scanner.nextLine();
                    List<Book> searchResults = facade.searchBooks(bookTitle);
                    if (!searchResults.isEmpty()) {
                        System.out.println("Search results:");
                        for (Book book : searchResults) {
                            System.out.println("- " + book.getTitle());
                        }
                    } else {
                        System.out.println("No books found matching the search query \"" + bookTitle + "\"");
                    }
                    break;
                case 4:
                    System.out.print("Enter book title: ");
                    bookTitle = scanner.nextLine();
                    boolean available = facade.checkBookAvailability(bookTitle);
                    if (available) {
                        System.out.println("The book \"" + bookTitle + "\" is available.");
                    } else {
                        System.out.println("Sorry, the book \"" + bookTitle + "\" is not available.");
                    }
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please enter a number from 1 to 5.");
            }
        }
    }

    private static void addBooksToInventory(BookInventorySystem bookInventorySystem) {
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("Add books to the inventory system:");
        while (true) {
            System.out.print("Enter book title (or 'done' to finish adding books): ");
            String title = scanner.nextLine();
            if (title.equalsIgnoreCase("done")) {
                break;
            }
            bookInventorySystem.addBook(title);
        }
    }

    private static void addUsersToSystem(UserManagementSystem userManagementSystem) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nAdd users to the system:");
        while (true) {
            System.out.print("Enter user name (or 'done' to finish adding users): ");
            String userName = scanner.nextLine();
            if (userName.equalsIgnoreCase("done")) {
                break;
            }
            userManagementSystem.addUser(userName);
        }
    }

     private static String getUserName(Scanner scanner) {
        System.out.print("Enter your name: ");
        return scanner.nextLine();
    }

    private static String getBookTitle(Scanner scanner) {
        System.out.print("Enter book title: ");
        return scanner.nextLine();
    }
}

interface LibraryFacade {
    boolean borrowBook(String userName, String bookTitle);

    boolean returnBook(String userName, String bookTitle);

    List<Book> searchBooks(String query);

    boolean checkBookAvailability(String bookTitle);
}


class BookInventorySystem {
    private Map<String, Boolean> bookAvailability;

    public BookInventorySystem() {
        this.bookAvailability = new HashMap<>();
    }

    public void addBook(String bookTitle) {
        bookAvailability.put(bookTitle, true);
    }

    public void borrowBook(String bookTitle) {
        bookAvailability.put(bookTitle, false);
    }

    public void returnBook(String bookTitle) {
        bookAvailability.put(bookTitle, true);
    }

    public List<String> searchBook(String query) {
        List<String> searchResults = new ArrayList<>();
        for (String title : bookAvailability.keySet()) {
            if (title.toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(title);
            }
        }
        return searchResults;
    }

    public boolean checkBookAvailability(String bookTitle) {
        return bookAvailability.getOrDefault(bookTitle, false);
    }
}

class UserManagementSystem {
    private Map<String, List<String>> userBooks;

    public UserManagementSystem() {
        this.userBooks = new HashMap<>();
    }

    public void addUser(String userName) {
        userBooks.put(userName, new ArrayList<>());
    }

    public void addBookToUser(String userName, String bookTitle) {
        if (!userBooks.containsKey(userName)) {
            addUser(userName);
        }
        userBooks.get(userName).add(bookTitle);
    }

    public void removeBookFromUser(String userName, String bookTitle) {
        if (userBooks.containsKey(userName)) {
            userBooks.get(userName).remove(bookTitle);
        }
    }

    public List<String> getUserBooks(String userName) {
        return userBooks.get(userName);
    }
}

class Book {
    private String title;
    private String author;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}

class LibraryFacadeImpl implements LibraryFacade {
    private BookInventorySystem bookInventorySystem;
    private UserManagementSystem userManagementSystem;

    public LibraryFacadeImpl(BookInventorySystem bookInventorySystem, UserManagementSystem userManagementSystem) {
        this.bookInventorySystem = bookInventorySystem;
        this.userManagementSystem = userManagementSystem;
    }

    @Override
    public boolean borrowBook(String userName, String bookTitle) {
        if (bookInventorySystem.checkBookAvailability(bookTitle)) {
            bookInventorySystem.borrowBook(bookTitle);
            userManagementSystem.addBookToUser(userName, bookTitle);
            return true;
        }
        return false;
    }

    @Override
    public boolean returnBook(String userName, String bookTitle) {
        List<String> userBooks = userManagementSystem.getUserBooks(userName);
        if (userBooks != null && userBooks.contains(bookTitle)) {
            bookInventorySystem.returnBook(bookTitle);
            userManagementSystem.removeBookFromUser(userName, bookTitle);
            return true;
        }
        return false;
    }

    @Override
    public List<Book> searchBooks(String query) {
        List<String> bookTitles = bookInventorySystem.searchBook(query);
        List<Book> searchResults = new ArrayList<>();
        for (String title : bookTitles) {
            searchResults.add(new Book(title, "Unknown")); 
        }
        return searchResults;
    }

    @Override
    public boolean checkBookAvailability(String bookTitle) {
        return bookInventorySystem.checkBookAvailability(bookTitle);
    }
}
