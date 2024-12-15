package edu.virginia.cs.hw7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DatabaseManagerTests {

    private DatabaseManager manager;

    @BeforeEach
    public void createManager() {
        manager = new DatabaseManager();
    }

    @Test
    public void connectToDatabaseSuccess() {
        manager.connect();
        assertThrows(IllegalStateException.class, manager::connect);
        manager.disconnect();
    }

    @Test
    public void createTablesSuccess() {
        manager.connect();
        manager.createTables();
        manager.deleteTables();
        manager.disconnect();
    }


    @Test
    public void insertStudent() {
        manager.connect();
        manager.createTables();
        manager.clearTables();

        manager.createNewStudent("mark", "markPassword");
        manager.disconnect();
    }

    @Test
    public void testReset() {
        manager.connect();
        manager.deleteTables();
        manager.disconnect();
    }

    @Test
    public void insertAndGetStudentSuccess() {
        Student student1 = new Student(3, "james", "jamesPassword");
        Student student2 = new Student(4, "bobby", "bobbyPassword");

        manager.connect();
        manager.createTables();
        manager.clearTables();

        manager.insertStudent(student1);
        manager.insertStudent(student2);
        assertEquals(student1, manager.getStudentByID(3));
        assertEquals(student1, manager.getStudentByUsername("james"));
        assertEquals(student1.getPassword(), manager.getPasswordForUsername("james"));

        manager.deleteTables();
        manager.disconnect();
    }

    @Test
    public void createAndGetStudentSuccess() {
        manager.connect();
        manager.createTables();
        manager.clearTables();

        Student student = manager.createNewStudent("james", "jamesPassword");
        manager.createNewStudent("bobby", "bobbyPassword");
        assertEquals("jamesPassword", student.getPassword());

        manager.deleteTables();
        manager.disconnect();
    }

    @Test
    public void getCourseByIDSuccess() {
        Course course = new Course(6, "CS", 3140);
        manager.connect();
        manager.createTables();
        manager.clearTables();

        manager.insertCourse(course);
        assertEquals(course, manager.getCourseByID(6));

        manager.deleteTables();
        manager.disconnect();
    }

    @Test
    public void getCourseByNameSuccess() {
        Course course = new Course(6, "CS", 3140);
        manager.connect();
        manager.createTables();
        manager.clearTables();

        manager.insertCourse(course);
        assertEquals(course, manager.getCourseByName("CS", 3140));

        manager.deleteTables();
        manager.disconnect();
    }

    @Test
    public void insertAndGetReviewSuccess() {
        Student student = new Student(3, "james", "jamesPassword");
        Course course = new Course(6, "CS", 3140);
        Review review = new Review(1, student, course, "Best course ever!!!", 5);
        manager.connect();
        manager.createTables();
        manager.clearTables();

        manager.insertStudent(student);
        manager.insertCourse(course);
        manager.insertReview(review);

        manager.deleteTables();
        manager.disconnect();
    }

}
