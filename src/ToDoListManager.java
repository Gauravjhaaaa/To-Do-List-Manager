import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class ToDoListManager extends JFrame implements ActionListener {
    private int counter;
    private List<Task> tasks;
    private List<Task> pq;
    private Connection connection;

    public ToDoListManager() {
        counter = 0;
        tasks = new ArrayList<>();
        pq = new ArrayList<>();

        // Connect to MySQL database
        connectToDatabase();

        loadTasksFromDatabase();

        setTitle("To-Do List Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 1));

        JLabel welcomeLabel = new JLabel("Welcome to the To-Do List Manager!");
        add(welcomeLabel);

        JButton addTaskButton = new JButton("Add a task");
        addTaskButton.addActionListener(this);
        add(addTaskButton);

        JButton completeTaskButton = new JButton("Complete a task");
        completeTaskButton.addActionListener(this);
        add(completeTaskButton);

        JButton viewTasksButton = new JButton("View all tasks");
        viewTasksButton.addActionListener(this);
        add(viewTasksButton);

        JButton viewNextTaskButton = new JButton("View next task");
        viewNextTaskButton.addActionListener(this);
        add(viewNextTaskButton);

        JButton eraseListButton = new JButton("Erase the current list");
        eraseListButton.addActionListener(this);
        add(eraseListButton);

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(this);
        add(quitButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void addTask(int priority, String description) {
        Task task = new Task(priority, counter, description);
        tasks.add(task);
        pq.add(task);
        counter++;
        Collections.sort(pq);
        insertTaskIntoDatabase(task);
    }

    public void completeTask() {
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks remaining.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Task task = tasks.remove(0);
        pq.remove(task);
        JOptionPane.showMessageDialog(this, "Completed task:\n" + task.getDescription(), "Success", JOptionPane.INFORMATION_MESSAGE);
        deleteTaskFromDatabase(task);
    }

    public void viewTasks() {
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks to display.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder taskList = new StringBuilder();
        for (Task task : tasks) {
            taskList.append(task.getDescription()).append("\n");
        }

        JOptionPane.showMessageDialog(this, "All tasks:\n" + taskList.toString(), "Task List", JOptionPane.INFORMATION_MESSAGE);
    }

    public void viewNextTask() {
        if (pq.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks remaining.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Task task = pq.get(0);
        JOptionPane.showMessageDialog(this, "Next task:\n" + task.getDescription(), "Next Task", JOptionPane.INFORMATION_MESSAGE);
    }

    public void eraseList() {
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tasks to erase.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Clear tasks from the database
        deleteAllTasksFromDatabase();

        // Clear tasks from the application
        tasks.clear();
        pq.clear();
        counter = 0;

        JOptionPane.showMessageDialog(this, "To-do list erased successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        if (action.equals("Add a task")) {
            String priorityInput = JOptionPane.showInputDialog(this, "Enter the priority (1-5) for the task:");
            String descriptionInput = JOptionPane.showInputDialog(this, "Enter the task description:");

            try {
                int priority = Integer.parseInt(priorityInput);
                addTask(priority, descriptionInput);
                JOptionPane.showMessageDialog(this, "Task added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid priority. Please try again with a numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (action.equals("Complete a task")) {
            completeTask();
        } else if (action.equals("View all tasks")) {
            viewTasks();
        } else if (action.equals("View next task")) {
            viewNextTask();
        } else if (action.equals("Erase the current list")) {
            eraseList();
        } else if (action.equals("Quit")) {
            closeDatabaseConnection();
            System.exit(0);
        }
    }

    private void connectToDatabase() {
        try {
            // Provide your MySQL database credentials
            String url = "jdbc:mysql://localhost:3306/todolist";
            String username = "root";
            String password = "Gaurav@2004";

            // Establish connection
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void closeDatabaseConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTasksFromDatabase() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int priority = resultSet.getInt("priority");
                String description = resultSet.getString("description");

                Task task = new Task(priority, id, description);
                tasks.add(task);
                pq.add(task);
                counter = Math.max(counter, id + 1);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertTaskIntoDatabase(Task task) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO tasks (priority, description) VALUES (?, ?)");
            statement.setInt(1, task.getPriority());
            statement.setString(2, task.getDescription());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteTaskFromDatabase(Task task) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM tasks WHERE id = ?");
            statement.setInt(1, task.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteAllTasksFromDatabase() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM tasks");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ToDoListManager();
            }
        });
    }
}

class Task implements Comparable<Task> {
    private int priority;
    private int id;
    private String description;

    public Task(int priority, int id, String description) {
        this.priority = priority;
        this.id = id;
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int compareTo(Task other) {
        int priorityComparison = Integer.compare(priority, other.getPriority());
        if (priorityComparison != 0) {
            return priorityComparison;
        } else {
            return Integer.compare(id, other.getId());
        }
    }
}
