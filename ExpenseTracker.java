import java.io.*;
import java.util.*;

public class ExpenseTracker {
    private HashMap<String, User> users;
    private User currentUser;

    public ExpenseTracker() {
        users = new HashMap<>();
        loadUsers();
    }

    public void registerUser(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please choose a different username.");
        } else {
            User newUser = new User(username, password);
            users.put(username, newUser);
            saveUsers();
            System.out.println("User registered successfully!");
        }
    }

    public void loginUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
            currentUser = users.get(username);
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    public void addExpense(Date date, String category, double amount) {
        if (currentUser != null) {
            Expense expense = new Expense(date, category, amount);
            currentUser.addExpense(expense);
            saveUsers();
            System.out.println("Expense added successfully!");
        } else {
            System.out.println("Please login first.");
        }
    }

    public void listExpenses() {
        if (currentUser != null) {
            ArrayList<Expense> expenses = currentUser.getExpenses();
            for (Expense expense : expenses) {
                System.out.println(expense.getDate() + " - " + expense.getCategory() + " - " + expense.getAmount());
            }
        } else {
            System.out.println("Please login first.");
        }
    }

    public void showTotalExpensesByCategory(String category) {
        if (currentUser != null) {
            double total = currentUser.getTotalExpensesByCategory(category);
            System.out.println("Total expenses for " + category + ": " + total);
        } else {
            System.out.println("Please login first.");
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"))) {
            users = (HashMap<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Add Expense");
            System.out.println("4. List Expenses");
            System.out.println("5. Show Total Expenses By Category");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    tracker.registerUser(username, password);
                    break;
                case 2:
                    System.out.print("Enter username: ");
                    username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    password = scanner.nextLine();
                    tracker.loginUser(username, password);
                    break;
                case 3:
                    System.out.print("Enter date (yyyy-mm-dd): ");
                    String dateStr = scanner.nextLine();
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                        tracker.addExpense(date, category, amount);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format.");
                    }
                    break;
                case 4:
                    tracker.listExpenses();
                    break;
                case 5:
                    System.out.print("Enter category: ");
                    category = scanner.nextLine();
                    tracker.showTotalExpensesByCategory(category);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
