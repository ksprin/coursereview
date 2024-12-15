package edu.virginia.cs.hw7;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Reads starter data from a JSON file.
 */
public class JSONReader {

    private final String filename;
    private JSONObject jsonFile = null;

    public JSONReader(String filename) {
        this.filename = filename;
    }

    private String readJSONFile() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))
        ) {
            StringBuilder lines = new StringBuilder();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                lines.append(line);
            }
            return lines.toString();
        } catch (Exception e) {
            throw new RuntimeException("Could not read file " + filename);
        }
    }

    private void parseJSONFile() {
        if (jsonFile != null) return;
        try {
            jsonFile = new JSONObject(readJSONFile());
        } catch (JSONException e) {
            throw new RuntimeException("Could not parse json file " + filename);
        }
    }

    public List<Student> getStudents() {
        parseJSONFile();
        List<Student> students = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonFile.getJSONArray("students");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String username = jsonObject.getString("username");
                String password = jsonObject.getString("password");
                students.add(new Student(id, username, password));
            }
        } catch (JSONException e) {
            throw new RuntimeException("Could read students from json file");
        }
        return students;
    }

    public List<Course> getCourses() {
        parseJSONFile();
        List<Course> courses = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonFile.getJSONArray("courses");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String department = jsonObject.getString("department");
                int catalogNum = jsonObject.getInt("catalog_number");
                courses.add(new Course(id, department, catalogNum));
            }
        } catch (JSONException e) {
            throw new RuntimeException("Could read courses from json file");
        }
        return courses;
    }

    public List<Review> getReviews() {
        parseJSONFile();
        List<Student> students = getStudents();
        List<Course> courses = getCourses();
        List<Review> reviews = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonFile.getJSONArray("reviews");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                try {
                    Review review = getReviewFromObject(jsonObject, students, courses);
                    reviews.add(review);
                } catch (NoSuchElementException e) {
                    continue;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Could read reviews from json file");
        }
        return reviews;
    }

    private Review getReviewFromObject(JSONObject jsonObject, List<Student> students, List<Course> courses) {
        int id = jsonObject.getInt("id");
        int studentID = jsonObject.getInt("student_id");
        int courseID = jsonObject.getInt("course_id");
        String message = jsonObject.getString("message");
        int rating = jsonObject.getInt("rating");

        Optional<Student> optStudent = students.stream().filter(s -> s.getId() == studentID).findFirst();
        if (optStudent.isEmpty()) throw new NoSuchElementException("No student with id" + studentID);
        Student student = optStudent.get();

        Optional<Course> optCourse = courses.stream().filter(c -> c.getId() == courseID).findFirst();
        if (optCourse.isEmpty()) throw new NoSuchElementException("No course with id" + courseID);
        Course course = optCourse.get();

        return new Review(id, student, course, message, rating);
    }

}
