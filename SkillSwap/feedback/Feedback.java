
package feedback;

import java.sql.*;

import database.DBConnection;
import util.Input;

public class Feedback {

    // DISPLAY COMPLETED SESSIONS

    public boolean showCompletedSessions(Connection con)
            throws SQLException {

        String sql =
            "SELECT s.session_id, " +
            "a.student_name AS learner, " +
            "b.student_name AS teacher, " +
            "k.skill_name, s.session_date " +
            "FROM SESSION s " +
            "JOIN LEARNING_REQUEST lr " +
            "ON s.request_id=lr.request_id " +
            "JOIN STUDENT a " +
            "ON lr.learner_id=a.student_id " +
            "JOIN STUDENT b " +
            "ON lr.teacher_id=b.student_id " +
            "JOIN SKILL k " +
            "ON lr.skill_id=k.skill_id " +
            "WHERE s.status='COMPLETED' " +
            "AND NOT EXISTS (" +
            "SELECT 1 FROM FEEDBACK f " +
            "WHERE f.session_id=s.session_id)";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        boolean found = false;

        System.out.println("\n--- COMPLETED SESSIONS ---");

        while (rs.next()) {

            found = true;

            System.out.println(
                "\nSession ID: " + rs.getInt("session_id")
            );

            System.out.println(
                "Learner: " + rs.getString("learner")
            );

            System.out.println(
                "Teacher: " + rs.getString("teacher")
            );

            System.out.println(
                "Skill: " + rs.getString("skill_name")
            );

            System.out.println(
                "Date: " + rs.getDate("session_date")
            );

            System.out.println("--------------------------");
        }

        if (!found) {
            System.out.println(
                "No completed sessions available for feedback."
            );
        }

        rs.close();
        st.close();

        return found;
    }


    // ADD FEEDBACK

