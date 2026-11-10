
package studentSkill;

import java.sql.*;
import database.DBConnection;
import util.Input;

public class StudentSkillManager {

    public void add() {

        int studentId = Input.getId("Enter student ID: ");
        int skillId = Input.getId("Enter skill ID: ");

        System.out.println("1. TEACHING");
        System.out.println("2. LEARNING");

        int choice = Input.getChoice(2);

        String type = (choice == 1) ? "TEACHING" : "LEARNING";

        try (Connection con = DBConnection.getConnection()) {

            String sql = "INSERT INTO STUDENT_SKILL " +
                "(student_id,skill_id,skill_type) VALUES(?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, studentId);
            ps.setInt(2, skillId);
            ps.setString(3, type);

            ps.executeUpdate();

            System.out.println("Student skill added successfully!");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Invalid IDs or duplicate skill.");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public void display() {

        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "SELECT ss.student_skill_id, s.student_name, " +
                "k.skill_name, ss.skill_type " +
                "FROM STUDENT_SKILL ss " +
                "JOIN STUDENT s ON ss.student_id=s.student_id " +
                "JOIN SKILL k ON ss.skill_id=k.skill_id";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                System.out.println("\nRecord ID: " +
                    rs.getInt("student_skill_id"));

                System.out.println("Student: " +
                    rs.getString("student_name"));

                System.out.println("Skill: " +
                    rs.getString("skill_name"));

                System.out.println("Type: " +
                    rs.getString("skill_type"));
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public void update() {

        int id = Input.getId("Enter student skill ID: ");

        System.out.println("1. TEACHING");
        System.out.println("2. LEARNING");

        int choice = Input.getChoice(2);

        String type = (choice == 1) ? "TEACHING" : "LEARNING";

        try (Connection con = DBConnection.getConnection()) {

            String sql = "UPDATE STUDENT_SKILL SET skill_type=? " +
                "WHERE student_skill_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, type);
            ps.setInt(2, id);

            if (ps.executeUpdate() > 0)
                System.out.println("Student skill updated!");
            else
                System.out.println("Record not found.");

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public void delete() {

        int id = Input.getId("Enter student skill ID: ");

        try (Connection con = DBConnection.getConnection()) {

            String sql = "DELETE FROM STUDENT_SKILL " +
                "WHERE student_skill_id=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            if (ps.executeUpdate() > 0)
                System.out.println("Student skill deleted!");
            else
                System.out.println("Record not found.");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Cannot delete: Related request exists.");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}
