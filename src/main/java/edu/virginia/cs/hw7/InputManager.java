package edu.virginia.cs.hw7;

import java.util.List;

//checks validity of user input and sends it where it needs to go
public class InputManager {
    private Course curCourse;
    private Review curReview;
    private Student student;
    private DatabaseManager database;

    //constuctor
    public InputManager() {
        database = new DatabaseManager();
        setUpDatabase();
    }

    //Set Up methods
    public void setUpStudent(String username, String password) {
        this.student = new Student(username, password);
    }

    private void setUpDatabase() {
        database.connect();
        if (!database.doTablesExist()) {
            database.createTables();
        }
    }

    public void reset() {
        //create with auto incrementing ID
        curCourse = new Course();
        curReview = new Review(student, curCourse);
    }

    public void exit() {
        database.disconnect();
    }

    //methods to validate inputs
    public boolean isUsernameInTable() {
        //See if student.username exists in the table, returns false if username is not in table
        if (database.doesStudentExist(student.getUsername())) {
            //set ID
            student = database.getStudentByUsername(student.getUsername());
            return true;
        }
        return false;
    }

    public boolean validatePassword(String p) {
        //see if password is correct for this.username
        Student testStudent = database.getStudentByUsername(student.getUsername());
        return testStudent.getPassword().equals(p);
    }

    public boolean confirmPassword(String confirm) {
        return student.getPassword().equals(confirm);
    }

    public boolean checkCourseExists() {
        //See if curCourse is in table
        if (database.doesCourseExist(curCourse.getDepartment(), curCourse.getCatalogNumber())) {
            //set ID
            curCourse = database.getCourseByName(curCourse.getDepartment(), curCourse.getCatalogNumber());
            return true;
        }
        return false;
    }

    public boolean validateCourseInput(String course) {
        String[] split = course.split(" ");
        if (split.length == 2) {
            String dep = split[0];
            String catNum = split[1];
            return depVal(dep) && catNumVal(catNum);
        }
        return false;
    }

    private boolean catNumVal(String catNum) {
        //4 digit number
        if (catNum.length() == 4) {
            try {
                int num = Integer.parseInt(catNum);
                curCourse.setCatalogNumber(num);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private boolean depVal(String dep) {
        //String of 4 or fewer capital letters
        if ((dep.length() <= 4) && (dep.length() > 0)) {
            if (dep.toUpperCase().equals(dep)) {
                curCourse.setDepartment(dep);
                return true;
            }
        }
        return false;
    }

    public void makeReviewVal() {
        if (!checkCourseExists()) {
            addCourse();
        }
    }

    public boolean alreadyReviewed() {
        //check if user has already made a review for curCourse
        return database.hasUserReviewedCourse(student.getId(), curCourse.getId());
    }

    public boolean courseReviewVal(String courseReview, String ratingStr) {
        curReview.setMessage(courseReview);
        try {
            int num = Integer.parseInt(ratingStr);
            if ((num >= 1) && (num <= 5)) {
                curReview.setRating(num);
                return true;
            }
        } catch (NumberFormatException ignored) {
        }
        return false;
    }

    //methods to add to the tables
    public void addUser() {
        //add student to the table and create ID
        student = database.createNewStudent(student.getUsername(), student.getPassword());
    }

    public void addReview() {
        //make sure review is updated
        curReview.setCourse(curCourse);
        curReview.setStudent(student);
        //add curReview to the table
        database.createNewReview(student.getId(), curCourse.getId(), curReview.getMessage(), curReview.getRating());
    }

    public void addCourse() {
        //add curCourse to the table and çreate ID
        curCourse = database.createNewCourse(curCourse.getDepartment(), curCourse.getCatalogNumber());
    }

    //methods to get things from the tables
    public List<Review> getReviews() {
        //get list of reviews
        return database.getReviews(curCourse.getId());
    }

}
