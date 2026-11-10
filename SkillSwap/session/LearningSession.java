
package session;

import java.sql.*;

import database.DBConnection;
import util.Input;

public class LearningSession {

    public boolean showAcceptedRequests(Connection con)
            throws SQLException {

        String sql =
            "SELECT lr.request_id, " +
            "a.student_name AS learner, " +
            "b.student_name AS teacher, " +
            "k.skill_name " +
            "FROM LEARNING_REQUEST lr " +
            "JOIN STUDENT a ON lr.learner_id=a.student_id " +
            "JOIN STUDENT b ON lr.teacher_id=b.student_id " +
            "JOIN SKILL k ON lr.skill_id=k.skill_id " +
            "WHERE lr.status='ACCEPTED' " +
            "AND NOT EXISTS (" +
            "SELECT 1 FROM SESSION s " +
            "WHERE s.request_id=lr.request_id)";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        boolean found = false;

        System.out.println("\n--- ACCEPTED LEARNING REQUESTS ---");

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
        }

        if (!found) {
            System.out.println("No accepted requests available.");
        }

        rs.close();
        st.close();

        return found;
    }

    public void add() {

        try (Connection con = DBConnection.getConnection()) {

            boolean found = showAcceptedRequests(con);

            if (!found) {
                return;
            }

            int requestId =
                Input.getId("Enter Request ID: ");

            String check =
                "SELECT request_id FROM LEARNING_REQUEST " +
                "WHERE request_id=? AND status='ACCEPTED' " +
                "AND NOT EXISTS (" +
                "SELECT 1 FROM SESSION " +
                "WHERE SESSION.request_id=LEARNING_REQUEST.request_id)";

            PreparedStatement cp =
                con.prepareStatement(check);

            cp.setInt(1, requestId);

            ResultSet rs = cp.executeQuery();

            if (!rs.next()) {

                System.out.println(
                    "Error: Invalid or already scheduled request."
                );

                rs.close();
                cp.close();
                return;
            }

            rs.close();
            cp.close();

            String date = Input.getText(
                "Enter session date (YYYY-MM-DD): "
            );

            String time = Input.getText(
                "Enter session time (HH:MM:SS): "
            );

            int duration = Input.getId(
                "Enter session duration (minutes): "
            );

            String sql =
                "INSERT INTO SESSION " +
                "(request_id, session_date, session_time, duration, status) " +
                "VALUES (?, ?, ?, ?, 'SCHEDULED')";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, requestId);
            ps.setDate(2, Date.valueOf(date));
            ps.setTime(3, Time.valueOf(time));
            ps.setInt(4, duration);

            ps.executeUpdate();

            ps.close();

            System.out.println(
                "\nLearning session scheduled successfully!"
            );


        } catch (IllegalArgumentException e) {

            System.out.println(
                "Error: Enter date as YYYY-MM-DD " +
                "and time as HH:MM:SS."
            );

        } catch (SQLIntegrityConstraintViolationException e) {

            System.out.println(
                "Error: Session already exists for this request."
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
                "SELECT s.session_id, s.request_id, " +
                "a.student_name AS learner, " +
                "b.student_name AS teacher, " +
                "k.skill_name, s.session_date, " +
                "s.session_time, s.duration, s.status " +
                "FROM SESSION s " +
                "JOIN LEARNING_REQUEST lr " +
                "ON s.request_id=lr.request_id " +
                "JOIN STUDENT a " +
                "ON lr.learner_id=a.student_id " +
                "JOIN STUDENT b " +
                "ON lr.teacher_id=b.student_id " +
                "JOIN SKILL k " +
                "ON lr.skill_id=k.skill_id";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            boolean found = false;

            System.out.println("\n--- LEARNING SESSIONS ---");

            while (rs.next()) {

                found = true;

                System.out.println("\nSession ID: " +
                    rs.getInt("session_id"));

                System.out.println("Request ID: " +
                    rs.getInt("request_id"));

                System.out.println("Learner: " +
                    rs.getString("learner"));

                System.out.println("Teacher: " +
                    rs.getString("teacher"));

                System.out.println("Skill: " +
                    rs.getString("skill_name"));

                System.out.println("Date: " +
                    rs.getDate("session_date"));

                System.out.println("Time: " +
                    rs.getTime("session_time"));

                System.out.println("Duration: " +
                    rs.getInt("duration") + " minutes");

                System.out.println("Status: " +
                    rs.getString("status"));

                System.out.println("--------------------------");
            }

            if (!found) {
                System.out.println("No sessions available.");
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

        try (Connection con = DBConnection.getConnection()) {

            String displaySql =
                "SELECT s.session_id, " +
                "a.student_name AS learner, " +
                "b.student_name AS teacher, " +
                "k.skill_name, s.session_date, " +
                "s.session_time, s.duration, s.status " +
                "FROM SESSION s " +
                "JOIN LEARNING_REQUEST lr " +
                "ON s.request_id=lr.request_id " +
                "JOIN STUDENT a " +
                "ON lr.learner_id=a.student_id " +
                "JOIN STUDENT b " +
                "ON lr.teacher_id=b.student_id " +
                "JOIN SKILL k " +
                "ON lr.skill_id=k.skill_id";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(displaySql);

            boolean found = false;

            System.out.println("\n--- AVAILABLE SESSIONS ---");

            while (rs.next()) {

                found = true;

                System.out.println(
                    "\nSession ID: " +
                    rs.getInt("session_id")
                );

                System.out.println("Learner: " +
                    rs.getString("learner"));

                System.out.println("Teacher: " +
                    rs.getString("teacher"));

                System.out.println("Skill: " +
                    rs.getString("skill_name"));

                System.out.println("Date: " +
                    rs.getDate("session_date"));

                System.out.println("Time: " +
                    rs.getTime("session_time"));

                System.out.println("Duration: " +
                    rs.getInt("duration") + " minutes");

                System.out.println("Status: " +
                    rs.getString("status"));
            }

            rs.close();
            st.close();

            if (!found) {
                System.out.println("No sessions available.");
                return;
            }

            int id = Input.getId("Enter Session ID: ");

            System.out.println("\n1. SCHEDULED");
            System.out.println("2. COMPLETED");
            System.out.println("3. CANCELLED");

            int choice = Input.getChoice(3);

            String status;

            if (choice == 1) {
                status = "SCHEDULED";
            } else if (choice == 2) {
                status = "COMPLETED";
            } else {
                status = "CANCELLED";
            }

            String sql =
                "UPDATE SESSION SET status=? WHERE session_id=?";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setString(1, status);
            ps.setInt(2, id);

            int result = ps.executeUpdate();

            if (result > 0) {

                System.out.println(
                    "Session status updated successfully!"
                );

            } else {

                System.out.println("Session not found.");
            }

            ps.close();

        } catch (SQLException e) {

            System.out.println(
                "Database Error: " + e.getMessage()
            );
        }
    }

    public void delete() {

        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "SELECT s.session_id, " +
                "a.student_name AS learner, " +
                "b.student_name AS teacher, " +
                "k.skill_name, s.session_date, " +
                "s.session_time, s.duration, s.status " +
                "FROM SESSION s " +
                "JOIN LEARNING_REQUEST lr " +
                "ON s.request_id=lr.request_id " +
                "JOIN STUDENT a " +
                "ON lr.learner_id=a.student_id " +
                "JOIN STUDENT b " +
                "ON lr.teacher_id=b.student_id " +
                "JOIN SKILL k " +
                "ON lr.skill_id=k.skill_id";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            boolean found = false;

            System.out.println("\n--- AVAILABLE SESSIONS ---");

            while (rs.next()) {

                found = true;

                System.out.println(
                    "\nSession ID: " + rs.getInt("session_id") +
                    " | Learner: " + rs.getString("learner") +
                    " | Teacher: " + rs.getString("teacher") +
                    " | Skill: " + rs.getString("skill_name") +
                    " | Date: " + rs.getDate("session_date") +
                    " | Time: " + rs.getTime("session_time") +
                    " | Duration: " + rs.getInt("duration") +
                    " minutes | Status: " + rs.getString("status")
                );
            }

            rs.close();
            st.close();

            if (!found) {
                System.out.println("No sessions available.");
                return;
            }

            int id = Input.getId(
                "Enter Session ID to delete: "
            );

            String deleteSql =
                "DELETE FROM SESSION WHERE session_id=?";

            PreparedStatement ps =
                con.prepareStatement(deleteSql);

            ps.setInt(1, id);

            int result = ps.executeUpdate();

            if (result > 0) {

                System.out.println(
                    "Session deleted successfully!"
                );

            } else {

                System.out.println("Session not found.");
            }

            ps.close();

        } catch (SQLIntegrityConstraintViolationException e) {

            System.out.println(
                "Session cannot be deleted because it has related records."
            );

        } catch (SQLException e) {

            System.out.println(
                "Database Error: " + e.getMessage()
            );
        }
    }
}
