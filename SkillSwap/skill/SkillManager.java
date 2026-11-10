
package skill;

import java.sql.*;
import common.Manageable;
import database.DBConnection;
import util.Input;

public class SkillManager implements Manageable {

    @Override
    public void add() {

        String name = Input.getText("Enter skill name: ");
        String category = Input.getText("Enter category: ");
        String description = Input.getText("Enter description: ");

        try (Connection con = DBConnection.getConnection()) {

            String sql = "INSERT INTO SKILL " +
                "(skill_name,category,description) VALUES(?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, category);
            ps.setString(3, description);

            ps.executeUpdate();

            System.out.println("Skill added successfully!");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Skill already exists.");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    @Override
    public void display() {

        try (Connection con = DBConnection.getConnection()) {

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM SKILL");

            while (rs.next()) {

                Skill s = new Skill(
                    rs.getInt("skill_id"),
                    rs.getString("skill_name"),
                    rs.getString("category"),
                    rs.getString("description")
                );

                s.displaySkill();
                System.out.println("--------------------");
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    @Override
    public void update() {

        int id = Input.getId("Enter skill ID: ");
        String name = Input.getText("Enter new skill name: ");
        String category = Input.getText("Enter new category: ");
        String description = Input.getText("Enter new description: ");

        try (Connection con = DBConnection.getConnection()) {

            String sql = "UPDATE SKILL SET skill_name=?, " +
                "category=?, description=? WHERE skill_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, category);
            ps.setString(3, description);
            ps.setInt(4, id);

            if (ps.executeUpdate() > 0)
                System.out.println("Skill updated successfully!");
            else
                System.out.println("Skill not found.");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: Skill name already exists.");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    @Override
    public void delete() {

        int id = Input.getId("Enter skill ID: ");

        try (Connection con = DBConnection.getConnection()) {

            String sql = "DELETE FROM SKILL WHERE skill_id=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            if (ps.executeUpdate() > 0)
                System.out.println("Skill deleted successfully!");
            else
                System.out.println("Skill not found.");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Cannot delete: Skill is in use.");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public void searchSkill(String name) {
        searchSkill(name, "");
    }

    public void searchSkill(String name, String category) {

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT * FROM SKILL " +
                "WHERE skill_name LIKE ? AND category LIKE ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "%" + name + "%");
            ps.setString(2, "%" + category + "%");

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println(
                    rs.getInt("skill_id") + " - " +
                    rs.getString("skill_name") + " - " +
                    rs.getString("category")
                );
            }

            if (!found)
                System.out.println("No matching skills found.");

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }
}
