package edu.virginia.cs.hw7;

import java.util.List;
import java.util.Scanner;


/**
 * The command-line user interface for the program.
 */
public class CourseReviewUI {
    private InputManager manager;
    private boolean running;
    private Scanner scanner;

    private void initalize() {
        manager = new InputManager();
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to Course Reviews");
        initalize();
        run();
    }

    private void run() {
        running = true;
        while (running) {
            // keep showing menu until exit
            showLoginMenu();
            boolean mainMenu = true;
            while (mainMenu) {
                mainMenu = showMainMenu();
            }
        }
        exit();
    }

    private void exit() {
        running = false;
        manager.exit();
        scanner.close();
        System.exit(0);
    }

    private void showLoginMenu() {
        boolean canProceed = false;
        while (!canProceed) {
            String userInput = getUserInput("""
                    Please enter an option:
                    (1) Login
                    (2) Create a New User
                    (3) Quit
                    """);
            switch (userInput) {
                case "1" -> canProceed = login();
                case "2" -> canProceed = createNewUser();
                case "3" -> canProceed = logout();
                default -> reEnter();
            }
        }
    }

    private boolean showMainMenu() {
        System.out.println("Main Menu - Enter one of the Following Options:");
        System.out.println("(1) Submit a Review for a Course");
        System.out.println("(2) See Reviews for a Course");
        System.out.println("(3) Logout");
        System.out.println("(4) Logout and Quit");

        Scanner input = new Scanner(System.in);
        String userInput = input.nextLine();
        switch (userInput) {
            case "1" -> submitReview();
            case "2" -> seeReviews();
            case "3" -> {
                System.out.print("Logging out...\n");
                return false;
            }
            case "4" -> {
                System.out.print("Logging out and quiting...\n");
                running = false;
                return false;
            }
            default -> reEnter();
        }
        return true;
    }

    private boolean login() {
        //collects user input and sets manager
        System.out.println("Logging in...");
        String username = getUserInput("Enter Username: ");
        String password = getUserInput("Enter Password: ");
        manager.setUpStudent(username, password);

        if (!manager.isUsernameInTable()) {
            System.out.printf("Username %s does not exist\n", username);
        } else if (!manager.validatePassword(password)) {
            System.out.println("Password is not correct");
        } else {
            return true; //make sure ID is set up
        }
        return false;
    }

    private boolean createNewUser() {
        //collects user input and sets manager
        System.out.println("Create a New User");
        String username = getUserInput("Enter Username: ");
        String password = getUserInput("Enter Password: ");
        manager.setUpStudent(username, password);
        if (manager.isUsernameInTable()) {
            //username already exists
            System.out.println("Username already exists");
            return false;
        }
        //ask to confirm password
        String confirm = getUserInput("Confirm Password: ");
        if (!manager.confirmPassword(confirm)) {
            //password not confirmed
            System.out.println("Passwords do not match");
            return false;
        }
        manager.addUser();
        System.out.println("Account Created\n");
        return true;
    }

    private boolean logout() {
        System.out.println("Logged out.");
        exit();
        return true;
    }

    private void reEnter() {
        System.out.println("Incorrect input\nUnable to proceed, please try again");
    }

    private void submitReview() {
        //reset the current course so a new one can be saved
        manager.reset();
        if (!isValidCourseName()) {
            return;
        }
        //check if course exists - add if needed
        manager.makeReviewVal();
        //check if user has already reviewed course
        if (manager.alreadyReviewed()) {
            System.out.println("User has already reviewed this course\n");
            return;
        }
        askForReviewAndRating();
        manager.addReview();
        System.out.println("Course Review has been added!\n");
    }

    private void seeReviews() {
        //reset the current course so a new one can be saved
        manager.reset();
        //enter course name
        if (!isValidCourseName()) {
            return;
        }
        //make sure course exists in the table
        if (!manager.checkCourseExists()) {
            System.out.println("Course has not been inputed into the database\n");
        }
        //make sure reviews exists
        List<Review> courseReviews = manager.getReviews();
        if (courseReviews.isEmpty()) {
            System.out.println("No reviews exist for the course\n");
        } else {
            System.out.println("\nReviews for course: ");
            for (Review review : courseReviews) {
                System.out.println("Review: " + review.getMessage() + "\nRating: " + review.getRating() + "\n");
            }
        }

    }

    private boolean isValidCourseName() {
        //enter course name
        String courseName = getUserInput("Enter Course Name: ");
        //validate input format
        if (!manager.validateCourseInput(courseName)) {
            System.out.println("Invalid course name\n");
            return false;
        }
        return true;
    }

    private void askForReviewAndRating() {
        String courseReview = getUserInput("Enter Course Review/Comments: ");
        boolean ratingIsValid;
        do {
            String rating = getUserInput("Enter Course Rating: ");
            ratingIsValid = manager.courseReviewVal(courseReview, rating);
            if (!ratingIsValid) {
                reEnter();
            }
        } while (!ratingIsValid);
    }

    private String getUserInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

}