    public void add() {

        try (Connection con = DBConnection.getConnection()) {

            boolean found = showCompletedSessions(con);

            if (!found) {
                return;
            }

            int sessionId =
                Input.getId("Enter Session ID: ");

            // CHECK SESSION

            String check =
                "SELECT session_id FROM SESSION " +
                "WHERE session_id=? AND status='COMPLETED' " +
                "AND NOT EXISTS (" +
                "SELECT 1 FROM FEEDBACK " +
                "WHERE FEEDBACK.session_id=SESSION.session_id)";

            PreparedStatement cp =
                con.prepareStatement(check);

            cp.setInt(1, sessionId);

            ResultSet rs = cp.executeQuery();

            if (!rs.next()) {

                System.out.println(
                    "Error: Invalid session or feedback already submitted."
                );

                rs.close();
                cp.close();
                return;
            }

            rs.close();
            cp.close();

            // ENTER RATING

            int rating = Input.getRating(
                "Enter Rating (1-5): "
            );

            // ENTER COMMENTS

            String comments = Input.getText(
                "Enter Feedback Comments: "
            );

            // INSERT FEEDBACK

            String sql =
                "INSERT INTO FEEDBACK " +
                "(session_id, rating, comments) " +
                "VALUES (?, ?, ?)";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, sessionId);
            ps.setInt(2, rating);
            ps.setString(3, comments);

            ps.executeUpdate();

            ps.close();

            System.out.println(
                "Feedback submitted successfully!"
            );

        } catch (SQLIntegrityConstraintViolationException e) {

            System.out.println(
                "Error: Feedback already exists for this session."
            );

        } catch (SQLException e) {

            System.out.println(
                "Database Error: " + e.getMessage()
            );
        }
    }


    // DISPLAY ALL FEEDBACK

    public void display() {

        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "SELECT f.feedback_id, f.session_id, " +
                "a.student_name AS learner, " +
                "b.student_name AS teacher, " +
                "k.skill_name, f.rating, f.comments " +
                "FROM FEEDBACK f " +
                "JOIN SESSION s ON f.session_id=s.session_id " +
                "JOIN LEARNING_REQUEST lr " +
                "ON s.request_id=lr.request_id " +
                "JOIN STUDENT a ON lr.learner_id=a.student_id " +
                "JOIN STUDENT b ON lr.teacher_id=b.student_id " +
                "JOIN SKILL k ON lr.skill_id=k.skill_id";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            boolean found = false;

            System.out.println("\n--- ALL FEEDBACK ---");

            while (rs.next()) {

                found = true;

                System.out.println(
                    "\nFeedback ID: " +
                    rs.getInt("feedback_id")
                );

                System.out.println(
                    "Session ID: " +
                    rs.getInt("session_id")
                );

                System.out.println(
                    "Learner: " +
                    rs.getString("learner")
                );

                System.out.println(
                    "Teacher: " +
                    rs.getString("teacher")
                );

                System.out.println(
                    "Skill: " +
                    rs.getString("skill_name")
                );

                System.out.println(
                    "Rating: " +
                    rs.getInt("rating") + "/5"
                );

                System.out.println(
                    "Comments: " +
                    rs.getString("comments")
                );

                System.out.println("--------------------------");
            }

            if (!found) {
                System.out.println("No feedback available.");
            }

            rs.close();
            st.close();

        } catch (SQLException e) {

            System.out.println(
                "Database Error: " + e.getMessage()
            );
        }
    }


    // UPDATE FEEDBACK

    public void update() {

        try (Connection con = DBConnection.getConnection()) {

            // DISPLAY EXISTING FEEDBACK

            display();

            int id = Input.getId(
                "Enter Feedback ID to update: "
            );

            // CHECK FEEDBACK

            String check =
                "SELECT feedback_id FROM FEEDBACK WHERE feedback_id=?";

            PreparedStatement cp =
                con.prepareStatement(check);

            cp.setInt(1, id);

            ResultSet rs = cp.executeQuery();

            if (!rs.next()) {

                System.out.println("Feedback not found.");

                rs.close();
                cp.close();
                return;
            }

            rs.close();
            cp.close();

            // ENTER NEW DETAILS

            int rating = Input.getRating(
                "Enter New Rating (1-5): "
            );

            String comments = Input.getText(
                "Enter New Comments: "
            );

            // UPDATE

            String sql =
                "UPDATE FEEDBACK SET rating=?, comments=? " +
                "WHERE feedback_id=?";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, rating);
            ps.setString(2, comments);
            ps.setInt(3, id);

            ps.executeUpdate();

            ps.close();

            System.out.println(
                "Feedback updated successfully!"
            );

        } catch (SQLException e) {

            System.out.println(
                "Database Error: " + e.getMessage()
            );
        }
    }


    // DELETE FEEDBACK

    public void delete() {

        try (Connection con = DBConnection.getConnection()) {

            display();

            int id = Input.getId(
                "Enter Feedback ID to delete: "
            );

            String sql =
                "DELETE FROM FEEDBACK WHERE feedback_id=?";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, id);

            int result = ps.executeUpdate();

            if (result > 0) {

                System.out.println(
                    "Feedback deleted successfully!"
                );

            } else {

                System.out.println("Feedback not found.");
            }

            ps.close();

        } catch (SQLException e) {

            System.out.println(
                "Database Error: " + e.getMessage()
            );
        }
    }


    // SHOW AVERAGE RATING

    public void showAverageRating() {

        try (Connection con = DBConnection.getConnection()) {

            String sql =
                "SELECT COUNT(*) AS total_feedback, " +
                "AVG(rating) AS average_rating " +
                "FROM FEEDBACK";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {

                System.out.println("\n--- FEEDBACK SUMMARY ---");

                System.out.println(
                    "Total Feedback: " +
                    rs.getInt("total_feedback")
                );

                if (rs.getInt("total_feedback") > 0) {

                    System.out.printf(
                        "Average Rating: %.2f/5%n",
                        rs.getDouble("average_rating")
                    );

                } else {

                    System.out.println(
                        "No ratings available."
                    );
                }
            }

            rs.close();
            st.close();

        } catch (SQLException e) {

            System.out.println(
                "Database Error: " + e.getMessage()
            );
        }
    }
}
