
package student;

public class Student extends User {

    private String department;
    private int year;

    public Student() {
    }

    public Student(int id, String name, String email,
                   String department, int year) {

        super(id, name, email);
        this.department = department;
        this.year = year;
    }

    @Override
    public void displayDetails() {

        System.out.println("Student ID: " + userId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Department: " + department);
        System.out.println("Year: " + year);
    }

    @Override
    public void showRole() {
        System.out.println("Role: Student");
    }
}
