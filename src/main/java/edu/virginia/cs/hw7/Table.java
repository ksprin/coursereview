package edu.virginia.cs.hw7;

/**
 * Represents a table in the database.
 */
public enum Table {

    // Source: https://www.sqlitetutorial.net/sqlite-autoincrement/
    // Purpose: How to make a column autoincrement
    // Note: For some reason autoincrement only works with INTEGER and not INT
    STUDENTS("""
                CREATE TABLE Students (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Username VARCHAR(64) NOT NULL,
                    Password VARCHAR(64) NOT NULL
                )
            """),
    COURSES("""
                CREATE TABLE Courses (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Department VARCHAR(16) NOT NULL,
                    CatalogNumber INT NOT NULL
                )
            """),
    REVIEWS("""
                CREATE TABLE Reviews (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    StudentID INT NOT NULL,
                    CourseID INT NOT NULL,
                    Message VARCHAR(512) NOT NULL,
                    Rating INT NOT NULL,
                    
                    FOREIGN KEY (StudentID) REFERENCES Students(ID),
                    FOREIGN KEY (CourseID) REFERENCES Course(ID)
                )
            """);

    public final String createQuery;

    Table(String createQuery) {
        this.createQuery = createQuery;
    }

}
