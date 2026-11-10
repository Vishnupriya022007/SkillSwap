
package skill;

public class Skill {

    private int skillId;
    private String skillName;
    private String category;
    private String description;

    public Skill(int id, String name) {
        this.skillId = id;
        this.skillName = name;
    }

    public Skill(int id, String name,
                 String category, String description) {

        this.skillId = id;
        this.skillName = name;
        this.category = category;
        this.description = description;
    }

    public void displaySkill() {

        System.out.println("Skill ID: " + skillId);
        System.out.println("Skill Name: " + skillName);
        System.out.println("Category: " + category);
        System.out.println("Description: " + description);
    }
}
