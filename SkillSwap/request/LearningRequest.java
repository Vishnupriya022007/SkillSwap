
package request;

import java.sql.*;

import database.DBConnection;
import util.Input;
import common.SkillSwapException;

public class LearningRequest {

    public void validateRequest(int learner, int teacher)
            throws SkillSwapException {

        if (learner == teacher) {
            throw new SkillSwapException(
                "A student cannot send a request to themselves!"
            );
        }
    }

    public boolean showAllStudents(Connection con)
            throws SQLException {

        String sql =
            "SELECT student_id, student_name FROM STUDENT";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        boolean found = false;

        System.out.println("\n--- AVAILABLE STUDENTS ---");

        while (rs.next()) {

            found = true;

            System.out.println(
                "Student ID: " + rs.getInt("student_id") +
                " | Name: " + rs.getString("student_name")
            );
        }

        if (!found) {
            System.out.println("No students available.");
        }

        rs.close();
        st.close();

        return found;
    }

    public boolean showAllSkills(Connection con)
            throws SQLException {

        String sql =
            "SELECT skill_id, skill_name FROM SKILL";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        boolean found = false;

        System.out.println("\n--- AVAILABLE SKILLS ---");

        while (rs.next()) {

            found = true;

            System.out.println(
                "Skill ID: " + rs.getInt("skill_id") +
                " | Skill Name: " + rs.getString("skill_name")
            );
        }

        if (!found) {
            System.out.println("No skills available.");
        }

        rs.close();
        st.close();

        return found;
    }

    public boolean showAvailableTeachers(
            Connection con, int learner, int skill)
            throws SQLException {

        String sql =
            "SELECT s.student_id, s.student_name " +
            "FROM STUDENT s " +
            "JOIN STUDENT_SKILL ss " +
            "ON s.student_id = ss.student_id " +
            "WHERE ss.skill_id = ? " +
            "AND ss.skill_type = 'TEACHING' " +
            "AND s.student_id <> ?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, skill);
        ps.setInt(2, learner);

        ResultSet rs = ps.executeQuery();

        boolean found = false;

        System.out.println("\n--- AVAILABLE TEACHERS ---");

        while (rs.next()) {

            found = true;

            System.out.println(
                "Teacher ID: " + rs.getInt("student_id") +
                " | Name: " + rs.getString("student_name")
            );
        }

        if (!found) {
            System.out.println(
                "No teachers available for this skill."
            );
        }

        rs.close();
        ps.close();

        return found;
    }

