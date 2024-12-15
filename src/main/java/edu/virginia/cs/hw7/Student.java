package edu.virginia.cs.hw7;

/**
 * A student with a student ID, username, and password.
 */
public class Student {

    private int id;
    private String username;
    private String password;

    // Constructors
    public Student(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Student(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Student other
                && this.id == other.id
                && this.username.equals(other.username)
                && this.password.equals(other.password);
    }

    @Override
    public String toString() {
        return "Student(Username = %s, Password = %s)"
                .formatted(username, password);
    }

}
