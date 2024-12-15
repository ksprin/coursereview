package edu.virginia.cs.hw7;

/**
 * A course with a course ID, department, and catalog number.
 */
public class Course {

    private int id;
    private String department;
    private int catalogNumber;

    // Constructors
    public Course(int id, String department, int catalogNumber) {
        this.id = id;
        this.department = department;
        this.catalogNumber = catalogNumber;
    }
    public Course(String department, int catalogNumber) {
        this.department = department;
        this.catalogNumber = catalogNumber;
    }

    public Course(){
    }

    // Getters/Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(int catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Course other
                && this.id == other.id
                && this.department.equals(other.department)
                && this.catalogNumber == other.catalogNumber;
    }

    @Override
    public String toString() {
        return "Course(%s %d)"
                .formatted(department, catalogNumber);
    }

}
