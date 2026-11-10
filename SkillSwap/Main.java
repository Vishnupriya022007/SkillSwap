
import util.Input;

import student.StudentManager;
import skill.SkillManager;
import studentSkill.StudentSkillManager;
import request.LearningRequest;
import session.LearningSession;
import feedback.Feedback;

public class Main {

    public static void main(String[] args) {

        StudentManager student = new StudentManager();
        SkillManager skill = new SkillManager();

        StudentSkillManager studentSkill =
            new StudentSkillManager();

        LearningRequest request = new LearningRequest();
        LearningSession session = new LearningSession();
        Feedback feedback = new Feedback();

        int choice;

        do {

            System.out.println("\n================================");
            System.out.println("          SKILLSWAP");
            System.out.println("  STUDENT SKILL EXCHANGE PLATFORM");
            System.out.println("================================");

            System.out.println("1. Student Management");
            System.out.println("2. Skill Management");
            System.out.println("3. Student Skill Management");
            System.out.println("4. Learning Request Management");
            System.out.println("5. Learning Session Management");
            System.out.println("6. Feedback Management");
            System.out.println("7. Exit");

            choice = Input.getChoice(7);

            switch (choice) {

                case 1:
                    studentMenu(student);
                    break;

                case 2:
                    skillMenu(skill);
                    break;

                case 3:
                    studentSkillMenu(studentSkill);
                    break;

                case 4:
                    requestMenu(request);
                    break;

                case 5:
                    sessionMenu(session);
                    break;

                case 6:
                    feedbackMenu(feedback);
                    break;

                case 7:
                    System.out.println("Thank you for using SkillSwap!");
                    break;
            }

        } while (choice != 7);
    }

    static void studentMenu(StudentManager s) {

        int ch;

        do {

            System.out.println("\n--- STUDENT MANAGEMENT ---");
            System.out.println("1. Add Student");
            System.out.println("2. Display Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Back");

            ch = Input.getChoice(5);

            switch (ch) {

                case 1: s.add(); break;
                case 2: s.display(); break;
                case 3: s.update(); break;
                case 4: s.delete(); break;
                case 5: break;
            }

        } while (ch != 5);
    }

    static void skillMenu(SkillManager s) {

        int ch;

        do {

            System.out.println("\n--- SKILL MANAGEMENT ---");
            System.out.println("1. Add Skill");
            System.out.println("2. Display Skills");
            System.out.println("3. Update Skill");
            System.out.println("4. Delete Skill");
            System.out.println("5. Search Skill");
            System.out.println("6. Back");

            ch = Input.getChoice(6);

            switch (ch) {

                case 1: s.add(); break;
                case 2: s.display(); break;
                case 3: s.update(); break;
                case 4: s.delete(); break;

                case 5:

                    String name = Input.getText("Enter skill name: ");

                    System.out.println("1. Search by name");
                    System.out.println("2. Search by name and category");

                    int option = Input.getChoice(2);

                    if (option == 1) {
                        s.searchSkill(name);
                    } else {
                        String category =
                            Input.getText("Enter category: ");

                        s.searchSkill(name, category);
                    }
                    break;

                case 6: break;
            }

        } while (ch != 6);
    }

    static void studentSkillMenu(StudentSkillManager s) {

        int ch;

        do {

            System.out.println("\n--- STUDENT SKILL MANAGEMENT ---");
            System.out.println("1. Add");
            System.out.println("2. Display");
            System.out.println("3. Update");
            System.out.println("4. Delete");
            System.out.println("5. Back");

            ch = Input.getChoice(5);

            switch (ch) {

                case 1: s.add(); break;
                case 2: s.display(); break;
                case 3: s.update(); break;
                case 4: s.delete(); break;
                case 5: break;
            }

        } while (ch != 5);
    }

    static void requestMenu(LearningRequest r) {

        int ch;

        do {

            System.out.println("\n--- LEARNING REQUEST MANAGEMENT ---");
            System.out.println("1. Send Request");
            System.out.println("2. Display Requests");
            System.out.println("3. Accept / Reject Request");
            System.out.println("4. Delete Request");
            System.out.println("5. Back");

            ch = Input.getChoice(5);

            switch (ch) {

                case 1: r.add(); break;
                case 2: r.display(); break;
                case 3: r.update(); break;
                case 4: r.delete(); break;
                case 5: break;
            }

        } while (ch != 5);
    }

    static void sessionMenu(LearningSession s) {

        int ch;

        do {

            System.out.println("\n--- SESSION MANAGEMENT ---");
            System.out.println("1. Schedule Session");
            System.out.println("2. Display Sessions");
            System.out.println("3. Update Session Status");
            System.out.println("4. Delete Cancelled Session");
            System.out.println("5. Back");

            ch = Input.getChoice(5);

            switch (ch) {

                case 1: s.add(); break;
                case 2: s.display(); break;
                case 3: s.update(); break;
                case 4: s.delete(); break;
                case 5: break;
            }

        } while (ch != 5);
    }

    static void feedbackMenu(Feedback f) {

        int ch;

        do {

            System.out.println("\n--- FEEDBACK MANAGEMENT ---");
            System.out.println("1. Add Feedback");
            System.out.println("2. Display Feedback");
            System.out.println("3. Update Feedback");
            System.out.println("4. Delete Feedback");
            System.out.println("5. Average Teacher Rating");
            System.out.println("6. Back");

            ch = Input.getChoice(6);

            switch (ch) {

                case 1: f.add(); break;
                case 2: f.display(); break;
                case 3: f.update(); break;
                case 4: f.delete(); break;
                case 5: f.showAverageRating(); break;
                case 6: break;
            }

        } while (ch != 6);
    }
}
