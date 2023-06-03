**ToDo List Manager**

This is a Java Swing application developed as a project for managing a to-do list using priority queues as the underlying data structure. The application allows users to add tasks with priorities, view tasks, complete tasks, and view the next task with the highest priority.


**Features**

**Add tasks:** Users can add tasks to the to-do list by specifying a priority (on a scale of 1 to 5) and a description for the task. The tasks are stored in a priority queue based on their priorities.
**Complete tasks:** Users can mark tasks as completed, and the application will remove the completed tasks from the to-do list. The tasks are removed from both the priority queue and the application.
**View tasks:** Users can view all the tasks in the to-do list. The application displays the descriptions of all the tasks in a dialog box.
**View next task:** Users can view the task with the highest priority that is currently in the to-do list. The application displays the description of the next task in a dialog box.
**Erase the current list:** Users can erase the entire to-do list, deleting all tasks from the database and clearing the task lists in the application.


**Technologies Used**

**Java Swing:** The graphical user interface (GUI) is developed using Java Swing, a powerful GUI toolkit for Java.
**Priority Queues:** The tasks in the to-do list are managed using priority queues. The tasks are sorted based on their priorities, allowing efficient access to the task with the highest priority.


**Database**

The application is connected to a MySQL database for persistently storing the tasks. The tasks are loaded from the database when the application starts and saved to the database when tasks are added or deleted.


**How to Run**

To run the ToDo List Manager application, follow these steps:

Ensure that you have Java Development Kit (JDK) installed on your system.
Set up a MySQL database and update the database connection details in the connectToDatabase method of the ToDoListManager class.

**Compile the Java source files using the following command:**
javac ToDoListManager.java
**Run the application using the following command:**
java ToDoListManager


**Future Enhancements**

**User authentication:** Implement a login system to allow multiple users to manage their own to-do lists.
**Task editing:** Allow users to edit task priorities and descriptions.
**Task due dates:** Add the ability to assign due dates to tasks and implement reminders for upcoming tasks.
**Task categories:** Introduce the concept of task categories to organize tasks more effectively.
**Data visualization:** Display the tasks using charts or graphs to provide a visual representation of the priorities.


**Contributors**

Feedback and Suggestions will be appreciated at Email: gauravkumarjha306@gmail.com


**License**
This project is licensed under the MIT License.
