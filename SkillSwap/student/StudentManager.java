
package student;

import java.sql.*;
import common.Manageable;
import database.DBConnection;
import util.Input;

public class StudentManager implements Manageable {

    @Override
    public void add() {

        try {
            String name = Input.getName("Enter student name: ");
            String email = Input.getEmail("Enter email: ");
            String password = Input.getText("Enter password: ");
            String dept = Input.getText("Enter department: ");
            int year = Input.getYear("Enter year: ");

            try (Connection con = DBConnection.getConnection()) {

                String sql = "INSERT INTO STUDENT " +
                    "(student_name,email,password,department,year_of_study) " +
                    "VALUES(?,?,?,?,?)";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.setString(4, dept);
                ps.setInt(5, year);

                ps.executeUpdate();

                System.out.println("Student added successfully!");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Email already exists.");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    @Override
    public void display() {

        try (Connection con = DBConnection.getConnection()) {

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM STUDENT");

            while (rs.next()) {

                Student s = new Student(
                    rs.getInt("student_id"),
                    rs.getString("student_name"),
                    rs.getString("email"),
                    rs.getString("department"),
                    rs.getInt("year_of_study")
                );

                s.displayDetails();
                System.out.println("--------------------");
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    @Override
    public void update() {

        int id = Input.getId("Enter student ID: ");
        String dept = Input.getText("Enter new department: ");
        int year = Input.getYear("Enter new year: ");

        try (Connection con = DBConnection.getConnection()) {

            String sql = "UPDATE STUDENT SET department=?, " +
                         "year_of_study=? WHERE student_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, dept);
            ps.setInt(2, year);
            ps.setInt(3, id);

            if (ps.executeUpdate() > 0)
                System.out.println("Student updated successfully!");
            else
                System.out.println("Student not found.");

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    @Override
    public void delete() {

        int id = Input.getId("Enter student ID: ");

        try (Connection con = DBConnection.getConnection()) {

            String sql = "DELETE FROM STUDENT WHERE student_id=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            if (ps.executeUpdate() > 0)
                System.out.println("Student deleted successfully!");
            else
                System.out.println("Student not found.");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Cannot delete: Student has related records.");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public void showUser(User user) {
        user.displayDetails();
        user.showRole();
    }

}
