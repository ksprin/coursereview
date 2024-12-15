package edu.virginia.cs.hw7;

/**
 * A review with a student, course, message, and rating.
 */
public class Review {

    private int id;
    private Student student; // student who wrote the review
    private Course course; // course the review is about
    private String message;
    private int rating; // rating from 1-5, verify in InputManager.java

    // Constructors
    public Review(int id, Student student, Course course, String message, int rating) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.message = message;
        this.rating = rating;
    }

    //constructor to be used when ID is not known yet
    public Review(Student student, Course course){
        this.student = student;
        this.course = course;
    }

    public Review(){

    }
    // Getters/Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Review other
                && this.id == other.id
                && this.course.equals(other.course)
                && this.student.equals(other.student)
                && this.message.equals(other.message)
                && this.rating == other.rating;
    }

    @Override
    public String toString() {
        return "Review (Student = %s, Course = %s, Rating = %d/5)"
                .formatted(student.getUsername(), getCourse(), rating);
    }
}
