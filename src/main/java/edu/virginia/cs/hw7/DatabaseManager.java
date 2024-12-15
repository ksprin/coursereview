package edu.virginia.cs.hw7;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// TODO move all print statements to presentation
public class DatabaseManager {

    public static String DATABASE_NAME = "Reviews.sqlite3";
    private Connection connection;

    /**
     * Connects to the database and creates the file of none exists. Must be called
     * before any other methods are called.
     *
     * @throws IllegalStateException if the Manager is already connected
     */
    public void connect() {
        if (connection != null) {
            throw new IllegalStateException("Database is already connected");
        }

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite drivers are not downloaded");
        }

        try {
            String url = "jdbc:sqlite:" + DATABASE_NAME;
            connection = DriverManager.getConnection(url);
            if (connection == null) return;

            connection.setAutoCommit(false);
//            System.out.println("Connected to database.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates empty tables for students, courses and reviews if they do not exist.
     *
     * @throws IllegalStateException if the database hasn't been connected
     */
    public void createTables() {
        if (doTablesExist()) return;

        try (Statement statement = connection.createStatement()) {
            for (Table table : Table.values()) {
                statement.execute(table.createQuery);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Checks whether any tables exist in the database.
     *
     * @return if the database file contains tables
     */
    public boolean doTablesExist() {
        // Source: https://www.baeldung.com/jdbc-check-table-exists
        // Purpose: To check for tables in JDBC
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Clears all data from the database but does not delete any tables.
     *
     * @throws IllegalStateException if the database hasn't been connected
     */
    public void clearTables() {
        try (Statement statement = connection.createStatement()) {
            for (Table table : Table.values()) {
                statement.execute("DELETE FROM " + table.name());
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Clears all data and deletes all the tables from the database.
     *
     * @throws IllegalStateException if the database hasn't been connected
     */
    public void deleteTables() {
        try (Statement statement = connection.createStatement()) {
            for (Table table : Table.values()) {
                // this isn't working, throwing error: "table locked"
                statement.execute("DROP TABLE IF EXISTS " + table.name());
            }
        } catch (SQLException e) {
//            throw new IllegalStateException(e);
        }
    }

    /**
     * Commits any changes made to the database.
     *
     * @throws IllegalStateException if the database hasn't been connected
     */
    public void save() {
        try {
            connection.commit();
//            System.out.println("Saved to database.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ends the connection but does not commit any changes.
     *
     * @throws IllegalStateException if the database hasn't been connected
     */
    public void disconnect() {
        try {
            connection.close();
//            System.out.println("Disconnected from database.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserts a student with an ID, username, and password into the table.
     *
     * @param student the student to insert
     */
    public void insertStudent(Student student) {
        String insert = """
                INSERT INTO Students
                VALUES(%d, '%s', '%s')
                """.formatted(student.getId(), student.getUsername(), student.getPassword());
        try (Statement statement = connection.createStatement()) {
            statement.execute(insert);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a student into with a username and password, auto-generates an ID,
     * and inserts it into the table.
     *
     * @param username the student's username
     * @param password the student's password
     * @return the new student
     */
    public Student createNewStudent(String username, String password) {
        String insert = """
                INSERT INTO Students (Username, Password)
                VALUES('%s', '%s')
                """.formatted(username, password);
        try (Statement statement = connection.createStatement()) {
            statement.execute(insert);
            return getStudentByUsername(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserts a course with an ID, department, and catalog number into the table.
     *
     * @param course the course to insert
     */
    public void insertCourse(Course course) {
        String insert = """
                INSERT INTO Courses
                VALUES(%d, '%s', %d)
                """.formatted(course.getId(), course.getDepartment(), course.getCatalogNumber());
        try (Statement statement = connection.createStatement()) {
            statement.execute(insert);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a student into with a username and password, auto-generates an ID,
     * and inserts it into the table.
     *
     * @param department    the course's subject code
     * @param catalogNumber the course's catalog number
     * @return the new course
     */
    public Course createNewCourse(String department, int catalogNumber) {
        String insert = """
                INSERT INTO Courses (Department, CatalogNumber)
                VALUES('%s', %d)
                """.formatted(department, catalogNumber);
        try (Statement statement = connection.createStatement()) {
            statement.execute(insert);
            return getCourseByName(department, catalogNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertReview(Review review) {
        String insert = """
                INSERT INTO Reviews
                VALUES(%d, %d, %d, "%s", %d)
                """.formatted(review.getId(), review.getStudent().getId(), review.getCourse().getId(),
                review.getMessage(), review.getRating());
        try (Statement statement = connection.createStatement()) {
            statement.execute(insert);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createNewReview(int studentID, int courseID, String message, int rating) {
        String insert = """
                INSERT INTO Reviews (StudentID, CourseID, Message, Rating)
                VALUES(%d, %d, '%s', %d)
                """.formatted(studentID, courseID, message, rating);
        try (Statement statement = connection.createStatement()) {
            statement.execute(insert);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Student getStudentByID(int id) {
        try (Statement statement = connection.createStatement()) {
            String getStudent = "SELECT * FROM Students WHERE ID = '%d'".formatted(id);
            ResultSet results = getQueryResult(statement, getStudent);

            if (!results.next()) {
                throw new IllegalArgumentException("No student with ID " + id);
            }

            String username = results.getString("Username");
            String password = results.getString("Password");
            return new Student(id, username, password);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Student getStudentByUsername(String username) {
        try (Statement statement = connection.createStatement()) {
            String getStudent = "SELECT * FROM Students WHERE USERNAME = '%s'".formatted(username);
            ResultSet results = getQueryResult(statement, getStudent);

            if (!results.next()) {
                throw new IllegalArgumentException("No student with username " + username);
            }

            int id = results.getInt("ID");
            String password = results.getString("Password");
            statement.close();
            return new Student(id, username, password);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean doesStudentExist(String username) {
        try (Statement statement = connection.createStatement()) {
            String getStudent = "SELECT * FROM Students WHERE USERNAME = '%s'".formatted(username);
            ResultSet results = getQueryResult(statement, getStudent);
            return results.next();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public String getPasswordForUsername(String username) {
        Student student = getStudentByUsername(username);
        return student.getPassword();
    }

    public Course getCourseByID(int id) {
        try (Statement statement = connection.createStatement()) {
            String getCourse = "SELECT * FROM Courses WHERE ID = '%d'".formatted(id);
            ResultSet results = getQueryResult(statement, getCourse);

            if (!results.next()) {
                throw new IllegalArgumentException("No course with ID " + id);
            }

            String department = results.getString("Department");
            int catalogNum = results.getInt("CatalogNumber");
            return new Course(id, department, catalogNum);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Course getCourseByName(String department, int catalogNum) {
        try (Statement statement = connection.createStatement()) {
            String getCourse = """
                    SELECT * FROM (
                        SELECT * FROM Courses WHERE Department = '%s'
                    ) WHERE CatalogNumber = %d
                    """.formatted(department, catalogNum);
            ResultSet results = getQueryResult(statement, getCourse);

            if (!results.next()) {
                throw new IllegalArgumentException("No course with name %s %s".formatted(department, catalogNum));
            }

            int id = results.getInt("id");
            return new Course(id, department, catalogNum);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean doesCourseExist(String department, int catalogNum) {
        try (Statement statement = connection.createStatement()) {
            String getCourse = """
                    SELECT * FROM (
                        SELECT * FROM Courses WHERE Department = '%s'
                    ) WHERE CatalogNumber = %d
                    """.formatted(department, catalogNum);
            ResultSet results = getQueryResult(statement, getCourse);

            return results.next();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean hasUserReviewedCourse(int studID, int courseID) {
        String select = """
                SELECT * FROM (
                    SELECT * FROM Reviews WHERE CourseID = '%s'
                ) WHERE StudentID = %d
                """.formatted(courseID, studID);
        try (Statement statement = connection.createStatement()) {
            ResultSet results = getQueryResult(statement, select);

            return results.next();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Review> getReviews(int id) {
        List<Review> reviews = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String getReviews = "SELECT * FROM Reviews WHERE CourseID = '%d'".formatted(id);
            ResultSet results = getQueryResult(statement, getReviews);

            while (results.next()) {
                Student student = getStudentByID(results.getInt("StudentID"));
                Course course = getCourseByID(results.getInt("CourseID"));
                Review r = new Review(results.getInt("ID"), student, course, results.getString("Message"), results.getInt("Rating"));
                reviews.add(r);
            }
            return reviews;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private ResultSet getQueryResult(Statement statement, String query) throws SQLException {
        statement.execute(query);
        return statement.getResultSet();
    }
}

