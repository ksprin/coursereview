package edu.virginia.cs.hw7;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JSONReaderTest {

    @Test
    public void readJSONFileSuccess() {
        JSONReader json = new JSONReader("sample_data.json");
        List<Student> students = json.getStudents();
        assertEquals(9, students.size());

        List<Course> courses = json.getCourses();
        assertEquals(7, courses.size());

        List<Review> reviews = json.getReviews();
        assertEquals(8, reviews.size());
    }

    @Test
    @Disabled
    // Only run this to add starter data
    public void addStarterDataToDatabase() {
        JSONReader json = new JSONReader("sample_data.json");
        List<Student> students = json.getStudents();
        List<Course> courses = json.getCourses();
        List<Review> reviews = json.getReviews();

        DatabaseManager db = new DatabaseManager();
        db.connect();
        db.createTables();
        db.clearTables();

        students.forEach(db::insertStudent);
        courses.forEach(db::insertCourse);
        reviews.forEach(db::insertReview);

        db.save();
        db.disconnect();
    }

}