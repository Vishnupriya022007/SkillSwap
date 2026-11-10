
package student;

public class Admin extends User {

    public Admin(int id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public void displayDetails() {

        System.out.println("Admin ID: " + userId);
        System.out.println("Admin Name: " + name);
        System.out.println("Email: " + email);
    }

    @Override
    public void showRole() {
        System.out.println("Role: Administrator");
    }
}
