package app;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainForm extends JFrame {

    private static final String url = "jdbc:postgresql://localhost:5432/BookStore";
    private static final String user = "postgres";
    private static final String password = "hLkSj9ST%yk@JPC3";

    private static Connection connection;

    private static void connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Connection problem");
        }

        connection = conn;
    }

    private JTextField textField1;
    private JPanel panel1;
    private JButton createAdminAccountButton;
    private JPasswordField passwordField1;
    private JButton initializeDatabaseButton;

    private PasswordEncoder encoder;

    private String initQuerry = """
            insert into roles (name) values ('ROLE_USER');
            insert into roles (name) values ('ROLE_ADMIN');
                        
            insert into order_status (status_id, description) values(1, 'Zlozone');
            insert into order_status (status_id, description) values(2, 'Anulowane');
            insert into order_status (status_id, description) values(3, 'Zrealizowane');
            insert into order_status (status_id, description) values(4, 'Zarezerwowane');
            """;

    private boolean checkInit() {
        boolean result = false;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM order_status");
            ResultSet statuses = statement.executeQuery();

            statement = connection.prepareStatement("SELECT * FROM roles");
            ResultSet roles = statement.executeQuery();

            result = roles.next() && statuses.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Connection problem");
        }

        return result;
    }

    private void initDB() {
        try {
            if (!checkInit()) {
                PreparedStatement statement = connection.prepareStatement(initQuerry);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Database initialized successfully");
            } else {
                JOptionPane.showMessageDialog(null, "Database already initialized!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Connection problem");
        }
    }

    private void createAdmin() {
        if (checkInit()) {
            Pattern pattern = Pattern.compile("[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}");
            Matcher matcher = pattern.matcher(textField1.getText());

            if (!matcher.find()) {
                JOptionPane.showMessageDialog(null, "Wrong email");
                return;
            }

            pattern = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}");
            matcher = pattern.matcher(String.valueOf(passwordField1.getPassword()));

            if (!matcher.find()) {
                JOptionPane.showMessageDialog(null, "Wrong password:\nPassword must contain at least one digit [0-9],\none lowercase Latin character [a-z],\none uppercase Latin character [A-Z],\none special character like ! @ # & ( ),\nlength of at least 8 characters\nand a maximum of 20 characters\"");
                return;
            }

            PreparedStatement statement;
            try {
                statement = connection.prepareStatement(
                        "INSERT INTO users(login, password, name, surname, phone_number) VALUES (?, ?, 'admin', 'admin', 123456789)");

                statement.setString(1, textField1.getText());
                statement.setString(2, encoder.encode(String.valueOf(passwordField1.getPassword())));

                statement.executeUpdate();

                statement = connection.prepareStatement("SELECT user_id FROM users WHERE login = ?");
                statement.setString(1, textField1.getText());
                ResultSet user = statement.executeQuery();

                user.next();
                int id = user.getInt("user_id");

                statement = connection.prepareStatement("SELECT id FROM roles WHERE name = 'ROLE_ADMIN'");
                ResultSet role = statement.executeQuery();

                role.next();
                int role_id = role.getInt("id");

                statement = connection.prepareStatement("insert into user_roles (user_id, role_id) values(?, ?);");
                statement.setInt(1, id);
                statement.setInt(2, role_id);
                statement.executeUpdate();

                JOptionPane.showMessageDialog(null, "Admin created successfully");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Database not initialized!");
        }
    }

    public MainForm() {

        encoder = new BCryptPasswordEncoder();

        createAdminAccountButton.addActionListener(e -> {
            createAdmin();
        });
        initializeDatabaseButton.addActionListener(e -> {
            initDB();
        });

        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        connect();
        if (connection != null) {
            new MainForm();
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(11, 1, new Insets(0, 0, 0, 0), -1, -1));
        textField1 = new JTextField();
        panel1.add(textField1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Login");
        panel1.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Password");
        panel1.add(label2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        createAdminAccountButton = new JButton();
        createAdminAccountButton.setText("Create Admin Account");
        panel1.add(createAdminAccountButton, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordField1 = new JPasswordField();
        panel1.add(passwordField1, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        initializeDatabaseButton = new JButton();
        initializeDatabaseButton.setText("Initialize Database");
        panel1.add(initializeDatabaseButton, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 100), null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 20), null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 100), null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel1.add(spacer4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 20), null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel1.add(spacer5, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
