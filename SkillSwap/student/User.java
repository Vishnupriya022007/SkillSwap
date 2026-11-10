
package student;

public abstract class User {

    protected int userId;
    protected String name;
    protected String email;

    public User() {
    }

    public User(int id, String name, String email) {
        this.userId = id;
        this.name = name;
        this.email = email;
    }

    public abstract void displayDetails();

    public void showRole() {
        System.out.println("User");
    }
}
