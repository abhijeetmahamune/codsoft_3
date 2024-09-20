import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ATMInterfaceUI {
    static UserAccount user1 = new UserAccount();
    static Random rand = new Random();
    static int captchaAnswer;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ATMInterfaceUI().createAndShowGUI());
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("ATM Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Create header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(135, 206, 250));
        JLabel titleLabel = new JLabel("Welcome to the ATM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Create main panel for buttons
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1, 10, 10));
        mainPanel.setBackground(new Color(255, 228, 196));

        JButton checkBalanceButton = new JButton("Check Balance");
        JButton withdrawButton = new JButton("Withdraw");
        JButton depositButton = new JButton("Deposit");
        JButton exitButton = new JButton("Exit");

        // Add action listeners
        checkBalanceButton.addActionListener(e -> checkBalance());
        withdrawButton.addActionListener(e -> withdraw());
        depositButton.addActionListener(e -> deposit());
        exitButton.addActionListener(e -> System.exit(0));

        mainPanel.add(checkBalanceButton);
        mainPanel.add(withdrawButton);
        mainPanel.add(depositButton);
        mainPanel.add(exitButton);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setVisible(true);
        checkPassword();
    }

    private void checkPassword() {
        int attempts = 0;
        while (attempts < 3) {
            String password = JOptionPane.showInputDialog(null, "Enter your 4-digit Password:", "Password",
                    JOptionPane.QUESTION_MESSAGE);
            if (password == null) {
                JOptionPane.showMessageDialog(null, "Exiting application...", "Exit", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }

            try {
                int inputPassword = Integer.parseInt(password);
                if (user1.verifyPassword(inputPassword)) {
                    return;
                } else {
                    attempts++;
                    JOptionPane.showMessageDialog(null, "Incorrect Password. Attempts left: " + (3 - attempts),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a numeric password.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Maximum attempts reached. Exiting...", "Error", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    private void checkBalance() {
        JOptionPane.showMessageDialog(null, "Your balance is: ₹" + user1.getBalance(), "Balance",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void withdraw() {
        String amountString = JOptionPane.showInputDialog(null, "Enter the amount to withdraw:", "Withdraw",
                JOptionPane.QUESTION_MESSAGE);
        if (amountString != null) {
            try {
                int amount = Integer.parseInt(amountString);
                if (showCaptchaDialog("withdraw", amount)) {
                    user1.withdraw(amount);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deposit() {
        String amountString = JOptionPane.showInputDialog(null, "Enter the amount to deposit:", "Deposit",
                JOptionPane.QUESTION_MESSAGE);
        if (amountString != null) {
            try {
                int amount = Integer.parseInt(amountString);
                // Show CAPTCHA before proceeding
                if (showCaptchaDialog("deposit", amount)) {
                    user1.deposit(amount);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean showCaptchaDialog(String action, int amount) {
        int num1 = rand.nextInt(10);
        int num2 = rand.nextInt(10);
        captchaAnswer = num1 + num2;

        String captchaInput = JOptionPane.showInputDialog(null, "Solve the CAPTCHA: " + num1 + " + " + num2 + " = ?",
                "CAPTCHA", JOptionPane.QUESTION_MESSAGE);

        if (captchaInput != null) {
            try {
                int userAnswer = Integer.parseInt(captchaInput);
                if (userAnswer == captchaAnswer) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "CAPTCHA incorrect. Please try again.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }
}

class UserAccount {
    private int balance = 1000;
    private final int password = 9632;

    int getBalance() {
        return balance;
    }

    boolean verifyPassword(int inputPassword) {
        return inputPassword == password;
    }

    void withdraw(int amount) {
        if (amount > balance) {
            JOptionPane.showMessageDialog(null, "Insufficient balance", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            balance -= amount;
            JOptionPane.showMessageDialog(null,
                    "Withdrawal successful.\nAmount withdrawn: " + amount + "\nYour new balance is: ₹" + balance,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    void deposit(int amount) {
        balance += amount;
        JOptionPane.showMessageDialog(null, "Amount deposited successfully.\nYour new balance is: ₹" + balance,
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