    public void add() {

        try (Connection con = DBConnection.getConnection()) {

            boolean studentsFound = showAllStudents(con);

            if (!studentsFound) {
                return;
            }

            int learner = Input.getId("Enter learner ID: ");

            String learnerCheck =
                "SELECT student_id FROM STUDENT WHERE student_id=?";

            PreparedStatement lp =
                con.prepareStatement(learnerCheck);

            lp.setInt(1, learner);

            ResultSet lrs = lp.executeQuery();

            if (!lrs.next()) {

                System.out.println(
                    "Error: Learner does not exist."
                );

                lrs.close();
                lp.close();
                return;
            }

            lrs.close();
            lp.close();

            boolean skillsFound = showAllSkills(con);

            if (!skillsFound) {
                return;
            }

            int skill = Input.getId("Enter skill ID: ");

            String skillCheck =
                "SELECT skill_id FROM SKILL WHERE skill_id=?";

            PreparedStatement kp =
                con.prepareStatement(skillCheck);

            kp.setInt(1, skill);

            ResultSet krs = kp.executeQuery();

            if (!krs.next()) {

                System.out.println(
                    "Error: Skill does not exist."
                );

                krs.close();
                kp.close();
                return;
            }

            krs.close();
            kp.close();

            boolean teachersFound =
                showAvailableTeachers(con, learner, skill);

            if (!teachersFound) {
                return;
            }

            int teacher =
                Input.getId("Enter teacher ID: ");

            validateRequest(learner, teacher);

            String teacherCheck =
                "SELECT student_id FROM STUDENT_SKILL " +
                "WHERE student_id=? AND skill_id=? " +
                "AND skill_type='TEACHING'";

            PreparedStatement tp =
                con.prepareStatement(teacherCheck);

            tp.setInt(1, teacher);
            tp.setInt(2, skill);

            ResultSet trs = tp.executeQuery();

            if (!trs.next()) {

                System.out.println(
                    "Error: Teacher does not teach this skill."
                );

                trs.close();
                tp.close();
                return;
            }

            trs.close();
            tp.close();

            String sql =
                "INSERT INTO LEARNING_REQUEST " +
                "(learner_id, teacher_id, skill_id, request_date) " +
                "VALUES (?, ?, ?, CURDATE())";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, learner);
            ps.setInt(2, teacher);
            ps.setInt(3, skill);

            ps.executeUpdate();

            ps.close();

            System.out.println(
                "\nLearning request sent successfully!"
            );


        } catch (SkillSwapException e) {

            System.out.println("Error: " + e.getMessage());

        } catch (SQLIntegrityConstraintViolationException e) {

            System.out.println(
                "Error: Invalid or duplicate request."
            );

        } catch (SQLException e) {

            System.out.println(
                "Database Error: " + e.getMessage()
            );
        }
    }

    public void display() {

        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "SELECT lr.request_id, " +
                "a.student_name AS learner, " +
                "b.student_name AS teacher, " +
                "k.skill_name, lr.status " +
                "FROM LEARNING_REQUEST lr " +
                "JOIN STUDENT a " +
                "ON lr.learner_id = a.student_id " +
                "JOIN STUDENT b " +
                "ON lr.teacher_id = b.student_id " +
                "JOIN SKILL k " +
                "ON lr.skill_id = k.skill_id";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("\nRequest ID: " +
                    rs.getInt("request_id"));

                System.out.println("Learner: " +
                    rs.getString("learner"));

                System.out.println("Teacher: " +
                    rs.getString("teacher"));

                System.out.println("Skill: " +
                    rs.getString("skill_name"));

                System.out.println("Status: " +
                    rs.getString("status"));

                System.out.println("--------------------------");
            }

            if (!found) {
                System.out.println("No learning requests found.");
            }

            rs.close();
            st.close();

        } catch (SQLException e) {

            System.out.println(
                "Database Error: " + e.getMessage()
            );
        }
    }

    public void update() {

        int id = Input.getId("Enter request ID: ");

        System.out.println("\n1. ACCEPT");
        System.out.println("2. REJECT");

        int choice = Input.getChoice(2);

        String status =
            (choice == 1) ? "ACCEPTED" : "REJECTED";

        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "UPDATE LEARNING_REQUEST " +
                "SET status=? " +
                "WHERE request_id=? AND status='PENDING'";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setString(1, status);
            ps.setInt(2, id);

            int result = ps.executeUpdate();

            if (result > 0) {

                System.out.println(
                    "Request " + status + " successfully!"
                );

            } else {

                System.out.println(
                    "Pending request not found."
                );
            }

            ps.close();

        } catch (SQLException e) {

            System.out.println(
                "Database Error: " + e.getMessage()
            );
        }
    }

    public void delete() {

        int id = Input.getId("Enter request ID: ");

        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "DELETE FROM LEARNING_REQUEST " +
                "WHERE request_id=? " +
                "AND status IN ('PENDING','REJECTED')";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, id);

            int result = ps.executeUpdate();

            if (result > 0) {

                System.out.println(
                    "Request deleted successfully!"
                );

            } else {

                System.out.println(
                    "Cannot delete this request."
                );
            }

            ps.close();

        } catch (SQLIntegrityConstraintViolationException e) {

            System.out.println(
                "Request is connected to a session."
            );

        } catch (SQLException e) {

            System.out.println(
                "Database Error: " + e.getMessage()
            );
        }
    }
}
